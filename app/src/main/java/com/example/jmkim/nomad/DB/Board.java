package com.example.jmkim.nomad.DB;

public class Board {
    public String title;
    public String publisher;
    public String like;
    public String country;

    public Board(String title, String publisher, String like, String country) {
        this.title = title;
        this.publisher = publisher;
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
