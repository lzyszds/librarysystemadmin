package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.User;
import org.apache.ibatis.annotations.*;

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

    @Select("SELECT username FROM users WHERE id = #{id}")
    String queryUserById(String id);

    @Select("SELECT role from users where username = #{username}")
    int voucherRole(String username);

    @Select("SELECT id, username, role, email, phone, address, name, created_at, sex FROM users" +
            " where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%' LIMIT #{page}, #{limit};")
    User[] querySearchUsers(String search, int page, int limit);

    @Select("SELECT count(*) FROM users where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%'")
    int querySearcUserListCount(String search);

    @Delete("DELETE FROM users WHERE id in (${id})")
    int devastateUser(String id);

    // 重置密码
    @Update("UPDATE users SET password = #{password} , token = #{password} WHERE id = #{id}")
    int resetPassword(String id, String password);

    // 修改用户信息（管理）
    @Update("UPDATE users SET name=#{name}, email=#{email}, phone=#{phone}, role=#{role}, sex=#{sex}, address=#{address} WHERE id=#{id} ")
    int updateUserListInfoAdmin(String id, String name, String email, String phone, String role, String sex, String address);
}
