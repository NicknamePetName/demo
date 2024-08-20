package com.example.demo.service.excel.impl;

import com.example.demo.model.Result;
import com.example.demo.service.excel.FileService;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public Result<String> saveFile(MultipartFile multipartFile) {

        Result<String> result = Result.create();
        result.setSuccess(false);

        // Define the directories for temp and extracted files
        String resourcesDir = new File("src/main/resources").getAbsolutePath();
        String tempDir = resourcesDir + File.separator + "temp";
        String destDir = resourcesDir + File.separator + "file";

        File tempFile = null;
        File tempDirectory = new File(tempDir);
        File destDirectory = new File(destDir);

        try {
            // Ensure temp directory exists
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }
            // Ensure destination directory exists
            if (!destDirectory.exists()) {
                destDirectory.mkdirs();
            }

            // Create a temp file in the resources/temp directory
            tempFile = File.createTempFile("upload", ".zip", tempDirectory);
            multipartFile.transferTo(tempFile); // Save uploaded file to temp file

            // Unzip file
            unzip(tempFile, destDir);

            // Delete temp file
            if (tempDirectory.exists()) {
                tempFile.delete();
            }

            result.setSuccess(true);
            result.setCode("200");
            result.setData(destDir);
            result.setMessage("文件上传至服务器成功");

            return result;
        } catch (IOException e) {
            result.setCode("405");
            result.setMessage(e.getMessage());
            return result;
        }
    }

    private void unzip(File zipFile, String destDir) throws IOException {
        File destDirectory = new File(destDir);
        if (!destDirectory.exists()) {
            destDirectory.mkdir();
        }

        try (ZipFile zip = new ZipFile(zipFile, "GBK")) {
            Enumeration<ZipArchiveEntry> entries = zip.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                String filePath = destDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zip.getInputStream(entry), filePath);
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
            }
        }
    }

    private void extractFile(InputStream zipIn, String filePath) throws IOException {
        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(Path.of(filePath)))) {
            byte[] bytesIn = new byte[4096];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    public void delFile() {
        String resourcesDir = new File("src/main/resources").getAbsolutePath();
        String destDir = resourcesDir + File.separator + "file";
        File destDirectory = new File(destDir);
        if (destDirectory.exists()) {
            FileUtils.deleteQuietly(destDirectory);
        }
    }




}
