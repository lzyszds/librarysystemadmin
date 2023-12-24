package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.mapper.UsersMapper;
import com.example.librarysystemadmin.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    public int registerUser(User user) {
        return usersMapper.registerUser(user);

    }

    public User queryUser(String username) {
        return usersMapper.queryUser(username);
    }

    public User[] queryUserList(int page, int limit) {
        return usersMapper.queryUserList(page, limit);
    }

    public int queryUserListCount() {
        return usersMapper.queryUserListCount();
    }

    public int voucherRole(String username) {
        return usersMapper.voucherRole(username);
    }

    public User[] querySearchUsers(String search, int page, int limit) {
        return usersMapper.querySearchUsers(search, page, limit);

    }

    public int querySearcUserListCount(String search) {
        return usersMapper.querySearcUserListCount(search);
    }

    public int devastateUser(String id) {
        return usersMapper.devastateUser(id);
    }

}
