package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BookLoanMapper {

    @Select("SELECT l.loan_id as loanId, l.loan_date as loanDate, l.due_date as dueDate, l.return_date as returnDate, " +
            "b.book_id as bookId, b.book_name as bookName,b.isbn as bookIsbn ,bc.copy_id as copyId, " +
            "u.id as userId, u.name as name " +
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

    @Update("UPDATE library_book_loan SET return_date = #{returnDate} WHERE loan_id = #{loanId}")
    int updateBookLoan(BookLoan bookLoan);

    @Delete("DELETE FROM library_book_loan WHERE book_id in (#{bookId})")
    int deleteBookLoan(String bookId);

    @Select("SELECT COUNT(*) FROM library_book_loan WHERE book_id in (#{bookId})")
    int getBookLoanList(String bookId);


}
