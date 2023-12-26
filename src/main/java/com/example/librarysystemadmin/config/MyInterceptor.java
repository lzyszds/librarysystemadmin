package com.example.librarysystemadmin.config;


import com.example.librarysystemadmin.service.UsersService;
import com.example.librarysystemadmin.utils.RSAUtils;
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

        boolean voucher = false;
        //获取token 键值
        if (cookies == null) return voucher;


        // 遍历 Cookie 数组
        for (Cookie cookie : cookies) {
            // 检查是否是你需要的 Cookie，这里假设你的 Cookie 名称为 "token"
            if ("token".equals(cookie.getName())) {
                // 获取 Cookie 的值
                String tokenValue = cookie.getValue();
                //解密token 获取用户名
                String username = RSAUtils.decrypt(tokenValue);
                if (username == null) return voucher;
                //查询用户角色
                if (usersService.voucherRole(username) == 0) voucher = true;

            }
        }
        return voucher;
    }
}
