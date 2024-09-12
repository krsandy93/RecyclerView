package com.alkyeservices.recyclerviewexample.model;

import java.util.ArrayList;
import java.util.List;

// User.java
public class UserModel {
    private int page;
    private int perPage;
    private int total;
    private int totalPages;
    private List<Data> data;
    private Support support;

    public void setPage(int page) {
        this.page = page;
    }
    public int getPage() {
        return page;
    }
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
    public int getPerPage() {
        return perPage;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public int getTotalPages() {
        return totalPages;
    }
    public void setData(List<Data> data) {
        this.data = data;
    }
    public List<Data> getData() {
        return data;
    }
    public void setSupport(Support support) {
        this.support = support;
    }
    public Support getSupport() {
        return support;
    }
}

