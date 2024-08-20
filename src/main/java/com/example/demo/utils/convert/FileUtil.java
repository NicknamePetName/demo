package com.example.demo.utils.convert;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class FileUtil {

    /**
     * File 转换为 MultipartFile
     * @param file 转换对象
     * @return MultipartFile
     * @throws IOException e
     */
    public static MultipartFile toMultipartFile(File file) throws IOException {
        // 检查文件是否存在
        if (!file.exists() || !file.isFile()) {
            return null;
        }

        // 创建一个输入流
        try (InputStream inputStream = new FileInputStream(file)) {
            // 使用MockMultipartFile构造函数创建MultipartFile
            return new MockMultipartFile(
                    file.getName(), // 文件名
                    file.getName(), // 原始文件名
                    "application/octet-stream", // 内容类型，这里使用二进制流类型
                    inputStream // 输入流
            );
        }
    }

    /**
     * multipartFile 转换为 File
     * @param multipartFile 转换对象
     * @param destinationPath 保存地址( /x/x/x.txt )
     * @return File
     * @throws IOException e
     */
    public static File toFile(MultipartFile multipartFile, String destinationPath) throws IOException {
        // 检查MultipartFile是否为空
        if (multipartFile.isEmpty()) {
            return null;
        }

        // 创建File对象，指定转换后的文件存储路径
        File file = new File(destinationPath);

        // 将MultipartFile的内容转移到File对象中
        multipartFile.transferTo(file);

        // 返回转换后的File对象
        return file;
    }

}
