package com.example.librarysystemadmin.service.impl;

import com.example.librarysystemadmin.domain.*;
import com.example.librarysystemadmin.mapper.BookCopiesMapper;
import com.example.librarysystemadmin.mapper.BookLoanMapper;
import com.example.librarysystemadmin.mapper.BooksMapper;
import com.example.librarysystemadmin.mapper.UsersMapper;
import com.example.librarysystemadmin.service.BooksService;
import com.example.librarysystemadmin.service.StatisticService;
import com.example.librarysystemadmin.utils.ApiResponse;
import com.example.librarysystemadmin.utils.FormatExcelData;
import com.example.librarysystemadmin.utils.ProcessFiles;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class BooksServiceImpl implements BooksService {

    /*
     * 上传文件路径
     * System.getProperty("user.dir") 获取当前项目路径
     * 上传文件路径为当前项目路径下的uploadFile文件夹
     * */
    private String uploadPath = System.getProperty("user.dir") + "/uploadFile/";

    private static final Log log = LogFactory.getLog(BooksServiceImpl.class);

    /*
     * 注入mapper
     * 1.BooksMapper
     * 2.BookLoanMapper
     * 3.BookCopiesMapper
     * */
    @Autowired
    private BooksMapper booksMapper;

    @Autowired
    private BookLoanMapper bookLoanMapper;

    @Autowired
    private BookCopiesMapper bookCopiesMapper;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private StatisticService statisticsService;

    /*
     * 获取图书列表
     * @param search 查询字段
     * @param page 当前页
     * @param limit 每页显示条数
     * 1.获取图书总数
     * 2.获取图书列表
     * 3.返回结果
     * */
    public ApiResponse<ListDataCount<CategoryCopiesBook[]>> getBookList(String search, int page, int limit) {
        ApiResponse<ListDataCount<CategoryCopiesBook[]>> apiResponse = new ApiResponse<>();
        ListDataCount<CategoryCopiesBook[]> listDataCount = new ListDataCount<>();

        try {
            listDataCount.setCount(booksMapper.getBookCount(search));
            page = (page - 1) * limit;

            listDataCount.setData(booksMapper.getBookList(search, page, limit));
            apiResponse.setSuccessResponse(listDataCount);
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "查询失败");
        }
        return apiResponse;
    }

    public int getBookCount(String search) {
        return booksMapper.getBookCount(search);
    }

    /*
     * 参数验证
     * @param book 书籍信息
     * 1.书名不能为空
     * 2.作者不能为空
     * 3.分类不能为空
     * 4.ISBN不能为空
     * 5.出版社不能为空
     * 6.出版日期不能为空
     * 7.简介可以为空
     * 8.新增书籍
     * 9.获取当前插入书籍的ID
     * 10.添加书籍成功后，根据copies_Number来添加书籍副本
     * 11.修改书籍
     * 12.检查书籍是否存在
     * 13.检查ISBN是否已经存在 且不是当前书籍 先查出当前isbn对应的书籍id 再判断是否是当前书籍
     * 14.添加书籍成功
     * 15.获取当前插入书籍的ID
     * 16.添加书籍成功后，根据copies_Number来添加书籍副本
     * 17.返回结果
     * */
    static String visitedInfo(Book book) {
        if (book.getBookName() == null || book.getBookName().equals("")) {
            return "请填写书名";
        }
        if (book.getAuthor() == null || book.getAuthor().equals("")) {
            return "请填写作者";
        }
        if (book.getCategoryId() == null || book.getCategoryId().equals("")) {
            return "请选择分类";
        }
        if (book.getIsbn() == null || book.getIsbn().equals("")) {
            return "请填写ISBN";
        }
        if (book.getPublisher() == null || book.getPublisher().equals("")) {
            return "请填写出版社";
        }
        if (book.getPublishDate() == null || book.getPublishDate().equals("")) {
            return "请填写出版日期";
        }
        return null;
    }

    /*
     *  保存书籍信息
     * @param book 书籍信息
     * 1.判断是添加还是修改
     * 2.参数验证
     * 3.简介可以为空
     * 4.新增书籍
     * 5.获取当前插入书籍的ID
     * 6.添加书籍成功后，根据copies_Number来添加书籍副本
     * 7.修改书籍
     * 8.检查书籍是否存在
     * 9.检查ISBN是否已经存在 且不是当前书籍 先查出当前isbn对应的书籍id 再判断是否是当前书籍
     * 10.添加书籍成功
     * 11.获取当前插入书籍的ID
     * 12.添加书籍成功后，根据copies_Number来添加书籍副本
     * 13.返回结果
     * */
    public ApiResponse<String> saveBookInfo(FetchBook book) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 判断是添加还是修改
        Boolean requestType = book.getBookId() == null;

        try {
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
                // 检查ISBN是否已经存在
                if (booksMapper.findByIdIsbn(book.getIsbn()) > 0) {
                    apiResponse.setErrorResponse(500, "ISBN已存在");
                }
                if (booksMapper.saveBookInfo(book) == 1) {
                    //获取当前插入书籍的ID
                    int newId = booksMapper.getLastInsertedCategoryId();
                    // 添加书籍成功后，根据copies_Number来添加书籍副本
                    BookCopies[] bookCopiesArray = new BookCopies[book.getCopies_number()];

                    for (int i = 0; i < book.getCopies_number(); i++) {
                        BookCopies bookCopies = new BookCopies();
                        bookCopies.setBook_id(newId);
                        bookCopies.setCopy_id(book.getIsbn() + "0725" + (i + 1));
                        bookCopies.setStatus(book.getIsBorrowable());
                        bookCopiesArray[i] = bookCopies;
                    }


                    apiResponse.setSuccessResponse("添加成功");
                } else {
                    apiResponse.setErrorResponse(500, "添加失败");
                }
            } else {
                //修改书籍
                // 检查书籍是否存在
                if (booksMapper.findById(book.getBookId().toString()) == 0) {
                    apiResponse.setErrorResponse(500, "书籍不存在");
                } else {
                    // 检查ISBN是否已经存在 且不是当前书籍 先查出当前isbn对应的书籍id 再判断是否是当前书籍
                    if (booksMapper.findByIdIsbn(book.getIsbn()) > 0 && !(booksMapper.findByIdIsbn(book.getIsbn()) == book.getBookId())) {
                        apiResponse.setErrorResponse(500, "ISBN已存在");
                    }
                    if (booksMapper.updateBookInfo(book) == 1) {
                        apiResponse.setSuccessResponse("修改成功");
                    } else {
                        apiResponse.setErrorResponse(500, "修改失败");
                    }
                }
            }
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, e.getMessage());
        }

        return apiResponse;
    }

    /*
     * 上传图书封面
     * @param file 上传的文件
     * 1.验证文件信息
     * 2.获取文件名
     * 3.获取文件的md5值（16进制） 防止上传重复文件或者文件名重复
     * 4.检验文件夹是否存在 不存在则创建
     * 5.生成新的文件名  MD5 + 时间戳 + 随机数 + 用户ID
     * 6.检索文件夹下是否有同名文件
     * 7.将文件保存到指定位置
     * 8.压缩图片 超过300px的图片压缩
     * 9.返回图片路径
     * */
    public ApiResponse<String> uploadBookCover(MultipartFile file) {
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


    /*
     * 删除书籍
     * @param book_id 书籍ID
     * 1.参数验证
     * 2.检查书籍是否存在
     * 3.先确定当前书籍是否被借出 将关于当前书籍的借阅记录删除
     * 4.删除书籍副本
     * 5.删除书籍
     * 6.返回结果
     * */
    public ApiResponse<String> devastateBook(String book_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        // 参数验证
        if (book_id == null) {
            apiResponse.setErrorResponse(400, "书籍ID不能为空");
            return apiResponse;
        }
        try {
            // 检查书籍是否存在
            int result = booksMapper.findById(book_id);
            if (result == 0) {
                apiResponse.setErrorResponse(400, "书籍不存在");
            } else {
                //先确定当前书籍是否被借出 将关于当前书籍的借阅记录删除
                if (bookLoanMapper.getBookLoanList(book_id) > 0) {
                    throw new RuntimeException("当前书籍已被借出,请先收回,无法删除");
                } else {
                    bookLoanMapper.deleteBookLoan(book_id);
                }
                //先检查当前图书是否有副本 删除书籍副本
                if (bookCopiesMapper.getBookCopiesList(book_id) > 0) {
                    bookCopiesMapper.deleteBookCopies(book_id);
                }
                // 删除书籍
                if (booksMapper.devastateBook(book_id) < 1) {
                    throw new RuntimeException("删除失败");
                }
                apiResponse.setSuccessResponse("删除成功");
            }
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, e.getMessage());
        }
        return apiResponse;
    }

    /*
     * 获取图书分类列表
     * @param search 查询字段
     * 1.返回结果
     * */
    public ApiResponse<BookCategories[]> getBookCategoryList(String search) {
        ApiResponse<BookCategories[]> apiResponse = new ApiResponse<>();
        try {
            apiResponse.setSuccessResponse(booksMapper.getBookCategoryList(search.equals("") ? "%" : search));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "查询失败");
        }
        return apiResponse;
    }

    /*
     * 添加图书分类
     * @param category_name 分类名称
     * 1.参数验证
     * 2.检查分类是否已经存在
     * 3.添加分类 并返回当前添加分类的ID
     * 4.返回结果
     * */
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
        apiResponse.setSuccessResponse(String.valueOf(booksMapper.getLastInsertedCategoryId()));
        return apiResponse;
    }

    /*
     * 删除图书分类
     * @param id 分类ID
     * 1.参数验证
     * 2.检查分类是否存在
     * 3.删除分类
     * 4.返回结果
     * */
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

    /*
     * 通过excel导入图书
     * @param file 上传的文件
     * 1.读取excel文件
     * 2.读取第一个工作表
     * 3.获取行数
     * 4.获取列数
     * 5.获取数据
     * 6.遍历数组 插入数据库
     * 7.检查ISBN是否已经存在
     * ...
     * */
    public ApiResponse<String> addBooksExcel(MultipartFile file) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        try {
            // 读取excel文件
            XSSFWorkbook xssfworkbook = new XSSFWorkbook(file.getInputStream());
            // 读取第一个工作表
            XSSFSheet sheet = xssfworkbook.getSheetAt(0);
            // 获取行数
            int rows = sheet.getPhysicalNumberOfRows();
            // 获取列数
            int cells = sheet.getRow(0).getPhysicalNumberOfCells();
            FetchBook[] books = FormatExcelData.getData(rows, cells, sheet, booksMapper);
            String message = "";
            //遍历数组 插入数据库
            for (FetchBook book : books) {
                if (book == null) {
                    break;
                }
                // 检查ISBN是否已经存在
                if (booksMapper.findByIdIsbn(book.getIsbn()) > 0) {
                    message += "ISBN:" + book.getIsbn() + "已存在;";
                } else {
                    if (book.getCover() == null || book.getCover().equals("")) {
                        book.setCover("/uploadFile/coverUndefined.png");
                    }
                    //新增书籍
                    if (booksMapper.saveBookInfo(book) == 1) {
                        //添加书籍成功
                        message += "ISBN:" + book.getIsbn() + "添加成功;";
                    } else {
                        message += "ISBN:" + book.getIsbn() + "添加失败;";
                    }
                }
            }
            apiResponse.setSuccessResponse(message);
        } catch (Exception e) {
            System.out.print(e);
            apiResponse.setErrorResponse(500, "文件上传失败");
        }

        return apiResponse;
    }

    /*
     * 获取热门图书接口
     * @param page 当前页
     * @param limit 每页显示条数
     * 1.获取热门图书列表
     * 2.返回结果
     * */
    public ApiResponse<CategoryCopiesBook[]> getHotBookList(int page, int limit) {
        ApiResponse<CategoryCopiesBook[]> apiResponse = new ApiResponse<>();
        try {
            page = (page - 1) * limit;

            apiResponse.setSuccessResponse(booksMapper.getHotBookList(page, limit));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "文件上传失败");
        }
        return apiResponse;
    }

    /*
     * 获取热门图书接口
     * @param page 当前页
     * @param limit 每页显示条数
     * 1.获取热门图书列表
     * 2.返回结果
     * */
    public ApiResponse<CategoryCopiesBook[]> getNewBookList(int page, int limit) {
        ApiResponse<CategoryCopiesBook[]> apiResponse = new ApiResponse<>();
        try {
            page = (page - 1) * limit;

            apiResponse.setSuccessResponse(booksMapper.getNewBookList(page, limit));
        } catch (Exception e) {
            apiResponse.setErrorResponse(500, "文件上传失败");
        }
        return apiResponse;
    }

    /*
     * 指定某一字段名进行查询 并返回图书列表(非模糊查询)
     * @param search 查询字段
     * @param page 当前页
     * @param limit 每页显示条数
     * @param column_name 查询字段名
     * 1.获取图书列表
     * 2.返回结果
     * */
    public ApiResponse<CategoryCopiesBook[]> getBookListByField(String search, String field, int page, int limit) {
        ApiResponse<CategoryCopiesBook[]> apiResponse = new ApiResponse<>();
        //检查字段名field是否存在
        if (field == null || field.equals("")) {
            apiResponse.setErrorResponse(400, "字段名不能为空,必须为book_name,author,publisher,isbn,introduction,category_name等其中之一");
            return apiResponse;
        }
        if (search == null || search.equals("")) {
            apiResponse.setErrorResponse(400, "查询字段不能为空");
            return apiResponse;
        }
        try {
            page = (page - 1) * limit;

            apiResponse.setSuccessResponse(booksMapper.getBookListByField(search, field, page, limit));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            apiResponse.setErrorResponse(500, "查询失败");
        }
        return apiResponse;
    }

    /*
     * 获取分类书籍量最大的分类前n项
     * @param n 分类数量
     * 1.获取图书分类
     * 2.返回结果
     * */
    public ApiResponse<BookCategories[]> getTopNCategories(int n) {
        ApiResponse<BookCategories[]> apiResponse = new ApiResponse<>();

        try {
            apiResponse.setSuccessResponse(booksMapper.getTopNCategories(n));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            apiResponse.setErrorResponse(500, "查询失败");
        }
        return apiResponse;
    }

    /*
     * 获取图书详情
     * @param book_id 书籍ID
     * 1.参数验证
     * 2.检查书籍是否存在
     * 3.获取书籍信息
     * 4.返回结果
     * */
    public ApiResponse<CategoryCopiesBook> getBookInfo(String book_id) {
        ApiResponse<CategoryCopiesBook> apiResponse = new ApiResponse<>();
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
            // 获取书籍信息
            CategoryCopiesBook book = booksMapper.getBookInfo(book_id);
            // 添加浏览量
            statisticsService.setStatisticsHandle("visits");
            apiResponse.setSuccessResponse(book);
        }
        return apiResponse;
    }


}
