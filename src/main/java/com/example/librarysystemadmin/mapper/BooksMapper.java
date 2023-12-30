package com.example.librarysystemadmin.mapper;


import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookCategories;
import com.example.librarysystemadmin.domain.CategoryCopiesBook;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BooksMapper {

    @Select("SELECT book_id, book_name, author, cover,publisher, publish_date, isbn, introduction, c.category_name,c.category_id, is_borrowable\n" +
            "FROM books b LEFT JOIN categories c ON b.category_id = c.category_id WHERE book_name like '%${search}%' or author like '%${search}%' or " +
            "publisher like '%${search}%' or isbn like '%${search}%' or introduction like '%${search}%' LIMIT #{page}, #{limit} ;")
    CategoryCopiesBook[] getBookList(String search, int page, int limit);

    @Select("SELECT count(*) FROM books where book_name like '%${search}%' or author like '%${search}%' or " +
            "publisher like '%${search}%' or isbn like '%${search}%' or introduction like '%${search}%'")
    int getBookCount(String search);

    @Insert("INSERT INTO books (book_name, author, cover, publisher, publish_date, isbn, introduction, category_id, is_borrowable) " +
            "VALUES (#{book_name}, #{author}, #{cover}, #{publisher}, #{publish_date}, #{isbn}, #{introduction}, #{category_id}, #{is_borrowable})")
    int saveBookInfo(Book book);

    @Update("UPDATE books SET book_name = #{book_name}, author = #{author}, cover = #{cover}, publisher = #{publisher}, publish_date = #{publish_date}, " +
            "isbn = #{isbn}, introduction = #{introduction}, category_id = #{category_id}, is_borrowable = #{is_borrowable} WHERE book_id = #{book_id}")
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

    //添加图书分类 并返回自增id
    @Insert("INSERT INTO categories (category_name) VALUES (#{category_name})")
    int addBookCategory(String category_name);

    //查询最后插入的分类id
    @Select("SELECT LAST_INSERT_ID()")
    int getLastInsertedCategoryId();

    //删除图书分类
    @Delete("DELETE FROM categories WHERE category_id in (${category_id})")
    int devastateBookCategory(String category_id);


}
