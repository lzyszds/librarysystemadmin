package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.config.LibraryConfig;
import com.example.librarysystemadmin.domain.BookCopies;
import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;
import com.example.librarysystemadmin.domain.UserSecret;
import com.example.librarysystemadmin.mapper.BookCopiesMapper;
import com.example.librarysystemadmin.mapper.BookLoanMapper;
import com.example.librarysystemadmin.mapper.UsersMapper;
import com.example.librarysystemadmin.service.BookLoanService;
import com.example.librarysystemadmin.service.StatisticService;
import com.example.librarysystemadmin.utils.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            String[] borrowIds = users.getBorrow().split(",");
            //判断用户借阅数量是否上限
            if (borrowIds.length >= libraryConfig.getMaxBooksPerUser()) {
                return "借阅数量已达上限";
            }
            //判断用户是否借阅过该本书籍
            if (arrayUtils.includes(borrowIds, Integer.toString(param.getBook_id()))) {
                return "您已借阅过该书籍";
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
                //如果用户没有借阅过书籍，就给用户添加借阅字段
                if (users.getBorrow().equals("") || users.getBorrow() == null) users.setBorrow("");
                else users.setBorrow(users.getBorrow() + ",");
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

    public String returnBook(String bookId, String token) {
        UserSecret users = usersMapper.getUserByToken(token);
        if (users == null) {
            return "用户不存在";
        } else {
            BookLoan bookLoan = new BookLoan();
            bookLoan.setBookId(Integer.parseInt(bookId));
            bookLoan.setUserId(users.getId());
            bookLoan.setReturnDate(new Date(System.currentTimeMillis()));
            if (bookLoanMapper.updateBookLoan(bookLoan) < 1) {
                return "还书失败";
            }
            //给用户借阅字段删除当前书籍id
            String[] borrowIds = users.getBorrow().split(",");
            String borrow = "";
            for (int i = 0; i < borrowIds.length; i++) {
                if (!borrowIds[i].equals(bookId)) {
                    borrow += borrowIds[i] + ",";
                }
            }
            if (borrow.equals("")) {
                usersMapper.updateBorrowCount(users.getId(), "");
            } else {
                usersMapper.updateBorrowCount(users.getId(), borrow.substring(0, borrow.length() - 1));
            }
            //给书籍副本添加归还状态
            bookCopiesMapper.setBookCopiesStatus(bookId, "0");
            //给日志表添加归还记录
            statisticService.setStatisticsHandle("returned");
            return null;
        }
    }
}
