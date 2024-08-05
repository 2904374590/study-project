package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.RestBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @SneakyThrows
    @Bean
    //过滤链
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //配置请求授权规则
                .authorizeHttpRequests()
                //所有请求都需要认证
                .anyRequest().authenticated()
                .and()
                //配置表单登录
                .formLogin()
                //处理登录请求的URL
                .loginProcessingUrl("/api/auth/login")
                //认证成功后的处理逻辑
                .successHandler(this::onAuthenticationSuccess)
                //认证失败
                .failureHandler(this::onAuthenticationFailure)
                .and()
                //注销功能
                .logout()
                //处理注销请求的URL
                .logoutUrl("/api/auth/logout")
                .and()
                //配置CSRF保护
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(this::onAuthenticationFailure)
                .and()
                //构建并返回SecurityFilterChain
                .build();
    }

    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.success("登陆成功!")));
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage())));
    }


}