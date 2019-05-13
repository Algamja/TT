package com.example.jmkim.nomad.prev;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns{
        public static final String CITYID = "id";
        public static final String CITY = "city";
        public static final String CODE = "code";
        public static final String _TABLENAME0 = "tripbox";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +CITYID+" text not null , "
                +CITY+" text not null , "
                +CODE+" text not null );";
    }
}