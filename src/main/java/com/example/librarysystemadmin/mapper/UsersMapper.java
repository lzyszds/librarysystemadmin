package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.domain.UserSecret;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UsersMapper {
    @Insert("INSERT INTO users (username, password, role, email, phone, address, name, created_at,token,sex) " +
            "VALUES (#{username}, #{password}, #{role}, #{email}, #{phone}, #{address}, #{name}, #{created_at},#{token},#{sex})")
    int registerUser(User user);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User getUser(String username);

    @Select("SELECT * FROM users" +
            " where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%' LIMIT #{page}, #{limit};")
    UserSecret[] getUserList(String search, int page, int limit);

    // 获取用户列表总数
    @Select("SELECT count(*) FROM users where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%'")
    int getUserListCount(String search);

    // 根据用户id获取用户名称
    @Select("SELECT username FROM users WHERE id = #{id}")
    String getUserNameById(String id);

    // 根据用户id获取用户名称
    @Select("SELECT username FROM users WHERE id in (#{id})")
    User[] getUserByid(String id);

    // 根据用户名获取用户角色
    @Select("SELECT role from users where token = #{token} ")
    Integer voucherRole(String token);

    @Select("SELECT role from users where id = #{id} ")
    int voucherIdRole(String id);

    // 根据token获取用户信息
    @Select("SELECT * FROM users WHERE token = #{token}")
    UserSecret getUserByToken(String token);

    @Select("SELECT count(*) FROM users where username like '%${search}%' or email like '%${search}%' or phone like '%${search}%' or " +
            "address like '%${search}%' or name like '%${search}%'")
    int getSearcUserListCount(String search);

    @Delete("DELETE FROM users WHERE id in (${id})")
    int devastateUser(String id);

    // 重置密码
    @Update("UPDATE users SET password = #{password} , token = #{token} WHERE id = #{id}")
    void resetPassword(String id, String password, String token);

    // 修改用户信息（管理）
    @Update("UPDATE users SET name=#{name}, email=#{email}, phone=#{phone}, role=#{role}, sex=#{sex}, address=#{address} WHERE id=#{id} ")
    int updateUserListInfo(String id, String name, String email, String phone, String role, String sex, String address);

    @Update("UPDATE users SET borrow=#{borrow} WHERE id=#{id}")
    void updateBorrowCount(int id, String borrow);
}
