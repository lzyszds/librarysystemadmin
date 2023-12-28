package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookCategories;
import com.example.librarysystemadmin.domain.BookWithCategory;
import com.example.librarysystemadmin.domain.BookLoan;
import com.example.librarysystemadmin.mapper.BooksMapper;
import com.example.librarysystemadmin.service.BooksService;
import com.example.librarysystemadmin.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BooksServiceImpl implements BooksService {
    @Autowired
    private BooksMapper booksMapper;

    public BookWithCategory[] getBookList(String search, int page, int limit) {
        return booksMapper.getBookList(search, page, limit);
    }

    public int getBookCount(String search) {
        return booksMapper.getBookCount(search);
    }

    static String visitedInfo(Book book) {
        if (book.getBook_name() == null || book.getBook_name().equals("")) {
            return "请填写书名";
        }
        if (book.getAuthor() == null || book.getAuthor().equals("")) {
            return "请填写作者";
        }
        if (book.getCategory_id() == null || book.getCategory_id().equals("")) {
            return "请选择分类";
        }
        if (book.getIsbn() == null || book.getIsbn().equals("")) {
            return "请填写ISBN";
        }
        if (book.getPublisher() == null || book.getPublisher().equals("")) {
            return "请填写出版社";
        }
        if (book.getPublish_date() == null || book.getPublish_date().equals("")) {
            return "请填写出版日期";
        }
        return null;
    }

    public ApiResponse<String> saveBookInfo(Book book) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 判断是添加还是修改
        Boolean requestType = book.getBook_id() == null;

        // 参数验证
        String visitedInfoReslut = visitedInfo(book);
        if (!(visitedInfoReslut == null)) {
            apiResponse.setErrorResponse(500, visitedInfoReslut);
            return apiResponse;
        }
        //简介可以为空
        if (book.getIntroduction() == null) {
            book.setIntroduction("");
        }
        //新增书籍
        if (requestType) {
            //初始设置书籍状态为未借出
            book.setStatus(0);
            // 检查ISBN是否已经存在
            if (booksMapper.findByIdIsbn(book.getIsbn()) > 0) {
                apiResponse.setErrorResponse(500, "ISBN已存在");
            }
            if (booksMapper.saveBookInfo(book) == 1) {
                apiResponse.setSuccessResponse("添加成功");
            } else {
                apiResponse.setErrorResponse(500, "添加失败");
            }
        } else {
            //修改书籍
            // 检查书籍是否存在
            if (booksMapper.findById(book.getBook_id().toString()) == 0) {
                apiResponse.setErrorResponse(500, "书籍不存在");
            } else {
                // 检查ISBN是否已经存在 且不是当前书籍 先查出当前isbn对应的书籍id 再判断是否是当前书籍
                if (booksMapper.findByIdIsbn(book.getIsbn()) > 0 && !(booksMapper.findByIdIsbn(book.getIsbn()) == book.getBook_id())) {
                    apiResponse.setErrorResponse(500, "ISBN已存在");
                }
                if (booksMapper.updateBookInfo(book) == 1) {
                    apiResponse.setSuccessResponse("修改成功");
                } else {
                    apiResponse.setErrorResponse(500, "修改失败");
                }
            }
        }


        return apiResponse;

    }


    public ApiResponse<String> devastateBook(String book_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 参数验证
        if (book_id == null) {
            apiResponse.setErrorResponse(400, "书籍ID不能为空");
            return apiResponse;
        }
        // 检查书籍是否存在
        int result = booksMapper.findById(book_id);
        if (result == 0) {
            apiResponse.setErrorResponse(400, "书籍不存在");
        } else {
            // 删除书籍
            if (booksMapper.devastateBook(book_id) == 1) {
                apiResponse.setSuccessResponse("删除成功");
            } else {
                throw new RuntimeException("删除失败");
            }
        }
        return apiResponse;
    }

    public BookCategories[] getBookCategoryList(String search) {
        return booksMapper.getBookCategoryList("%" + search + "%");
    }

    public ApiResponse<String> addBookCategory(String category_name) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 参数验证
        if (category_name == null || category_name.equals("")) {
            apiResponse.setErrorResponse(500, "分类名称不能为空");
            return apiResponse;
        }
        // 检查分类是否已经存在
        if (booksMapper.getBookCategoryList(category_name).length > 0) {
            apiResponse.setErrorResponse(500, "分类已存在");
            return apiResponse;
        }
        if (booksMapper.addBookCategory(category_name) == 1) {
            apiResponse.setSuccessResponse("添加成功");
        } else {
            apiResponse.setErrorResponse(500, "添加失败");
        }
        // 添加分类 并返回当前添加分类的ID
        apiResponse.setSuccessResponse(Integer.toString(booksMapper.getLastInsertedCategoryId()));
        return apiResponse;
    }

    public ApiResponse<String> devastateBookCategory(String id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 参数验证
        if (id == null) {
            apiResponse.setErrorResponse(400, "分类ID不能为空");
            return apiResponse;
        }
        // 检查分类是否存在
        int result = booksMapper.getBookCategoryListById(id);
        if (result == 0) {
            apiResponse.setErrorResponse(400, "分类名称不存在");
        } else {
            // 删除分类
            if (booksMapper.devastateBookCategory(id) == 1) {
                apiResponse.setSuccessResponse("删除成功");
            } else {
                throw new RuntimeException("删除失败");
            }
        }
        return apiResponse;
    }




}
