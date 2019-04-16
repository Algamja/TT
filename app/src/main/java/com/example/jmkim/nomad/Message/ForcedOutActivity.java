package com.example.jmkim.nomad.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForcedOutActivity extends AppCompatActivity {
    ChatModel chatModel = new ChatModel();
    List<UserModel> userModelList = new ArrayList<>();
    List<UserModel>userModel = new ArrayList<>();
    List<ChatModel> chatModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forced_out);

        String myUid;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String destRoom;
        destRoom = getIntent().getStringExtra("destRoom");

        int size;
        size = getIntent().getIntExtra("size",0);
        String users[] = new String[size];

        chatModels = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("ChatRooms")
                .child(destRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatModels.clear();
                        chatModels.add(dataSnapshot.getValue(ChatModel.class));
                        chatModel = chatModels.get(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        userModelList.clear();
        for(int i=0;i<size;i++) {
            users[i] = getIntent().getStringExtra("users" + i);

            if(users[i].equals(myUid)){
                continue;
            }

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("UserBasic")
                    .child(users[i])
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userModel.clear();

                            userModel.add(dataSnapshot.getValue(UserModel.class));
                            userModelList.add(userModel.get(0));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        RecyclerView recyclerView = findViewById(R.id.forced_rv);
        recyclerView.setAdapter(new ForcedOutRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button button = (Button)findViewById(R.id.forced_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatModel.users.put(myUid, true);
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("ChatRooms")
                        .child(destRoom)
                        .setValue(chatModel);

                finish();
            }
        });
    }

    class ForcedOutRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forced_out_row, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Glide.with(holder.itemView.getContext())
                    .load(userModelList.get(position).profileImageUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(((CustomViewHolder)holder).imageView);

            ((CustomViewHolder)holder).textView.setText(userModelList.get(position).userName);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CustomViewHolder) holder).checkBox.isChecked()){
                        chatModel.users.put(userModelList.get(position).uid, true);
                    }else{
                        ((CustomViewHolder) holder).checkBox.setChecked(true);
                        chatModel.users.put(userModelList.get(position).uid, false);
                    }
                }
            });

            ((CustomViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        chatModel.users.put(userModelList.get(position).uid, false);
                    }else{
                        chatModel.users.put(userModelList.get(position).uid, true);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return userModelList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public CheckBox checkBox;
            public ImageView imageView;
            public TextView textView;
            public CustomViewHolder(View view) {
                super(view);
                checkBox = (CheckBox)view.findViewById(R.id.forced_row_checkBox);
                imageView = (ImageView)view.findViewById(R.id.forced_row_imageView);
                textView = (TextView)view.findViewById(R.id.forced_row_textView);
            }
        }
    }
}
