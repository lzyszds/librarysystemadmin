package com.example.librarysystemadmin.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private MyInterceptor myInterceptor;
    public String pathAll = "";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] user = {"login", "register", "voucherToken"};
        String[] book = {
                "getBookList",
                "getBookInfo",
                "getHotBookList",
                "getNewBookList",
                "getBookListByField",
                "getTopNCategories",
        };
        String[] util = {"captcha"};

        // 将数组转换为一个由逗号分隔的路径字符串，并传递给excludePathPatterns
        Map<String, String[]> allExcludedPaths = new HashMap<String, String[]>() {{
            put("User", user);
            put("Book", book);
            put("util", util);
        }};
        allExcludedPaths.forEach((k, v) -> {
            for (String s : v) {
                pathAll += "/Api/" + k + "/" + s + ",";
            }
        });
        // pathAll[0] 将其转换为数组
        String[] allExcludedPathsArr = pathAll.split(",");
        registry.addInterceptor(myInterceptor).addPathPatterns("/Api/**")//拦截所有路由
                //放行路由
                .excludePathPatterns(allExcludedPathsArr);
    }
}
