package com.example.jmkim.nomad.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.NotificationModel;
import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.DB.ReportModel;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class MessageActivity extends AppCompatActivity {

    private String destUid;
    private String uid;
    private String chatRoomUid;

    private RecyclerView recyclerView;
    private EditText chat;
    private Button send;

    private UserModel destModel;
    private UserModel userModel;

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAAW7l3vo:APA91bHJNsj3OIVn4hAq5fDKFM0GdAiK_HOU8z_rRt5zmueQGqXWeENItP6UWzBcSsskA50AeifQyMYHnatZOLAqjkvEFSvnlQsQdQ4Pg3NVEQrHiC1LrLqvxcXa6VQfNHZ8Y1cRrR-6";

    private View dlg_report;
    private AlertDialog dlg = null;
    private RadioButton dlg_swear;
    private RadioButton dlg_sexually;
    private RadioButton dlg_spam;
    private RadioButton dlg_etc;
    private EditText dlg_text;
    private ImageView dlg_add;
    private TextView dlg_img_root;
    private String report;
    private String img;
    private int PICK_FROM_ALBUM = 10;

    private ProgressDialog pd;

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

            messageViewHolder.imageView_dest_profile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dlg_report = (View)View.inflate(MessageActivity.this, R.layout.report_dialog, null);
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MessageActivity.this);
                    dlg.setView(dlg_report);

                    dlg_swear = (RadioButton)dlg_report.findViewById(R.id.report_swear_word);
                    dlg_sexually = (RadioButton)dlg_report.findViewById(R.id.report_sexually);
                    dlg_spam = (RadioButton)dlg_report.findViewById(R.id.report_spam);
                    dlg_etc = (RadioButton)dlg_report.findViewById(R.id.report_etc);
                    dlg_text = (EditText)dlg_report.findViewById(R.id.report_text);
                    dlg_add = (ImageView)dlg_report.findViewById(R.id.report_iv);
                    dlg_img_root = (TextView)dlg_report.findViewById(R.id.report_image_root);

                    dlg_etc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(dlg_etc.isChecked()){
                                dlg_text.setVisibility(View.VISIBLE);
                            }else{
                                dlg_text.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    dlg_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            startActivityForResult(intent, PICK_FROM_ALBUM);
                        }
                    });

                    dlg.setPositiveButton("신고", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(dlg_swear.isChecked()){
                                report = "욕설/비방";
                            }else if(dlg_sexually.isChecked()){
                                report = "성희롱";
                            }else if(dlg_spam.isChecked()){
                                report = "스팸메시지";
                            }else if(dlg_etc.isChecked()){
                                report = dlg_text.getText().toString();
                            }else{
                                Toast.makeText(MessageActivity.this, "신고 사유를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ReportModel reportModel = new ReportModel();
                            reportModel.reporter = uid;
                            reportModel.reportee = destUid;
                            reportModel.reportUrl = img;
                            reportModel.reason = report;

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Report")
                                    .push()
                                    .setValue(reportModel)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent email = new Intent(Intent.ACTION_SEND);
                                            email.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                            email.setType("plaine/text");
                                            String[] addr = {"todaynomad97@gmail.com"};
                                            email.putExtra(Intent.EXTRA_EMAIL, addr);
                                            email.putExtra(Intent.EXTRA_SUBJECT, destUid + " 신고");
                                            email.putExtra(Intent.EXTRA_TEXT, report + "\n" + "신고자 : " + uid + "\n" + "============================================================" + "\n 위 내용은 수정하지 마세요.");
                                            startActivity(email);
                                        }
                                    });
                        }
                    });
                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseStorage
                                    .getInstance()
                                    .getReference()
                                    .child(destUid)
                                    .child(uid)
                                    .delete();
                        }
                    });
                    dlg.show();
                    return true;
                }
            });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            pd = new ProgressDialog(MessageActivity.this);
            pd.setMessage("잠시만 기다려주세요");
            pd.show();

            Uri uri;
            uri = data.getData();

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String formatDate = simpleDateFormat.format(date);

            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("report")
                    .child(destUid)
                    .child(uid)
                    .child(formatDate)
                    .putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        final StorageReference ref =
                                FirebaseStorage
                                        .getInstance()
                                        .getReference()
                                        .child("report")
                                        .child(destUid)
                                        .child(uid)
                                        .child(formatDate);

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(MessageActivity.this, "처리중", Toast.LENGTH_SHORT).show();
                            Task<Uri> uriTask = ref.getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            Uri download = uriTask.getResult();
                            img = String.valueOf(download);

                            Log.e("IMG_ROOT",img);

                            dlg_add.setVisibility(View.GONE);
                            dlg_img_root.setVisibility(View.VISIBLE);
                            dlg_img_root.setText(img);
                            pd.dismiss();
                        }
                    });

        }
    }
}