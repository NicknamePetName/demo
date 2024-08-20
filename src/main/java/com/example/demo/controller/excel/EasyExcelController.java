package com.example.demo.controller.excel;

import com.alibaba.excel.metadata.CellExtra;
import com.example.demo.helper.EasyExcelHelper;
import com.example.demo.model.excel.Drawing;
import com.example.demo.model.excel.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/easy")
public class EasyExcelController {

    @Autowired
    private EasyExcelHelper easyExcelHelper;


    @PostMapping("/drawing")
    public List<Drawing> drawing(@RequestPart("file") MultipartFile multipartFile) {
        List<Map<Integer, String>> mapList = null;
        try {
            mapList = easyExcelHelper.getList(multipartFile.getInputStream(), 0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (mapList.isEmpty()) {
            try {
                mapList = easyExcelHelper.getList(multipartFile.getInputStream(), 1, 0);
            } catch (IOException e) {

            }
        }

        if (mapList.isEmpty()) {
            return null;
        }

        int startRow = 0;

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
            startRow++;
        }

        List<Drawing> drawingList = new ArrayList<>();
        for (int i = startRow + 1; i < mapList.size() - 7; i++) {
            if ( i > 20 && mapList.get(i).get(3) == null) {
                break;
            }
            if (mapList.get(i).get(7) == null && mapList.get(i).get(8) == null && mapList.get(i).get(9) == null) {
                // todo
                continue;
            }
            Map<Integer, String> map = mapList.get(i);
            Drawing drawing = new Drawing();
            drawing.setDrawingCode(map.get(3));

            drawing.setDescription(map.get(5));
            drawing.setThickness((int) (Double.parseDouble(map.get(7)) * 1000));
            drawing.setWidth((int) (Double.parseDouble(map.get(8)) * 1000));
            drawing.setLength((int) (Double.parseDouble(map.get(9)) * 1000));

            drawingList.add(drawing);
        }
        return drawingList;
    }

    @PostMapping("/merge")
    public List<CellExtra> getExtraMergeInfoList(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        return easyExcelHelper.getExtraMergeInfoList(multipartFile.getInputStream(), 0, 0);
    }

    @PostMapping("/project")
    public Project project(@RequestPart("file") MultipartFile multipartFile) {
        List<Map<Integer, String>> mapList = null;

        try {
            mapList = easyExcelHelper.getList(multipartFile.getInputStream(), 0, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (mapList.isEmpty()) {
            try {
                mapList = easyExcelHelper.getList(multipartFile.getInputStream(), 1, 0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (mapList.isEmpty()) {
            return null;
        }

        int lastRowNum = mapList.size() - 1;

        Project project = new Project();
        project.setProjectName(mapList.get(3).get(4));
        project.setProjectNumber(mapList.get(3).get(9));
        project.setContainerType(mapList.get(4).get(4));

        Pattern pattern1 = Pattern.compile("\\d+");
        Matcher matcher1 = pattern1.matcher(mapList.get(4).get(6));
        // 检查是否找到匹配的数字
        if (matcher1.find()) {
            // 提取匹配的数字
            String quantity = matcher1.group();
            // 将提取的字符串转换为整数
            int containerQuantity = Integer.parseInt(quantity);
            project.setContainerQuantity(containerQuantity);
//            System.out.println("容器数量：" + containerQuantity);
        }
        project.setOrderNumber(mapList.get(4).get(9));
        project.setSurfaceTreatment(mapList.get(6).get(9));
        project.setListBelong(mapList.get(0).get(9));

        Pattern pattern = Pattern.compile("表码：(.*?) 编号：(.*)"); // 正则表达式，捕获表码
        Matcher matcher = pattern.matcher(mapList.get(1).get(9));
        if (matcher.find()) {
            String listCode = matcher.group(1); // 提取捕获组中的表码
            project.setListCode(listCode);
//            System.out.println("表码：" + listCode);
            String listNumber = matcher.group(2).strip(); // 提取捕获组中的表码
            project.setListNumber(listNumber);
//            System.out.println("编号：" + listNumber);
        }

        while (mapList.get(lastRowNum).get(2) == null || !"铜排设计员：".equals(mapList.get(lastRowNum).getOrDefault(2,""))) {
            --lastRowNum;
        }
        project.setDesignerUser(mapList.get(lastRowNum).get(4));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy.M.dd");

        try {
            LocalDate date = LocalDate.parse(mapList.get(lastRowNum).get(6), formatter);
            LocalDateTime strDesignDateTime = date.atStartOfDay();
            project.setDesignDate(strDesignDateTime);
        }catch (Exception e) {
            try {
                LocalDate date = LocalDate.parse(mapList.get(lastRowNum).get(6), formatter2);
                LocalDateTime strDesignDateTime = date.atStartOfDay();
                project.setDesignDate(strDesignDateTime);
            }catch (Exception e3) {
                try {
                    LocalDate date = LocalDate.parse(mapList.get(lastRowNum).get(6), formatter3);
                    LocalDateTime strDesignDateTime = date.atStartOfDay();
                    project.setDesignDate(strDesignDateTime);
                }catch (Exception e4) {
                    LocalDate date = LocalDate.parse(mapList.get(lastRowNum).get(6), formatter4);
                    LocalDateTime strDesignDateTime = date.atStartOfDay();
                    project.setDesignDate(strDesignDateTime);
                }
            }
        }

        project.setReviewerUser(mapList.get(lastRowNum).get(8));

        if (mapList.get(lastRowNum).get(11) != null) {
            project.setReviewDate(LocalDate.parse(mapList.get(lastRowNum).get(11), formatter3).atStartOfDay());
        }
        System.out.println();

        project.setHandoverUser(mapList.get(lastRowNum + 2).get(2).substring("交接签字：".length()));
        project.setProductionUser(mapList.get(lastRowNum + 3).get(4));

        if (mapList.get(lastRowNum + 3).get(6) != null) {
            project.setProductionDate(LocalDate.parse(mapList.get(lastRowNum + 3).get(6), formatter3).atStartOfDay());
        }
        project.setOutboundUser(mapList.get(lastRowNum + 3).get(8));

        if (mapList.get(lastRowNum + 3).get(11) != null) {
            project.setOutboundDate(LocalDate.parse(mapList.get(lastRowNum + 3).get(11), formatter3).atStartOfDay());
        }
        return project;
    }
}
