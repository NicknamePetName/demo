package com.example.demo.dao.electric;

import com.example.demo.dataobject.electric.OtherNoRegularDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OtherNoRegularDAO {
    int insert(OtherNoRegularDO otherNoRegularDO);
}
