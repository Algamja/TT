package com.example.jmkim.nomad.added;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jmkim.nomad.R;

public class ItemReco extends LinearLayout {
    WritePlan parent;
    String tag;
    String img;

    public ItemReco(Context context, String tag, String img) {
        super(context);
        this.tag = tag;
        this.img = img;
        parent = (WritePlan) context;
        init();
    }

    public ItemReco(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        parent = (WritePlan) context;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.activity_recommend_title, this, true);

        LinearLayout layout = rootView.findViewById(R.id.recom_item_layout);
        ImageView imageView = rootView.findViewById(R.id.recom_item_img);
        TextView textView = rootView.findViewById(R.id.recom_item_title);
        
        Glide.with(parent).load(img).into(imageView);
        textView.setText(tag);

        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, HashTagInfoActivity.class);
                intent.putExtra("tag",tag);
                parent.startActivity(intent);
            }
        });
    }
}