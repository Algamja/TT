package com.example.jmkim.nomad.added;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private TextView topbar;
    private RecyclerView list_layout;

    private List<UserModel> friends = new ArrayList<>();
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        topbar = (TextView)findViewById(R.id.friend_topbar);
        list_layout = (RecyclerView)findViewById(R.id.friend_list);

        list_layout.setHasFixedSize(true);
        list_layout.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        readFriends();
    }

    private void readFriends() {
        String publisher = getIntent().getStringExtra("publisher");
        List<String> friend_keys = new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(publisher)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        topbar.setText(user.userName + "의 친구 목록");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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
                                        friendAdapter = new FriendAdapter(getApplicationContext(), friends);
                                        list_layout.setAdapter(friendAdapter);
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
