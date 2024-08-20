package com.example.demo.dao.excel;

import com.example.demo.dataobject.excel.FigurePageDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FigurePageDAO {
    int insert(FigurePageDO figurePageDO);

    FigurePageDO findByFileName(@Param("fileName") String fileName);
}
