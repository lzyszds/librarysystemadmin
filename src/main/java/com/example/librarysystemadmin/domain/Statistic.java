package com.example.librarysystemadmin.domain;

import javax.persistence.Column;
import java.sql.Date;

public class Statistic {
    int id;

    @Column(name = "log_date")
    Date logDate;

    @Column(name = "new_users_count")
    int newUsersCount;

    @Column(name = "new_books_count")
    int newBooksCount;

    @Column(name = "books_borrowed_count")
    int booksBorrowedCount;

    @Column(name = "books_returned_count")
    int booksReturnedCount;
    @Column(name = "visits_count")
    int visitsCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", logDate=" + logDate +
                ", newUsersCount=" + newUsersCount +
                ", newBooksCount=" + newBooksCount +
                ", booksBorrowedCount=" + booksBorrowedCount +
                ", booksReturnedCount=" + booksReturnedCount +
                ", visitsCount=" + visitsCount +
                '}';
    }
}
