package com.example.jmkim.nomad.DB;

public class Add_Tag {
    public String position;
    public String index;
    public String tag_name;
    public String memo;

    public Add_Tag(String position, String index, String tag_name, String memo) {
        this.position = position;
        this.index = index;
        this.tag_name = tag_name;
        this.memo = memo;
    }

    public Add_Tag() {
    }
}
