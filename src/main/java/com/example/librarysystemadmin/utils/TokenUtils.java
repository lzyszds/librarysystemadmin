package com.example.librarysystemadmin.utils;

import javax.servlet.http.Cookie;

public class TokenUtils {

    public static String getToken(Cookie[] cookies) {
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }

}
