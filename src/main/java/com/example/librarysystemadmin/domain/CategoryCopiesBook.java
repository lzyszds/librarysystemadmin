package com.example.librarysystemadmin.domain;

public class CategoryCopiesBook extends Book {

    private String category_name;

    private int copy_id;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }


    public int getCopy_id() {
        return copy_id;
    }

    public void setCopy_id(int copy_id) {
        this.copy_id = copy_id;
    }

    @Override
    public String toString() {
        return "CategoryCopiesBook{" +
                "category_name='" + category_name + '\'' +
                ", copy_id=" + copy_id +
                '}';
    }
}
