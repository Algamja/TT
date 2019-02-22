package com.example.jmkim.nomad.DB;

public class Board {
    public String title;
    public String publisher;
    public String img_1;
    public String img_2;
    public String img_3;
    public String like;
    public String country;

    public Board(String title, String publisher, String img_1, String img_2, String img_3, String like, String country) {
        this.title = title;
        this.publisher = publisher;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.img_3 = img_3;
        this.like = like;
        this.country = country;
    }

    public Board(String title, String publisher, String img_1, String img_2, String like, String country) {
        this.title = title;
        this.publisher = publisher;
        this.img_1 = img_1;
        this.img_2 = img_2;
        this.like = like;
        this.country = country;
    }

    public Board(String title, String publisher, String img_1, String like, String country) {
        this.title = title;
        this.publisher = publisher;
        this.img_1 = img_1;
        this.like = like;
        this.country = country;
    }

    public Board() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getImg_1() {
        return img_1;
    }

    public void setImg_1(String img_1) {
        this.img_1 = img_1;
    }

    public String getImg_2() {
        return img_2;
    }

    public void setImg_2(String img_2) {
        this.img_2 = img_2;
    }

    public String getImg_3() {
        return img_3;
    }

    public void setImg_3(String img_3) {
        this.img_3 = img_3;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
