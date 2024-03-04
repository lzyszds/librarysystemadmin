package com.example.librarysystemadmin.controller;

import com.example.librarysystemadmin.domain.ListDataCount;
import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.domain.UserSecret;
import com.example.librarysystemadmin.service.UsersService;
import com.example.librarysystemadmin.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/*
 *  在HTTP协议中，状态码用于表示服务器对请求的处理结果。每个状态码都有特定的含义，以下是一些常见的状态码及其含义：
 *   200 OK：请求成功。服务器成功处理了请求，并返回了相应的数据。
 *   201 Created：资源创建成功。服务器成功创建了新资源，并返回了相应的数据。
 *   400 Bad Request：请求错误。服务器无法理解或处理请求的语法。
 *   401 Unauthorized：未授权。请求需要身份验证，但用户未提供有效的凭据。
 *   403 Forbidden：禁止访问。服务器理解请求，但拒绝执行请求。
 *   404 Not Found：资源未找到。服务器无法找到请求的资源。
 *  500 Internal Server Error：服务器内部错误。服务器在处理请求时发生了错误。
 *
 * */
@RestController
@RequestMapping("/Api/User")
public class userController {

    @Autowired
    private UsersService usersService;


    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody User params) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            String result = usersService.registerUser(params);
            if (result == null) {
                apiResponse.setSuccessResponse("注册成功");
            } else {
                apiResponse.setErrorResponse(400, result);
            }
        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(400, "注册失败", "/Api/register", e);
        }
        return apiResponse;
    }


    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            //如果登录成功，返回token token的长度肯定大于30
            String result = usersService.loginService(params, request);
            if (result.length() > 30) {
                apiResponse.setSuccessResponse(result);
            } else {
                apiResponse.setErrorResponse(400, result);
            }
        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(400, "登录失败", "/Api/login", e);
        }

        return apiResponse;
    }

    //验证用户token
    @RequestMapping("/voucherToken")
    public ApiResponse<UserSecret> voucherToken(HttpServletRequest request) {
        // 从请求中获取token
        Cookie[] cookies = request.getCookies();

        //获取token 键值
        if (cookies == null) {
            ApiResponse<UserSecret> apiResponse = new ApiResponse<>();
            apiResponse.setErrorResponse(401, "未登录");
            return apiResponse;
        }
        String tokenValue = null;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName())) {
                tokenValue = cookie.getValue();
            } else {
                tokenValue = request.getHeader("token");
            }
        }

        if (tokenValue == null) {
            ApiResponse<UserSecret> apiResponse = new ApiResponse<>();
            apiResponse.setErrorResponse(401, "未登录");
            return apiResponse;
        }
        System.out.println("tokenValue: " + tokenValue);
        return usersService.getUserByToken(tokenValue);
    }


    //获取用户列表 required = false表示参数不是必须的 defaultValue = "1"表示默认值为1
    @GetMapping("/getUserList")
    public ApiResponse<ListDataCount<UserSecret[]>> getUserlist(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int limit, @RequestParam(required = false, defaultValue = "") String search) {
        ApiResponse<ListDataCount<UserSecret[]>> apiResponse = new ApiResponse<>();

        try {
            // 查询用户列表 分页 page是页数 limit是每页的数量 让数据库从第page*limit条开始查询 limit条数据
            UserSecret[] list = usersService.getUserList(search, (page - 1) * limit, limit);
            Integer size = usersService.getUserListCount(search); // 查询用户总数

            // 返回成功结果和成功码 返回用户列表 和 用户总数
            ListDataCount<UserSecret[]> listDataCount = new ListDataCount<>(size, list);
            apiResponse.setSuccessResponse(listDataCount);
        } catch (NumberFormatException e) {
            apiResponse.setErrorResponse(400, "Invalid page or limit parameter", "/Api/getUserList", e);
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "Internal Server Error", "/Api/getUserList", e);
        }
        return apiResponse;
    }


    /*
     *  删除用户
     *  传入用户id 字符串 示例：10,11,12 或者 10
     * */
    @PostMapping("/devastateUser")
    public ApiResponse<String> devastateUser(@RequestBody Map<String, String> params, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        //只有为管理员才能获取
        String id = params.get("id");
        if (id.contains(".0.")) {
            //不能删除超级管理员
            apiResponse.setErrorResponse(403, "不能删除超级管理员");
            return apiResponse;
        }
        try {
            int result = usersService.devastateUser(id);
            if (result == 0) {
                // 返回失败结果和失败码
                apiResponse.setErrorResponse(500, "删除失败,用户可能不存在，请重新尝试");
            } else {
                // 返回成功结果和成功码
                apiResponse.setErrorResponse(200, "删除成功");
            }

        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(500, e.toString());
        }

        return apiResponse;
    }

    /*
     *  重置用户密码
     *  1.获取用户id
     * */
    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        //只有为管理员才能获取
        String id = params.get("id");
        try {
            String result = usersService.resetPassword(id, request.getCookies());
            if (result == null) {
                // 返回成功结果和成功码
                apiResponse.setSuccessResponse("重置成功");
            } else {
                // 返回失败结果和失败码
                apiResponse.setErrorResponse(500, "重置失败,用户可能不存在，请重新尝试");
            }
        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(500, e.toString());
        }

        return apiResponse;
    }

    /*
     * 修改用户信息（管理）
     * */
    @PostMapping("/updateUserInfoAdmin")
    public ApiResponse<String> updateUserListInfoAdmin(@RequestBody Map<String, String> params, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            String result = usersService.updateUserListInfo(params, request.getCookies());
            if (result == null) {
                // 返回成功结果和成功码
                apiResponse.setSuccessResponse("修改成功");
            } else {
                // 返回失败结果和失败码
                apiResponse.setErrorResponse(500, result);
            }
        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(500, e.toString());
        }
        return apiResponse;
    }

    /*
     * 修改用户信息（个人）
     * */
    @PostMapping("/updateUserInfoPrivate")
    public ApiResponse<String> updateUserInfoPrivate(@RequestBody Map<String, String> params, HttpServletRequest request) {


        return usersService.updateUserInfoPrivate(params, request);
    }
}
