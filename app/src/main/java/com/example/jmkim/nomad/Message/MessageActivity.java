package com.example.jmkim.nomad.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.example.jmkim.nomad.WriterActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String formatDate;

    private DrawerLayout drawerLayout;
    private LinearLayout drawer_menu;
    private LinearLayout my_info;
    private ImageView my_profile;
    private TextView my_name;
    private LinearLayout dest_info;
    private ImageView dest_profile;
    private TextView dest_name;
    private LinearLayout message_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        recyclerView = (RecyclerView) findViewById(R.id.message_rv);
        chat = (EditText) findViewById(R.id.message_et_text);
        send = (Button) findViewById(R.id.message_btn_send);

        drawerLayout = (DrawerLayout)findViewById(R.id.message_drawer);
        drawer_menu = (LinearLayout)findViewById(R.id.message_drawer_menu);
        my_info = (LinearLayout)findViewById(R.id.message_menu_my);
        my_profile = (ImageView)findViewById(R.id.message_menu_my_profile);
        my_name = (TextView)findViewById(R.id.message_menu_my_name);
        dest_info = (LinearLayout)findViewById(R.id.message_menu_dest);
        dest_profile = (ImageView)findViewById(R.id.message_menu_dest_profile);
        dest_name = (TextView)findViewById(R.id.message_menu_dest_name);
        message_exit = (LinearLayout)findViewById(R.id.message_exit);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        destUid = getIntent().getStringExtra("destUid");

        final List<UserModel> userModel = new ArrayList<>();

        //userModel에 본인 DB저장
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //읽어오기 시작
                        userModel.clear();

                        userModel.add(dataSnapshot.getValue(UserModel.class));

                        //drawerlayout 내 프로필 사진과 내 이름
                        Glide.with(MessageActivity.this)
                                .load(userModel.get(0).profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(my_profile);
                        my_name.setText(userModel.get(0).userName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //destModel에 상대방 DB저장
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(destUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //읽어오기 시작
                        userModel.clear();

                        userModel.add(dataSnapshot.getValue(UserModel.class));

                        //drawerlayout 상대방 프로필사진과 이름
                        Glide.with(MessageActivity.this)
                                .load(userModel.get(0).profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(dest_profile);
                        dest_name.setText(userModel.get(0).userName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        //drawerlayout의 상대방 정보를 클릭하면 상대방 프로필을 보여줌
        dest_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, WriterActivity.class);
                intent.putExtra("publisher",destUid);
                startActivity(intent);
            }
        });

        message_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MessageActivity.this);
                dlg.setMessage("방을 나가면 모든 대화내용이 삭제되며 \n상대방도 이 대화방을 사용할 수 없습니다.");
                dlg.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("ChatRooms")
                                .child(chatRoomUid)
                                .removeValue();
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        //전송버튼 클릭
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB에 chatModel 저장하기 위함
                ChatModel chatModel = new ChatModel();
                chatModel.users.put(uid, true);
                chatModel.users.put(destUid, true);
                chatModel.type = "one";

                //채팅방 처음 만들어졌을 때, chatRoomUid가 없는 경우
                if (chatRoomUid == null) {
                    //전송버튼을 막아둠
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
                                    //푸시메세지보냄
                                    sendPostToFCM();
                                    //editText 공란처리
                                    chat.setText("");
                                }
                            });
                }
            }
        });
        checkChatRoom();
    }

    //푸시메세지 보내기
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

    //채팅방 확인
    void checkChatRoom() {
        //자신이 속해있는 채팅방의 정보를 불러옴
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
                            //속한 채팅방 중 일반 채팅인 경우만 보여줌
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
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ChatModel.Comment> comments;

        public RecyclerViewAdapter() {
            comments = new ArrayList<>();

            //상대방 정보 불러옴
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("UserBasic")
                    .child(destUid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            destModel = dataSnapshot.getValue(UserModel.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            //내 정보 불러옴
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
            //채팅이 추가되었을 경우
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

            //내가 채팅을 보냈을 경우
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
                //우측 정렬
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
            }

            //상대방이 채팅을 보냈을 경우
            else {
                Glide.with(holder.itemView.getContext())
                        .load(destModel.profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_dest_profile);
                messageViewHolder.textView_dest_name.setText(destModel.userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.linearLayout_user.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                //좌측 정렬
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }

            //상대방 프로필 롱클릭
            messageViewHolder.imageView_dest_profile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    //신고 대화상자 띄우기
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

                    //기타버튼
                    dlg_etc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            //눌리면
                            if(dlg_etc.isChecked()){
                                //의견 쓰는 textBox보이기
                                dlg_text.setVisibility(View.VISIBLE);
                            }else{
                                dlg_text.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

                    //증거사진 첨부하는 버튼 눌리면
                    dlg_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //갤러리 이동
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                            startActivityForResult(intent, PICK_FROM_ALBUM);
                        }
                    });

                    //신고버튼 눌리면
                    dlg.setPositiveButton("신고", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //신고 사유 받아오기
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

                            //신고DB에 저장하기 위한 Model
                            ReportModel reportModel = new ReportModel();
                            reportModel.reporter = uid;
                            reportModel.reportee = destUid;
                            reportModel.reportUrl = img;
                            reportModel.reason = report;

                            //Report DB저장
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

                                            //Email보내기
                                            email.setType("plaine/text");
                                            //우리 이메일 주소(받는사람)
                                            String[] addr = {"todaynomad97@gmail.com"};
                                            email.putExtra(Intent.EXTRA_EMAIL, addr);
                                            //제목
                                            email.putExtra(Intent.EXTRA_SUBJECT, destUid + " 신고");
                                            email.putExtra(Intent.EXTRA_TEXT, report + "\n" + "신고자 : " + uid + "\n" + "============================================================" + "\n 위 내용은 수정하지 마세요.");
                                            startActivity(email);
                                        }
                                    });
                        }
                    });

                    //취소버튼 눌렸을 때
                    dlg.setNegativeButton("취소", null);

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
            //갤러리에서 사진 선택되었을 때
            //progressDialog
            pd = new ProgressDialog(MessageActivity.this);
            pd.setMessage("잠시만 기다려주세요");
            pd.show();

            Uri uri;
            uri = data.getData();

            //Timestamp
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            formatDate = simpleDateFormat.format(date);

            //사진 저장소에 저장
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
                            Task<Uri> uriTask = ref.getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            Uri download = uriTask.getResult();
                            img = String.valueOf(download);

                            dlg_add.setVisibility(View.GONE);
                            dlg_img_root.setVisibility(View.VISIBLE);
                            dlg_img_root.setText(img);
                            pd.dismiss();
                        }
                    });

        }
    }
}