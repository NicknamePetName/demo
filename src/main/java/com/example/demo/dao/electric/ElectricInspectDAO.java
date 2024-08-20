package com.example.demo.dao.electric;

import com.example.demo.dataobject.electric.ElectricInspectDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ElectricInspectDAO {
    int insert (ElectricInspectDO electricInspectDO);
}
