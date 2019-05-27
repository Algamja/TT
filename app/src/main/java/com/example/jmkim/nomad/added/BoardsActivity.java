package com.example.jmkim.nomad.added;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.PlanReadActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BoardsActivity extends AppCompatActivity {
    private List<String> plan_keys = new ArrayList<>();
    private List<String> review_keys = new ArrayList<>();

    private TextView topbar;
    private LinearLayout boards_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);
        String uid = getIntent().getStringExtra("publisher");

        topbar = (TextView)findViewById(R.id.boards_topbar);
        boards_layout = (LinearLayout)findViewById(R.id.boards_layout);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(uid)
                .child("userName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = String.valueOf(dataSnapshot.getValue());
                        topbar.setText(name + "의 글");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            plan_keys.add(item.getKey());
                        }

                        for(int i = 0; i<plan_keys.size(); i++){
                            int I = i;
                            ItemBoards itemBoards = new ItemBoards(BoardsActivity.this, i, "plan", uid);

                            itemBoards.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(BoardsActivity.this, PlanReadActivity.class);
                                    intent.putExtra("publisher",uid);
                                    intent.putExtra("key",plan_keys.get(I));
                                    startActivity(intent);
                                }
                            });
                            boards_layout.addView(itemBoards);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Review")
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            review_keys.add(item.getKey());
                        }

                        for(int i = 0; i<review_keys.size(); i++){
                            int I = i;
                            ItemBoards itemBoards = new ItemBoards(BoardsActivity.this, i, "review", uid);

                            itemBoards.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(BoardsActivity.this, ReadReviewActivity.class);
                                    intent.putExtra("publisher",uid);
                                    intent.putExtra("key",review_keys.get(I));
                                    startActivity(intent);
                                }
                            });
                            setView(itemBoards);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setView(ItemBoards itemBoards) {
        boards_layout.addView(itemBoards);
    }
}
