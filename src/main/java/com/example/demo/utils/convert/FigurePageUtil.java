package com.example.demo.utils.convert;

import com.example.demo.dataobject.excel.FigurePageDO;
import com.example.demo.model.excel.FigurePage;
import org.springframework.beans.BeanUtils;

public class FigurePageUtil {
    public static FigurePage toModel(FigurePageDO figurePageDO) {
        FigurePage figurePage = new FigurePage();
        BeanUtils.copyProperties(figurePageDO, figurePage);
        return figurePage;
    }
    public static FigurePageDO toDO(FigurePage figurePage) {
        FigurePageDO figurePageDO = new FigurePageDO();
        BeanUtils.copyProperties(figurePage, figurePageDO);
        return figurePageDO;
    }
}
