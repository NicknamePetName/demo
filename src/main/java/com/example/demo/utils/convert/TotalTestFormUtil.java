package com.example.demo.utils.convert;

import com.example.demo.model.electric.*;
import org.springframework.beans.BeanUtils;

public class TotalTestFormUtil {

    /**
     * 电器检验(单)表 - 总
     */
    public static ElectricInspect toModel(TotalTestForm totalTestForm, ElectricInspect electricInspect) {
        BeanUtils.copyProperties(totalTestForm, electricInspect);
        return electricInspect;
    }
    public static TotalTestForm toTotalTestForm(ElectricInspect electricInspect, TotalTestForm totalTestForm) {
        BeanUtils.copyProperties(electricInspect,totalTestForm);
        return totalTestForm;
    }

    /**
     * 装备检验表
     */
    public static EquipInspect toModel(TotalTestForm totalTestForm,EquipInspect equipInspect) {
        BeanUtils.copyProperties(totalTestForm, equipInspect);
        return equipInspect;
    }
    public static TotalTestForm TotalTestForm(EquipInspect equipInspect, TotalTestForm totalTestForm) {
        BeanUtils.copyProperties(equipInspect,totalTestForm);
        return totalTestForm;
    }
    /**
     * 检验项表
     */
    public static InspectItem toModel(TotalTestForm totalTestForm, InspectItem inspectItem) {
        BeanUtils.copyProperties(totalTestForm, inspectItem);
        return inspectItem;
    }
    public static TotalTestForm toTotalTestForm(InspectItem inspectItem, TotalTestForm totalTestForm) {
        BeanUtils.copyProperties(inspectItem,totalTestForm);
        return totalTestForm;
    }
    /**
     * 回路检验表
     */
    public static LoopInspect toModel(TotalTestForm totalTestForm, LoopInspect loopInspect) {
        BeanUtils.copyProperties(totalTestForm, loopInspect);
        return loopInspect;
    }
    public static TotalTestForm toTotalTestForm(LoopInspect loopInspect, TotalTestForm totalTestForm) {
        BeanUtils.copyProperties(loopInspect,totalTestForm);
        return totalTestForm;
    }
    /**
     * 其它不合格项表
     */
    public static OtherNoRegular toModel(TotalTestForm totalTestForm, OtherNoRegular otherNoRegular) {
        BeanUtils.copyProperties(totalTestForm, otherNoRegular);
        return otherNoRegular;
    }
    public static TotalTestForm toTotalTestForm(OtherNoRegular otherNoRegular, TotalTestForm totalTestForm) {
        BeanUtils.copyProperties(otherNoRegular,totalTestForm);
        return totalTestForm;
    }
}
