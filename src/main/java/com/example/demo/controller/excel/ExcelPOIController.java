package com.example.demo.controller.excel;

import com.example.demo.model.excel.Project;
import com.example.demo.model.excel.Drawing;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping("/poi")
public class ExcelPOIController {

    @PostMapping("/drawing")
    public List<Drawing> drawing(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IOException("上传的文件为空");
        }
        // MultipartFile => File
        // 获取系统的临时目录路径
        String tempDirPath = System.getProperty("java.io.tmpdir");
        // 创建目标文件的完整路径
        File file = new File(tempDirPath, multipartFile.getOriginalFilename());


        // 将MultipartFile的内容写入到destFile中
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            // 处理可能发生的IOException
            throw new IOException("文件写入失败", e);
        }


        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(file));

        XSSFSheet sheetAt = sheets.getSheetAt(0);

        List<CellRangeAddress> mergedRegions = sheetAt.getMergedRegions();

        List<Drawing> lists = new ArrayList<>();

        for (int rowNum = 10; rowNum <= sheetAt.getLastRowNum() - 7; rowNum++) {
            Row row = sheetAt.getRow(rowNum);
            if (row != null) {
                Drawing drawing = new Drawing();
                int cellIndex = 0;
                for (Cell cell : row) {
                    // 检查单元格是否是合并单元格的一部分
                    Cell mergedCell = null;
                    Object mergedCellValue = null;
                    for (CellRangeAddress mergedRegion : mergedRegions) {
                        if (mergedRegion.isInRange(rowNum, cellIndex)) {
                            // 获取合并单元格的起始单元格
                            mergedCell = sheetAt.getRow(mergedRegion.getFirstRow()).getCell(mergedRegion.getFirstColumn());
                            if (mergedCell != null) {
                                mergedCellValue = switch (mergedCell.getCellType()) {
                                    case STRING -> mergedCell.getStringCellValue();
                                    default -> "";
                                };

                                // 打印或使用mergedCellValue
//                                System.out.println("合并单元格的值: " + mergedCellValue);

                            }
                            break;
                        }
                    }
//                    if (!isMerged) {
                    // 处理单元格数据
                    String cellValue = switch (cell.getCellType()) {
                        case STRING -> cell.getStringCellValue();
                        case NUMERIC -> String.valueOf(cell.getNumericCellValue());
                        case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
                        case FORMULA -> String.valueOf(cell.getNumericCellValue());
                        case BLANK -> mergedCellValue != null ? mergedCellValue.toString() : "";
                        default -> ""; // 空单元格
                        // 其他情况可以继续添加
                    };
                    // 假设第一列是名字，第二列是年龄，第三列是邮箱
                    switch (cellIndex) {
                        case 3:
                            drawing.setDrawingCode(cellValue);
                            break;
                        case 5:
                            drawing.setDescription(cellValue);
                            break;
                        case 7:
                            drawing.setThickness((int) Double.parseDouble(cellValue));
                            break;
                        case 8:
                            drawing.setWidth((int) Double.parseDouble(cellValue));
                            break;
                        case 9:
                            drawing.setLength((int) Double.parseDouble(cellValue));
                            break;
                    }
//                    }
                    cellIndex++;
                }
                lists.add(drawing);
            }
        }
        sheets.close();
        return lists;
    }

    @PostMapping("/project")
    public Project project(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        Project project = new Project();

        if (multipartFile.isEmpty()) {
            throw new IOException("上传的文件为空");
        }
        // MultipartFile => File
        // 获取系统的临时目录路径
        String tempDirPath = System.getProperty("java.io.tmpdir");
        // 创建目标文件的完整路径
        File file = new File(tempDirPath, multipartFile.getOriginalFilename());


        // 将MultipartFile的内容写入到destFile中
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            // 处理可能发生的IOException
            throw new IOException("文件写入失败", e);
        }


        XSSFWorkbook sheets = new XSSFWorkbook(new FileInputStream(file));

        XSSFSheet sheetAt = sheets.getSheetAt(0);

        int lastRowNum = sheetAt.getLastRowNum();
        if (sheetAt.getRow(lastRowNum).getCell(2).getStringCellValue() == null) {
            lastRowNum -= 1;
        }

        String listBelong = sheetAt.getRow(1).getCell(9).getStringCellValue();
        project.setListBelong(listBelong);

        String CodeNumber = sheetAt.getRow(2).getCell(9).getStringCellValue();

        System.out.println(CodeNumber);

        Pattern pattern = Pattern.compile("表码：(.*?) 编号：(.*)"); // 正则表达式，捕获表码
        Matcher matcher = pattern.matcher(CodeNumber);
        if (matcher.find()) {
            String listCode = matcher.group(1); // 提取捕获组中的表码
            project.setListCode(listCode);
//            System.out.println("表码：" + code);
            String listNumber = matcher.group(2).strip(); // 提取捕获组中的表码
            project.setListNumber(listNumber);
//            System.out.println("编号：" + number);
        }

        String projectName = sheetAt.getRow(4).getCell(4).getStringCellValue();
        project.setProjectName(projectName);
        String projectNumber = sheetAt.getRow(4).getCell(9).getStringCellValue();
        project.setProjectNumber(projectNumber);
        String containerType = sheetAt.getRow(5).getCell(4).getStringCellValue();
        project.setContainerType(containerType);
        String stringContainerQuantity = switch (sheetAt.getRow(5).getCell(6).getCellType()) {
            case NUMERIC -> String.valueOf((int) sheetAt.getRow(5).getCell(6).getNumericCellValue());
            case STRING -> sheetAt.getRow(5).getCell(6).getStringCellValue();
            default -> "";
        };
        Pattern pattern1 = Pattern.compile("\\d+");
        Matcher matcher1 = pattern1.matcher(stringContainerQuantity);
        // 检查是否找到匹配的数字
        if (matcher1.find()) {
            // 提取匹配的数字
            String quantity = matcher1.group();
            // 将提取的字符串转换为整数
            int containerQuantity = Integer.parseInt(quantity);
            project.setContainerQuantity(containerQuantity);
//            System.out.println("容器数量：" + containerQuantity);
        }

        String orderNumber = sheetAt.getRow(5).getCell(9).getStringCellValue();
        project.setOrderNumber(orderNumber);

        String surfaceTreatment = sheetAt.getRow(6).getCell(9).getStringCellValue();
        project.setSurfaceTreatment(surfaceTreatment);


        String designerUser = sheetAt.getRow(lastRowNum - 5).getCell(4).getStringCellValue();
        project.setDesignerUser(designerUser);

        switch (sheetAt.getRow(lastRowNum - 5).getCell(6).getCellType()) {
            case NUMERIC:
                int intDesignDate = (int) sheetAt.getRow(lastRowNum - 5).getCell(6).getNumericCellValue();
                LocalDateTime intDesignDateTime = toLocalDataTime(intDesignDate);
                project.setDesignDate(intDesignDateTime);
                break;
            case STRING:
                String strDesignDate = sheetAt.getRow(lastRowNum - 5).getCell(6).getStringCellValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                LocalDate date = LocalDate.parse(strDesignDate, formatter);
                LocalDateTime strDesignDateTime = date.atStartOfDay();
                project.setDesignDate(strDesignDateTime);
                break;
        }



        String reviewerUser = sheetAt.getRow(lastRowNum - 5).getCell(8).getStringCellValue();
        project.setReviewerUser(reviewerUser);

        int reviewDate = (int) sheetAt.getRow(lastRowNum - 5).getCell(11).getNumericCellValue();
        LocalDateTime reviewDateTime = toLocalDataTime(reviewDate);
        project.setReviewDate(reviewDateTime);

        String handoverUser = sheetAt.getRow(lastRowNum - 3).getCell(2).getStringCellValue().substring("交接签字：".length());
        project.setHandoverUser(handoverUser);

        String productionUser = sheetAt.getRow(lastRowNum - 2).getCell(3).getStringCellValue();
        project.setProductionUser(productionUser);

        int productionDate = (int) sheetAt.getRow(lastRowNum - 2).getCell(6).getNumericCellValue();
        LocalDateTime productionDateTime = toLocalDataTime(productionDate);
        project.setProductionDate(productionDateTime);


        String outboundUser = sheetAt.getRow(lastRowNum - 2).getCell(8).getStringCellValue();
        project.setProductionUser(outboundUser);

        int outboundDate = (int) sheetAt.getRow(lastRowNum - 2).getCell(11).getNumericCellValue();
        LocalDateTime outboundDateTime = toLocalDataTime(outboundDate);
        project.setProductionDate(outboundDateTime);


        return project;
    }

    private LocalDateTime toLocalDataTime(int day) {
        Calendar calendar = new GregorianCalendar(1900, 0, -1); // 修正这里
        Date date = DateUtils.addDays(calendar.getTime(), day);
        java.time.Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}



