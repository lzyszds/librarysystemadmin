package com.example.librarysystemadmin.service;

import com.example.librarysystemadmin.domain.*;
import com.example.librarysystemadmin.utils.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface BooksService {
    ApiResponse<ListDataCount<CategoryCopiesBook[]>> getBookList(String search, int page, int limit);

    int getBookCount(String search);

    ApiResponse<String> saveBookInfo(FetchBook book);

    ApiResponse<String> uploadBookCover(MultipartFile file);

    ApiResponse<String> devastateBook(String book_id);

    ApiResponse<BookCategories[]> getBookCategoryList(String search);

    ApiResponse<String> addBookCategory(String category_name);

    ApiResponse<String> devastateBookCategory(String category_id);

    ApiResponse<String> addBooksExcel(MultipartFile file);

    ApiResponse<CategoryCopiesBook[]> getHotBookList(int page, int limit);

    ApiResponse<CategoryCopiesBook[]> getNewBookList(int page, int limit);

    ApiResponse<CategoryCopiesBook[]> getBookListByField(String search, String field, int page, int limit);

    ApiResponse<BookCategories[]> getTopNCategories(int n);

    ApiResponse<CategoryCopiesBook> getBookInfo(String book_id);


  
}
