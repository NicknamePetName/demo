package com.example.demo.controller.excel;

import com.example.demo.model.Result;
import com.example.demo.model.excel.FigurePage;
import com.example.demo.service.excel.MinioService;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioService minioService;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 图纸 图号 信息图库，图纸文件 上传至Minio
     * @param multipartFile .zip文件
     * @return Result<Void>
     */
    @PostMapping("/upload-zip")
    public Result<Map<String, FigurePage>> upload(@RequestBody MultipartFile multipartFile) {
        return minioService.upload(multipartFile);
    }

    /**
     * 上传文件，成功返回文件 url
     */
    @PostMapping("/upload2")
    public String upload2(@RequestBody MultipartFile file) {
        FileInfo fileInfo = fileStorageService.of(file)
//                .setPath("/") //保存到相对路径下，为了方便管理，不需要可以不写
                .setSaveFilename("image.jpg") //设置保存的文件名，不需要可以不写，会随机生成
                .setObjectId("0")   //关联对象id，为了方便管理，不需要可以不写
                .setObjectType("0") //关联对象类型，为了方便管理，不需要可以不写
                .putAttr("role","admin") //保存一些属性，可以在切面、保存上传记录、自定义存储平台等地方获取使用，不需要可以不写
                .upload();  //将文件上传到对应地方
        return fileInfo == null ? "上传失败！" : fileInfo.getUrl();
    }


}
