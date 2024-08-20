package com.example.demo.service.excel.impl;

import com.example.demo.dao.excel.FigureNumberDAO;
import com.example.demo.dao.excel.FigurePageDAO;
import com.example.demo.dataobject.excel.FigureNumberDO;
import com.example.demo.dataobject.excel.FigurePageDO;
import com.example.demo.model.Result;
import com.example.demo.model.excel.FigureMatch;
import com.example.demo.model.excel.FigureNumber;
import com.example.demo.model.excel.FigurePage;
import com.example.demo.service.excel.MatchPathService;
import com.example.demo.service.excel.MinioService;
import com.example.demo.service.excel.FileService;
import com.example.demo.utils.convert.FigureNumberUtil;
import com.example.demo.utils.convert.FigurePageUtil;
import com.example.demo.utils.convert.FileUtil;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MinioServiceImpl implements MinioService {

    @Autowired
    private FileService fileService;

    @Autowired
    private MatchPathService matchPathService;

    @Autowired
    private FigureNumberDAO figureNumberDAO;

    @Autowired
    private FigurePageDAO figurePageDAO;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public Result<Map<String, FigurePage>> upload(MultipartFile multipartFile) {
        Result<Map<String, FigurePage>> result = new Result<>();
        Map<String, FigurePage> map = new HashMap<>();
        result.setSuccess(false);
        Result<String> saveFileResult = fileService.saveFile(multipartFile);

        if (!saveFileResult.isSuccess()) {
            result.setCode("301");
            result.setMessage("文件保存至服务器失败！");
            return result;
        }
        String dirPath = saveFileResult.getData();


        Result<FigureMatch> figureMatchResult = matchPathService.matchPath(dirPath, dirPath);

        if (!figureMatchResult.isSuccess()) {
            result.setCode("302");
            result.setMessage("文件解析&匹配异常！");
            return result;
        }

        FigureMatch figureMatch = figureMatchResult.getData();
        // 匹配结果
        Map<FigureNumber, List<FigurePage>> regularResultMap = figureMatch.getResult();

        for (Map.Entry<FigureNumber, List<FigurePage>> entry : regularResultMap.entrySet()) {
            FigureNumber key = entry.getKey();
            List<FigurePage> value = entry.getValue();
            if (!value.isEmpty()) {
                for (FigurePage figurePage : value) {
                    FigurePageDO figurePageDO = figurePageDAO.findByFileName(figurePage.getFileName());
                    if ( figurePageDO != null) {
                        key.setFigurePageId(figurePageDO.getId());
                        figureNumberDAO.insert(FigureNumberUtil.toDO(key));
                        continue;
                    }

                    try {
                        File file = new File(figurePage.getFilePath());
                        if (!file.exists() || !file.isFile()) {
                            continue;
                        }
                        MultipartFile toMultipartFile = FileUtil.toMultipartFile(file);

                        String path = getUploadPath(figurePage.getFilePath());
                        String url = uploadMinio(toMultipartFile, path);
                        if (url == null) {
                            figurePage.setFilePath(figureMatch.getFigurePagePathMap().get(figurePage.getFileName()));
                            map.put(key.getFigureNumber(), figurePage);
                            continue;
                        }

                        FigurePage figurePageDB = new FigurePage();
                        figurePageDB.setFileName(figurePage.getFileName());
                        figurePageDB.setFilePath(url);

                        if (!toSaveDB(key, figurePageDB)){
                            // 入库失败删除 Minio文件
                            fileStorageService.delete(url);
                            figurePage.setFilePath(figureMatch.getFigurePagePathMap().get(figurePage.getFileName()));
                            map.put(key.getFigureNumber(), figurePage);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else {
                if (figureNumberDAO.insert(FigureNumberUtil.toDO(key)) < 1) {
                    map.put(key.getFigureNumber(), null);
                }
            }
        }

        fileService.delFile();

        if (map.isEmpty()) {
            result.setSuccess(true);
            result.setCode("200");
            result.setMessage("文件上传至Minio成功");
        }else {
            result.setCode("303");
            result.setData(map);
            result.setMessage("异常文件");
        }

        return result;
    }

    @Transactional
    protected Boolean toSaveDB(FigureNumber figureNumber, FigurePage figurePage) {
        if (figurePage == null || figureNumber == null) {
            return false;
        }

        FigurePageDO figurePageDO = FigurePageUtil.toDO(figurePage);
        FigureNumberDO figureNumberDO = FigureNumberUtil.toDO(figureNumber);

        if (figurePageDAO.insert(figurePageDO) < 1 || figurePageDO.getId() < 1) {
            return false;
        }
        figureNumberDO.setFigurePageId(figurePageDO.getId());
        return figureNumberDAO.insert(figureNumberDO) >= 1 && figureNumberDO.getId() >= 1;
    }

    private String uploadMinio(MultipartFile multipartFile, String path) {
         FileInfo fileInfo =  fileStorageService.of(multipartFile)
                .setPath(path)
                .setSaveFilename(multipartFile.getOriginalFilename())
                .upload();
         return fileInfo == null ? null : fileInfo.getUrl();
    }

    private String getUploadPath(String filePath) {
        String rePath = filePath.replace("\\", "/").replace("-","_");


        String regex = ".*?/src/main/resources/file/(.*)/";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rePath);
        if (!matcher.find()) return null;

        int year = LocalDateTime.now().getYear();
        int month = LocalDateTime.now().getMonthValue(); // 月份从1开始
        int day = LocalDateTime.now().getDayOfMonth();

        return matcher.group(1) + "/" + year + "-" + month + "-" + day + "/";
    }


}
