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

}
