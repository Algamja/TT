package com.example.jmkim.nomad;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import static android.view.Gravity.START;

public class MypageActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private FirebaseUser user;

    private View header;
    private ImageView Nav_UserProfile;
    private TextView Nav_UserText;
    private TextView Nav_UserEmail;
    private TextView Nav_UserStateMsg;


    String uid;
    private int PICK_FROM_ALBUM_PROFILE = 10;
    private int PICK_FROM_ALBUM_BOARD = 11;

    String[] UserImageArray = new String[] {"기본 이미지로 설정", "앨범에서 사진 선택"};
    private Uri profileImageUri;
    private Uri boardImageUri;

    private ImageView Mypage_UserProfile;
    private TextView Mypage_UserName;
    private TextView Mypage_UserEmail;
    private TextView Mypage_UserStateMsg;

    private EditText et;
    private Button btn;
    private Button end;

    String et_title;

    private RequestManager mGlide;

    private Close close;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        final Intent getintent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.mypage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true); //toolbar 사용하기 위함

        getSupportActionBar().setTitle("마이 페이지"); //app이름

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mypage_drawer_layout);

        navigationView = (NavigationView)findViewById(R.id.mypage_navigation_view); //햄버거바 사용위함

        header = navigationView.getHeaderView(0);
        Nav_UserProfile = (ImageView) header.findViewById(R.id.drawerHeader_imageView);
        Nav_UserText = (TextView) header.findViewById(R.id.drawerHeader_tv_name);
        Nav_UserEmail = (TextView) header.findViewById(R.id.drawerHeader_tv_email);
        Nav_UserStateMsg = (TextView) header.findViewById(R.id.drawerHeader_tv_stateMsg);

        Mypage_UserProfile = (ImageView)findViewById(R.id.mypage_iv_profile);
        Mypage_UserName = (TextView)findViewById(R.id.mypage_tv_name);
        Mypage_UserEmail = (TextView)findViewById(R.id.mypage_tv_email);
        Mypage_UserStateMsg = (TextView)findViewById(R.id.mypage_tv_stateMsg);

        /*et = (EditText)findViewById(R.id.mypage_et);
        btn = (Button)findViewById(R.id.mypage_btn);
        end = (Button)findViewById(R.id.mypage_btn_end);*/

        mGlide = Glide.with(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

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
                                .apply(new RequestOptions().circleCrop())
                                .into(Nav_UserProfile);
                        Nav_UserText.setText(userModel.get(0).userName + " 님 환영합니다.");
                        Nav_UserEmail.setText(userModel.get(0).userEmail);
                        Nav_UserStateMsg.setText(userModel.get(0).stateMessage);//햄버거바 내용

                        mGlide.load(userModel.get(0).profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(Mypage_UserProfile);
                        Mypage_UserName.setText(userModel.get(0).userName);
                        Mypage_UserEmail.setText(userModel.get(0).userEmail);
                        Mypage_UserStateMsg.setText(userModel.get(0).stateMessage); //마이페이지 상단
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        Nav_UserProfile.setOnClickListener(new View.OnClickListener() { //프로필 사진 눌렸을 때 이벤트 시작
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
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
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

        Mypage_UserProfile.setOnClickListener(new View.OnClickListener() {
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
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_main:
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.navigation_item_myPage:
                        break;

                    case R.id.navigation_item_infoEdit:
                        intent = new Intent(getApplicationContext(),UserInfoEditActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.nav_sub_item_logout:
                        intent = new Intent(getApplicationContext(),SplashActivity.class);
                        startActivity(intent);
                        finish();

                        break;

                }
                return true;
            }
        }); //햄버거바 끝

        Nav_UserStateMsg.setOnClickListener(new View.OnClickListener() { //햄버거바 내의 상태메시지 클릭 이벤트
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
                finish();
            }
        }); //햄버거바 상태메시지 클릭 종료

        Mypage_UserStateMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
                finish();
            }
        });

        close = new Close(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //toolbar 버튼 눌렸을 때
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

        if (requestCode == PICK_FROM_ALBUM_BOARD && resultCode == RESULT_OK) {

            Random random = new Random();

            final int i_random = random.nextInt(1000);

            et_title = et.getText().toString();

            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("boardImages")
                    .child(et_title)
                    .child(String.valueOf(i_random))
                    .putFile(data.getData())
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        StorageReference boardImageRef = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("boardImages")
                                .child(et_title)
                                .child(String.valueOf(i_random));

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            Task<Uri> uriTask = boardImageRef.getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();
                            String imageUrl = String.valueOf(downloadUrl);

                            Board board = new Board();
                            board.title = et_title;
                            board.writer = uid;
                            board.img_1 = imageUrl;

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Board")
                                    .child(et_title)
                                    .setValue(board);
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        close.onBackPressed();
    }
}