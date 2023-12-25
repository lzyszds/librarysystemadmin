package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.User;

public interface UsersService {
    int registerUser(User user);

    User queryUser(String username);

    User[] queryUserList(int page, int limit);

    int queryUserListCount();

    String queryUserById(String id);

    int voucherRole(String username);

    User[] querySearchUsers(String search, int page, int limit);

    int querySearcUserListCount(String search);

    int devastateUser(String id);

    int resetPassword(String id, String password);

    int updateUserListInfoAdmin(String id, String name, String email, String phone, String role, String sex, String address);
}
