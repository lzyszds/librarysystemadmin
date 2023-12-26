package com.example.librarysystemadmin.mapper;


import com.example.librarysystemadmin.domain.Book;
import com.example.librarysystemadmin.domain.BookWithCategory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BooksMapper {

    @Select("SELECT book_id, book_name, author, cover,publisher, publish_date, isbn, introduction, c.category_name, status\n" +
            "FROM books b LEFT JOIN categories c ON b.category_id = c.category_id WHERE book_name like '%${search}%' or author like '%${search}%' or " +
            "publisher like '%${search}%' or isbn like '%${search}%' or introduction like '%${search}%' LIMIT #{page}, #{limit} ;")
    BookWithCategory[] queryBookList(String search, int page, int limit);

    @Select("SELECT count(*) FROM books where book_name like '%${search}%' or author like '%${search}%' or " +
            "publisher like '%${search}%' or isbn like '%${search}%' or introduction like '%${search}%'")
    int queryBookCount(String search);

    @Insert("INSERT INTO books (book_name, author, cover, publisher, publish_date, isbn, introduction, category_id, status) " +
            "VALUES (#{book_name}, #{author}, #{cover}, #{publisher}, #{publish_date}, #{isbn}, #{introduction}, #{category_id}, #{status})")
    int addBook(Book book);

    //查询ISBN是否存在 对比名称来说，ISBN是唯一的 就是同一本书 名称相同但是出版社不是一家的书 就不算数一本书
    @Select("SELECT count(*) FROM books where isbn = #{isbn}")
    int queryBookByIsbn(String isbn);
}
