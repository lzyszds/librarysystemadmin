package com.example.librarysystemadmin.controller;


import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookWithCategory;
import com.example.librarysystemadmin.domain.ListDataCount;
import com.example.librarysystemadmin.service.BooksService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.ProcessFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/Api/Book")
public class bookController {

    private String uploadPath = System.getProperty("user.dir") + "/uploadFile/";

    @Autowired
    private BooksService booksService;

    @RequestMapping("/queryBookList")
    public ApiResponse<ListDataCount<BookWithCategory[]>> queryBookList(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int limit, @RequestParam(required = false, defaultValue = "") String search) {

        ApiResponse<ListDataCount<BookWithCategory[]>> apiResponse = new ApiResponse<>();
        ListDataCount<BookWithCategory[]> listDataCount = new ListDataCount<>();

        BookWithCategory[] books = booksService.queryBookList(search, (page - 1) * limit, limit);
        int count = booksService.queryBookCount(search);
        listDataCount.setCount(count);
        listDataCount.setData(books);
        apiResponse.setSuccessResponse(listDataCount);
        return apiResponse;
    }


    @PostMapping("/addBook")
    public ApiResponse<String> addBook(@RequestBody Book param) {
        ApiResponse<String> apiResponse = new ApiResponse<>();

        try {
            // 参数验证
            if (param.getBook_name() == null || param.getBook_name().isEmpty()) {
                apiResponse.setErrorResponse(400, "书名不能为空");
                return apiResponse;
            }
            if (param.getAuthor() == null || param.getAuthor().isEmpty()) {
                apiResponse.setErrorResponse(400, "作者不能为空");
                return apiResponse;
            }
            if (param.getIsbn() == null || param.getIsbn().isEmpty()) {
                apiResponse.setErrorResponse(400, "ISBN不能为空");
                return apiResponse;
            }
            if (param.getCategory_id() == null) {
                apiResponse.setErrorResponse(400, "分类不能为空");
                return apiResponse;
            }
            if (param.getPublisher() == null || param.getPublisher().isEmpty()) {
                apiResponse.setErrorResponse(400, "出版社不能为空");
                return apiResponse;
            }
            if (param.getPublish_date() == null) {
                apiResponse.setErrorResponse(400, "出版日期不能为空");
                return apiResponse;
            }
            //简介可以为空
            if (param.getIntroduction() == null) {
                param.setIntroduction("");
            }

            // 检查ISBN是否已经存在
            if (booksService.queryBookByIsbn(param.getIsbn()) > 0) {
                apiResponse.setErrorResponse(400, "ISBN已存在");
                return apiResponse;
            }
            param.setStatus(0);

            int result = booksService.addBook(param);
            if (result == 1) {
                apiResponse.setSuccessResponse("添加成功");
            } else {
                apiResponse.setErrorResponse(500, "添加失败");
            }
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "添加失败", "/Api/Book/addBook", e);
        }
        return apiResponse;
    }

    //param为前端传来的文件
    @PostMapping("/uploadBookCover")
    public ApiResponse<String> uploadBookCover(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 参数验证
        if (file == null || file.isEmpty()) {
            apiResponse.setErrorResponse(400, "文件不能为空");
            return apiResponse;
        }
        //验证文件格式
        String type = file.getContentType();
        if (!type.matches("image/(jpg|png|gif|jpeg|webp)")) {
            apiResponse.setErrorResponse(400, "文件格式不正确");
            return apiResponse;
        }
        //验证文件大小
        if (file.getSize() > 1024 * 1024 * 2) {
            apiResponse.setErrorResponse(400, "文件大小不能超过2M");
            return apiResponse;
        }
        //获取文件的md5值（16进制） 防止上传重复文件或者文件名重复
        String md5 = ProcessFiles.getFileMd5(file);
        File folder = new File(uploadPath);
        if (!folder.exists()) {
            if (!folder.mkdir()) {
                apiResponse.setErrorResponse(500, "文件夹创建失败");
                return apiResponse;
            }
        }
        //生成新的文件名  MD5 + 时间戳 + 随机数 + 用户ID
        String newName = md5 + ".jpg";

        //检索文件夹下是否有同名文件
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.getName().equals(newName)) {
                apiResponse.setSuccessResponse("/uploadFile/" + newName);
                return apiResponse;
            }
        }

        //初始化要返回的图片路径
        String filePath;
        try {

            File storeFile = new File(uploadPath + newName);
            //将文件保存到指定位置
            file.transferTo(storeFile);
            ProcessFiles.imageReduce(storeFile, 300, uploadPath);
            //返回图片路径
            filePath = "/uploadFile/" + newName;
            apiResponse.setSuccessResponse(filePath);

        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "文件上传失败");
        }

        return apiResponse;
    }

}