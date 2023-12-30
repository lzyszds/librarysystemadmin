package com.example.librarysystemadmin.domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "statistics_log")
public class Statistic {

    @Id
    @Column(name = "id")
    private int logId;

    @Column(name = "log_date")
    private Date logDate;

    @Column(name = "new_users_count")
    private int newUsersCount;

    @Column(name = "new_books_count")
    private int newBooksCount;

    @Column(name = "books_borrowed_count")
    private int booksBorrowedCount;

    @Column(name = "books_returned_count")
    private int booksReturnedCount;
    @Column(name = "visits_count")
    private int visitsCount;

    public int getLog_id() {
        return logId;
    }

    public void setLog_id(int log_id) {
        this.logId = log_id;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public int getNewUsersCount() {
        return newUsersCount;
    }

    public void setNewUsersCount(int newUsersCount) {
        this.newUsersCount = newUsersCount;
    }

    public int getNewBooksCount() {
        return newBooksCount;
    }

    public void setNewBooksCount(int newBooksCount) {
        this.newBooksCount = newBooksCount;
    }

    public int getBooksBorrowedCount() {
        return booksBorrowedCount;
    }

    public void setBooksBorrowedCount(int booksBorrowedCount) {
        this.booksBorrowedCount = booksBorrowedCount;
    }

    public int getBooksReturnedCount() {
        return booksReturnedCount;
    }

    public void setBooksReturnedCount(int booksReturnedCount) {
        this.booksReturnedCount = booksReturnedCount;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "log_id=" + logId +
                ", logDate=" + logDate +
                ", newUsersCount=" + newUsersCount +
                ", newBooksCount=" + newBooksCount +
                ", booksBorrowedCount=" + booksBorrowedCount +
                ", booksReturnedCount=" + booksReturnedCount +
                ", visitsCount=" + visitsCount +
                '}';
    }
}
