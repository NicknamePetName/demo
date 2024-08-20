package com.example.demo.service;

import com.example.demo.model.Result;
import com.example.demo.model.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    String SALT_VALUE = "YIXIN%s";

    Result<User> login(User user);

    Result<User> register(User user);

    Result<User> getLoginUser(HttpServletRequest request);


}
