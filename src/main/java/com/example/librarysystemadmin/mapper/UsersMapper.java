package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UsersMapper {
    @Insert("INSERT INTO users (username, password, role, email, phone, address, name, created_at,token,sex) " +
            "VALUES (#{username}, #{password}, #{role}, #{email}, #{phone}, #{address}, #{name}, #{created_at},#{token},#{sex})")
    int registerUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User queryUser(String username);

    @Select("SELECT id, username, role, email, phone, address, name, created_at, sex FROM users LIMIT #{page}, #{limit};")
    User[] queryUserList(int page, int limit);

    @Select("SELECT count(*) FROM users")
    int queryUserListCount();

    @Select("SELECT role from users where token = #{token}")
    int voucherRole(String token);

    @Select("SELECT id, username, role, email, phone, address, name, created_at, sex FROM users" +
            " where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%' LIMIT #{page}, #{limit};")
    User[] querySearchUsers(String search, int page, int limit);

    @Select("SELECT count(*) FROM users where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%'")
    int querySearcUserListCount(String search);

    @Delete("DELETE FROM users WHERE id in (${id})")
    int devastateUser(String id);
}
