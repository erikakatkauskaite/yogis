package com.example.yogaappwithdb.StuffForRequest;

public class Challenge {
    int id;
    String title;
    int total;

    public Challenge(int id, String title, int total) {
        this.id = id;
        this.title = title;
        this.total = total;
    }

    public Challenge() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
