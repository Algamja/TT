package com.example.jmkim.nomad.DB;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, Comment> comments = new HashMap<>();
    public String type;
    public String king;
    public String member_count;
    public String title;
    public String plan_key;

    public static class  Comment{
        public String uid;
        public String message;
    }
}
