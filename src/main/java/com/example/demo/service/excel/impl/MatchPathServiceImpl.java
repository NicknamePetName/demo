package com.example.demo.service.excel.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.example.demo.helper.EasyExcelHelper;
import com.example.demo.model.Result;
import com.example.demo.model.excel.FigureMatch;
import com.example.demo.model.excel.FigureNumber;
import com.example.demo.model.excel.FigurePage;
import com.example.demo.service.excel.MatchPathService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MatchPathServiceImpl implements MatchPathService {

    private final static FigureMatch figureMatch = new FigureMatch();
    private static final Logger log = LoggerFactory.getLogger(MatchPathServiceImpl.class);

    @Autowired
    private EasyExcelHelper easyExcelHelper;


    /**
     * 返回匹配结果
     * @param figureNumberPath 图号 (excel) 的路径
     * @param figurePagePath   图纸 的 路径
     * @return Result<FigureMatch>
     */
    @Override
    public Result<FigureMatch> matchPath(String figureNumberPath, String figurePagePath) {

        // 初始化 FigureMatch
        init();

        Result<FigureMatch> result = new Result<>();
        // 所有图纸 和 重复文件名图纸
        long time1 = System.currentTimeMillis();
        getAllFigurePagePath(figurePagePath);
        long time2 = System.currentTimeMillis();
        System.out.println("加载图纸所用时间：" + (time2 - time1) + "ms");

        listFiles(figureNumberPath);
        long time3 = System.currentTimeMillis();
        System.out.println("解析Excel所用时间：" + (time3 - time2) + "ms");



        figureMatch.getFigureNumberMap().forEach((figureKey, figureValue) -> {
            List<FigurePage> figurePageList = new ArrayList<>();
            figureMatch.getFigurePagePathMap().forEach((pathKey, pathValue) -> {
                try {
                    if (regular(figureKey, pathKey)) {
                        FigurePage figurePage = new FigurePage();
                        figurePage.setFileName(pathKey);
                        figurePage.setFilePath(pathValue);
                        figurePageList.add(figurePage);
                        // 匹配到的图纸文件名删除，剩下的就是没有匹配到的图纸
                        figureMatch.getResidueFigurePageMap().remove(pathKey);
                    }
                } catch (Exception e) {
//                    System.out.println("正则匹配图号错误：" + figureKey);
                    figureMatch.getRegularErrorFigureNumberMap().put(figureKey, figureValue);
                }
            });


            if (figurePageList.isEmpty()) {
                // 剩余图号
                figureMatch.getResidueFigureNumberList().add(figureValue);
            }
            figureMatch.getResult().put(figureValue, figurePageList);
        });
        long time4 = System.currentTimeMillis();
        System.out.println("图号匹配图纸所用时间：" + (time4 - time3) + "ms");

        System.out.println("全部图纸文件数：" + figureMatch.getFigurePagePathMap().size());
        System.out.println("全部图号数：" + figureMatch.getFigureNumberMap().size());
        System.out.println("第一次解析Excel异常文件数：" + figureMatch.getExceptFileMap().size());
        System.out.println("正则表达式匹配异常(因为 图号 带括号等字符)数：" + figureMatch.getRegularErrorFigureNumberMap().size());
        System.out.println("重复的图纸文件名数：" + figureMatch.getReFigurePageList().size());
        System.out.println("剩余图纸文件数：" + figureMatch.getResidueFigurePageMap().size());
        System.out.println("剩余图号数：" + figureMatch.getResidueFigureNumberList().size());

        result.setSuccess(true);
        result.setCode("200");
        result.setData(figureMatch);
        result.setMessage("图号匹配图纸成功");
        return result;
    }

    private void init() {
        figureMatch.setFigurePagePathMap(new LinkedHashMap<>());
        figureMatch.setReFigurePageList(new ArrayList<>());
        figureMatch.setExceptFileMap(new HashMap<>());
        figureMatch.setFigureNumberMap(new LinkedHashMap<>());
        figureMatch.setResidueFigurePageMap(new HashMap<>());
        figureMatch.setRegularErrorFigureNumberMap(new HashMap<>());
        figureMatch.setResidueFigureNumberList(new ArrayList<>());
        figureMatch.setResult(new HashMap<>());
    }


    /**
     * 遍历图纸，将图纸文件名，路径加载到内存中（hashMap）
     */
    private void getAllFigurePagePath(String figurePagePath) {

        try (Stream<Path> paths = Files.walk(Paths.get(figurePagePath))) {
            // 过滤出所有普通文件，获取路径
            paths.filter(Files::isRegularFile)
                    .forEach(path -> {
                        String suffix = FileUtil.getSuffix(path.getFileName().toString());
                        final List<String> validFileSuffix = Arrays.asList("dwg", "DWG", "stp", "STP", "sldprt", "SLDPRT", "sldasm", "SLDASM", "SLDDRW", "slddrw");
                        //  是否有重复key,有则保存
                        if (validFileSuffix.contains(suffix) && !path.getFileName().toString().startsWith("~$")
                        ) {
                            if (figureMatch.getFigurePagePathMap().get(path.getFileName().toString()) != null) {
                                FigurePage figurePage = new FigurePage();
                                figurePage.setFileName(path.getFileName().toString());
                                figurePage.setFilePath(path.toString());
                                figureMatch.getReFigurePageList().add(figurePage);
                            } else {
                                figureMatch.getFigurePagePathMap().put(path.getFileName().toString(), path.toString());
                            }
                        }
                    });
            figureMatch.getResidueFigurePageMap().putAll(figureMatch.getFigurePagePathMap());
        } catch (IOException e) {
            // 处理可能的IO异常
            log.error("加载图纸到内存中异常！！！");
        }
    }

    public static void main(String[] args) {
        List<String> validFileSuffix = Arrays.asList("dwg", "DWG", "stp", "STP", "sldprt", "SLDPRT", "sldasm", "SLDASM", "SLDDRW", "slddrw");

        Object[] array = validFileSuffix.toArray();
        System.out.println(Arrays.toString(array));

        String[] arr = {"a","b","c"};
        List<String> stringList = Arrays.asList(arr);
        System.out.println(stringList);
        arr[0] = "aa";
        System.out.println(stringList + "\n");
        System.out.println(validFileSuffix);

    }

    /**
     * 递归遍历给定文件夹及其子文件夹中的所有文件
     * @param figureNumberPath 图号 (Excel) 路径
     */
     public void listFiles(String figureNumberPath) {

        File folder = new File(figureNumberPath);

        // 获取当前文件夹中的所有文件和子文件夹
        File[] files = folder.listFiles();
        if (files != null) {  // 检查files是否为空，以防止NullPointerException
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹，递归调用listFiles方法
                    listFiles(file.getAbsolutePath());
                } else {
                    // 如果是文件，打印文件的绝对路径
//                    System.out.println(file.getAbsolutePath());
                    // 打印文件名
                    String suffix = FileUtil.getSuffix(file.getName());
                    if (suffix.equalsIgnoreCase("xlsx") && !file.getName().startsWith("~$")) {
                        // 解析Excel
                        parseContent(file);
                    }
                }
            }
        }
    }


    /**
     * 解析 Excel - 图号
     * @param file excel文件
     */

    private void parseContent(File file) {
        // 解析的单个文件的数据
        List<Map<Integer, String>> mapList = null;
        try {
            mapList = easyExcelHelper.getList(file, 0, 0);
            mapList = mapList.isEmpty() ? easyExcelHelper.getList(file, 1, 0) : mapList;
        } catch (Exception e) {
            log.error("解析Excel异常！！！");
        }
        if (mapList == null || mapList.isEmpty()) {
            return;
        }

        int index = 0;
        int figureIndex = 0;
        int plyIndex = 0;
        int widthIndex = 0;
        int lengthIndex = 0;
        boolean isBreak = false;
        int designerIndex = 0;

        for (Map<Integer, String> map : mapList) {
            for (Integer key : map.keySet()) {
                if ("图号".equals(map.getOrDefault(key, ""))) {
                    figureIndex = key;
                }
                if ("厚度（mm)".equals(map.getOrDefault(key, ""))) {
                    plyIndex = key;
                }
                if ("宽度（mm)".equals(map.getOrDefault(key, ""))) {
                    widthIndex = key;
                }
                if ("长度（mm)".equals(map.getOrDefault(key, ""))) {
                    lengthIndex = key;
                    break;
                }
                if ("铜排设计员：".equals(map.getOrDefault(key, ""))) {
                    isBreak = true;
                    break;
                }
            }
            if (lengthIndex == 0) index++;

            if (!isBreak) {
                designerIndex++;
            }else {
                break;
            }
        }

        if (index >= mapList.size() || designerIndex == 0 || plyIndex ==0 || widthIndex == 0 || lengthIndex == 0) {
//            System.out.println("———异常文件：" + file.getName());
            figureMatch.getExceptFileMap().put(file.getName(), file.getAbsolutePath());
            return;
        }

        for (int i = index + 1; i < designerIndex; i++) {

            if (mapList.get(i).get(figureIndex) == null || mapList.get(i).get(plyIndex) == null &&
                mapList.get(i).get(widthIndex) == null && mapList.get(i).get(lengthIndex) == null &&
                mapList.get(i).get(lengthIndex + 1) == null && mapList.get(i).get(lengthIndex + 2) == null)
                continue;

            FigureNumber figureNumber = new FigureNumber();
            figureNumber.setFigureNumber(mapList.get(i).get(figureIndex));
            figureNumber.setPly(mapList.get(i).get(plyIndex));
            figureNumber.setWidth(mapList.get(i).get(widthIndex));
            figureNumber.setLength(mapList.get(i).get(lengthIndex));
            figureMatch.getFigureNumberMap().put(mapList.get(i).get(figureIndex),figureNumber);
//            System.out.println(figureNumber);
        }
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
        // 正则表达式，添加前瞻来排除以 ~$ 开头的字符串 String.format("^(?!\\~\\$).*%s", patternString)
        Pattern pattern = Pattern.compile(String.format("^%s.*", patternString), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        // Check if the pattern matches
        return matcher.find();
    }


}
