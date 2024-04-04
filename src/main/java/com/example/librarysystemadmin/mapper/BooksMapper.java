package com.example.librarysystemadmin.mapper;


import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookCategories;
import com.example.librarysystemadmin.domain.BookCopies;
import com.example.librarysystemadmin.domain.CategoryCopiesBook;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BooksMapper {

    String selectBook = "SELECT book_id, book_name, author, cover,publisher, publish_date, isbn, introduction, c.category_name,c.category_id, is_borrowable\n"
            + "FROM books b LEFT JOIN categories c ON b.category_id = c.category_id ";
    //获取图书列表

    @Select(selectBook + " WHERE book_name like'%${search}%'or author like'%${search}%'or \n" + "publisher like '%${search}%' or isbn like '%${search}%' or introduction like '%${search}%' or category_name like '%${search}%' LIMIT #{page}, #{limit} ;")
    CategoryCopiesBook[] getBookList(String search, int page, int limit);

    @Select("SELECT count(*) FROM books where book_name like '%${search}%' or author like '%${search}%' or " + "publisher like '%${search}%' or isbn like '%${search}%' or introduction like '%${search}%'")
    int getBookCount(String search);

    //保存图书信息
    @Insert("INSERT INTO books (book_name, author, cover, publisher, publish_date, isbn, introduction, category_id, is_borrowable) " + "VALUES (#{book_name}, #{author}, #{cover}, #{publisher}, #{publish_date}, #{isbn}, #{introduction}, #{category_id}, #{is_borrowable})")
    int saveBookInfo(Book book);

    //更新图书信息
    @Update("UPDATE books SET book_name = #{book_name}, author = #{author}, cover = #{cover}, publisher = #{publisher}, publish_date = #{publish_date}, " + "isbn = #{isbn}, introduction = #{introduction}, category_id = #{category_id}, is_borrowable = #{is_borrowable} WHERE book_id = #{book_id}")
    int updateBookInfo(Book book);

    //查询ISBN是否存在 对比名称来说，ISBN是唯一的 就是同一本书 名称相同但是出版社不是一家的书 就不算数一本书
    @Select("SELECT count(*) FROM books where isbn = #{isbn}")
    int findByIdIsbn(String isbn);

    //删除图书
    @Delete("DELETE FROM books WHERE book_id in (${book_id})")
    int devastateBook(String book_id);

    //查询图书是否存在
    @Select("SELECT count(*) FROM books where book_id in (${book_id})")
    int findById(String book_id);

    //查询图书分类
    @Select("SELECT * FROM categories WHERE category_name like '${search}'")
    BookCategories[] getBookCategoryList(String search);

    //检查分类是否存在 id
    @Select("SELECT count(*) FROM categories where category_id = #{category_id}")
    int getBookCategoryListById(String category_id);

    //检查分类是否存在 名称 并返回id
    @Select("SELECT category_id FROM categories where category_name = #{category_name}")
    String getBookCategoryListByName(String category_name);

    //添加图书分类 并返回自增id
    @Insert("INSERT INTO categories (category_name) VALUES (#{category_name})")
    int addBookCategory(String category_name);

    //查询最后插入的分类id
    @Select("SELECT LAST_INSERT_ID()")
    int getLastInsertedCategoryId();

    //删除图书分类
    @Delete("DELETE FROM categories WHERE category_id in (${category_id})")
    int devastateBookCategory(String category_id);

    //获取热门图书列表
    @Select(selectBook + "ORDER BY borrowing_volume DESC LIMIT #{page}, #{limit} ;")
    CategoryCopiesBook[] getHotBookList(int page, int limit);

    //将borrowing_volume字段++，表示借阅量+1
    @Update("UPDATE books SET borrowing_volume = borrowing_volume + 1 WHERE book_id = #{book_id}")
    int updateBorrowingVolume(int book_id);

    //获取新书列表
    @Select(selectBook + "ORDER BY publish_date DESC LIMIT #{page}, #{limit}")
    CategoryCopiesBook[] getNewBookList(int page, int limit);

    //获取指定列名的图书列表
    @Select(selectBook + "WHERE ${field} like '${search}' LIMIT #{page}, #{limit}")
    CategoryCopiesBook[] getBookListByField(String search, String field, int page, int limit);

    //获取分类书籍量最大的分类前n项
    @Select("SELECT c.category_id, c.category_name, count(*) as count \n" +
            "FROM books LEFT JOIN categories c ON books.category_id = c.category_id \n" +
            "GROUP BY c.category_id  \n" +
            "ORDER BY count \n" +
            "DESC LIMIT #{book_id}")
    BookCategories[] getTopNCategories(int n);

    //查询图书信息
    @Select("SELECT * FROM books LEFT JOIN categories c ON books.category_id = c.category_id WHERE book_id = #{book_id} ")
    CategoryCopiesBook getBookInfo(String book_id);

    //获取借阅图书列表
    @Select("SELECT b.book_id, b.book_name, b.author, b.cover, b.publisher, b.publish_date, b.isbn, b.introduction, c.category_name, c.category_id, b.is_borrowable\n" +
            "FROM books b LEFT JOIN categories c ON b.category_id = c.category_id\n" +
            "WHERE b.book_id in (SELECT book_id FROM library_book_loan WHERE user_id = #{user_id})")
    CategoryCopiesBook[] getBorrowedBooks(String user_id);
}
