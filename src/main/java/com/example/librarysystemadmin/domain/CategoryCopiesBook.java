package com.example.librarysystemadmin.domain;

public class CategoryCopiesBook extends Book {

    private String categoryName;
    private long copyId;


    public String getcategoryName() {
        return categoryName;
    }

    public void setcategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public long getcopyId() {
        return copyId;
    }

    public void setcopyId(long copyId) {
        this.copyId = copyId;
    }

    @Override
    public String toString() {
        return "CategoryCopiesBook{" +
                "categoryName='" + categoryName + '\'' +
                ", copyId=" + copyId +
                '}';
    }
}
