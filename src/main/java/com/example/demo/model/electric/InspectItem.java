package com.example.demo.model.electric;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * 检验项表
 */
public class InspectItem implements Serializable {
    private Integer id;
    private Integer locationResult;
    private String locationCorrector;
    private String locationRetest;
    private Integer phaseSequenceResult;
    private String phaseSequenceCorrector;
    private String phaseSequenceRetest;
    private Integer secondaryElementResult;
    private String secondaryElementCorrector;
    private String secondaryElementRetest;
    private Integer lightResult;
    private String lightCorrector;
    private String lightRetest;
    private Integer meterShowResult;
    private String meterShowCorrector;
    private String meterShowRetest;
    private Integer meterParamResult;
    private String meterParamCorrector;
    private String meterParamRetest;
    private Integer signalResult;
    private String signalCorrector;
    private String signalRetest;
    private Integer electricLinkageResult;
    private String electricLinkageCorrector;
    private String electricLinkageRetest;
    private Integer cableInterlockResult;
    private String cableInterlockCorrector;
    private String cableInterlockRetest;
    private Integer allFunctionResult;
    private String allFunctionCorrector;
    private String allFunctionRetest;
    private Integer powerControllerResult;
    private String powerControllerCorrector;
    private String powerControllerRetest;
    private Integer switchResult;
    private String switchCorrector;
    private String switchRetest;
    private Integer fanTemperatureResult;
    private String fanTemperatureCorrector;
    private String fanTemperatureRetest;
    private Integer arresterResult;
    private String arresterCorrector;
    private String arresterRetest;
    private Integer doorLandResult;
    private String doorLandCorrector;
    private String doorLandRetest;
    private Integer elementLandResult;
    private String elementLandCorrector;
    private String elementLandRetest;
    private Integer specialPointResult;
    private String specialPointCorrector;
    private String specialPointRetest;
    private Integer blotResult;
    private String blotCorrector;
    private String blotRetest;
    private Integer sundriesResult;
    private String sundriesCorrector;
    private String sundriesRetest;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
