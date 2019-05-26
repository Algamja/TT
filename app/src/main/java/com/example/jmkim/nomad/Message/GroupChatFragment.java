package com.example.jmkim.nomad.Message;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GroupChatFragment extends Fragment {
    String group_title = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.groupChat_rv);
        recyclerView.setAdapter(new GroupChatRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        return view;
    }

    class GroupChatRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private List<ChatModel> chatModels = new ArrayList<>();
        private List<String> room_keys = new ArrayList<>();
        private String uid;
        private ArrayList<String> destUser = new ArrayList<>();

        private Map<String, UserModel> userModelMap = new HashMap<>();

        public GroupChatRecyclerViewAdapter(){
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("ChatRooms")
                    .orderByChild("users/" + uid)
                    .equalTo(true)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            chatModels.clear();

                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                if(item.getValue(ChatModel.class).type.equals("group")){
                                    chatModels.add(item.getValue(ChatModel.class));
                                    room_keys.add(item.getKey());
                                }
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_chat_item_row, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final CustomViewHolder customViewHolder = (CustomViewHolder)holder;
            String destUid = "";



            for(String user : chatModels.get(position).users.keySet()){
                if(!user.equals(uid)){
                    destUid = user;
                    destUser.add(destUid);
                }
            }

            Glide.with(customViewHolder.imageView.getContext())
                    .load("https://firebasestorage.googleapis.com/v0/b/trip-box-1be2a.appspot.com/o/group.png?alt=media&token=6bf6e800-ba18-425b-bbe4-3e31a78e2ce7")
                    .apply(new RequestOptions().circleCrop())
                    .into(customViewHolder.imageView);
            Map<String, ChatModel.Comment> commentMap = new TreeMap<>(Collections.<String>reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);

            if(commentMap.keySet().toArray().length > 0 ){
                String lastMessageKey = (String)commentMap.keySet().toArray()[0];
                customViewHolder.textView_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);
            }
            customViewHolder.textView_title.setText(chatModels.get(position).title);

            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getView().getContext(), GroupMessageActivity.class);
                    intent.putExtra("destRoom", room_keys.get(position));
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView textView_title;
            public TextView textView_last_message;

            public CustomViewHolder(View view) {
                super(view);

                imageView = itemView.findViewById(R.id.group_chatItem_imageView);
                textView_title = itemView.findViewById(R.id.group_chatItem_textView_title);
                textView_last_message = itemView.findViewById(R.id.group_chatItem_textView_lastMessage);
            }
        }
    }
}
