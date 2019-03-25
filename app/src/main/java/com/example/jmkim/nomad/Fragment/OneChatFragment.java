package com.example.jmkim.nomad.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.OneChatModel;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OneChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_one_chat, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.oneChat_rv);
        recyclerView.setAdapter(new OneChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        return view;
    }

    class OneChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<OneChatModel> oneChatModels = new ArrayList<>();
        private String uid;
        public OneChatRecyclerViewAdapter(){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("OneChatRooms")
                    .orderByChild("UserBasic/"+uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            oneChatModels.clear();
                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                oneChatModels.add(item.getValue(OneChatModel.class));
                            }
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_chat_item_row, parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destUid = null;

            for(String user : oneChatModels.get(position).users.keySet()){
                if(!user.equals(uid)){
                    destUid = user;
                }
            }
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("UserBasic")
                    .child(destUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);
                            Glide.with(customViewHolder.itemView.getContext())
                                    .load(userModel.profileImageUrl)
                                    .apply(new RequestOptions().circleCrop())
                                    .into(customViewHolder.imageView);

                            customViewHolder.textView_title.setText(userModel.userName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            Map<String, OneChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
            commentMap.putAll(oneChatModels.get(position).comments);
            String lastMessageKey = (String)commentMap.keySet().toArray()[0];
            customViewHolder.textView_last_message.setText(oneChatModels.get(position).comments.get(lastMessageKey).message);
        }

        @Override
        public int getItemCount() {
            return oneChatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder{
            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = itemView.findViewById(R.id.one_chatItem_imageView);
                textView_title = itemView.findViewById(R.id.one_chatItem_textView_title);
                textView_last_message = itemView.findViewById(R.id.one_chatItem_textView_lastMessage);
            }
        }
    }
}
