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
 * 回路检验表
 */
public class LoopInspectDO implements Serializable {
    private Integer id;
    private String loopVdc;
    private String voltage;
    private String formula;
    private String l1Prevoltage;
    private String l2Prevoltage;
    private String l3Prevoltage;
    private String nPrevoltage;
    private String assistPrevoltage;
    private String l1NoPrevoltage;
    private String l2NoPrevoltage;
    private String l3NoPrevoltage;
    private String nNoPrevoltage;
    private String assistNoPrevoltage;
    private String l1LeakageCurrent;
    private String l2LeakageCurrent;
    private String l3LeakageCurrent;
    private String nLeakageCurrent;
    private String assistLeakageCurrent;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
