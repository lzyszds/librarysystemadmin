package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.domain.UserSecret;

public interface UsersService {
    int registerUser(User user);

    User getUser(String username);

    UserSecret[] getUserList(String search, int page, int limit);

    int getUserListCount(String search);

    User[] getUserById(String id);

    String getUserNameById(String id);

    int voucherRole(String username);


    int getSearcUserListCount(String search);

    int devastateUser(String id);

    int resetPassword(String id, String password);

    int updateUserListInfoAdmin(String id, String name, String email, String phone, String role, String sex, String address);

    UserSecret getUserByToken(String token);

}
