package com.example.librarysystemadmin.utils;

import javax.servlet.http.Cookie;

public class TokenUtils {

    public String getToken(Cookie[] tokens) {
        String token = "";
        if (tokens != null) {
            for (Cookie cookie : tokens) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }

}
