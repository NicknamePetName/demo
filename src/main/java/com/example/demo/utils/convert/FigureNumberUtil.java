package com.example.demo.utils.convert;

import com.example.demo.dataobject.excel.FigureNumberDO;
import com.example.demo.model.excel.FigureNumber;
import org.springframework.beans.BeanUtils;

public class FigureNumberUtil {
    public static FigureNumber toModel(FigureNumberDO figureNumberDO) {
        FigureNumber figureNumber = new FigureNumber();
        BeanUtils.copyProperties(figureNumberDO, figureNumber);
        return figureNumber;
    }
    public static FigureNumberDO toDO(FigureNumber figureNumber) {
        FigureNumberDO figureNumberDO = new FigureNumberDO();
        BeanUtils.copyProperties(figureNumber, figureNumberDO);
        return figureNumberDO;
    }
}
