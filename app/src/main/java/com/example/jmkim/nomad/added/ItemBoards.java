package com.example.jmkim.nomad.added;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemBoards extends LinearLayout {
    private ArrayList<Plan> plans = new ArrayList<>();
    private ArrayList<Review> reviews = new ArrayList<>();
    private List<String> plan_keys = new ArrayList<>();
    private List<String> review_keys = new ArrayList<>();

    public ItemBoards(Context context, int num, String type, String publisher) {
        super(context);

        if (type.equals("plan")) {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Plan")
                    .child(publisher)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            plans.clear();

                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                plans.add(item.getValue(Plan.class));
                                plan_keys.add(item.getKey());
                            }
                            init(num,"plan", publisher);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else if (type.equals("review")) {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Review")
                    .child(publisher)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            reviews.clear();

                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                reviews.add(item.getValue(Review.class));
                                review_keys.add(item.getKey());
                            }
                            init(num,"review", publisher);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void init (int num, String type, String publisher){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.added_item_boards, this, true);

        List<Plan> plans = new ArrayList<>();
        if(type.equals("plan")){
            for (int i = 0; i < plan_keys.size(); i++) {
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Plan")
                        .child(publisher)
                        .child(plan_keys.get(num))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                plans.clear();

                                plans.add(dataSnapshot.getValue(Plan.class));

                                com.example.jmkim.nomad.added.CustomImageView img = rootView.findViewById(R.id.list_img);
                                TextView city = rootView.findViewById(R.id.list_country);
                                TextView period = rootView.findViewById(R.id.list_period);

                                img.setImageDrawable(getResources().getDrawable(R.drawable.plan_img));
                                city.setText("#" + plans.get(0).country);
                                period.setText(plans.get(0).period);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }else if(type.equals("review")){
            for (int i = 0; i < review_keys.size(); i++) {
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Review")
                        .child(publisher)
                        .child(review_keys.get(num))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                reviews.clear();

                                reviews.add(dataSnapshot.getValue(Review.class));

                                com.example.jmkim.nomad.added.CustomImageView img = rootView.findViewById(R.id.list_img);
                                TextView city = rootView.findViewById(R.id.list_country);
                                TextView period = rootView.findViewById(R.id.list_period);

                                img.setImageDrawable(getResources().getDrawable(R.drawable.review_img));
                                city.setText("#" + reviews.get(0).city);
                                period.setText(reviews.get(0).period);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }
    }
}