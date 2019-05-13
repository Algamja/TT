package com.example.jmkim.nomad.prev;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

public class Sub extends LinearLayout {

    public Sub(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public Sub(Context context) {
        super(context);

        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_recommend_title,this,true);
    }
}