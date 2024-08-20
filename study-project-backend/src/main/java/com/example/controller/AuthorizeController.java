package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    private final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String USERNAME_REGEX = "^[a-zA-Z\\u4e00-\\u9fa5]+$";
    @Resource
    AuthorizeService service;
    @PostMapping("/valid-email")
    public RestBean<String> validateEmail(@Pattern (regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                          HttpSession session){

        if (service.sendValidateEmail(email,session.getId())) {
            return RestBean.success("邮件已发送,请注意查收");
        }else return RestBean.failure(400,"邮件发送失败,请联系管理员");
    }
    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 2,max = 8) @RequestParam("username") String username,
                                         @Length(min = 6,max = 16) @RequestParam("password") String password,
                                         @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                         @Length(min = 6,max = 6) @RequestParam("code") String code){
       if (service.validateAndRegister(username,password,email,code)){
           return RestBean.success("注册成功");
       }else {
           return RestBean.failure(400,"注册失败，验证码填写错误");
       }
    }
}
