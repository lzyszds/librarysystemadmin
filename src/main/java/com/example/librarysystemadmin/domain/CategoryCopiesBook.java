package com.example.librarysystemadmin.domain;

public class CategoryCopiesBook extends Book {

    private String category_name;

    private int copiesId;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }



    public int getCopiesId() {
        return copiesId;
    }

    public void setCopiesId(int copiesId) {
        this.copiesId = copiesId;
    }

    @Override
    public String toString() {
        return "CategoryCopiesBook{" +
                "category_name='" + category_name + '\'' +
                ", copiesId=" + copiesId +
                '}';
    }
}
