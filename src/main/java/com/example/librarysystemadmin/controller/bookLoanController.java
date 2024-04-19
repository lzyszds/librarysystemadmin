package com.example.librarysystemadmin.controller;


import com.example.librarysystemadmin.domain.*;
import com.example.librarysystemadmin.service.BookLoanService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Api/BookLoan")
public class bookLoanController {


    @Autowired
    BookLoanService bookLoanService;


    TokenUtils tokenUtils = new TokenUtils();

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
     *  借书接口需要传入图书id和副本号
     *  用户id是通过token获取的
     *  借书成功返回借书成功
     * */
    @PostMapping("/borrowingBook")
    public ApiResponse<String> borrowingBook(@RequestBody BookLoan param, HttpServletRequest request) {
        String token = request.getHeader("token");
        return bookLoanService.borrowingBook(param.getBookId(), token);
    }

    /*
     *  获取用户查看的书籍的副本id
     *  return 副本id copy_id 方便用户还书
     * */

    @RequestMapping("/getCopyId")
    public ApiResponse<String> getCopyId(@RequestParam Integer bookId) {
        return bookLoanService.getCopyId(bookId);
    }


    //根据用户id获取用户借阅书籍列表
    @RequestMapping("/getBorrowedBooks")
    public ApiResponse<CategoryCopiesBook[]> getBorrowedBooks(HttpServletRequest request) {
        return bookLoanService.getBorrowedBooks(request);
    }

    //根据用户id查看用户借阅的书籍归还时间
    @RequestMapping("/getBookLoanByUserId")
    public ApiResponse<BookLoan[]> getBookLoanByUserId(HttpServletRequest request) {
        return bookLoanService.getBookLoanByUserId(request.getHeader("token"));
    }

    /*
     *  还书接口
     *  还书接口需要传入图书id和副本号和用户id
     *  因为是后台操作，所以使用token来获取用户id
     *  还书成功返回还书成功
     * */
    @PostMapping("/returnBook")
    public ApiResponse<String> returnBook(@RequestBody BookLoan param, HttpServletRequest request) {
        Cookie[] tokens = request.getCookies();
        String token = TokenUtils.getToken(tokens);
        return bookLoanService.returnBook(param, token);
    }

}
