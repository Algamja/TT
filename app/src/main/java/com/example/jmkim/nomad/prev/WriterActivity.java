package com.example.jmkim.nomad.prev;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.Message.MessageActivity;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.added.BoardsActivity;
import com.example.jmkim.nomad.added.FriendsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WriterActivity extends AppCompatActivity {
    private ImageView bGround;
    private TextView userName;
    private Button follow;

    private LinearLayout chat;
    private LinearLayout boards;
    private LinearLayout friends;
    private LinearLayout reports;

    private ImageView other;
    private ImageView my;

    private RequestManager mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);

        bGround = (ImageView) findViewById(R.id.writer_iv_bground);
        userName = (TextView) findViewById(R.id.writer_tv_name);
        follow = (Button)findViewById(R.id.writer_btn_follow);

        chat = (LinearLayout) findViewById(R.id.writer_icon_chat);
        boards = (LinearLayout) findViewById(R.id.writer_icon_boards);
        friends = (LinearLayout) findViewById(R.id.writer_icon_friends);
        reports = (LinearLayout) findViewById(R.id.writer_icon_notification);

        other = (ImageView) findViewById(R.id.writer_chat);
        my = (ImageView) findViewById(R.id.writer_my_chat);

        mGlide = Glide.with(this);

        Intent intent = getIntent();
        final String publisher = intent.getExtras().getString("publisher");

        final List<UserModel> userModels = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("UserBasic")
                .child(publisher)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userModels.clear();

                        userModels.add(dataSnapshot.getValue(UserModel.class));

                        mGlide.load(userModels.get(0).profileImageUrl)
                                .into(bGround);
                        userName.setText(userModels.get(0).userName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (publisher.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            my.setVisibility(View.VISIBLE);
            other.setVisibility(View.GONE);
            chat.setEnabled(false);
            follow.setVisibility(View.INVISIBLE);
        }

        if(FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Follow")
                .child(publisher)
                .child("follower")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .equals(true)){
            follow.setText("친구삭제");
        }else{
            follow.setText("친구추가");
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(follow.getText().toString().equals("친구추가")){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("following")
                            .child(publisher)
                            .setValue(true);

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(publisher)
                            .child("follower")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(true);

                    follow.setText("친구삭제");
                }else if(follow.getText().toString().equals("친구삭제")){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("following")
                            .child(publisher)
                            .removeValue();

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Follow")
                            .child(publisher)
                            .child("follower")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .removeValue();

                    follow.setText("친구추가");
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriterActivity.this, MessageActivity.class);
                intent.putExtra("destUid", publisher);
                intent.putExtra("chatType", "One");
                startActivity(intent);
            }
        });

        boards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriterActivity.this, BoardsActivity.class);
                intent.putExtra("publisher",publisher);
                startActivity(intent);
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriterActivity.this, FriendsActivity.class);
                intent.putExtra("publisher",publisher);
                startActivity(intent);
            }
        });
    }
}
