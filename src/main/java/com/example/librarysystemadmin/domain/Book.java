package com.example.librarysystemadmin.domain;

import java.sql.Date;

public class Book {
    //书籍id
    private Long bookId;
    //书籍名称
    private String bookName;
    //作者
    private String author;
    //封面
    private String cover;
    //简介
    private String introduction;
    //出版社
    private String publisher;
    //出版日期
    private Date publishDate;
    //ISBN 编码 全国图书统一编号
    private String isbn;
    //书籍分类id
    private Long categoryId;
    //书籍状态 0：可以外借 1：只能馆内阅读
    private int isBorrowable;

    private int borrowingVolume;


    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getIsBorrowable() {
        return isBorrowable;
    }

    public void setIsBorrowable(int isBorrowable) {
        this.isBorrowable = isBorrowable;
    }

    public int getBorrowingVolume() {
        return borrowingVolume;
    }

    public void setBorrowingVolume(int borrowingVolume) {
        this.borrowingVolume = borrowingVolume;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                ", introduction='" + introduction + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publishDate=" + publishDate +
                ", isbn='" + isbn + '\'' +
                ", categoryId=" + categoryId +
                ", isBorrowable=" + isBorrowable +
                ", borrowingVolume=" + borrowingVolume +
                '}';
    }
}
