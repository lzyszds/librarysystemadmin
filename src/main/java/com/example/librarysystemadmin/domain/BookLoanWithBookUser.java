package com.example.librarysystemadmin.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

public class BookLoanWithBookUser extends BookLoan {
    @Id
    @Column(name = "loan_id")
    private Integer loanId;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "book_isbn")
    private String bookIsbn;

    @Column(name = "name")
    private String name;



    @Override
    public Integer getLoanId() {
        return loanId;
    }

    @Override
    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BookLoanWithBookUser{" +
                "loanId=" + loanId +
                ", bookName='" + bookName + '\'' +
                ", bookIsbn='" + bookIsbn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
