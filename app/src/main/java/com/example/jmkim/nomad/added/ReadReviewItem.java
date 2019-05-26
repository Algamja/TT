package com.example.jmkim.nomad.added;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jmkim.nomad.DB.Add_Tag;
import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.PlanReadActivity;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReadReviewItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int size;
    ArrayList<String> tag_keys;
    Review review;
    ReadReviewActivity readReviewActivity;

    public ReadReviewItem(Context context) {
        this.context = context;
    }

    public ReadReviewItem(Context context, ArrayList<String> tag_keys, Review review, ReadReviewActivity readReviewActivity) {
        this.context = context;
        this.size = tag_keys.size();
        this.tag_keys = tag_keys;
        this.review = review;
        this.readReviewActivity = readReviewActivity;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tag_name;
        RatingBar ratingBar;
        TextView comment;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tag_name = (TextView)itemView.findViewById(R.id.review_read_tv_hashtag);
            ratingBar = (RatingBar)itemView.findViewById(R.id.review_read_ratingBar);
            comment = (TextView)itemView.findViewById(R.id.review_read_tv_comment);
            img = (ImageView)itemView.findViewById(R.id.review_read_image);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_read, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder)holder;
        List<Review.Review_Tag> tags = new ArrayList<>();

        for(int i=0;i<size;i++){
            tags.add(review.hashtag.get(tag_keys.get(i)));
        }

        for(int i=0;i<size;i++){
            for(int pos = 0 ; pos<size;pos++){
                if(position == pos && Integer.parseInt(tags.get(i).position) == position){
                    myViewHolder.tag_name.setText("#"+tag_keys.get(i));
                    myViewHolder.ratingBar.setRating(Float.parseFloat(tags.get(i).rate));
                    myViewHolder.comment.setText(tags.get(i).comment);
                    Glide.with(context)
                            .load(tags.get(i).imageUri)
                            .into(myViewHolder.img);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
