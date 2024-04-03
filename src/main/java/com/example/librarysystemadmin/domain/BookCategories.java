package com.example.librarysystemadmin.domain;

public class BookCategories {

    private Long categoryId;
    private String categoryName;

    public Long getcategoryId() {
        return categoryId;
    }

    public String getcategoryName() {
        return categoryName;
    }

    public void setcategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setcategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "BookCategories{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
