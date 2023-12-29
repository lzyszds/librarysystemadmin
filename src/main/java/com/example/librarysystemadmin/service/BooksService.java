package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.BookCategories;
import com.example.librarysystemadmin.domain.CategoryCopiesBook;
import com.example.librarysystemadmin.domain.FetchBook;
import com.example.librarysystemadmin.utils.ApiResponse;

public interface BooksService {
    CategoryCopiesBook[] getBookList(String search, int page, int limit);

    int getBookCount(String search);

    ApiResponse<String> saveBookInfo(FetchBook book);

    ApiResponse<String> devastateBook(String book_id);

    BookCategories[] getBookCategoryList(String search);

    ApiResponse<String> addBookCategory(String category_name);

    ApiResponse<String> devastateBookCategory(String category_id);
}
