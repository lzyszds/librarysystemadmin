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

    public String borrowingBook(BookCopies param, String token) {
        UserSecret users = usersMapper.getUserByToken(token);
        if (users == null) {
            return "用户不存在";
        } else {
            // 判断书籍id跟副本号是否匹配
            if (bookCopiesMapper.getCopiesByCopyId(param.getCopy_id()).getBook_id() != param.getBook_id()) {
                return "书籍id不匹配";
            }
            System.out.println(users.getBorrow());
            if (users.getBorrow() != null) {
                String[] borrowIds = users.getBorrow().split(",");
                //判断用户借阅数量是否上限
                if (borrowIds.length >= libraryConfig.getMaxBooksPerUser()) {
                    return "借阅数量已达上限";
                }
                //判断用户是否借阅过该本书籍
                if (arrayUtils.includes(borrowIds, Integer.toString(param.getBook_id()))) {
                    return "您已借阅过该书籍";
                }
            }
            //判断当前书籍是否有库存（未借出的）
            BookCopies bookCopies = bookCopiesMapper.getCopiesByCopyId(param.getCopy_id());

            if (bookCopies == null || bookCopies.getStatus() != 0) {
                return "当前书籍已被借出";
            }

            BookLoan bookLoan = new BookLoan();
            bookLoan.setCopyId(param.getCopy_id());
            bookLoan.setBookId(param.getBook_id());
            bookLoan.setUserId(users.getId());
            bookLoan.setLoanDate(new Date(System.currentTimeMillis()));
            bookLoan.setDueDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));

            if (bookLoanMapper.insertBookLoan(bookLoan) < 1) {
                return "借阅失败";
            } else {
                //给用户添加借阅字段
                if (users.getBorrow() == null || users.getBorrow().equals("")) {
                    users.setBorrow(param.getBook_id() + "");
                } else {
                    users.setBorrow("," + users.getBorrow());
                }
                String borrow = users.getBorrow() + param.getBook_id();
                //给用户添加借阅次数
                usersMapper.updateBorrowCount(users.getId(), borrow);
                //给书籍副本添加借出状态
                bookCopiesMapper.setBookCopiesStatus(param.getCopy_id(), "1");
                //给日志表添加借阅记录
                statisticService.setStatisticsHandle("borrowed");
            }
            return null;
        }

    }

    public ApiResponse<CategoryCopiesBook[]> getBorrowedBooks(HttpServletRequest request) {

        ApiResponse<CategoryCopiesBook[]> apiResponse = new ApiResponse<>();
        try {
            //通过token获取用户id
            String token = request.getHeader("token");
            String userId = Integer.toString(usersMapper.getUserByToken(token).getId());
            apiResponse.setSuccessResponse(bookCopiesMapper.getBorrowedBooks(userId));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "查询失败");
        }
        return apiResponse;
    }

    public ApiResponse<String> returnBook(BookLoan param) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        User users = usersMapper.getUserByid(param.getUserId());
        if (users == null) {
            apiResponse.setErrorResponse(400, "用户不存在");
        } else {
            //获取当前时间
            Date returnDate = new Date(System.currentTimeMillis());
            //获取当前书籍借阅信息的id
            int loanId = param.getLoanId();
            if (bookLoanMapper.updateBookLoan(returnDate, loanId) < 1) {
                apiResponse.setErrorResponse(400, "归还失败");
                return apiResponse;
            }
            //给用户借阅字段删除当前书籍id
            String[] borrowIds = users.getBorrow().split(",");
            String borrow = "";
            for (int i = 0; i < borrowIds.length; i++) {
                if (!borrowIds[i].equals(param.getBookId().toString())) {
                    borrow += borrowIds[i] + ",";
                }
            }
            if (borrow.equals("")) {
                usersMapper.updateBorrowCount(users.getId(), "");
            } else {
                usersMapper.updateBorrowCount(users.getId(), borrow.substring(0, borrow.length() - 1));
            }
            //给书籍副本添加归还状态
            bookCopiesMapper.setBookCopiesStatus(param.getCopyId(), "0");
            //给日志表添加归还记录
            statisticService.setStatisticsHandle("returned");
            apiResponse.setSuccessResponse("归还成功");
        }
        return apiResponse;
    }
}
