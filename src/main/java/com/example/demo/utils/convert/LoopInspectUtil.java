package com.example.demo.utils.convert;

import com.example.demo.dataobject.electric.LoopInspectDO;
import com.example.demo.model.electric.LoopInspect;
import org.springframework.beans.BeanUtils;

public class LoopInspectUtil {
    public static LoopInspect toModel(LoopInspectDO loopInspectDO) {
        LoopInspect loopInspect = new LoopInspect();
        BeanUtils.copyProperties(loopInspectDO, loopInspect);
        return loopInspect;
    }
    public static LoopInspectDO toDO(LoopInspect loopInspect) {
        LoopInspectDO loopInspectDO = new LoopInspectDO();
        BeanUtils.copyProperties(loopInspect, loopInspectDO);
        return loopInspectDO;
    }
}
