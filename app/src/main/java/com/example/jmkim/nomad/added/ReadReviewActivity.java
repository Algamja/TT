package com.example.jmkim.nomad.added;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReadReviewActivity extends AppCompatActivity {

    private RecyclerView main;
    private RecyclerView.LayoutManager layoutManager;

    Review review = new Review();
    ArrayList<String> tag_keys = new ArrayList<>();
    Map<String,Review.Review_Tag> review_tagMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_read);

        main = (RecyclerView)findViewById(R.id.read_review_main);
        main.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        main.setLayoutManager(layoutManager);

        String get_publisher = getIntent().getStringExtra("publisher");
        String get_key = getIntent().getStringExtra("key");

        List<Review> reviews = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Review")
                .child(get_publisher)
                .child(get_key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        reviews.clear();

                        reviews.add(dataSnapshot.getValue(Review.class));


                        for(int i=0;i<reviews.get(0).hashtag.size();i++){
                            Set set = reviews.get(0).hashtag.keySet();
                            Iterator iterator = set.iterator();

                            tag_keys.clear();
                            while(iterator.hasNext()){
                                String tag_key = (String)iterator.next();
                                tag_keys.add(tag_key);
                            }
                        }
                        Log.e("size", String.valueOf(tag_keys.size()));
                        ReadReviewItem readReviewItem = new ReadReviewItem(ReadReviewActivity.this, tag_keys, reviews.get(0), ReadReviewActivity.this);
                        main.setAdapter(readReviewItem);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
