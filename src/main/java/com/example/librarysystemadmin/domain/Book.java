package com.example.librarysystemadmin.domain;

import java.sql.Date;

public class Book {
    //书籍id
    private Long book_id;
    //书籍名称
    private String book_name;
    //作者
    private String author;
    //封面
    private String cover;
    //简介
    private String introduction;
    //出版社
    private String publisher;
    //出版日期
    private Date publish_date;
    //ISBN 编码 全国图书统一编号
    private String isbn;
    //书籍分类id
    private Long category_id;
    //书籍状态 0：未借出 1：已借出
    private int status;

    public Long getBook_id() {
        return book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
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

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book{" +
                "book_id=" + book_id +
                ", book_name='" + book_name + '\'' +
                ", author='" + author + '\'' +
                ", cover='" + cover + '\'' +
                ", introduction='" + introduction + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publish_date=" + publish_date +
                ", isbn='" + isbn + '\'' +
                ", category_id=" + category_id +
                ", status='" + status + '\'' +
                '}';
    }
}