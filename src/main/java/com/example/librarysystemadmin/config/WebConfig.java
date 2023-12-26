package com.example.librarysystemadmin.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/Api/**")//拦截所有路由
                //放行路由
                .excludePathPatterns("/Api/User/login", "/Api/User/register", "/Api/util/captcha",
                        "/Api/Book/queryBookList", "/Api/Book/queryBookById", "/Api/Book/queryBookByBookName"
                );
    }
}
