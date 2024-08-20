package com.example.demo.dao.excel;

import com.example.demo.dataobject.excel.FigureNumberDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FigureNumberDAO {
    int insert(FigureNumberDO figureNumberDO);
}
