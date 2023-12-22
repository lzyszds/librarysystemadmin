package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UsersMapper {
    @Insert("INSERT INTO users (username, password, role, email, phone, address, name, created_at,token) " +
            "VALUES (#{username}, #{password}, #{role}, #{email}, #{phone}, #{address}, #{name}, #{created_at},#{token})")
    int registerUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User queryUser(String username);
}
