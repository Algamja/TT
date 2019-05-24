package com.example.jmkim.nomad.added;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private RecyclerView list_layout;

    private List<UserModel> friends = new ArrayList<>();
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        list_layout = (RecyclerView)findViewById(R.id.friend_list);
        list_layout.setHasFixedSize(true);
        list_layout.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        readFriends();
        Log.e("IN","in");
        friendAdapter = new FriendAdapter(getApplicationContext(), friends);
        list_layout.setAdapter(friendAdapter);
    }

    private void readFriends() {
        String publisher = getIntent().getStringExtra("publisher");
        List<String> friend_keys = new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Follow")
                .child(publisher)
                .child("follower")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        friend_keys.clear();

                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            friend_keys.add(item.getKey());
                        }

                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("UserBasic")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        friends.clear();

                                        for(DataSnapshot item : dataSnapshot.getChildren()){
                                            UserModel user = item.getValue(UserModel.class);
                                            Log.e("NAME",user.userName);

                                            for(int i=0;i<friend_keys.size();i++){
                                                if(user.uid.equals(friend_keys.get(i))){
                                                    friends.add(user);
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
