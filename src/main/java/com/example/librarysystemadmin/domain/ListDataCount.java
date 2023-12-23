package com.example.librarysystemadmin.domain;

public class ListDataCount<T> {

    private int count;
    private T data;

    public ListDataCount() {

    }

    public ListDataCount(int count, T data) {
        this.count = count;
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public T getData() {
        return data;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListDataCount{" +
                "count=" + count +
                ", data=" + data +
                '}';
    }
}
