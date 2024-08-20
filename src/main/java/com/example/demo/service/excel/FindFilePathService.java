package com.example.demo.service.excel;

import com.example.demo.model.excel.FigureMatch;
import com.example.demo.model.excel.FindFilePath;

public interface FindFilePathService {
    FindFilePath fileWalker(String figureNumberPath, String figurePagePath);
}
