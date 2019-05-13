package com.example.jmkim.nomad.added;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

public class ItemReview extends LinearLayout {
    Review parent;

    public ItemReview(Context context, int num) {
        super(context);
        init(num);
        parent = (Review) context;
    }

    private void init(int num) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.added_item_review, this, true);

        //TextView day = rootView.findViewById(R.id.day);

    }
}