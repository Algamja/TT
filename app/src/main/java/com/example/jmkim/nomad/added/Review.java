package com.example.jmkim.nomad.added;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

import androidx.appcompat.app.AppCompatActivity;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_review);

        LinearLayout reviews = findViewById(R.id.reviews);
        for(int i=0; i<3; i++) {
            ItemReview item = new ItemReview(Review.this, i);
            item.setOnClickListener(view -> {
                Intent intent = new Intent(Review.this, WritePlan.class);
                intent.putExtra("review","true");
                startActivity(intent);
            });
            reviews.addView(item);
        }
    }



}