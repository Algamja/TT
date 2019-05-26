package com.example.jmkim.nomad.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.DB.NotificationModel;
import com.example.jmkim.nomad.DB.ReportModel;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.WriterActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMessageActivity extends Activity {

    private Map<String, UserModel> users = new HashMap<>();
    private String destRoom;
    private String myUid;
    private EditText chat;

    private RecyclerView recyclerView;

    private List<ChatModel.Comment> comments = new ArrayList<>();

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
    private String dest;
    private int PICK_FROM_ALBUM = 10;

    private DrawerLayout drawerLayout;
    private LinearLayout drawer_menu;
    private ListView userview;
    private LinearLayout exit;
    private LinearLayout forced;
    private LinearLayout copy_key;

    private List<String> uids = new ArrayList<>();
    private List<UserModel> att = new ArrayList<>();
    private List<ChatModel> chatModels = new ArrayList<>();
    private ChatModel chatModel = new ChatModel();

    private String king;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_message);

        //단체방UID
        destRoom = getIntent().getStringExtra("destRoom");
        //내uid
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        chat = (EditText) findViewById(R.id.groupMessage_et);

        drawerLayout = (DrawerLayout)findViewById(R.id.groupMessage_drawer);
        drawer_menu = (LinearLayout)findViewById(R.id.groupMessage_drawer_menu);
        userview = (ListView)findViewById(R.id.groupMessage_drawer_lv);
        exit = (LinearLayout)findViewById(R.id.groupMessage_exit);
        forced = (LinearLayout)findViewById(R.id.groupMessage_forced);
        copy_key = (LinearLayout)findViewById(R.id.groupMessage_get_key);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            users.put(item.getKey(), item.getValue(UserModel.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

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
                        chatModel =  chatModels.get(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        att.clear();
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("ChatRooms")
                .child(destRoom)
                .child("users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                        for(DataSnapshot item : dataSnapshot1.getChildren()){
                            uids.add(item.getKey());
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("UserBasic")
                                    .child(uids.get(uids.size()-1))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            if(item.getValue(Boolean.class)){
                                                att.add(dataSnapshot2.getValue(UserModel.class));

                                                String[] names =  new String[att.size()];
                                                for(int i=0;i<att.size();i++){
                                                    names[i] = att.get(i).userName;
                                                }
                                            }
                                            ListviewAdapter adapter = new ListviewAdapter(GroupMessageActivity.this, R.layout.drawer_friend_row, att);
                                            userview.setAdapter(adapter);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                        init();

                        recyclerView  = (RecyclerView)findViewById(R.id.groupMessage_rv);
                        recyclerView.setAdapter(new GroupMessageRecyclerViewAdapter());
                        recyclerView.setLayoutManager(new LinearLayoutManager(GroupMessageActivity.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    void init() {
        Button send = (Button) findViewById(R.id.groupMessage_btn);
        //전송버튼 클릭
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatModel.Comment comment = new ChatModel.Comment();
                comment.uid = myUid;
                comment.message = chat.getText().toString();

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("ChatRooms")
                        .child(destRoom)
                        .child("comments")
                        .push()
                        .setValue(comment)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("ChatRooms")
                                        .child(destRoom)
                                        .child("users")
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Map<String, Boolean> map = (Map<String, Boolean>) dataSnapshot.getValue();

                                                for (String item : map.keySet()) {
                                                    if (item.equals(myUid)) {
                                                        continue;
                                                    }

                                                    sendPostToFCM(users.get(item).pushToken, comment.message);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                chat.setText("");
                            }
                        });
            }
        });

        userview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroupMessageActivity.this, WriterActivity.class);
                intent.putExtra("publisher",att.get(position).uid);
                startActivity(intent);
            }
        });

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("ChatRooms")
                .child(destRoom)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        king = chatModel.king;

                        if(!king.equals(myUid)){
                            forced.setVisibility(View.GONE);
                            copy_key.setVisibility(View.GONE);
                        }else{
                            forced.setVisibility(View.VISIBLE);
                            copy_key.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(king.equals(myUid)){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(GroupMessageActivity.this);
                    dlg.setMessage("방을 나가면 모든 대화내용이 삭제되며 \n상대방도 이 대화방을 사용할 수 없습니다.");
                    dlg.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("ChatRooms")
                                    .child(destRoom)
                                    .removeValue();
                        }
                    });
                    dlg.setNegativeButton("취소", null);
                    dlg.show();
                }else{
                    AlertDialog.Builder dlg = new AlertDialog.Builder(GroupMessageActivity.this);
                    dlg.setMessage("채팅방을 나가시겠습니까?");
                    dlg.setPositiveButton("나가기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                            chatModel.users.remove(myUid);
                            chatModel.users.put(myUid, false);
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("ChatRooms")
                                    .child(destRoom)
                                    .setValue(chatModel);
                        }
                    });
                    dlg.setNegativeButton("취소", null);
                    dlg.show();
                }
            }
        });

        forced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupMessageActivity.this, ForcedOutActivity.class);
                intent.putExtra("size",att.size());
                for(int i=0;i<att.size();i++){
                    intent.putExtra("users"+i ,att.get(i).uid);
                }
                intent.putExtra("destRoom",destRoom);
                startActivity(intent);
            }
        });

        copy_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ChatPW",destRoom);
                clipboardManager.setPrimaryClip(clipData);}
        });
    }

    //푸시메세지 전송
    void sendPostToFCM(String pushToken, String message) {
        Gson gson = new Gson();

        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.to = pushToken;
        notificationModel.notification.title = userName;
        notificationModel.notification.text = message;
        notificationModel.data.title = userName;
        notificationModel.data.text = message;

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

    class GroupMessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public GroupMessageRecyclerViewAdapter() {
            getMessageList();
        }

        void getMessageList() {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("ChatRooms")
                    .child(destRoom)
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

            return new GroupMessageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            GroupMessageViewHolder messageViewHolder = ((GroupMessageViewHolder) holder);

            if (comments.get(position).uid.equals(myUid)) {
                Glide.with(holder.itemView.getContext())
                        .load(users.get(comments.get(position).uid).profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_user_profile);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.rightbubble);
                messageViewHolder.textView_user_name.setText(users.get(comments.get(position).uid).userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                messageViewHolder.linearLayout_user.setVisibility(View.VISIBLE);
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(users.get(comments.get(position).uid).profileImageUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(messageViewHolder.imageView_dest_profile);
                messageViewHolder.textView_dest_name.setText(users.get(comments.get(position).uid).userName);
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                messageViewHolder.linearLayout_user.setVisibility(View.INVISIBLE);
                messageViewHolder.textView_message.setBackgroundResource(R.drawable.leftbubble);
                messageViewHolder.textView_message.setText(comments.get(position).message);
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
            }

            messageViewHolder.imageView_dest_profile.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    for (int i = 0; i < users.size(); i++) {
                        if (position == i) {
                            dest = comments.get(position).uid;

                            dlg_report = (View) View.inflate(GroupMessageActivity.this, R.layout.report_dialog, null);
                            AlertDialog.Builder dlg = new AlertDialog.Builder(GroupMessageActivity.this);
                            dlg.setView(dlg_report);

                            dlg_swear = (RadioButton) dlg_report.findViewById(R.id.report_swear_word);
                            dlg_sexually = (RadioButton) dlg_report.findViewById(R.id.report_sexually);
                            dlg_spam = (RadioButton) dlg_report.findViewById(R.id.report_spam);
                            dlg_etc = (RadioButton) dlg_report.findViewById(R.id.report_etc);
                            dlg_text = (EditText) dlg_report.findViewById(R.id.report_text);
                            dlg_add = (ImageView) dlg_report.findViewById(R.id.report_iv);
                            dlg_img_root = (TextView) dlg_report.findViewById(R.id.report_image_root);

                            dlg_etc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (dlg_etc.isChecked()) {
                                        dlg_text.setVisibility(View.VISIBLE);
                                    } else {
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
                                    if (dlg_swear.isChecked()) {
                                        report = "욕설/비방";
                                    } else if (dlg_sexually.isChecked()) {
                                        report = "성희롱";
                                    } else if (dlg_spam.isChecked()) {
                                        report = "스팸메시지";
                                    } else if (dlg_etc.isChecked()) {
                                        report = dlg_text.getText().toString();
                                    } else {
                                        Toast.makeText(GroupMessageActivity.this, "신고 사유를 선택해주세요.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ReportModel reportModel = new ReportModel();
                                    reportModel.reporter = myUid;
                                    reportModel.reportee = comments.get(position).uid;
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
                                                    email.putExtra(Intent.EXTRA_SUBJECT, comments.get(position).uid + " 신고");
                                                    email.putExtra(Intent.EXTRA_TEXT, report + "\n" + "신고자 : " + myUid + "\n" + "============================================================" + "\n 위 내용은 수정하지 마세요.");
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
                                            .child(comments.get(position).uid)
                                            .child(myUid)
                                            .delete();
                                }
                            });
                            dlg.show();
                        }
                    }

                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        private class GroupMessageViewHolder extends RecyclerView.ViewHolder {

            public TextView textView_message;
            public TextView textView_dest_name;
            public ImageView imageView_dest_profile;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public TextView textView_user_name;
            public ImageView imageView_user_profile;
            public LinearLayout linearLayout_user;

            public GroupMessageViewHolder(View view) {
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
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            pd = new ProgressDialog(GroupMessageActivity.this);
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
                    .child(dest)
                    .child(myUid)
                    .child(formatDate)
                    .putFile(uri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        final StorageReference ref =
                                FirebaseStorage
                                        .getInstance()
                                        .getReference()
                                        .child("report")
                                        .child(dest)
                                        .child(myUid)
                                        .child(formatDate);

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri download = uri;
                                    img = String.valueOf(download);

                                    dlg_add.setVisibility(View.GONE);
                                    dlg_img_root.setVisibility(View.VISIBLE);
                                    dlg_img_root.setText(img);
                                    pd.dismiss();
                                }
                            });
                        }
                    });
        }
    }

    private class ListviewAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<UserModel> att;
        private int layout;

        public ListviewAdapter(Context context, int layout, List<UserModel> att) {
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.att = att;
            this.layout = layout;
        }

        @Override
        public int getCount() {
            return att.size();
        }

        @Override
        public Object getItem(int position) {
            return att.get(position).userName;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(layout, parent, false);
            }

            UserModel userModel = att.get(position);

            ImageView imageView = (ImageView)convertView.findViewById(R.id.friendrow_imageView);
            Glide.with(convertView)
                    .load(userModel.profileImageUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);

            TextView textView = (TextView)convertView.findViewById(R.id.friendrow_textView);
            textView.setText(userModel.userName);
            return convertView;
        }
    }
}