package com.example.librarysystemadmin.controller;


import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookCopies;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;
import com.example.librarysystemadmin.domain.ListDataCount;
import com.example.librarysystemadmin.service.BookLoanService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Api/bookLoan")
public class bookLoanController {


    @Autowired
    BookLoanService bookLoanService;

    TokenUtils TokenUtils;

    /*
     * 获取图书借阅列表
     * */
    @RequestMapping("/getList")
    public ApiResponse<ListDataCount<BookLoanWithBookUser[]>> getBorrowBookList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int limit, @RequestParam(required = false, defaultValue = "") String search) {

        ApiResponse<ListDataCount<BookLoanWithBookUser[]>> apiResponse = new ApiResponse<>();
        ListDataCount<BookLoanWithBookUser[]> listDataCount = new ListDataCount<>();

        BookLoanWithBookUser[] bookLoans = bookLoanService.getBorrowBookList(search, page, limit);
        int count = bookLoanService.getBorrowBookListCount(search);
        listDataCount.setCount(count);
        listDataCount.setData(bookLoans);
        apiResponse.setSuccessResponse(listDataCount);
        return apiResponse;
    }

    /*
     *  借书接口
     * */
    @PostMapping("/borrowingBook")
    public ApiResponse<String> borrowingBook(@RequestBody BookCopies param, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Cookie[] tokens = request.getCookies();
        String token = TokenUtils.getToken(tokens);
        String result = bookLoanService.borrowingBook(param, token);
        if (result == null) {
            apiResponse.setSuccessResponse("借阅成功");
        } else {
            apiResponse.setErrorResponse(400, result);
        }

        return apiResponse;
    }

    /*
     *  还书接口
     * */
    @PostMapping("/returnBook")
    public ApiResponse<String> returnBook(@RequestBody Book param, HttpServletRequest request) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        Cookie[] tokens = request.getCookies();
        String token = TokenUtils.getToken(tokens);
        String result = bookLoanService.returnBook(param.getBook_id().toString(), token);
        if (result == null) {
            apiResponse.setSuccessResponse("归还成功");
        } else {
            apiResponse.setErrorResponse(400, result);
        }

        return apiResponse;
    }
}
