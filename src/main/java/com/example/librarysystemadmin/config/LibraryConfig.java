package com.example.librarysystemadmin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * 使用@ConfigurationProperties注解来配置全局字段。这个注解可以将属性值绑定到一个类上，以便统一管理和访问这些属性。
 * 具体配置可以前往 application.properties（或application.yml）文件中配置这个属性：
 * */

@ConfigurationProperties(prefix = "library")
public class LibraryConfig {
    private int maxBooksPerUser;

    public int getMaxBooksPerUser() {
        return maxBooksPerUser;
    }

    public void setMaxBooksPerUser(int maxBooksPerUser) {
        this.maxBooksPerUser = maxBooksPerUser;
    }
}