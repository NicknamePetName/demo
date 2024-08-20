package com.example.demo.utils.convert;

import com.example.demo.dataobject.electric.OtherNoRegularDO;
import com.example.demo.model.electric.OtherNoRegular;
import org.springframework.beans.BeanUtils;

public class OtherNoRegularUtil {
    public static OtherNoRegular toModel(OtherNoRegularDO otherNoRegularDO) {
        OtherNoRegular otherNoRegular = new OtherNoRegular();
        BeanUtils.copyProperties(otherNoRegularDO, otherNoRegular);
        return  otherNoRegular;
    }
    public static OtherNoRegularDO toDo(OtherNoRegular otherNoRegular) {
        OtherNoRegularDO otherNoRegularDO = new OtherNoRegularDO();
        BeanUtils.copyProperties(otherNoRegular, otherNoRegularDO);
        return otherNoRegularDO;
    }
}
