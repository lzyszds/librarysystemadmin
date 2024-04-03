package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.BookCopies;
import com.example.librarysystemadmin.domain.CategoryCopiesBook;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BookCopiesMapper {

    /*
     * 一次性插入多条数据
     * */
    @Insert("<script>" +
            "INSERT INTO book_copies (copy_id,book_id, status) VALUES " +
            "<foreach collection='bookCopiesArray' item='item' index='index' separator=','>" +
            "(#{item.copy_id}, #{item.book_id}, #{item.status})" +
            "</foreach>" +
            "</script>")
    int addBookCopies(@Param("bookCopiesArray") BookCopies[] bookCopiesArray);

    //一次性删除多条数据
    @Delete("DELETE FROM book_copies WHERE book_id in (${book_id})")
    int deleteBookCopies(String book_id);

    // 查询图书副本是否存在
    @Select("SELECT COUNT(*) FROM book_copies WHERE book_id in (#{book_id})")
    int getBookCopiesList(String book_id);

    /*
     * 查询copyid的书籍是否被借出
     * */
    @Select("SELECT * FROM book_copies where copy_id = '#{copyId}' ")
    BookCopies getCopiesByCopyId(Long copyId);

    /*
     * 设置书籍副本状态 0未借出 1已借出
     * */
    @Update("UPDATE book_copies SET status = 1 WHERE copy_id = #{copyid}")
    void setBookCopiesStatus(String copyid, int status);

    /*
     * 根据用户id获取用户借阅书籍列表
     * */
    @Select("SELECT * FROM books as b " +
            "INNER JOIN book_copies as bc ON bc.book_id = b.book_id " +
            "INNER JOIN categories as c ON c.category_id = b.category_id " +
            "WHERE copy_id in (SELECT copy_id FROM library_book_loan WHERE user_id = #{userId})")
    CategoryCopiesBook[] getBorrowedBooks(String userId);
}
