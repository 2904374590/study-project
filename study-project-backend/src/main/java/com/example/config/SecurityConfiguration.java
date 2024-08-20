package com.example.config;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.RestBean;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Resource
    DataSource dataSource;

    @Bean
    //过滤链
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           PersistentTokenRepository repository) throws Exception {
        return http
                //配置请求授权规则
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
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
                .logoutSuccessHandler(this::onAuthenticationSuccess)
                .and()
                //记住我功能
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenRepository(repository)
                .tokenValiditySeconds(3600 * 24 * 7)
                .and()
                //配置CSRF保护
                .csrf()
                .disable()
                .cors()
                .configurationSource(this.corsConfigurationSource())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(this::onAuthenticationFailure)
                .and()
                //构建并返回SecurityFilterChain
                .build();
    }
//    记住，关于创业，你的人生中可以失去很多次机会，但是只要抓住其中一次，那你的人生就可以逆转！
//    管理持久化令牌---记住我功能
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl  jdbcTokenRepositoryImpl = new  JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        //设置是否在启动时创建表
        jdbcTokenRepositoryImpl.setCreateTableOnStartup(false);
        return jdbcTokenRepositoryImpl;
    }


    //跨域配置
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOriginPattern("*");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        cors.setAllowCredentials(true);
        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security) throws Exception {
        return security
                .getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(authorizeService)
                .and()
                .build();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException {
        response.setCharacterEncoding("utf-8");
        if (request.getRequestURL().toString().endsWith("/login"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("登陆成功!")));
        else if (request.getRequestURL().toString().endsWith("/logout"))
            response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功")));
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{
        response.setCharacterEncoding("utf-8");
        if (request.getRequestURI().endsWith("/login"))
           response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,"失败")));
        else if (request.getRequestURI().endsWith("/logout"))
           response.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功!")));
    }


}
