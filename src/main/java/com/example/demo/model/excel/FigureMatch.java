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
public class FigureMatch implements Serializable {

    /**
     * 所有图纸 文件名——路径
     */
    private Map<String,String> figurePagePathMap;

    /**
     * 重复文件名图纸
     */
    private List<FigurePage> reFigurePageList;

    /**
     * 异常文件(Excel)
     */
    private Map<String,String> exceptFileMap;

    /**
     * 所有的图号名&参数
     */
    private Map<String, FigureNumber> figureNumberMap;

    /**
     * 剩余未匹配的图纸
     */
    private Map<String, String> residueFigurePageMap;

    /**
     * 图号 匹配 图纸错误 (error:图号中包含括号等特殊字符)
     */
    private Map<String,FigureNumber> regularErrorFigureNumberMap;

    /**
     * 剩余未匹配到图纸的图号
     */
    private List<FigureNumber> residueFigureNumberList;

    /**
     * 图号 匹配 图纸 结果
     */
    private Map<FigureNumber, List<FigurePage>> result;

}
