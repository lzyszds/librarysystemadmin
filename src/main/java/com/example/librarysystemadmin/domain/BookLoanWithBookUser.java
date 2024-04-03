package com.example.librarysystemadmin.domain;

public class BookLoanWithBookUser extends BookLoan {
    private Integer loanId;

    private String bookName;

    private String isbn;

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

    public String getisbn() {
        return isbn;
    }

    public void setisbn(String isbn) {
        this.isbn = isbn;
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
                ", isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
