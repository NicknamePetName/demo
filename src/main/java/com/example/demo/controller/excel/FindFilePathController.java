package com.example.demo.controller.excel;

import com.example.demo.model.excel.FigureMatch;
import com.example.demo.model.excel.FindFilePath;
import com.example.demo.service.excel.FindFilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/local")
public class FindFilePathController {
    @Autowired
    private FindFilePathService findFilePathService;

    @PostMapping("/upload")
    public FindFilePath fileWalker(@RequestParam("figureNumberPath") String figureNumberPath, @RequestParam("figurePagePath") String figurePagePath) {
        return findFilePathService.fileWalker(figureNumberPath, figurePagePath);
    }
}
