package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookWithCategory;

public interface BooksService {
    BookWithCategory[] queryBookList(String search, int page, int limit);

    int queryBookCount(String search);

    int addBook(Book book);
    int queryBookByIsbn(String isbn);
}
