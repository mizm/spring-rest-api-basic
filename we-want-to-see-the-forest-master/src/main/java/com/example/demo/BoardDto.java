package com.example.demo;

public class BoardDto {
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    private String title;
}
