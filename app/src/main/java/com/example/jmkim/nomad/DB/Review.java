package com.example.jmkim.nomad.DB;

public class Review {
    public String uid;
    public String position;
    public String index;
    public String imageUri;

    public Review(String uid, String position, String index, String imageUri) {
        this.uid = uid;
        this.position = position;
        this.index = index;
        this.imageUri = imageUri;
    }

    public Review() {
    }
}
