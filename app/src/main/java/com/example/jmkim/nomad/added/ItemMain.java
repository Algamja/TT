package com.example.jmkim.nomad.added;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.PlanReadActivity;
import com.example.jmkim.nomad.prev.WriterActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ItemMain extends LinearLayout {
    Main parent;

    private RequestManager mGlide;
    String tag;
    int imsi_like;

    public ItemMain(Context context, Plan plan, UserModel userModel, String key) {
        super(context);
        init(plan, userModel, key);
        parent = (Main) context;
    }

    public ItemMain(Context context, Review review, UserModel userModel, String key) {
        super(context);
        init(review, userModel, key);
        parent = (Main) context;
    }

    private void init(Plan plan, UserModel userModel, String key) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Context context = getContext();
        View rootView = inflater.inflate(R.layout.added_item_main, this, true);

        ImageView profile;
        TextView name;
        TextView country;
        ImageView img;
        TextView like_count;
        ToggleButton btn_like;

        profile = (ImageView)rootView.findViewById(R.id.board_profile);
        name = (TextView)rootView.findViewById(R.id.board_name);
        country = (TextView)rootView.findViewById(R.id.board_country);
        img = (ImageView)rootView.findViewById(R.id.board_img);
        like_count = (TextView)rootView.findViewById(R.id.board_tv_like);
        btn_like = (ToggleButton)rootView.findViewById(R.id.board_btn_like);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(userModel.uid)
                .child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(int i=0;i<plan.like.size();i++){
                            Set set = plan.like.keySet();
                            Iterator iterator = set.iterator();

                            while(iterator.hasNext()){
                                if( FirebaseAuth.getInstance().getCurrentUser().getUid().equals(((String)iterator.next()))){
                                    btn_like.setChecked(true);
                                    break;
                                }
                            }
                        }

                        for(int i=0;i<plan.hashtag.size();i++){
                            Set set = plan.hashtag.keySet();
                            Iterator iterator = set.iterator();

                            while (iterator.hasNext()){
                                tag = (String)iterator.next();
                                break;
                            }
                        }

                        mGlide = Glide.with(context);
                        String imsi_city = plan.country;
                        String imsi_country[] = imsi_city.split(" ");

                        FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("city")
                                .child(imsi_country[0]+".jpg")
                                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mGlide.load(uri)
                                .apply(new RequestOptions().fitCenter())
                                .into(img);
                            }
                        });

                        mGlide.load(userModel.profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(profile);
                        name.setText(userModel.userName);
                        country.setText(plan.country);

                        if(plan.like.size()!=0){
                            like_count.setText(String.valueOf(plan.like.size()));
                        }else{
                            like_count.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlanReadActivity.class);
                intent.putExtra("publisher",plan.publisher);
                intent.putExtra("key",key);
                context.startActivity(intent);
            }
        });

        profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WriterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("publisher",plan.publisher);
                context.startActivity(intent);
            }
        });

        imsi_like=plan.like.size();

        btn_like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_like.isChecked()){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Plan")
                            .child(plan.publisher)
                            .child(key)
                            .child("like")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btn_like.setChecked(true);
                                    imsi_like +=1;
                                    like_count.setText(String.valueOf(imsi_like));
                                }
                            });
                }else{
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Plan")
                            .child(plan.publisher)
                            .child(key)
                            .child("like")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btn_like.setChecked(false);
                                   imsi_like-=1;
                                    like_count.setText(String.valueOf(imsi_like));
                                }
                            });
                }

            }
        });
    }

    private void init(Review review, UserModel userModel, String key) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Context context = getContext();
        View rootView = inflater.inflate(R.layout.added_item_main, this, true);

        ImageView profile;
        TextView name;
        TextView country;
        ImageView img;
        TextView like_count;
        ToggleButton btn_like;

        profile = (ImageView)rootView.findViewById(R.id.board_profile);
        name = (TextView)rootView.findViewById(R.id.board_name);
        country = (TextView)rootView.findViewById(R.id.board_country);
        img = (ImageView)rootView.findViewById(R.id.board_img);
        like_count = (TextView)rootView.findViewById(R.id.board_tv_like);
        btn_like = (ToggleButton)rootView.findViewById(R.id.board_btn_like);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(userModel.uid)
                .child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(int i=0;i<review.like.size();i++){
                            Set set = review.like.keySet();
                            Iterator iterator = set.iterator();

                            while(iterator.hasNext()){
                                if( FirebaseAuth.getInstance().getCurrentUser().getUid().equals(((String)iterator.next()))){
                                    btn_like.setChecked(true);
                                    break;
                                }
                            }
                        }

                        for(int i=0;i<review.hashtag.size();i++){
                            Set set = review.hashtag.keySet();
                            Iterator iterator = set.iterator();

                            while (iterator.hasNext()){
                                tag = (String)iterator.next();
                                break;
                            }
                        }

                        mGlide = Glide.with(context);

                        mGlide.load(userModel.profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(profile);
                        name.setText(userModel.userName);
                        country.setText(review.city);

                        mGlide.load(review.hashtag.get(tag).imageUri)
                                .apply(new RequestOptions().fitCenter())
                                .into(img);

                        if(review.like.size()!=0){
                            like_count.setText(String.valueOf(review.like.size()));
                        }else{
                            like_count.setText("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WriterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("publisher",review.publisher);
                context.startActivity(intent);
            }
        });

        imsi_like=review.like.size();

        btn_like.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_like.isChecked()){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Review")
                            .child(review.publisher)
                            .child(key)
                            .child("like")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btn_like.setChecked(true);
                                    imsi_like +=1;
                                    like_count.setText(String.valueOf(imsi_like));
                                }
                            });
                }else{
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Review")
                            .child(review.publisher)
                            .child(key)
                            .child("like")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    btn_like.setChecked(false);
                                    imsi_like-=1;
                                    like_count.setText(String.valueOf(imsi_like));
                                }
                            });
                }

            }
        });
    }
}