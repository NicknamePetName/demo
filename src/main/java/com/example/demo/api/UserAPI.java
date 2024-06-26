package com.example.demo.api;

import com.example.demo.model.Result;
import com.example.demo.model.User;
import com.example.demo.service.PersonalRecordService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserAPI {

    private static final Logger LOG = LoggerFactory.getLogger(UserAPI.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PersonalRecordService personalRecordService;

    @PostMapping("/api/user/reg")
    public String register(@RequestParam String userName, @RequestParam String pwd, Model model) {
        Result<User> result = userService.register(userName, pwd);

        LOG.warn("register result : ", result);

        if (result != null && result.isSuccess()) {
            model.addAttribute("info", "注册成功！");
            personalRecordService.initDefaultData(result.getData().getId());
        } else {
            model.addAttribute("info", "注册失败！");
        }
        return "info";
    }

    @PostMapping("/api/user/login")
    public String login(@RequestParam String userName, @RequestParam String pwd, HttpServletRequest request, Model model) {
        Result<User> result = userService.login(userName, pwd);

        LOG.warn("register result : ", result);

        if (result != null && result.isSuccess()) {
            request.getSession().setAttribute("userLoginInfo",result.getData());
            model.addAttribute("info", "登录成功！");
        }else {
            model.addAttribute("info", "登录失败！");
        }
        return "info";
    }

    @ResponseBody
    @GetMapping("/api/user/logout")
    public Result logout(HttpServletRequest request){

        Result result = new Result();

        request.getSession().removeAttribute("userId");

        result.setSuccess(true);

        return result;
    };

}
