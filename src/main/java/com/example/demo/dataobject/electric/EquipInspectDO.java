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
 * 装备检验表
 */
public class EquipInspectDO implements Serializable {
    private Integer id;
    private String testerNum;
    private String powerOnNum;
    private String proLoopNum;
    private Integer drawerConfirm;
    private Integer processConfirm;
    private Integer transferListConfirm;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
