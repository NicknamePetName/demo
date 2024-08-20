package com.example.demo.utils.convert;

import com.example.demo.dataobject.electric.EquipInspectDO;
import com.example.demo.model.electric.EquipInspect;
import org.springframework.beans.BeanUtils;

public class EquipInspectUtil {
    public static EquipInspect toModel(EquipInspectDO equipInspectDO) {
        EquipInspect equipInspect = new EquipInspect();
        BeanUtils.copyProperties(equipInspectDO, equipInspect);
        return equipInspect;
    }
    public static EquipInspectDO toDO(EquipInspect equipInspect) {
        EquipInspectDO equipInspectDO = new EquipInspectDO();
        BeanUtils.copyProperties(equipInspect, equipInspectDO);
        return equipInspectDO;
    }
}
