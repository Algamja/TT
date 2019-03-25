package com.example.jmkim.nomad;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class WriterActivity extends AppCompatActivity {

    private FirebaseUser user;
    String uid;

    private ImageView bGround;
    private ImageView back;
    private TextView userName;

    private LinearLayout chat;
    private LinearLayout docks;
    private LinearLayout location;
    private LinearLayout friends;
    private LinearLayout settings;
    private LinearLayout notification;

    private RequestManager mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writer);

        bGround = (ImageView)findViewById(R.id.writer_iv_bground);
        back = (ImageView)findViewById(R.id.writer_iv_back);
        userName = (TextView)findViewById(R.id.writer_tv_name);

        chat = (LinearLayout)findViewById(R.id.writer_icon_chat);
        docks = (LinearLayout)findViewById(R.id.writer_icon_docks);
        location = (LinearLayout)findViewById(R.id.writer_icon_location);
        friends = (LinearLayout)findViewById(R.id.writer_icon_friends);
        settings = (LinearLayout)findViewById(R.id.writer_icon_settings);
        notification = (LinearLayout)findViewById(R.id.writer_icon_notification);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

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

        if(publisher.equals(uid)){
            chat.setEnabled(false);
        }

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WriterActivity.this, MessageActivity.class);
                intent.putExtra("destUid",publisher);
                intent.putExtra("chatType","One");
                startActivity(intent);
            }
        });

        //상단 뒤로가기 화살표
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
