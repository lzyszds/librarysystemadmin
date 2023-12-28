package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;
import com.example.librarysystemadmin.domain.UserSecret;
import com.example.librarysystemadmin.mapper.BookLoanMapper;
import com.example.librarysystemadmin.mapper.UsersMapper;
import com.example.librarysystemadmin.service.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;


@Component
public class BookLoanServiceImpl implements BookLoanService {

    @Autowired
    BookLoanMapper bookLoanMapper;

    @Autowired
    UsersMapper usersMapper;

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

    public String borrowingBook(String bookId, String token) {
        UserSecret users = usersMapper.getUserByToken(token);
        if (users == null) {
            return "用户不存在";
        } else {
            BookLoan bookLoan = new BookLoan();
            bookLoan.setBookId(Integer.parseInt(bookId));
            bookLoan.setuserId(users.getId());
            bookLoan.setLoanDate(new Date(System.currentTimeMillis()));
            bookLoan.setDueDate(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));

            if (bookLoanMapper.insertBookLoan(bookLoan) < 1) {
                return "借阅失败";
            }else{
                //给用户添加借阅次数
            }
            return null;
        }

    }
}
