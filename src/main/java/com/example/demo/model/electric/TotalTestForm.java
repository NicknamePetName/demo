package com.example.demo.model.electric;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalTestForm implements Serializable {
    private Integer userId;

    // Table 1 —— 电器检验(单)表
    private String tableCode;
    private String projectName;
    private String cabinetType;
    private String cabinetNum;
    private String inspector;
    private String date;
    private String other;
    private String remark;

    // Table 2 —— 装备检验表
    private String testerNum;
    private String powerOnNum;
    private String proLoopNum;
    private Integer drawerConfirm;
    private Integer processConfirm;
    private Integer transferListConfirm;

    // Table 3 —— 回路检验表
    private String loopVdc;
    private String voltage;
    private String formula;
    private String l1Prevoltage;
    private String l2Prevoltage;
    private String l3Prevoltage;
    @JsonProperty
    private String nPrevoltage;
    private String assistPrevoltage;
    private String l1NoPrevoltage;
    private String l2NoPrevoltage;
    private String l3NoPrevoltage;
    @JsonProperty
    private String nNoPrevoltage;
    private String assistNoPrevoltage;
    private String l1LeakageCurrent;
    private String l2LeakageCurrent;
    private String l3LeakageCurrent;
    @JsonProperty
    private String nLeakageCurrent;
    private String assistLeakageCurrent;

    // Table 4 —— 检验项表
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

    // Table 5 —— 其它不合格项表
    private String oneText;
    private String twoText;
    private String threeText;
    private String fourText;
    private String fiveText;
    private String sixText;
    private String oneCorrector;
    private String twoCorrector;
    private String threeCorrector;
    private String fourCorrector;
    private String fiveCorrector;
    private String sixCorrector;
    private String oneRecheck;
    private String twoRecheck;
    private String threeRecheck;
    private String fourRecheck;
    private String fiveRecheck;
    private String sixRecheck;

}
