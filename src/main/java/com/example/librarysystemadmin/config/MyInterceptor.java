package com.example.librarysystemadmin.config;


import com.example.librarysystemadmin.service.UsersService;
import com.example.librarysystemadmin.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyInterceptor implements HandlerInterceptor {
    /* 当前代码作用
     * 1. 拦截器拦截所有请求
     * 2. 判断是否登录
     * 3. 未登录跳转到登录页面
     * 4. 已登录放行
     */

    @Autowired
    private UsersService usersService;

    //普通用户权限
    String[] userPower = {
            "/Api/BookLoan/borrowingBook",
            "/Api/User/updateUserPassword",
            "/Api/User/updateUserInfoPrivate",
            "/Api/BookLoan/getBookLoanByUserId",
            "/Api/BookLoan/getBorrowedBooks"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!hasPermission(request)) {
            // 如果用户没有权限，设置响应的状态码和消息
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("You don't have permission to access this resource");
            return false;
        }
        // 用户有权限，继续处理请求
        // 验证通过，继续处理请求
        return true;
    }

    private boolean hasPermission(HttpServletRequest request) {
        // 编写检查用户权限的逻辑
        // 这里只是示例，你需要根据实际情况进行实现
        // 返回true表示用户有权限，返回false表示用户没有权限


        // 从请求中获取token
        Cookie[] cookies = request.getCookies();
        //获取请求头中的token
        String token = request.getHeader("token");
        //获取请求路径
        String path = request.getRequestURI();

        boolean voucher = false;
        //获取token 键值
        if (cookies == null) {
            if (token != null) {
                voucher = hasVerified(token, path);
            }
            return voucher;
        } else {
            voucher = hasVerified(TokenUtils.getToken(cookies), path);
            //判断是否有权限 有权限返回true 没有通过请求头中的token再次验证
            if (voucher) {
                return voucher;
            } else {
                if (token != null) {
                    voucher = hasVerified(token, path);
                }
            }
        }
        return voucher;
    }

    public Boolean hasVerified(String tokenValue, String path) {
        Boolean voucher = false;
        Integer role = usersService.voucherRole(tokenValue);
        //查询用户角色
        if (role == null) {
            return voucher;
        }
        //判断用户权限 0为管理员 1为普通用户 如果是普通用户则判断是否有权限
        for (String power : userPower) {
            if (path.equals(power) && role == 1) {
                return true;
            }
        }

        if (role == 0) {
            voucher = true;
        } else {
            voucher = false;
        }
        return voucher;
    }
}
