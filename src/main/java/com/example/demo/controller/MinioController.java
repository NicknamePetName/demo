package com.example.demo.controller;

import com.example.demo.model.Result;
import com.example.demo.model.excel.FigurePage;
import com.example.demo.service.excel.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioService minioService;

    /**
     * 图纸 图号 信息图库，图纸文件 上传至Minio
     * @param multipartFile .zip文件
     * @return Result<Void>
     */
    @PostMapping("/upload-zip")
    public Result<Map<String, FigurePage>> upload(@RequestBody MultipartFile multipartFile) {
        return minioService.upload(multipartFile);
    }

}
