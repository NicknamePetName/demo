package com.example.demo.controller.excel;

import com.example.demo.model.Result;
import com.example.demo.model.excel.FigureMatch;
import com.example.demo.service.excel.MatchPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
public class MatchPathController {

    @Autowired
    private MatchPathService matchPathService;

    @PostMapping("/result")
    public Result<FigureMatch> matchPath(@RequestParam("figureNumberPath") String figureNumberPath, @RequestParam("figurePagePath") String figurePagePath) {
        return matchPathService.matchPath(figureNumberPath, figurePagePath);
    }

}
