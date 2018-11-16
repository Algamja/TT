package com.example.jmkim.nomad;

import android.annotation.SuppressLint;
import android.os.StrictMode;

public class NetworkUtil {
    @SuppressLint("NewApi")
    static public void setNetWorkPlicy(){
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
