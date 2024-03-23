package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.config.LibraryConfig;
import com.example.librarysystemadmin.domain.*;
import com.example.librarysystemadmin.mapper.BookCopiesMapper;
import com.example.librarysystemadmin.mapper.BookLoanMapper;
import com.example.librarysystemadmin.mapper.UsersMapper;
import com.example.librarysystemadmin.service.BookLoanService;
import com.example.librarysystemadmin.service.StatisticService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;


@Component

public class BookLoanServiceImpl implements BookLoanService {

    @Autowired
    BookLoanMapper bookLoanMapper;

    @Autowired
    UsersMapper usersMapper;

    @Autowired
    BookCopiesMapper bookCopiesMapper;

    @Autowired
    private LibraryConfig libraryConfig;

    @Autowired
    StatisticService statisticService;

    ArrayUtils<String> arrayUtils = new ArrayUtils<>();

    public BookLoanWithBookUser[] getBorrowBookList(String search, int page, int limit) {
        page = (page - 1) * limit;
        if (search == null || search.equals("")) {
            search = "";
        }
        return bookLoanMapper.findAll(search, page, limit);
    }

    public int getBorrowBookListCount(String search) {
        if (search == null || search.equals("")) {
            search = "";
        }
        return bookLoanMapper.findAllCount(search);
    }

    public ApiResponse<CategoryCopiesBook[]> getBorrowedBooks(HttpServletRequest request) {

        ApiResponse<CategoryCopiesBook[]> apiResponse = new ApiResponse<>();
        try {
            //通过token获取用户id
            String token = request.getHeader("token");
            String userId = Integer.toString(usersMapper.getUserByToken(token).getId());
            apiResponse.setSuccessResponse(bookCopiesMapper.getBorrowedBooks(userId));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            apiResponse.setErrorResponse(500, "查询失败");
        }
        return apiResponse;
    }

    public ApiResponse<String> borrowingBook(Integer bookId, String token) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        UserSecret users = usersMapper.getUserByToken(token);
        if (users == null) {
            apiResponse.setErrorResponse(400, "用户不存在");
            return apiResponse;
        }
        if (users.getBorrow() != null && !users.getBorrow().equals("")) {
            String[] borrowIds = users.getBorrow().split(",");
            //判断用户借阅数量是否上限
            if (borrowIds.length >= libraryConfig.getMaxBooksPerUser()) {
                apiResponse.setErrorResponse(400, "借阅数量已达上限");
                return apiResponse;
            }
            //判断用户是否借阅过该本书籍
            if (arrayUtils.includes(borrowIds, Integer.toString(bookId))) {
                apiResponse.setErrorResponse(400, "已借阅过该书籍");
                return apiResponse;
            }

        }
        //获取当前书籍的副本id还剩余库存
        String[] copyId = bookLoanMapper.getCopyIdByBookId(bookId);
        if (copyId.length < 1) {
            apiResponse.setErrorResponse(400, "书籍库存不足");
            return apiResponse;
        }

        BookLoan bookLoan = new BookLoan();
        bookLoan.setCopyId(copyId[0]);
        bookLoan.setBookId(bookId);
        bookLoan.setUserId(users.getId());
        bookLoan.setLoanDate(new Date(System.currentTimeMillis()));
        bookLoan.setDueDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        if (bookLoanMapper.insertBookLoan(bookLoan) < 1) {
            apiResponse.setErrorResponse(400, "借阅失败");
        } else {
            //给用户添加借阅字段
            if (users.getBorrow() == null || users.getBorrow().equals("")) {
                users.setBorrow(bookId + "");
            } else {
                users.setBorrow(users.getBorrow() + "," + bookId);
            }

            //给用户添加借阅次数
            usersMapper.updateBorrowCount(users.getId(), users.getBorrow());
            //给书籍副本添加借出状态
            bookCopiesMapper.setBookCopiesStatus(copyId[0].toString(), 1);
            //给日志表添加借阅记录
            statisticService.setStatisticsHandle("borrowed");
            apiResponse.setSuccessResponse("借阅成功");
        }
        return apiResponse;
    }

    @Override
    public ApiResponse<BookLoan[]> getBookLoanByUserId(String token) {
        ApiResponse<BookLoan[]> apiResponse = new ApiResponse<>();
        UserSecret user = usersMapper.getUserByToken(token);
        if (user == null) {
            apiResponse.setErrorResponse(400, "用户不存在");
        } else {
            BookLoan[] bookLoans = bookLoanMapper.getBookLoanByUserId(user.getId());
            System.out.println(bookLoans);
            apiResponse.setSuccessResponse(bookLoans);
        }
        return apiResponse;
    }


    public ApiResponse<String> getCopyId(Integer bookId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        String[] copyId = bookLoanMapper.getCopyIdByBookId(bookId);
        if (copyId == null) {
            apiResponse.setErrorResponse(400, "副本不存在");
        } else {
            apiResponse.setSuccessResponse(copyId[0]);
        }
        return apiResponse;
    }

    public ApiResponse<String> returnBook(BookLoan param, String token) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        UserSecret user = usersMapper.getUserByToken(token);

        if (user == null) {
            apiResponse.setErrorResponse(400, "用户不存在");
        } else if (user.getRole() == 0) {
            //获取当前时间
            Date returnDate = new Date(System.currentTimeMillis());
            //获取当前书籍借阅信息的id
            int loanId = param.getLoanId();
            if (bookLoanMapper.updateBookLoan(returnDate, loanId) < 1) {
                apiResponse.setErrorResponse(400, "归还失败");
                return apiResponse;
            }
            //获取当前借阅用户的信息
            User loanUser = usersMapper.getUserByid(param.getUserId());
            //给用户借阅字段删除当前书籍id
            String[] borrowIds = loanUser.getBorrow().split(",");
            String borrow = "";
            for (int i = 0; i < borrowIds.length; i++) {
                if (!borrowIds[i].equals(param.getBookId().toString())) {
                    borrow += borrowIds[i] + ",";
                }
            }
            if (borrow.equals("")) {
                usersMapper.updateBorrowCount(param.getUserId(), "");
            } else {
                usersMapper.updateBorrowCount(param.getUserId(), borrow.substring(0, borrow.length() - 1));
            }
            //给书籍副本添加归还状态
            bookCopiesMapper.setBookCopiesStatus(param.getCopyId(), 0);
            //给日志表添加归还记录
            statisticService.setStatisticsHandle("returned");
            apiResponse.setSuccessResponse("归还成功");
        } else {
            apiResponse.setErrorResponse(400, "权限不足");
        }
        return apiResponse;
    }
}
