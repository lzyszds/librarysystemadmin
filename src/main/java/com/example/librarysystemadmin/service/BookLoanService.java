package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.BookLoanWithBookUser;

public interface BookLoanService {

    BookLoanWithBookUser[] getBorrowBookList(String search, int page, int limit);

    int getBorrowBookListCount(String search);

    String borrowingBook(String bookId, String token);
}
