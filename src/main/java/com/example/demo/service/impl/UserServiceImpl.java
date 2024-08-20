package com.example.demo.service.impl;

import com.example.demo.dao.UserDAO;
import com.example.demo.dataobject.UserDO;
import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import com.example.demo.utils.convert.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public Result<User> login(User user) {
        Result<User> result = Result.create();
        result.setSuccess(false);
        if (user == null) {
            result.setMessage("参数为空");
            return result;
        }
        UserDO userDO = userDAO.findByUserName(user.getUserName());
        if(userDO == null) {
            result.setCode("401");
            result.setMessage("用户不存在");
            return result;
        }
        String md5Pwd = DigestUtils.md5Hex(String.format(SALT_VALUE, user.getPassword())).toUpperCase();
        if (userDO.getPassword().equals(md5Pwd)) {
            result.setSuccess(true);
            result.setCode("200");
            result.setData(UserUtil.toModel(userDO));
            result.setMessage("登录成功");
            Map map = new HashMap<>();
            map.put("userId",userDO.getId().toString());
            map.put("userName",user.getUserName());
            // 有效负载不可以放密码等敏感信息
//            map.put("password",user.getPassword());
            result.setToken(JwtUtil.produceToken(map));
        }else {
            result.setCode("402");
            result.setMessage("密码错误");
        }
        return result;
    }

    @Override
    public Result<User> register(User user) {
        Result<User> result = Result.create();
        result.setSuccess(false);
        if (user == null) {
            result.setCode("301");
            result.setMessage("参数错误");
            return result;
        }
        UserDO userDO = userDAO.findByUserName(user.getUserName());
        if (userDO != null) {
            result.setCode("302");
            result.setMessage("用户已存在");
            return result;
        }
        String md5Pwd = DigestUtils.md5Hex(String.format(SALT_VALUE, user.getPassword())).toUpperCase();
        user.setPassword(md5Pwd);
        int i = userDAO.insert(UserUtil.toDO(user));
        if (i == 1) {
            result.setSuccess(true);
            result.setCode("200");
            result.setMessage("注册成功");
        }else {
            result.setCode("303");
            result.setMessage("注册失败");
        }
        return result;
    }

    @Override
    public Result<User> getLoginUser(HttpServletRequest request) {
        Result<User> result = new Result<>();
        result.setSuccess(false);
        String token = request.getHeader("token");
        try {
            String userName = JwtUtil.parseToken(token).getClaim("userName").asString();
            User user = UserUtil.toModel(userDAO.findByUserName(userName));
            result.setSuccess(true);
            result.setData(user);
            result.setMessage("已登录");
            return result;
        }catch (Exception e) {
            result.setMessage("未登录");
            return result;
        }
    }
}
