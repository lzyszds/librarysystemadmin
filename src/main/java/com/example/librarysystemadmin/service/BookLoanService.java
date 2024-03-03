package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.BookCopies;
import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.domain.BookLoanWithBookUser;

public interface BookLoanService {

    BookLoanWithBookUser[] getBorrowBookList(String search, int page, int limit);

    int getBorrowBookListCount(String search);

    String borrowingBook(BookCopies bookId, String token);

    String returnBook(String bookId, String token);
}
