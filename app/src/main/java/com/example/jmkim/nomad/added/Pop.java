package com.example.jmkim.nomad.added;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.WriteReviewActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class Pop extends AppCompatActivity {

    private FloatingActionButton write_plan;
    private FloatingActionButton write_review;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_pop);

        write_plan = (FloatingActionButton) findViewById(R.id.main_fb_write_plan);
        write_review = (FloatingActionButton) findViewById(R.id.main_fb_write_review);

        //리뷰 작성하기 클릭되었을 때
        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WriteReviewActivity로 이동
                startActivity(new Intent(Pop.this, Review.class));
                finish();
            }
        });

        //일정 작성하기 클릭되었을 때
        write_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WritePlanActivity로 이동
                startActivity(new Intent(Pop.this, WritePlan.class));
                finish();
            }
        });
    }
}