package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.BookCopies;
import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;
import com.example.librarysystemadmin.domain.CategoryCopiesBook;
import com.example.librarysystemadmin.utils.ApiResponse;

import javax.servlet.http.HttpServletRequest;

public interface BookLoanService {

    BookLoanWithBookUser[] getBorrowBookList(String search, int page, int limit);

    int getBorrowBookListCount(String search);

    String borrowingBook(BookCopies bookId, String token);

    ApiResponse<CategoryCopiesBook[]> getBorrowedBooks(HttpServletRequest userId);

    ApiResponse<String> returnBook(BookLoan param);
}
