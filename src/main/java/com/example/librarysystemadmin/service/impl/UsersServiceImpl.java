package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.domain.UserSecret;
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

    public User getUser(String username) {
        return usersMapper.getUser(username);
    }

    public UserSecret[] getUserList(String search, int page, int limit) {
        return usersMapper.getUserList(search, page, limit);
    }

    public int getUserListCount(String search) {
        return usersMapper.getUserListCount(search);
    }

    public User[] getUserById(String id) {
        return usersMapper.getUserByid(id);
    }

    public String getUserNameById(String id) {
        return usersMapper.getUserNameById(id);
    }

    public int voucherRole(String username) {
        return usersMapper.voucherRole(username);
    }


    public int getSearcUserListCount(String search) {
        return usersMapper.getSearcUserListCount(search);
    }

    public int devastateUser(String id) {
        return usersMapper.devastateUser(id);
    }

    public int resetPassword(String id, String password) {
        return usersMapper.resetPassword(id, password);
    }

    public int updateUserListInfoAdmin(String id, String name, String email, String phone, String role, String sex, String address) {
        return usersMapper.updateUserListInfoAdmin(id, name, email, phone, role, sex, address);
    }

    public UserSecret getUserByToken(String token) {
        return usersMapper.getUserByToken(token);
    }
}
