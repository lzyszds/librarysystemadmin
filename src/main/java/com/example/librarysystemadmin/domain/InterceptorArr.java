package com.example.librarysystemadmin.domain;

import java.util.Arrays;


/*
* 用于存放拦截器的路径
* */

public class InterceptorArr {

    private String[] user;
    private String[] book;
    private String[] util;

    private static final String USER_API_PREFIX = "/Api/User/";
    private static final String BOOK_API_PREFIX = "/Api/Book/";
    private static final String UTIL_API_PREFIX = "/Api/util/";

    private static String addPrefix(String path, String type) {
        if (type.equals("user")) {
            return USER_API_PREFIX + path;
        } else if (type.equals("book")) {
            return BOOK_API_PREFIX + path;
        } else if (type.equals("util")) {
            return UTIL_API_PREFIX + path;
        }
        return null;
    }

    public String[] getUser() {
        return user;
    }

    public void setUser(String[] users) {
        //将其内部的所有路由添加前缀
        this.user = Arrays.stream(users).map(path -> addPrefix(path, "user")).toArray(String[]::new);
    }

    public String[] getBook() {
        return book;
    }

    public void setBook(String[] book) {
        this.book = Arrays.stream(book).map(path -> addPrefix(path, "book")).toArray(String[]::new);
    }

    public String[] getUtil() {
        return util;
    }

    public void setUtil(String[] util) {
        this.util = Arrays.stream(util).map(path -> addPrefix(path, "util")).toArray(String[]::new);
    }


    @Override
    public String toString() {
        return "InterceptorArr{" +
                "user=" + Arrays.toString(user) +
                ", book=" + Arrays.toString(book) +
                ", util=" + Arrays.toString(util) +
                '}';
    }
}
