package com.example.demo.utils.convert;

import com.example.demo.dataobject.UserDO;
import com.example.demo.model.User;
import org.springframework.beans.BeanUtils;

public class UserUtil {
    public static User toModel(UserDO userDO) {
        User user = new User();
        BeanUtils.copyProperties(userDO,user);
        return user;
    }
    public static UserDO toDO(User user) {
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(user, userDO);
        return userDO;
    }
}
