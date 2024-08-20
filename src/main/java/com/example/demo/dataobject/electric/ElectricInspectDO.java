package com.example.demo.dataobject.electric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 电器检验(单)表 - 总
 */
public class ElectricInspectDO implements Serializable {
    private Integer id;
    private Integer userId;
    private String tableCode;
    private String projectName;
    private String cabinetType;
    private String cabinetNum;
    private String inspector;
    private String date;
    private String other;
    private String remark;
    private Integer equipInspectId;
    private Integer loopInspectId;
    private Integer inspectItemId;
    private Integer otherNoRegularId;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
