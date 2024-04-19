package com.example.librarysystemadmin.controller;


import com.example.librarysystemadmin.domain.*;
import com.example.librarysystemadmin.service.BooksService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.ProcessFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

@RestController
//@CrossOrigin(origins = "http://localhost:5173")
@CrossOrigin(origins = "*")
@RequestMapping("/Api/Book")
public class bookController {


    @Autowired
    private BooksService booksService;

    @RequestMapping("/getBookList")
    public ApiResponse<ListDataCount<CategoryCopiesBook[]>> getBookList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int limit, @RequestParam(required = false, defaultValue = "") String search) {
        return booksService.getBookList(search, page, limit);
    }


    @PostMapping("/saveBookInfo")
    public ApiResponse<String> addBook(@RequestBody FetchBook param) {
        return booksService.saveBookInfo(param);
    }

    //param为前端传来的文件
    @PostMapping("/uploadBookCover")
    public ApiResponse<String> uploadBookCover(@RequestParam("file") MultipartFile file) {
        return booksService.uploadBookCover(file);
    }

    @PostMapping("/devastateBook")
    public ApiResponse<String> devastateBook(@RequestBody Map<String, String> param) {
        return booksService.devastateBook(param.get("id"));
    }

    @RequestMapping("/getBookCategoryList")
    public ApiResponse<BookCategories[]> getBookCategoryList(@RequestParam(required = false, defaultValue = "") String search) {
        return booksService.getBookCategoryList(search);
    }

    @PostMapping("/addBookCategory")
    public ApiResponse<String> addBookCategory(@RequestBody Map<String, String> param) {
        return booksService.addBookCategory(param.get("categoryName"));
    }

    @PostMapping("/devastateBookCategory")
    public ApiResponse<String> devastateBookCategory(@RequestBody Map<String, String> param) {
        return booksService.devastateBookCategory(param.get("id"));
    }

    // 通过excel导入图书
    @PostMapping("/addBooksExcel")
    public ApiResponse<String> addBooksExcel(@RequestParam("file") MultipartFile file) {
        return booksService.addBooksExcel(file);
    }

    //获取热门图书接口
    @RequestMapping("/getHotBookList")
    public ApiResponse<CategoryCopiesBook[]> getHotBookList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "5") int limit) {
        return booksService.getHotBookList(page, limit);
    }

    //新书抢鲜接口
    @RequestMapping("/getNewBookList")
    public ApiResponse<CategoryCopiesBook[]> getNewBookList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "5") int limit) {

        return booksService.getNewBookList(page, limit);
    }

    //指定某一字段名进行模糊查询 并返回图书列表
    @RequestMapping("/getBookListByField")
    public ApiResponse<CategoryCopiesBook[]> getBookListByField(
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "5") int limit,
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String field
    ) {
        return booksService.getBookListByField(search, field, page, limit);
    }

    //获取分类书籍量最大的分类前n项
    @RequestMapping("/getTopNCategories")
    public ApiResponse<BookCategories[]> getTopNCategories(@RequestParam(required = false, defaultValue = "5") int limit) {
        return booksService.getTopNCategories(limit);
    }

    //获取图书详情
    @RequestMapping("/getBookInfo")
    public ApiResponse<CategoryCopiesBook> getBookInfo(@RequestParam(required = false, defaultValue = "1") String bookId, HttpServletRequest request) {
        return booksService.getBookInfo(bookId, request.getHeader("token"));
    }


}