package com.example.demo.dao.electric;

import com.example.demo.dataobject.electric.InspectItemDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InspectItemDAO {
    int insert(InspectItemDO inspectItemDO);
}
