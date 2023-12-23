package com.example.librarysystemadmin.controller;

import com.example.librarysystemadmin.domain.ListDataCount;
import com.example.librarysystemadmin.domain.User;
import com.example.librarysystemadmin.service.UsersService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.Map;

@RestController
@RequestMapping("/Api")
public class userController {

    @Autowired
    private UsersService usersService;

    /*
     * 验证凭证
     * return 0 管理员  1 读者 2 游客
     */
    public int inspectCookie(Cookie[] cookies) {
        int voucher = 2;
        //获取token 键值
        if (cookies == null) {
            return voucher;
        }
        // 遍历 Cookie 数组
        for (Cookie cookie : cookies) {
            // 检查是否是你需要的 Cookie，这里假设你的 Cookie 名称为 "token"
            if ("token".equals(cookie.getName())) {
                // 获取 Cookie 的值
                String tokenValue = cookie.getValue();
                voucher = usersService.voucherRole(tokenValue);
            }
        }

        return voucher;
    }

    @PostMapping("/register")
    public ApiResponse<String> register(@RequestBody User params) {
        ApiResponse<String> apiResponse = new ApiResponse<String>();
        // 设置创建时间
        params.setCreated_at(new Date(System.currentTimeMillis()));
        // 对密码进行加密 使用用户账号作为初始密码
        params.setPassword(RSAUtils.encrypt(params.getUsername()));

        // 对token进行加密 并添加随机字符串
        String token = RSAUtils.encrypt(params.getUsername());
        params.setToken(token);

        try {
            int result = usersService.registerUser(params);
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
                apiResponse.setErrorResponse(400, "参数错误");
                return apiResponse;
            }
            if (request.getSession().getAttribute("captcha") == null) {
                apiResponse.setErrorResponse(400, "验证码失效");
                return apiResponse;
            } else if (!request.getSession().getAttribute("captcha").equals(captcha)) {
                apiResponse.setErrorResponse(400, "验证码错误");
                return apiResponse;
            }

            User userInfo = usersService.queryUser(username); // 查询用户是否存在
            if (userInfo == null) {
                apiResponse.setErrorResponse(404, "用户不存在");
                return apiResponse;
            }

            if (RSAUtils.decrypt(userInfo.getPassword()).equals(password)) {
                // 返回成功结果和成功码
                apiResponse.setSuccessResponse(userInfo.getToken());
            } else {
                // 返回失败结果和失败码
                apiResponse.setErrorResponse(400, "密码错误");
            }
        } catch (Exception e) {
            // 捕获异常并返回适当的错误信息
            apiResponse.setErrorResponse(500, e.toString());
        }

        return apiResponse;
    }


    //获取用户列表
    @GetMapping("/queryUserList")
    public ApiResponse<ListDataCount<User[]>> queryUserlist(@RequestParam(name = "page") String pageStr,
                                                            @RequestParam(name = "limit") String limitStr,
                                                            HttpServletRequest request) {
        ListDataCount<User[]> listDataCount = new ListDataCount<>();
        ApiResponse<ListDataCount<User[]>> apiResponse = new ApiResponse<>();
        Cookie[] cookies = request.getCookies();

        try {
            //只有为管理员才能获取
            if (inspectCookie(cookies) == 0) {
                int page = Integer.parseInt(pageStr) - 1;
                int limit = Integer.parseInt(limitStr);
                // 查询用户列表 分页 page是页数 limit是每页的数量 让数据库从第page*limit条开始查询 limit条数据
                User[] list = usersService.queryUserList(page * limit, limit);
                Integer size = usersService.queryUserListCount(); // 查询用户总数
                // 返回成功结果和成功码 返回用户列表 和 用户总数
                listDataCount.setCount(size);
                listDataCount.setData(list);
                apiResponse.setSuccessResponses(listDataCount);
            } else {
                apiResponse.setErrorResponse(403, "error");
            }
        } catch (NumberFormatException e) {
            apiResponse.setErrorResponse(400, "Invalid page or limit parameter");
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "Internal Server Error");
        }
        return apiResponse;

    }

    @GetMapping("/querySearchUsers")
    public ApiResponse<ListDataCount<User[]>> querySearchUsers(@RequestParam(name = "page") String pageStr,
                                                               @RequestParam(name = "limit") String limitStr,
                                                               @RequestParam(name = "search") String search,
                                                               HttpServletRequest request) {
        ListDataCount<User[]> listDataCount = new ListDataCount<>();
        ApiResponse<ListDataCount<User[]>> apiResponse = new ApiResponse<>();
        Cookie[] cookies = request.getCookies();
        //只有为管理员才能获取
        if (inspectCookie(cookies) == 0) {
            int page = Integer.parseInt(pageStr) - 1;
            int limit = Integer.parseInt(limitStr);
            // 查询用户列表 分页 page是页数 limit是每页的数量 让数据库从第page*limit条开始查询 limit条数据
            int size = usersService.querySearcUserListCount(search);
            User[] user = usersService.querySearchUsers(search, page * limit, limit);
            listDataCount.setCount(size);
            listDataCount.setData(user);
            apiResponse.setSuccessResponse(listDataCount);
        } else {
            apiResponse.setErrorResponse(403, "暂无权限");
        }

        return apiResponse;
    }

    @PostMapping("/devastateUser")
    public ApiResponse<String> devastateUser(@RequestBody Map<String, String> params, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Cookie[] cookies = request.getCookies();
        //只有为管理员才能获取
        if (inspectCookie(cookies) == 0) {
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
        } else {
            apiResponse.setErrorResponse(403, "暂无权限");
        }

        return apiResponse;
    }
}
