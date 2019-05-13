package com.example.jmkim.nomad.prev;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.Message.ChatActivity;
import com.example.jmkim.nomad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MypageActivity extends AppCompatActivity {

    private FirebaseUser user;

    private ImageView userProfile;
    private TextView userName;
    private TextView userEmail;
    private TextView userStateMsg;

    private ImageView myTalk;
    private ImageView infoEdit;

    private BottomNavigationView Mypage_bottomNavigationView;

    private View black;

    private FrameLayout fb_layout;
    private FloatingActionButton write_plan;
    private FloatingActionButton write_review;

    private Boolean bottom_click = false;

    String uid;
    private int PICK_FROM_ALBUM_PROFILE = 10;

    String[] UserImageArray = new String[] {"기본 이미지로 설정", "앨범에서 사진 선택"};
    private Uri profileImageUri;

    private RequestManager mGlide;

    private Close close;

    public static Activity Mypage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        final Intent getintent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.mypage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setTitle(""); //app이름

        userProfile = (ImageView)findViewById(R.id.mypage_iv_back_profile);
        userName = (TextView)findViewById(R.id.mypage_tv_name);
        userEmail = (TextView)findViewById(R.id.mypage_tv_email);
        userStateMsg = (TextView)findViewById(R.id.mypage_tv_stateMsg);

        myTalk = (ImageView)findViewById(R.id.mypage_iv_mytalk);
        infoEdit = (ImageView)findViewById(R.id.mypage_iv_infoedit);

        black = (View)findViewById(R.id.mypage_view_black);

        fb_layout = (FrameLayout)findViewById(R.id.mypage_ll_write_float);
        write_plan = (FloatingActionButton)findViewById(R.id.mypage_fb_write_plan);
        write_review = (FloatingActionButton)findViewById(R.id.mypage_fb_write_review);

        Mypage_bottomNavigationView = (BottomNavigationView)findViewById(R.id.mypage_bottomNavigation);
        Mypage_bottomNavigationView.setOnNavigationItemSelectedListener(mypage_navigationItemSelectedListener);

        Mypage_bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        Mypage = MypageActivity.this;

        mGlide = Glide.with(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        black.setMinimumHeight(size.y);

        final List<UserModel> userModel = new ArrayList<>(); //DB에서 읽어올 준비
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

                        mGlide.load(userModel.get(0).profileImageUrl)
                                .into(userProfile);
                        userName.setText(userModel.get(0).userName);
                        userEmail.setText(userModel.get(0).userEmail);
                        userStateMsg.setText(userModel.get(0).stateMessage); //마이페이지 상단
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        userProfile.setOnClickListener(new View.OnClickListener() { //프로필사진 눌렸을 때
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MypageActivity.this);
                dlg.setTitle("프로필 사진 수정");
                dlg.setItems(UserImageArray,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0: //기본 이미지로 설정
                                        final List<UserModel> userModel = new ArrayList<>();
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("UserBasic")
                                                .child(uid)
                                                .child("profileImageUrl")
                                                .setValue("android.resource://com.example.jmkim.nomad/drawable/profile");
                                        break;

                                    case 1: //앨범에서 선택하기
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                        startActivityForResult(intent, PICK_FROM_ALBUM_PROFILE);
                                        break;
                                }
                            }
                        });
                dlg.setPositiveButton("닫기",null);
                dlg.show();
            }
        });

        userStateMsg.setOnClickListener(new View.OnClickListener() { //상태메시지 눌렸을 때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MypageActivity.this, ChatActivity.class));
            }
        });

        infoEdit.setOnClickListener(new View.OnClickListener() { //정보수정 버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MypageActivity.this, UserInfoEditActivity.class));
            }
        });

        black.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Mypage_bottomNavigationView.setSelectedItemId(R.id.nav_profile);
                fb_layout.setVisibility(View.INVISIBLE);
                bottom_click = false;
                black.setVisibility(View.GONE);
                return false;
            }
        });

        if(!bottom_click){
            Mypage_bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }

        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        write_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MypageActivity.this, WritePlanActivity.class));
            }
        });

        close = new Close(this); //뒤로가기 누르면 종료
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mypage_navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){

                        case R.id.nav_home:
                            startActivity(new Intent(MypageActivity.this, MainActivity.class));
                            finish();
                            break;

                        case R.id.nav_search:
                            startActivity(new Intent(MypageActivity.this, SearchActivity.class));
                            finish();
                            break;

                        case R.id.nav_add:
                            if(bottom_click){
                                fb_layout.setVisibility(View.GONE);
                                bottom_click = false;
                                black.setVisibility(View.GONE);
                            }else {
                                fb_layout.setVisibility(View.VISIBLE);
                                bottom_click = true;
                                black.setVisibility(View.VISIBLE);
                                black.bringToFront();
                            }
                            break;

                        case R.id.nav_profile:
                            break;
                    }

                    return true;
                }
            };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == PICK_FROM_ALBUM_PROFILE && resultCode == RESULT_OK) {
            profileImageUri = data.getData();

            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("userImages")
                    .child(uid)
                    .putFile(profileImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        final StorageReference profileImageRef = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("userImages")
                                .child(uid);

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Task<Uri> uriTask = profileImageRef.getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();
                            String imageUrl = String.valueOf(downloadUrl);

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("UserBasic")
                                    .child(uid)
                                    .child("profileImageUrl")
                                    .setValue(imageUrl);
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        close.onBackPressed();
    }
}