package com.example.demo.service.excel.impl;

import cn.hutool.core.io.FileUtil;
import com.example.demo.helper.EasyExcelHelper;
import com.example.demo.model.excel.FigurePage;
import com.example.demo.model.excel.FindFilePath;
import com.example.demo.service.excel.FindFilePathService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class FindFilePathServiceImpl implements FindFilePathService {

    // 图纸文件名&路径
    private static Map<String, String> mapFigurePath = new HashMap<>();
    // 第一次解析excel异常 (原 D 列, 现 C 列)
    private static Map<String, String> mapAbnormalFile = new HashMap<>();
    // 第二次解析excel异常 (C 列正常, 图表格式异常,需要重新解析，例：NT10_项目汇总.xlsx)
    private static Map<String, String> mapAbnormalFile2 = new HashMap<>();
    // 第二次解析excel异常 (描述异常, 图表格式异常,需要重新解析，例：NS800双水_项目汇总.xlsx)
    private static Map<String, String> mapAbnormalFile3 = new HashMap<>();
    // 全体Excel中的图号
    private static Map<String, Object> mapFigure = new HashMap<>();
    // 正则表达式 异常（因为 图号 带括号等字符）
    private static Map<String, Object> mapErrorFigureNumber = new HashMap<>();
    // 重复的图纸文件名&路径
    private static List<FigurePage> reFigurePageList = new ArrayList<>();
    // 图号对应图纸结果
    private static Map<String, List<String>> result = new HashMap<>();
    // clone 图纸文件名&路径 --> 剩余的图纸文件
    private static Map<String, String> mapResidueFigurePage = new HashMap<>();
    // 图号未匹配到图纸的记录
    private static List<String> noMateList = new ArrayList<>();
    // 已匹配到图号的图纸数
    /**不同的图号匹配到 相同的图纸文件名(第一次匹配到，.remove已从hashMap中删除"总数减1"，
     * 第二次匹配到，.remove没有数据删除"总数未减1"),
     * 导致 剩余的图纸文件数 + 已匹配到图号的图纸数 > 全部图纸文件数
     * */
    private static int iii = 0;

    @Autowired
    private EasyExcelHelper easyExcelHelper;

    @Override
    public FindFilePath fileWalker(String figureNumberPath, String figurePagePath) {


        File folder = new File(figureNumberPath);
        // 加载图纸到内存
        mapDrawing(figurePagePath);
        // clone 图纸文件名&路径
        mapResidueFigurePage.putAll(mapFigurePath);
        // 加载图号到内存
        listFiles(folder);
        // 第一次解析异常excel文件
        parseErrorContent(mapAbnormalFile);
        // 第二次解析异常excel文件
        parseErrorContent2(mapAbnormalFile2);
        Map<String,String> map = new HashMap<>();
        mapFigure.forEach((figureKey, figureValue) -> {
            List<String> list = new ArrayList<>();
            mapFigurePath.forEach((pathKey, pathValue) -> {
                try {
                    if (regular(figureKey, pathKey)) {

                        list.add(pathValue);
                        map.put(pathValue,pathKey);
                        // 匹配到的图纸文件名删除，剩下的就是没有匹配到的图纸
                        mapResidueFigurePage.remove(pathKey);
                    }
                } catch (Exception e) {
//                    System.out.println("正则匹配图号错误：" + figureKey);
                    mapErrorFigureNumber.put(figureKey, null);
                }
            });
            if (list.isEmpty()) {
                // 图号未匹配到图纸的记录
                noMateList.add(figureKey);
            }

            // 已匹配到图号的图纸数
            iii += list.size();
            result.put(figureKey, list);
        });


        System.out.println("全部图纸文件数：" + mapFigurePath.size());
        System.out.println("全部图号数：" + mapFigure.size());
        System.out.println("第一次解析Excel异常文件数：" + mapAbnormalFile.size());
        System.out.println("第二次解析Excel异常文件数：" + mapAbnormalFile2.size());
        System.out.println("第三次解析Excel异常文件数：" + mapAbnormalFile3.size());
        System.out.println("正则表达式匹配异常(因为 图号 带括号等字符)数：" + mapErrorFigureNumber.size());
        System.out.println("图号对应图纸结果数：" + result.size());
        System.out.println("重复的图纸文件名数：" + reFigurePageList.size());
        System.out.println("图号未匹配到图纸数：" + noMateList.size());
        System.out.println("剩余的图纸文件数：" + mapResidueFigurePage.size());
        System.out.println("已匹配到图号的图纸数：" + iii);
        System.out.println(map.size());

        return getFigurePageResult();
    }

    private static @NotNull FindFilePath getFigurePageResult() {
        FindFilePath findFilePath = new FindFilePath();
        findFilePath.setMapFigurePath(mapFigurePath);
        findFilePath.setMapFigure(mapFigure);
        findFilePath.setMapAbnormalFile(mapAbnormalFile);
        findFilePath.setMapAbnormalFile2(mapAbnormalFile2);
        findFilePath.setMapAbnormalFile3(mapAbnormalFile3);
        findFilePath.setMapErrorFigureNumber(mapErrorFigureNumber);
        findFilePath.setResult(result);
        findFilePath.setReFigurePageList(reFigurePageList);
        findFilePath.setNoMateList(noMateList);
        findFilePath.setMapResidueFigurePage(mapResidueFigurePage);
        findFilePath.setIii(iii);
        return findFilePath;
    }


    /**
     * 递归遍历给定文件夹及其子文件夹中的所有文件
     *
     * @param folder 要遍历的文件夹
     */
    private void listFiles(File folder) {
        // 获取当前文件夹中的所有文件和子文件夹

        File[] files = folder.listFiles();
        if (files != null) {  // 检查files是否为空，以防止NullPointerException
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹，递归调用listFiles方法
                    listFiles(file);
                } else {
                    // 如果是文件，打印文件的绝对路径
//                    System.out.println(file.getAbsolutePath());
                    // 打印文件名
//                    System.out.println(file.getName());
                    String suffix = FileUtil.getSuffix(file.getName());
                    if (suffix.equalsIgnoreCase("xlsx")) {
                        // 解析Excel
                        parseContent(file);
                    }
                }
            }
        }
    }

    /**
     * 解析 Excel - 图号
     *
     * @param file
     */

    private void parseContent(File file) {
        // 解析的单个文件的数据
        List<Map<Integer, String>> mapList = null;
        try {
            mapList = easyExcelHelper.getList(file, 0, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 不是sheet0
        if (mapList.isEmpty()) {
            try {
                mapList = easyExcelHelper.getList(file, 1, 0);
            } catch (Exception e) {

            }
        }
        if (mapList.isEmpty()) {
            return;
        }

        int start = 0;

        for (Map<Integer, String> map : mapList) {
            boolean isMatch =
                    ("图号".equals(map.getOrDefault(3, ""))) &&
                            ("描述".equals(map.getOrDefault(5, ""))) &&
                            ("厚度（mm)".equals(map.getOrDefault(7, ""))) &&
                            ("宽度（mm)".equals(map.getOrDefault(8, ""))) &&
                            ("长度（mm)".equals(map.getOrDefault(9, ""))) &&
                            ("数量".equals(map.getOrDefault(10, ""))) &&
                            ("重量（kg)".equals(map.getOrDefault(11, "")));

            if (isMatch) {
                break; // 找到匹配的Map，退出循环
            }
            start++;
        }


        if (start >= mapList.size()) {
//            System.out.println("———异常文件0：" + file.getName());
            mapAbnormalFile.put(file.getName(), file.getAbsolutePath());
            return;
        }

        for (int i = start + 1; i < mapList.size(); i++) {

            // content 遍历结束
            if (mapList.get(i).get(2) == null || mapList.get(i).get(2).equals("铜排设计员：")) {
                return;
            }

            if (mapList.get(i).get(3) == null || mapList.get(i).get(7) == null && mapList.get(i).get(8) == null && mapList.get(i).get(9) == null && mapList.get(i).get(10) == null && mapList.get(i).get(11) == null) {
                continue;
            }


            // 图号
            String figureNumber = mapList.get(i).get(3);
            mapFigure.put(figureNumber, null);
        }
    }

    /**
     * 第一次解析异常文件
     *
     * @param mapAbnormalFile
     */
    private void parseErrorContent(Map<String, String> mapAbnormalFile) {

//        System.out.println("第一次解析异常excel文件");
        mapAbnormalFile.forEach((key, value) -> {

            File file = new File(value);

            // 解析的单个文件的数据
            List<Map<Integer, String>> mapList = null;
            try {
                mapList = easyExcelHelper.getList(file, 0, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 不是sheet0
            if (mapList.isEmpty()) {
                try {
                    mapList = easyExcelHelper.getList(file, 1, 0);
                } catch (Exception e) {

                }
            }
            if (mapList.isEmpty()) {
                return;
            }

            int start = 0;

            for (Map<Integer, String> map : mapList) {
                boolean isMatch =
                        ("图号".equals(map.getOrDefault(2, ""))) &&
                                ("描述".equals(map.getOrDefault(4, ""))) &&
                                ("厚度（mm)".equals(map.getOrDefault(6, ""))) &&
                                ("宽度（mm)".equals(map.getOrDefault(7, ""))) &&
                                ("长度（mm)".equals(map.getOrDefault(8, ""))) &&
                                ("数量".equals(map.getOrDefault(9, ""))) &&
                                ("重量（kg)".equals(map.getOrDefault(10, "")));

                if (isMatch) {
                    break; // 找到匹配的Map，退出循环
                }
                start++;
            }


            if (start >= mapList.size()) {
//                System.out.println("———异常文件1：" + file.getName());
                mapAbnormalFile2.put(file.getName(), file.getAbsolutePath());
                return;
            }

            for (int i = start + 1; i < mapList.size(); i++) {

                // content 遍历结束
                if (mapList.get(i).get(1) == null || mapList.get(i).get(1).equals("铜排设计员：")) {
                    return;
                }

                if (mapList.get(i).get(2) == null || mapList.get(i).get(6) == null && mapList.get(i).get(7) == null && mapList.get(i).get(8) == null && mapList.get(i).get(9) == null && mapList.get(i).get(10) == null) {
                    continue;
                }


                // 图号
                String figureNumber = mapList.get(i).get(2);
                mapFigure.put(figureNumber, null);

            }
        });

    }

    /**
     * 第二次解析异常文件
     *
     * @param mapAbnormalFile
     */
    private void parseErrorContent2(Map<String, String> mapAbnormalFile) {

//        System.out.println("第二次解析异常excel文件");
        mapAbnormalFile.forEach((key, value) -> {

            File file = new File(value);

            // 解析的单个文件的数据
            List<Map<Integer, String>> mapList = null;
            try {
                mapList = easyExcelHelper.getList(file, 0, 0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // 不是sheet0
            if (mapList.isEmpty()) {
                try {
                    mapList = easyExcelHelper.getList(file, 1, 0);
                } catch (Exception e) {

                }
            }
            if (mapList.isEmpty()) {
                return;
            }

            int start = 0;

            for (Map<Integer, String> map : mapList) {
                boolean isMatch =
                        ("零件号".equals(map.getOrDefault(4, ""))) &&
                                ("零件料号版本".equals(map.getOrDefault(5, ""))) &&
                                ("商业号".equals(map.getOrDefault(6, "")));
//                                ("中文描述".equals(map.getOrDefault(7, ""))) &&
//                                ("使用数量".equals(map.getOrDefault(8, ""))) &&
//                                ("使用数量单位".equals(map.getOrDefault(9, ""))) &&
//                                ("订货数量".equals(map.getOrDefault(10, "")));

                if (isMatch) {
                    break; // 找到匹配的Map，退出循环
                }
                start++;
            }


            if (start >= mapList.size()) {
//                System.out.println("———异常文件2：" + file.getName());
                mapAbnormalFile3.put(file.getName(), file.getAbsolutePath());
                return;
            }

            for (int i = start + 1; i < mapList.size(); i++) {
                if (mapList.get(i).get(4) == null && mapList.get(i).get(7) == null && mapList.get(i).get(16) == null) {
                    break;
                }

                // 图号
                String figureNumber = mapList.get(i).get(4);
                mapFigure.put(figureNumber, null);

            }
        });

    }


    /**
     * 正则匹配 图号 -> 图纸文件名
     *
     * @param patternString 图号
     * @param text          图纸 FileName
     * @return Boolean
     */
    private Boolean regular(String patternString, String text) {

        // Compile the pattern with CASE_INSENSITIVE flag
        // 正则表达式，添加前瞻来排除以 ~$ 开头的字符串
//        Pattern pattern = Pattern.compile(String.format("%s.*", patternString), Pattern.CASE_INSENSITIVE);
        Pattern pattern = Pattern.compile(String.format("^%s.*", patternString), Pattern.CASE_INSENSITIVE);
//      Pattern pattern = Pattern.compile(String.format("^(?!\\~\\$).*%s", patternString), Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(text);

        // Check if the pattern matches
        return matcher.find();
    }


    /**
     * 遍历图纸，将图纸文件名，路径加载到内存中（hashMap）
     */
    private void mapDrawing(String figurePagePath) {

        try (Stream<Path> paths = Files.walk(Paths.get(figurePagePath))) {
            // 过滤出所有普通文件，获取路径
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        String suffix = FileUtil.getSuffix(path.getFileName().toString());
                        final List<String> validFileSuffix = Arrays.asList("dwg", "DWG", "stp", "STP", "sldprt", "SLDPRT", "sldasm", "SLDASM", "SLDDRW", "slddrw");
                        //  是否有重复key,有则保存
                        if (validFileSuffix.contains(suffix) && !path.getFileName().toString().startsWith("~$")
                        ) {
                            if (mapFigurePath.get(path.getFileName().toString()) != null) {
                                FigurePage figurePage = new FigurePage();
                                figurePage.setFileName(path.getFileName().toString());
                                figurePage.setFilePath(path.toString());
                                reFigurePageList.add(figurePage);
                            } else {
                                mapFigurePath.put(path.getFileName().toString(), path.toString());
                            }
                        }
                    });
        } catch (IOException e) {
            // 处理可能的IO异常
            e.printStackTrace();
        }
    }


}
