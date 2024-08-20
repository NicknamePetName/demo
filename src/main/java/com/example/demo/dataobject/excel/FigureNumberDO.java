package com.example.demo.dataobject.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FigureNumberDO implements Serializable {
    private Integer id;
    private String figureNumber;
    private Integer figurePageId;
    private String ply;
    private String width;
    private String length;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
