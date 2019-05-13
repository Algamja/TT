package com.example.jmkim.nomad.added;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

public class ItemReco extends LinearLayout {

    WritePlan parent;

    public ItemReco(Context context) {
        super(context);
        init();
        parent = (WritePlan) context;
    }

    public ItemReco(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        parent = (WritePlan) context;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.activity_recommend_title, this, true);
    }
}