package com.example.demo.dataobject.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FigurePageDO implements Serializable {
    private Integer id;
    private String fileName;
    private String filePath;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
