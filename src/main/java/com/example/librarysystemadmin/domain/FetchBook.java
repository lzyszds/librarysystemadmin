package com.example.librarysystemadmin.domain;

public class FetchBook extends Book {
    private int copies_number;

    public int getCopies_number() {
        return copies_number;
    }

    public void setCopies_number(int copies_Number) {
        this.copies_number = copies_Number;
    }

    @Override
    public String toString() {
        return "FetchBook{" +
                "copies_Number=" + copies_number +
                '}';
    }
}
