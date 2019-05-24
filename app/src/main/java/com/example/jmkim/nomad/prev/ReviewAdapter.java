package com.example.jmkim.nomad.prev;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private ArrayList<ReviewMainInfo> reviewMainInfos;
    WriteReviewActivity writeReviewActivity;

    private RequestManager mGlide;

    private int PICK_FROM_ALBUM = 10;

    String tag_rate;
    String tag_comment;

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
        EditText comment;
        RatingBar rate;
        RelativeLayout add_image;
        ImageView image;
        ImageView iv;
        TextView tv;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hashtag = itemView.findViewById(R.id.review_item_tv_hashtag);
            comment = itemView.findViewById(R.id.review_item_et_comment);
            rate = itemView.findViewById(R.id.review_item_ratingBar);

            add_image = itemView.findViewById(R.id.review_item_rl_addImage);
            image = itemView.findViewById(R.id.review_item_iv_image);
            iv = itemView.findViewById(R.id.review_item_iv);
            tv = itemView.findViewById(R.id.review_item_tv);
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
        final pic picture = new pic();

        mGlide = Glide.with(mContext);

        myViewHolder.comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tag_comment = myViewHolder.comment.getText().toString();

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Imsi")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(reviewMainInfos.get(myViewHolder.getPosition()).hashtag)
                        .child("comment")
                        .setValue(tag_comment);
            }
        });

        myViewHolder.rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tag_rate = String.valueOf(rating);

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Imsi")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(reviewMainInfos.get(myViewHolder.getPosition()).hashtag)
                        .child("rate")
                        .setValue(tag_rate);
            }
        });

        myViewHolder.hashtag.setText("#"+reviewMainInfos.get(position).hashtag);

        myViewHolder.add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                ((WriteReviewActivity) writeReviewActivity).startActivityForResult(intent,PICK_FROM_ALBUM);

                pic pics = new pic();
                pics.setPosition(position);
                pics.setPosOk(true);

                if(picture.isPosOk()){
                    final List<Review.Review_Tag> review_tags = new ArrayList<>();
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Imsi")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(reviewMainInfos.get(myViewHolder.getPosition()).hashtag)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    review_tags.clear();
                                    review_tags.add(dataSnapshot.getValue(Review.Review_Tag.class));

                                    if(picture.isImaOk()){
                                        if(myViewHolder.getPosition() == Integer.parseInt(review_tags.get(0).position))
                                        mGlide.load(review_tags.get(0).imageUri)
                                                .into(myViewHolder.image);
                                        myViewHolder.iv.setVisibility(View.GONE);
                                        myViewHolder.tv.setVisibility(View.GONE);
                                        picture.setImaOk(false);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }
        });
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