package com.example.librarysystemadmin.domain;

import java.sql.Date;

public class UserSecret {
    private int id; // id
    private String username; // 用户名
    private int role;   // 角色
    private String email;   // 邮箱
    private String phone;  // 电话
    private String address; // 地址
    private String name;    // 姓名
    private Date createdAt;   // 创建时间
    private String sex; // 性别

    private String borrow; // 当前借阅

    private String historyBorrow; // 历史借阅

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getcreatedAt() {
        return createdAt;
    }

    public void setcreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBorrow() {
        return borrow;
    }

    public void setBorrow(String borrow) {
        this.borrow = borrow;
    }

    public String getHistoryBorrow() {
        return historyBorrow;
    }

    public void setHistoryBorrow(String historyBorrow) {
        this.historyBorrow = historyBorrow;
    }

    @Override
    public String toString() {
        return "UserSecret{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", sex='" + sex + '\'' +
                ", borrow='" + borrow + '\'' +
                ", historyBorrow='" + historyBorrow + '\'' +
                '}';
    }
}
