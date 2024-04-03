package com.example.librarysystemadmin.controller;

import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.utils.CaptchaUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/Api/util")
public class adminController {

    /**
     * 生成验证码
                */
        @RequestMapping("/captcha")
        public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
            // 使用gif验证码
            ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        // 几位数运算，默认是两位
        captcha.setLen(2);

        //将验证码存入session
        request.getSession().setAttribute("captcha", captcha.text());
        CaptchaUtil.out(captcha, request, response);
    }
}

