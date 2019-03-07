package com.example.jmkim.nomad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private ArrayList<ReviewMainInfo> reviewMainInfos;

    public ReviewAdapter(Context mContext, ArrayList<ReviewMainInfo> reviewMainInfos){
        this.mContext = mContext;
        this.reviewMainInfos = reviewMainInfos;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView hashtag;
        ImageView add_pic;
        LinearLayout pictures;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hashtag = itemView.findViewById(R.id.review_item_tv_hashtag);
            add_pic = itemView.findViewById(R.id.review_item_iv_addImage);
            pictures = itemView.findViewById(R.id.review_item_ll);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.hashtag.setText(reviewMainInfos.get(position).hashtag);

        myViewHolder.add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewAddPic layout = new ReviewAddPic(mContext);
                myViewHolder.pictures.addView(layout);

                myViewHolder.pictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "클릭됨", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewMainInfos.size();
    }
}
