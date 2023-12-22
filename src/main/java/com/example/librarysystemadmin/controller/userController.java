package com.example.librarysystemadmin.controller;

import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.service.UsersService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/Api")
public class userController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/register")
    public ApiResponse<String> register(@ModelAttribute User user) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 设置创建时间
        user.setCreated_at(new Date(System.currentTimeMillis()));
        // 对密码进行加密
        user.setPassword(RSAUtils.encrypt(user.getPassword()));


        // 对token进行加密 并添加随机字符串
        String token = RSAUtils.encrypt(user.getUsername());
        user.setToken(token);

        try {
            int result = usersService.registerUser(user);
            if (result == 0) {
                // 返回失败结果和失败码
                throw new Exception("注册失败");
            } else {
                // 返回成功结果和成功码
                apiResponse.setCode(200);
                apiResponse.setMessage("success");
                apiResponse.setData("注册成功");
            }

        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setCode(500);
            apiResponse.setMessage("error");
            apiResponse.setData(e.toString());
        }

        return apiResponse;
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String username = params.get("username");
        String password = params.get("password");
        String captcha = params.get("captcha");

        try {
            if (username == null || password == null || captcha == null) {
                //抛出异常
                apiResponse.setErrorResponse(400, "error", "参数错误");
                return apiResponse;
            }
            if (request.getSession().getAttribute("captcha") == null) {
                apiResponse.setErrorResponse(400, "error", "验证码失效");
                return apiResponse;
            } else if (!request.getSession().getAttribute("captcha").equals(captcha)) {
                apiResponse.setErrorResponse(400, "error", "验证码错误");
                return apiResponse;
            }

            User userInfo = usersService.queryUser(username); // 查询用户是否存在
            if (userInfo == null) {
                apiResponse.setErrorResponse(404, "error", "用户不存在");
                return apiResponse;
            }

            if (RSAUtils.decrypt(userInfo.getPassword()).equals(password)) {
                // 返回成功结果和成功码
                apiResponse.setSuccessResponse(userInfo.getToken());
            } else {
                // 返回失败结果和失败码
                apiResponse.setErrorResponse(400, "error", "密码错误");
            }
        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(500, "error", e.toString());
        }

        return apiResponse;
    }
}
