package com.example.librarysystemadmin.controller;


import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookCategories;
import com.example.librarysystemadmin.domain.BookWithCategory;
import com.example.librarysystemadmin.domain.ListDataCount;
import com.example.librarysystemadmin.service.BooksService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.ProcessFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/Api/Book")
public class bookController {

    private String uploadPath = System.getProperty("user.dir") + "/uploadFile/";

    @Autowired
    private BooksService booksService;

    @RequestMapping("/getBookList")
    public ApiResponse<ListDataCount<BookWithCategory[]>> getBookList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int limit, @RequestParam(required = false, defaultValue = "") String search) {

        ApiResponse<ListDataCount<BookWithCategory[]>> apiResponse = new ApiResponse<>();
        ListDataCount<BookWithCategory[]> listDataCount = new ListDataCount<>();

        BookWithCategory[] books = booksService.getBookList(search, (page - 1) * limit, limit);
        int count = booksService.getBookCount(search);
        listDataCount.setCount(count);
        listDataCount.setData(books);
        apiResponse.setSuccessResponse(listDataCount);
        return apiResponse;
    }


    @PostMapping("/saveBookInfo")
    public ApiResponse<String> addBook(@RequestBody Book param) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            apiResponse = booksService.saveBookInfo(param);
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "操作错误", "/Api/Book/saveBookInfo", e);
        }
        return apiResponse;
    }

    //param为前端传来的文件
    @PostMapping("/uploadBookCover")
    public ApiResponse<String> uploadBookCover(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        //验证文件信息
        if (!ProcessFiles.verifyFileConfig(file)) {
            apiResponse.setErrorResponse(400, "检查文件：格式不为图片或者大小超过2M的");
            return apiResponse;
        }
        //获取文件名
        //获取文件的md5值（16进制） 防止上传重复文件或者文件名重复
        String md5 = ProcessFiles.getFileMd5(file);
        //检验文件夹是否存在 不存在则创建
        if (!ProcessFiles.createDir(uploadPath)) {
            apiResponse.setErrorResponse(500, "文件夹创建失败");
            return apiResponse;
        }
        //生成新的文件名  MD5 + 时间戳 + 随机数 + 用户ID
        String newName = md5 + ".jpg";
        //检索文件夹下是否有同名文件
        if (!ProcessFiles.checkFileExist(uploadPath, newName)) {
            apiResponse.setSuccessResponse("/uploadFile/" + newName);
            return apiResponse;
        }
        //初始化要返回的图片路径
        String filePath;
        try {
            File storeFile = new File(uploadPath + newName);
            //将文件保存到指定位置
            file.transferTo(storeFile);
            //压缩图片 超过300px的图片压缩
            ProcessFiles.imageReduce(storeFile, 300, 0.25f, uploadPath);
            //返回图片路径
            filePath = "/uploadFile/" + newName;
            apiResponse.setSuccessResponse(filePath);
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "文件上传失败");
        }

        return apiResponse;
    }

    @PostMapping("/devastateBook")
    public ApiResponse<String> devastateBook(@RequestBody Map<String, String> param) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            apiResponse = booksService.devastateBook(param.get("id"));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "删除失败", "/Api/Book/devastateBook", e);
        }
        return apiResponse;
    }

    @RequestMapping("/getBookCategoryList")
    public ApiResponse<BookCategories[]> getBookCategoryList(@RequestParam(required = false, defaultValue = "") String search) {
        ApiResponse<BookCategories[]> apiResponse = new ApiResponse<>();
        try {
            BookCategories[] bookCategories = booksService.getBookCategoryList(search);
            apiResponse.setSuccessResponse(bookCategories);
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "获取失败", "/Api/Book/getBookCategoryList", e);
        }
        return apiResponse;
    }

    @PostMapping("/addBookCategory")
    public ApiResponse<String> addBookCategory(@RequestBody Map<String, String> param) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            apiResponse = booksService.addBookCategory(param.get("categoryName"));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "添加失败", "/Api/Book/addBookCategory", e);
        }
        return apiResponse;
    }

    @PostMapping("/devastateBookCategory")
    public ApiResponse<String> devastateBookCategory(@RequestBody Map<String, String> param) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            apiResponse = booksService.devastateBookCategory(param.get("id"));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "删除失败", "/Api/Book/devastateBookCategory", e);
        }
        return apiResponse;
    }


}