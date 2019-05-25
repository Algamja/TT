package com.example.jmkim.nomad.DB;

import java.util.HashMap;
import java.util.Map;

public class Review {
    public String publisher;
    public String period;
    public String city;
    public Map<String,Review_Tag> hashtag= new HashMap<>();
    public Map<String, Boolean> like = new HashMap<>();

    public static class Review_Tag{
        public String uid;
        public String position;
        public String index;
        public String imageUri;
        public String comment;
        public String rate;
    }
}
