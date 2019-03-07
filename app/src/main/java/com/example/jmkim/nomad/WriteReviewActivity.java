package com.example.jmkim.nomad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class WriteReviewActivity extends AppCompatActivity {

    RecyclerView rv_main;
    RecyclerView.LayoutManager main_layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        rv_main = findViewById(R.id.review_rv_main);
        rv_main.setHasFixedSize(true);
        main_layoutManager = new LinearLayoutManager(this);
        rv_main.setLayoutManager(main_layoutManager);

        ArrayList<ReviewMainInfo> reviewMainInfos = new ArrayList<>();
        reviewMainInfos.add(new ReviewMainInfo("해시태그1"));
        reviewMainInfos.add(new ReviewMainInfo("해시태그2"));
        reviewMainInfos.add(new ReviewMainInfo("해시태그3"));

        ReviewAdapter reviewAdapter = new ReviewAdapter(getApplicationContext(), reviewMainInfos);
        rv_main.setAdapter(reviewAdapter);
    }
}
