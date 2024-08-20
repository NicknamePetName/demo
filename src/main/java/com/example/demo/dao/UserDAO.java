package com.example.demo.dao;

import com.example.demo.dataobject.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDAO {
    int insert(UserDO userDO);

    UserDO findByUserName(@Param("userName") String userName);
}
