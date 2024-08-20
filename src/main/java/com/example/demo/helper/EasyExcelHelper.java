package com.example.demo.helper;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.example.demo.listener.EasyExcelListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EasyExcelHelper<T> {
    /**
     * 返回解析后的List
     *
     * @param: fileName 文件名
     * @param: clazz Excel对应属性名
     * @param: sheetNo 要解析的sheet
     * @param: headRowNumber 正文起始行
     * @return java.util.List<T> 解析后的List
     */
    public List<Map<Integer, String>> getList(File file, Integer sheetNo, Integer headRowNumber) {

        EasyExcelListener listener = new EasyExcelListener(headRowNumber);
        try {
            EasyExcelFactory.read(file, listener)
                    .excelType(ExcelTypeEnum.XLSX)
                    .extraRead(CellExtraTypeEnum.MERGE)
                    .sheet(sheetNo)
                    .headRowNumber(headRowNumber)
                    .doRead();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        List<CellExtra> extraMergeInfoList = listener.getExtraMergeInfoList();
        if (CollectionUtils.isEmpty(extraMergeInfoList)) {
            return listener.getDataList();
        }
        return explainMergeData(listener.getDataList(), extraMergeInfoList, headRowNumber);
    }

    public List<Map<Integer, String>> getList(InputStream inputStream, Integer sheetNo, Integer headRowNumber) {

        EasyExcelListener listener = new EasyExcelListener(headRowNumber);
        try {
            EasyExcelFactory.read(inputStream, listener)
                    .excelType(ExcelTypeEnum.XLSX)
                    .extraRead(CellExtraTypeEnum.MERGE)
                    .sheet(sheetNo)
                    .headRowNumber(headRowNumber)
                    .doRead();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        List<CellExtra> extraMergeInfoList = listener.getExtraMergeInfoList();
        if (CollectionUtils.isEmpty(extraMergeInfoList)) {
            return listener.getDataList();
        }
        return explainMergeData(listener.getDataList(), extraMergeInfoList, headRowNumber);
    }

    /**
     * 处理合并单元格
     *
     * @param data               解析数据
     * @param extraMergeInfoList 合并单元格信息
     * @param headRowNumber      起始行
     * @return 填充好的解析数据
     */
    private List<Map<Integer, String>> explainMergeData(List<Map<Integer, String>> data, List<CellExtra> extraMergeInfoList, Integer headRowNumber) {
        //循环所有合并单元格信息
        extraMergeInfoList.forEach(cellExtra -> {
            int firstRowIndex = cellExtra.getFirstRowIndex();
            int firstColumnIndex = cellExtra.getFirstColumnIndex();
            int lastRowIndex = cellExtra.getLastRowIndex();
            int lastColumnIndex = cellExtra.getLastColumnIndex();
            // 获取初始值
            String initValue = getInitValueFromList(firstRowIndex, firstColumnIndex, data);
            // 设置值
            for (int i = firstRowIndex; i <= lastRowIndex; i++) {
                for (int j = firstColumnIndex; j <= lastColumnIndex; j++) {
                    setInitValueToList(initValue, i-1, j, data);
                }
            }
        });
        return data;
    }

    /**
     * 设置合并单元格的值
     *
     * @param filedValue  值
     * @param rowIndex    行
     * @param columnIndex 列
     * @param data        解析数据
     */
    public void setInitValueToList(String filedValue, Integer rowIndex, Integer columnIndex, List<Map<Integer, String>> data) {
        Map<Integer, String> object = data.get(rowIndex);
        object.put(columnIndex, String.valueOf(filedValue));
    }

    /**
     * 获取合并单元格的初始值
     * rowIndex对应list的索引
     * columnIndex对应实体内的字段
     *
     * @param firstRowIndex    起始行
     * @param firstColumnIndex 起始列
     * @param data             列数据
     * @return 初始值
     */
    private String getInitValueFromList(Integer firstRowIndex, Integer firstColumnIndex, List<Map<Integer, String>> data) {
        return data.get(firstRowIndex - 1).get(firstColumnIndex);
    }


    /**
     * 查看合并的单元格
     * @param inputStream
     * @param sheetNo
     * @param headRowNumber
     * @return
     */

    public List<CellExtra> getExtraMergeInfoList(InputStream inputStream, Integer sheetNo, Integer headRowNumber) {
        EasyExcelListener listener = new EasyExcelListener(headRowNumber);
        try {
            EasyExcelFactory.read(inputStream, listener)
                    .excelType(ExcelTypeEnum.XLSX)
                    .extraRead(CellExtraTypeEnum.MERGE)
                    .sheet(sheetNo)
                    .headRowNumber(headRowNumber)
                    .doRead();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return listener.getExtraMergeInfoList();
    }


}
