package com.example.demo.dao.electric;

import com.example.demo.dataobject.electric.LoopInspectDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoopInspectDAO {
    int insert(LoopInspectDO loopInspectDO);
}
