package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.domain.UserSecret;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UsersService {
    String registerUser(User user);

    String loginService(Map<String, String> params, HttpServletRequest request);

    User getUser(String username);

    UserSecret[] getUserList(String search, int page, int limit);

    int getUserListCount(String search);

    User[] getUserById(String id);

    String getUserNameById(String id);

    Integer voucherRole(String token);

    int getSearcUserListCount(String search);

    int devastateUser(String id);

    String resetPassword(String id,  Cookie[] cookies);

    String updateUserListInfo(Map<String, String> params, Cookie[] cookies);

    UserSecret getUserByToken(String token);
}
