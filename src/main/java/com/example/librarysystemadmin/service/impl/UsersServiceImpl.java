package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.domain.UserSecret;
import com.example.librarysystemadmin.mapper.UsersMapper;
import com.example.librarysystemadmin.service.UsersService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.RSAUtils;
import com.example.librarysystemadmin.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Map;

@Component
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper; // 使用@Autowired注解将UsersMapper注入UsersServiceImpl

    TokenUtils tokenUtils; // TokenUtils的实例变量

    // 用户注册方法
    public String registerUser(User user) {
        // 设置创建时间
        user.setCreated_at(new Date(System.currentTimeMillis()));
        // 对密码进行加密 使用用户账号作为初始密码
        user.setPassword(RSAUtils.encrypt(user.getUsername()));

        // 参数验证
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "用户名不能为空";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "密码不能为空";
        }

        // 检查用户名是否已经存在
        if (usersMapper.getUser(user.getUsername()) != null) {
            return "用户已存在";
        }

        // 将用户名加密作为token
        String token = RSAUtils.encrypt(user.getUsername());
        user.setToken(token);
        usersMapper.registerUser(user);

        return null;
    }

    // 用户登录方法
    public String loginService(Map<String, String> params, HttpServletRequest request) {
        String username = params.get("username");
        String password = params.get("password");
        String captcha = params.get("captcha");

        // 检查参数是否为空
        if (username == null || username.isEmpty()) {
            return "账号不能为空";
        }
        if (password == null || password.isEmpty()) {
            return "密码不能为空";
        }
        if (captcha == null || captcha.isEmpty()) {
            return "验证码不能为空";
        }

        // 检查验证码
        Object sessionCaptcha = request.getSession().getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equals(captcha)) {
            return sessionCaptcha == null ? "验证码失效" : "验证码错误";
        }

        // 验证码正确后删除验证码
        request.getSession().removeAttribute("captcha");

        // 查询用户信息
        User userInfo = usersMapper.getUser(username);
        if (userInfo == null) {
            return "用户不存在";
        }

        // 验证密码
        if (RSAUtils.decrypt(userInfo.getPassword()).equals(password)) {
            return userInfo.getToken();
        } else {
            return "账号或密码错误";
        }
    }

    // 获取用户信息方法
    public User getUser(String username) {
        return usersMapper.getUser(username);
    }

    // 获取用户列表方法
    public UserSecret[] getUserList(String search, int page, int limit) {
        return usersMapper.getUserList(search, page, limit);
    }

    // 获取用户列表数量方法
    public int getUserListCount(String search) {
        return usersMapper.getUserListCount(search);
    }

    // 根据用户ID获取用户信息方法
    public User[] getUserById(String id) {
        return usersMapper.getUserByid(id);
    }

    // 根据用户ID获取用户名方法
    public String getUserNameById(String id) {
        return usersMapper.getUserNameById(id);
    }

    // 获取用户角色方法
    public Integer voucherRole(String username) {
        return usersMapper.voucherRole(username);
    }

    // 获取搜索用户列表数量方法
    public int getSearcUserListCount(String search) {
        return usersMapper.getSearcUserListCount(search);
    }

    // 删除用户方法
    public int devastateUser(String id) {
        return usersMapper.devastateUser(id);
    }

    // 重置用户密码方法
    public String resetPassword(String id, Cookie[] cookies) {
        //不能删除超级管理员
        if (id.contains(".0.") || id.equals("0")) {
            return "不能重置超级管理员密码";
        }
        String usernaem = usersMapper.getUserNameById(id);
        String password = RSAUtils.encrypt(usernaem);

        // 获取token
        String token = tokenUtils.getToken(cookies);
        // 根据token获取用户信息
        UserSecret user = usersMapper.getUserByToken(token);
        int operatorRole = user.getRole(); // 获取操作用户的角色
        int role = usersMapper.voucherIdRole(id); // 获取被操作用户的角色
        // 上级用户只能修改下级用户信息，不能修改跟自己同级的用户信息
        if (operatorRole <= role && !Integer.toString(user.getId()).equals("0")) {
            return "权限不足";
        }

        //根据用户名 + 账号 生成token
        String newToken = RSAUtils.encrypt(usernaem + "_" + user.getId());
        usersMapper.resetPassword(id, password, newToken);

        return null;
    }

    // 修改用户列表信息方法
    public String updateUserListInfo(Map<String, String> params, Cookie[] cookies) {
        String id = params.get("id");
        String name = params.get("name");
        String email = params.get("email");
        String phone = params.get("phone");
        String role = params.get("role");
        String sex = params.get("sex");
        String address = params.get("address");

        // 获取token
        String token = tokenUtils.getToken(cookies);
        // 根据token获取用户信息
        UserSecret user = usersMapper.getUserByToken(token);
        int operatorRole = user.getRole(); // 获取操作用户的角色
        // 上级用户只能修改下级用户信息，不能修改跟自己同级的用户信息
        if (operatorRole <= Integer.parseInt(role) && !Integer.toString(user.getId()).equals("0")) {
            return "权限不足";
        }
        //"修改失败,用户可能不存在，请重新尝试"
        if (usersMapper.getUserByid(id).length == 0) {
            return "修改失败,用户可能不存在，请重新尝试";
        }

        int result = usersMapper.updateUserListInfo(id, name, email, phone, role, sex, address);
        if (result == 0) {
            return "修改失败";
        }
        return null;
    }

    // 根据Token获取用户信息方法
    public ApiResponse<UserSecret> getUserByToken(String token) {
        ApiResponse<UserSecret> apiResponse = new ApiResponse<>();
        UserSecret user = usersMapper.getUserByToken(token);
        if (user == null) {
            apiResponse.setErrorResponse(500, "用户不存在");
        } else {
            apiResponse.setSuccessResponse(user);
        }
        return apiResponse;
    }
}
