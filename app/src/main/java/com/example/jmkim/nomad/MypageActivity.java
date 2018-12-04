package com.example.jmkim.nomad;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import static android.view.Gravity.START;

public class MypageActivity extends AppCompatActivity {

    private Menu navigation;
    private MenuItem nav_item_logout;
    private MenuItem nav_item_mypage;

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private FirebaseUser user;

    private View header;
    private ImageView Nav_UserProfile;
    private TextView Nav_UserText;
    private TextView Nav_UserEmail;
    private TextView Nav_UserStateMsg;

    int intToolbar;

    String uid;
    private int PICK_FROM_ALBUM = 10;

    String[] UserImageArray = new String[] {"기본 이미지로 설정", "앨범에서 사진 선택"};
    private Uri imageUri;

    private ImageView Mypage_UserProfile;
    private TextView Mypage_UserName;
    private TextView Mypage_UserEmail;
    private TextView Mypage_UserStateMsg;

    private RequestManager mGlide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        Intent getintent = getIntent();
        intToolbar = getintent.getIntExtra("Mypage",0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mypage_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true); //toolbar 사용하기 위함

        getSupportActionBar().setTitle(""); //app이름 없애기 위함

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
                                        startActivityForResult(intent,PICK_FROM_ALBUM);
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
                                        startActivityForResult(intent,PICK_FROM_ALBUM);
                                        break;
                                }
                            }
                        });
                dlg.setPositiveButton("닫기",null);
                dlg.show();
            }
        });

        navigation = navigationView.getMenu();

        nav_item_mypage = navigation.findItem(R.id.navigation_item_myPage);
        nav_item_logout = navigation.findItem(R.id.nav_sub_item_logout);

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
                        break;

                    case R.id.nav_sub_item_logout:
                        intent = new Intent(getApplicationContext(),SplashActivity.class);
                        startActivity(intent);
                        finish();

                        break;

                    case R.id.nav_sub_menu_item02:
                        break;
                }
                return true;
            }
        }); //햄버거바 끝
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();

            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("userImages")
                    .child(uid)
                    .putFile(imageUri)
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
}