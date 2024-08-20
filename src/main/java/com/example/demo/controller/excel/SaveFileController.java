package com.example.demo.controller.excel;

import com.example.demo.model.Result;
import com.example.demo.service.excel.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class SaveFileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/save")
    public Result<String> saveFile(@RequestBody MultipartFile multipartFile) {
        return fileService.saveFile(multipartFile);
    }

}
