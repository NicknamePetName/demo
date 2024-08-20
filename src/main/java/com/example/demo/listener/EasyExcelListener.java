package com.example.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.fastjson2.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class EasyExcelListener extends AnalysisEventListener<Map<Integer, String>> {
    /**
     * 数据,返回解析出来的List
     */
    List<Map<Integer, String>> dataList = new ArrayList<>();
    /**
     * 正文起始行
     */
    private Integer headRowNumber;
    /**
     * 合并单元格,返回解析出来的合并单元格List
     */
    private List<CellExtra> extraMergeInfoList = new ArrayList<>();

    public EasyExcelListener(Integer headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        dataList.add(data);
    }

    /**
     * 读取额外信息：合并单元格
     */
    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        switch (extra.getType()) {
            case MERGE: {
                if (extra.getLastRowIndex() - extra.getFirstRowIndex() > 1  && extra.getFirstColumnIndex() >= 5 && extra.getLastColumnIndex() <= 6) {
                    log.info("读取到了一条额外信息:{}", JSON.toJSONString(extra));
                    extraMergeInfoList.add(extra);
                }
                break;
            }
            default: break;
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成！");
    }

}
