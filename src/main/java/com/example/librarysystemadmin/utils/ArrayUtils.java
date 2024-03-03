package com.example.librarysystemadmin.utils;

public class ArrayUtils<T> {
    public boolean includes(T[] arr, String target) {
        for (T item : arr) {
            if (item.equals(target)) {
                return true;
            }
        }
        return false;
    }

    //返回数组中第一次出现的位置
    public int indexOf(T[] arr, String target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
