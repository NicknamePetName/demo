package com.example.demo.utils.convert;

import com.example.demo.dataobject.electric.ElectricInspectDO;
import com.example.demo.model.electric.ElectricInspect;
import org.springframework.beans.BeanUtils;

public class ElectricInspectUtil {
    public static ElectricInspect toModel(ElectricInspectDO electricInspectDO) {
        ElectricInspect electricInspect = new ElectricInspect();
        BeanUtils.copyProperties(electricInspectDO, electricInspect);
        return electricInspect;
    }
    public static ElectricInspectDO toDO(ElectricInspect electricInspect) {
        ElectricInspectDO electricInspectDO = new ElectricInspectDO();
        BeanUtils.copyProperties(electricInspect, electricInspectDO);
        return electricInspectDO;
    }
}
