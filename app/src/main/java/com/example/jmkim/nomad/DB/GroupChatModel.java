package com.example.jmkim.nomad.DB;

import java.util.HashMap;
import java.util.Map;

public class GroupChatModel {
    public Map<String, Boolean> users = new HashMap<>();
    public Map<String, Comment> comments = new HashMap<>();

    public static class  Comment{
        public String uid;
        public String message;
    }
}