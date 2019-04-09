package com.example.jmkim.nomad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.NotificationModel;
import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.DB.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private String destUid;
    private String uid;
    private String chatRoomUid;
    //private String chatType;

    private RecyclerView recyclerView;
    private EditText chat;
    private Button send;

    private UserModel destModel;
    private UserModel userModel;

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAAW7l3vo:APA91bHJNsj3OIVn4hAq5fDKFM0GdAiK_HOU8z_rRt5zmueQGqXWeENItP6UWzBcSsskA50AeifQyMYHnatZOLAqjkvEFSvnlQsQdQ4Pg3NVEQrHiC1LrLqvxcXa6VQfNHZ8Y1cRrR-6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = (RecyclerView) findViewById(R.id.message_rv);
        chat = (EditText) findViewById(R.id.message_et_text);
        send = (Button) findViewById(R.id.message_btn_send);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destUid = getIntent().getStringExtra("destUid");
        //chatType = getIntent().getStringExtra("chatType");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(chatType.equals("One")){
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);
                chatModel.users.put(destUid, true);
                chatModel.type = "one";

                if (chatRoomUid == null) {
                    send.setEnabled(false);
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("ChatRooms")
                            .push()
                            .setValue(chatModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    checkChatRoom();
                                }
                            });
                } else {
                    ChatModel.Comment comment = new ChatModel.Comment();
                    comment.uid = uid;
                    comment.message = chat.getText().toString();
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("ChatRooms")
                            .child(chatRoomUid)
                            .child("comments")
                            .push()
                            .setValue(comment)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    sendPostToFCM();
                                    chat.setText("");
                                }
                            });
                }
                //}
            }
        });
        checkChatRoom();
    }

    void sendPostToFCM() {
        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = destModel.pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.text = chat.getText().toString();
        notificationModel.data.title = userName;
        notificationModel.data.text = chat.getText().toString();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-type", "application/json")
                .addHeader("Authorization", "key=" + SERVER_KEY)
                .url(FCM_MESSAGE_URL)
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    void checkChatRoom() {
        //if(chatType.equals("One")){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("ChatRooms")
                .orderByChild("users/" + uid)
                .equalTo(true)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            ChatModel chatModel = item.getValue(ChatModel.class);
                            if (chatModel.users.containsKey(destUid) && chatModel.type.equals("one")) {
                                chatRoomUid = item.getKey();
                                send.setEnabled(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                                recyclerView.setAdapter(new RecyclerViewAdapter());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //}
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("UserBasic")
                    .child(destUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            destModel = dataSnapshot.getValue(UserModel.class);
                            //getMessageList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("UserBasic")
                    .child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userModel = dataSnapshot.getValue(UserModel.class);
                            getMessageList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        void getMessageList() {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("ChatRooms")
                    .child(chatRoomUid)
                    .child("comments")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            comments.clear();

                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                comments.add(item.getValue(ChatModel.Comment.class));
                            }
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(comments.size() - 1);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_row, parent, false);

            return new MessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);

            if (comments.get(position).uid.equals(uid)) {
                Glide.with(holder.itemView.getContext())
                        .load(userModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_user_profile);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.textView_user_name.setText(userModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.linearLayout_user.setVisibility(View.VISIBLE);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(destModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_dest_profile);
                messageViewHolder.textView_dest_name.setText(destModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.linearLayout_user.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView textView_message;
            public TextView textView_dest_name;
            public ImageView imageView_dest_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_user_name;
            public ImageView imageView_user_profile;
            public LinearLayout linearLayout_user;

            public MessageViewHolder(View view) {
                super(view);
                textView_message = (TextView) view.findViewById(R.id.message_item_tv_message);
                textView_dest_name = (TextView) view.findViewById(R.id.message_item_tv_name);
                imageView_dest_profile = (ImageView) view.findViewById(R.id.message_item_iv_profile);
                linearLayout_destination = (LinearLayout) view.findViewById(R.id.message_item_ll_dest);
                linearLayout_main = (LinearLayout) view.findViewById(R.id.message_item_ll_main);
                textView_user_name = (TextView) view.findViewById(R.id.message_item_tv_user_name);
                imageView_user_profile = (ImageView) view.findViewById(R.id.message_item_iv_user_profile);
                linearLayout_user = (LinearLayout) view.findViewById(R.id.message_item_ll_user);
            }
        }
    }
}