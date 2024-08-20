package com.example.demo.api;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("/isLogin")
    public Result<User> isLogin(HttpServletRequest request) {
        Result<User> result = new Result<>();
        result.setSuccess(false);
        String token = request.getHeader("token");
        try {
            JwtUtil.verify(token);
            result.setSuccess(true);
            result.setMessage("已登录");
            return result;
        }catch (TokenExpiredException e){
            result.setMessage("登录过期");
            return result;
        } catch (Exception e) {
            result.setMessage("未登录");
            return result;
        }
    }
}
