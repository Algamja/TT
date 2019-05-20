package com.example.jmkim.nomad.prev;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private ArrayList<ReviewMainInfo> reviewMainInfos;
    WriteReviewActivity writeReviewActivity;

    private FirebaseUser user;
    private String uid;
    private RequestManager mGlide;

    private int PICK_FROM_ALBUM = 10;

    public ReviewAdapter(Context mContext, ArrayList<ReviewMainInfo> reviewMainInfos, WriteReviewActivity writeReviewActivity){
        this.mContext = mContext;
        this.reviewMainInfos = reviewMainInfos;
        this.writeReviewActivity = writeReviewActivity;
    }

    public ReviewAdapter(Context mContext){
        this.mContext = mContext;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.hashtag.setText(reviewMainInfos.get(position).hashtag);

        myViewHolder.add_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ReviewAddPic layout = new ReviewAddPic(mContext);
                myViewHolder.pictures.addView(layout);

                RelativeLayout rl = layout.findViewById(R.id.review_item_rl_addImage);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        ((WriteReviewActivity) writeReviewActivity).startActivityForResult(intent,PICK_FROM_ALBUM);

                        pic pics = new pic();
                        pics.setPosition(position);
                        pics.setIndex(myViewHolder.pictures.indexOfChild(layout));
                        pics.setPosOk(true);

                        notifyDataSetChanged();
                    }
                });
            }
        });

        final pic picture = new pic();
        final int pos;
        final int ind;

        ind = picture.getIndex();
        pos = picture.getPosition();
        mGlide = Glide.with(mContext);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        if(picture.isPosOk()){
            final List<Review> reviews = new ArrayList<>();
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Review")
                    .child(uid)
                    .child(String.valueOf(picture.getPosition()))
                    .child(String.valueOf(picture.getIndex()))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reviews.clear();

                            reviews.add(dataSnapshot.getValue(Review.class));

                            if(pos == position){
                                final ImageView photo = myViewHolder.pictures.getChildAt(ind).findViewById(R.id.review_item_iv_image);
                                final ImageView plus = myViewHolder.pictures.getChildAt(ind).findViewById(R.id.review_item_iv);
                                final TextView txt = myViewHolder.pictures.getChildAt(ind).findViewById(R.id.review_item_tv);

                                if(picture.isImaOk()){
                                    mGlide.load(reviews.get(0).imageUri)
                                            .into(photo);
                                    plus.setVisibility(View.GONE);
                                    txt.setVisibility(View.GONE);
                                    picture.setImaOk(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return reviewMainInfos.size();
    }

    public static class pic{
        private static Uri imageUri;
        private static int position;
        private static int index;
        private static boolean posOk = false;
        private static boolean imaOk = false;

        public static boolean isPosOk() {
            return posOk;
        }

        public static void setPosOk(boolean posOk) {
            pic.posOk = posOk;
        }

        public static boolean isImaOk() {
            return imaOk;
        }

        public static void setImaOk(boolean imaOk) {
            pic.imaOk = imaOk;
        }

        public void setImageUri(Uri imageUri) {
            this.imageUri = imageUri;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Uri getImageUri() {
            return imageUri;
        }

        public int getPosition() {
            return position;
        }

        public int getIndex() {
            return index;
        }
    }
}