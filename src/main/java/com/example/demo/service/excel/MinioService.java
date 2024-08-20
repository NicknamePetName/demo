package com.example.demo.service.excel;

import com.example.demo.model.Result;
import com.example.demo.model.excel.FigurePage;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface MinioService {
    Result<Map<String, FigurePage>> upload(MultipartFile multipartFile);
}
