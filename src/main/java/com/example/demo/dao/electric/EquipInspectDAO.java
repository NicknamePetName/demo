package com.example.demo.dao.electric;

import com.example.demo.dataobject.electric.EquipInspectDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EquipInspectDAO {
    int insert(EquipInspectDO equipInspectDO);
}
