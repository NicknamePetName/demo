package com.example.demo.model.excel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Drawing implements Serializable {

    private Integer id;
    private String drawingCode;
    private String version;
    private Integer thickness;
    private Integer width;
    private Integer length;
    private String filename;
    private String storePath;
    private String modelType;
    private String description;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer createUser;
    private Integer updateUser;
}
