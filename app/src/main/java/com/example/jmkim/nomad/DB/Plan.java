package com.example.jmkim.nomad.DB;

import java.util.HashMap;
import java.util.Map;

public class Plan {
    public String country;
    public Map<String, Boolean> like = new HashMap<>();
    public String publisher;
    public Map<String,Add_Tag> hashtag = new HashMap<>();
    public boolean open;
    public String period;
}
