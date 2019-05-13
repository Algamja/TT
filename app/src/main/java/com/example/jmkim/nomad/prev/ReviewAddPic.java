package com.example.jmkim.nomad.prev;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

import androidx.annotation.Nullable;

public class ReviewAddPic extends LinearLayout {

    public ReviewAddPic(Context context) {
        super(context);

        init(context);
    }

    public ReviewAddPic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.review_add_image_row, this, true);
    }
}
