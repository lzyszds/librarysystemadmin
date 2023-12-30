package com.example.librarysystemadmin.domain;

public class BookCopies {
    private String copy_id;
    private int book_id;
    private long status;

    public String getCopy_id() {
        return copy_id;
    }

    public void setCopy_id(String copy_id) {
        this.copy_id = copy_id;
    }

    public int getBook_id() {

        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BookCopies{" +
                "copy_id=" + copy_id +
                ", book_id=" + book_id +
                ", status=" + status +
                '}';
    }
}
