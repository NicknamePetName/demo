package com.example.demo.utils.convert;

import com.example.demo.dataobject.electric.InspectItemDO;
import com.example.demo.model.electric.InspectItem;
import org.springframework.beans.BeanUtils;

public class InspectItemUtil {
    public static InspectItem toModel(InspectItemDO inspectItemDO) {
        InspectItem inspectItem = new InspectItem();
        BeanUtils.copyProperties(inspectItemDO, inspectItem);
        return inspectItem;
    }
    public static InspectItemDO toDO(InspectItem inspectItem) {
        InspectItemDO inspectItemDO = new InspectItemDO();
        BeanUtils.copyProperties(inspectItem, inspectItemDO);
        return inspectItemDO;
    }
}
