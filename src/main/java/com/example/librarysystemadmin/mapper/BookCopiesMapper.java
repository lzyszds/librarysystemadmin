package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.BookCopies;
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

    /*
     * 一次性删除多条数据
     * */

    @Delete("DELETE FROM book_copies WHERE book_id in (${book_id})")
    int deleteBookCopies(String book_id);

    /*
    *   查询图书副本是否存在
    * */
    @Select("SELECT COUNT(*) FROM book_copies WHERE book_id in (#{book_id})")
    int getBookCopiesList(String book_id);

}
