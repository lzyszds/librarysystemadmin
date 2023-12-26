package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookWithCategory;
import com.example.librarysystemadmin.mapper.BooksMapper;
import com.example.librarysystemadmin.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BooksServiceImpl implements BooksService {
    @Autowired
    private BooksMapper booksMapper;

    public BookWithCategory[] queryBookList(String search, int page, int limit) {
        return booksMapper.queryBookList(search, page, limit);
    }

    public int queryBookCount(String search) {
        return booksMapper.queryBookCount(search);
    }

    public int addBook(Book book) {
        return booksMapper.addBook(book);
    }

    public int queryBookByIsbn(String isbn) {
        return booksMapper.queryBookByIsbn(isbn);
    }


}
