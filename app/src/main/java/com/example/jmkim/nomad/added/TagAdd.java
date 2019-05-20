package com.example.jmkim.nomad.added;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

import androidx.annotation.Nullable;

public class TagAdd extends LinearLayout {

    public TagAdd(Context context) {
        super(context);

        init(context);
    }

    public TagAdd(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);

        init(context);

    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_add_row, this, true);
    }
}
