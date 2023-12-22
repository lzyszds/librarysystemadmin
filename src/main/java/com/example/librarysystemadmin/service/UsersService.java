package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.User;

public interface UsersService {
    int registerUser(User user);

    User queryUser(String username);

//    public User login(String username);


}
