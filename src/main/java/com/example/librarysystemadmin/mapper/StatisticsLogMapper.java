package com.example.librarysystemadmin.mapper;

import com.example.librarysystemadmin.domain.Statistic;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Date;

@Mapper
public interface StatisticsLogMapper {
    @Insert("INSERT INTO statistics_log (log_date, new_users_count, new_books_count, books_borrowed_count, books_returned_count,visits_count) " +
            "VALUES (#{date}, #{usersCount}, #{booksCount}, #{borrowedCount}, #{returnedCount},#{visitsCount})")
    int insertStatisticsLog(String date, int usersCount, int booksCount, int borrowedCount, int returnedCount, int visitsCount);

    @Update("UPDATE statistics_log SET new_users_count = #{usersCount}, new_books_count = #{booksCount}, " +
            "books_borrowed_count = #{borrowedCount}, books_returned_count = #{returnedCount}, " +
            "visits_count = #{visitsCount} WHERE log_date = #{date}")
    int updateStatisticsLog(Date date, int usersCount, int booksCount, int borrowedCount, int returnedCount, int visitsCount);

    @Update("UPDATE statistics_log SET visits_count = visits_count+1 WHERE log_date = #{date}")
    int updateVisitsCount(Date date);

    @Update("UPDATE statistics_log SET new_users_count = new_users_count+1 WHERE log_date = #{date}")
    int updateNewUsersCount(Date date);

    @Update("UPDATE statistics_log SET new_books_count = new_books_count+1 WHERE log_date = #{date}")
    int updateNewBooksCount(Date date);

    @Update("UPDATE statistics_log SET books_borrowed_count = books_borrowed_count+1 WHERE log_date = #{date}")
    int updateBooksBorrowedCount(Date date);

    @Update("UPDATE statistics_log SET books_returned_count = books_returned_count+1 WHERE log_date = #{date}")
    int updateBooksReturnedCount(Date date);

    @Select("SELECT id as logId,log_date as logDate,new_users_count as newUsersCount,new_books_count as newBooksCount," +
            "books_borrowed_count as booksBorrowedCount, books_returned_count as booksReturnedCount ," +
            "visits_count as visitsCount FROM statistics_log ORDER BY log_date DESC LIMIT #{limit}")
    Statistic[] getStatisticsLog(int limit);

    @Select("SELECT * FROM statistics_log WHERE log_date = #{date}")
    int getStatisticsLogByDate(Date date);
}
