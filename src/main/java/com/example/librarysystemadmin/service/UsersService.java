package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.User;

public interface UsersService {
    int registerUser(User user);

    User queryUser(String username);

    User[] queryUserList(int page, int limit);

    int queryUserListCount();

    int voucherRole(String username);

    User[] querySearchUsers(String search, int page, int limit);

    int querySearcUserListCount(String search);

    int devastateUser(String id);

}
