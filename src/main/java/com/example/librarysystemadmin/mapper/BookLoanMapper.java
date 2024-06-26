package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface BookLoanMapper {

    @Select("SELECT *" +
            "FROM library_book_loan l " +
            "INNER JOIN books b ON l.book_id = b.book_id " +
            "INNER JOIN book_copies bc ON bc.copy_id = l.copy_id " +
            "INNER JOIN users u ON l.user_id = u.id " +
            "WHERE book_name like '%${search}%' or u.name like '%${search}%' " +
            "LIMIT #{page}, #{limit} ")
    BookLoanWithBookUser[] findAll(String search, int page, int limit);

    @Select("SELECT count(*) " +
            "FROM library_book_loan l " +
            "LEFT JOIN books b ON l.book_id = b.book_id " +
            "LEFT JOIN users u ON l.user_id = u.id " +
            "WHERE book_name like '%${search}%' or u.name like '%${search}%' ")
    int findAllCount(String search);

    @Insert("INSERT INTO library_book_loan (book_id, user_id, copy_id, loan_date, due_date) " +
            "VALUES (#{bookId}, #{userId}, #{copyId}, #{loanDate}, #{dueDate})")
    int insertBookLoan(BookLoan bookLoan);

    //更新图书借阅
    @Update("UPDATE library_book_loan SET return_date = #{returnDate} WHERE loan_id = #{loanId}")
    int updateBookLoan(Date returnDate, Integer loanId);

    //删除图书借阅
    @Delete("DELETE FROM library_book_loan WHERE book_id in (#{bookId})")
    int deleteBookLoan(String bookId);

    //获取图书借阅列表
    @Select("SELECT COUNT(*) FROM library_book_loan WHERE book_id in (#{bookId})")
    int getBookLoanList(String bookId);

    @Select("SELECT copy_id FROM book_copies WHERE book_id = #{bookId} and status = 0")
    String[] getCopyIdByBookId(Integer bookId);

    @Select("SELECT * " +
            "FROM library_book_loan WHERE user_id = #{userId}")
    BookLoan[] getBookLoanByUserId(Integer userId);

    @Select("SELECT loan_id FROM library_book_loan WHERE book_id = #{bookId} and user_id = #{userId}")
    String getBookLoanByBookId(String bookId, Integer userId);

}
