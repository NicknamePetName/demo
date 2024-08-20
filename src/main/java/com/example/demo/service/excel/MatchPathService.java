package com.example.demo.service.excel;

import com.example.demo.model.Result;
import com.example.demo.model.excel.FigureMatch;

public interface MatchPathService {
    /**
     * 解析 Excel ——> 匹配 图纸 的结果
     * @param figureNumberPath 图号 (excel) 所在路径
     * @param figurePagePath   图纸 所在路径
     * @return Result<FigureMatch>
     */
    Result<FigureMatch> matchPath(String figureNumberPath, String figurePagePath);
}
