package com.example.demo.model.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Serializable {
    private Integer id;
    private String projectName;
    private String projectNumber;
    private String containerType;
    private Integer containerQuantity;
    private String orderNumber ;
    private String surfaceTreatment ;
    private String designerUser;
    private LocalDateTime designDate;
    private String reviewerUser ;
    private LocalDateTime reviewDate;
    private String handoverUser;
    private String productionUser;
    private LocalDateTime productionDate;
    private String outboundUser;
    private LocalDateTime outboundDate ;
    private String listCode ;
    private String listNumber ;
    private String listBelong;
    private Integer customerId;
    private String deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer createUser ;
    private Integer updateUser;
}
