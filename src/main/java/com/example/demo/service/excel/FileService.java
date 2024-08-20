package com.example.demo.service.excel;

import com.example.demo.model.Result;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * 保存 .zip 文件到 服务器 classpath 路径下
     * @param multipartFile 文件流
     * @return Result<String> String 是 保存文件
     */
    Result<String> saveFile(MultipartFile multipartFile);

    void delFile();
}
