package com.example.librarysystemadmin.common;


import com.example.librarysystemadmin.utils.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/*
 *  @ControllerAdvice 注解的类可以拦截所有 Controller 所抛出的异常
 *  @Component("customGlobalExceptionHandler") 注解用于将该类作为 Spring 的组件进行管理，并指定了一个自定义的名称。
 *  @ExceptionHandler(value = Exception.class) 注解用于指定处理所有异常的方法。
 *  @ResponseBody 注解用于将方法的返回值作为响应体返回。
 *  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 注解用于设置响应的 HTTP 状态码为 500 Internal Server Error。
 *  ApiResponse<String> 是一个泛型类，用于封装 API 的响应结果。它包含一个状态码、一个消息和一个数据字段。
 *  globalExceptionHandle 方法是处理所有异常的具体逻辑。当捕获到任何异常时，它会记录错误日志，包括异常的堆栈跟踪信息。然后，创建一个包含错误信息的 ApiResponse 对象，并将状态码设置为 500。
 *  getExceptionInfo 方法用于获取异常的堆栈跟踪信息，并将其转换为字符串形式返回。
 *  log配置文件在resources文件夹下的logback-spring.xml
 *  存储在<property name="FILE_PATH" value="C:/javaLog/"/>中
 * */
@ControllerAdvice
@Component("customGlobalExceptionHandler")
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<String> globalExceptionHandle(Exception e) {
        log.error("===========全局统一异常处理============");
        log.error(getExceptionInfo(e));
        return new ApiResponse<>(500, "error", getExceptionInfo(e));
    }

    private static String getExceptionInfo(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        ex.printStackTrace(printStream);
        String rs = new String(out.toByteArray());
        try {
            printStream.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

}
