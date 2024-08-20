package com.example.demo.model.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindFilePath implements Serializable {
    // 图纸文件名&路径
    private Map<String, String> mapFigurePath;
    // 第一次解析excel异常 (原 D 列, 现 C 列)
    private Map<String, String> mapAbnormalFile;
    // 第二次解析excel异常 (C 列正常, 图表格式异常,需要重新解析，例：NT10_项目汇总.xlsx)
    private Map<String, String> mapAbnormalFile2;
    // 第二次解析excel异常 (描述异常, 图表格式异常,需要重新解析，例：NS800双水_项目汇总.xlsx)
    private Map<String, String> mapAbnormalFile3;
    // 全体Excel中的图号
    private Map<String, Object> mapFigure;
    // 正则表达式 异常（因为 图号 带括号等字符）
    private Map<String, Object> mapErrorFigureNumber;
    // 重复的图纸文件名&路径
    private List<FigurePage> reFigurePageList;
    // 图号对应图纸结果
    private Map<String, List<String>> result;
    // clone 图纸文件名&路径 --> 剩余的图纸文件
    private Map<String, String> mapResidueFigurePage;
    // 图号未匹配到图纸的记录
    private List<String> noMateList;
    // 已匹配到图号的图纸数
    /**不同的图号匹配到 相同的图纸文件名(第一次匹配到，.remove已从hashMap中删除"总数减1"，
     * 第二次匹配到，.remove没有数据删除"总数未减1"),
     * 导致 剩余的图纸文件数 + 已匹配到图号的图纸数 > 全部图纸文件数
     * */
    private Integer iii;
}
