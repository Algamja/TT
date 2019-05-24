package com.example.jmkim.nomad.added;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.WriteReviewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class Review extends AppCompatActivity {
    public static Activity activity_review ;
    private List<String> keys = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_review);
        activity_review = Review.this;

        LinearLayout reviews = findViewById(R.id.reviews);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            keys.add(item.getKey());
                        }

                        for(int i=0; i<keys.size(); i++) {
                            ItemReview item = new ItemReview(Review.this, i);
                            int finalI = i;
                            item.setOnClickListener(view -> {
                                Intent intent = new Intent(Review.this, WriteReviewActivity.class);
                                intent.putExtra("key",keys.get(finalI));
                                startActivity(intent);
                            });
                            reviews.addView(item);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}