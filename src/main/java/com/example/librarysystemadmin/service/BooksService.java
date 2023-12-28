package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookCategories;
import com.example.librarysystemadmin.domain.BookWithCategory;
import com.example.librarysystemadmin.utils.ApiResponse;

public interface BooksService {
    BookWithCategory[] getBookList(String search, int page, int limit);

    int getBookCount(String search);

    ApiResponse<String> saveBookInfo(Book book);



    ApiResponse<String> devastateBook(String book_id);

    BookCategories[] getBookCategoryList(String search);

    ApiResponse<String> addBookCategory(String category_name);

    ApiResponse<String> devastateBookCategory(String category_id);
}
