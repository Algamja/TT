package com.example.jmkim.nomad;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.Fragment.DB_FragmentPageAdapter;
import com.example.jmkim.nomad.Fragment.FragmentPageAdapter;
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

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private FirebaseUser user;

    private View header;
    private ImageView Nav_UserProfile;
    private TextView Nav_UserName;
    private TextView Nav_UserEmail;
    private TextView Nav_UserStateMsg;

    private LinearLayout linear_board_1;
    private LinearLayout linear_board_2;
    private LinearLayout linear_board_3;

    private ImageView board_image_2;
    private ImageView board_image_3;

    String uid;
    private int PICK_FROM_ALBUM = 10;

    String[] UserImageArray = new String[] {"기본 이미지로 설정", "앨범에서 사진 선택"};
    private Uri imageUri;
    private RequestManager mGlide;

    private Close close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true); //toolbar 사용하기 위함

        getSupportActionBar().setTitle(""); //app이름 없애기 위함

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        navigationView = (NavigationView)findViewById(R.id.main_navigation_view); //햄버거바 사용위함

        header = navigationView.getHeaderView(0);
        Nav_UserProfile = (ImageView) header.findViewById(R.id.drawerHeader_imageView);
        Nav_UserName = (TextView) header.findViewById(R.id.drawerHeader_tv_name);
        Nav_UserEmail = (TextView) header.findViewById(R.id.drawerHeader_tv_email);
        Nav_UserStateMsg = (TextView) header.findViewById(R.id.drawerHeader_tv_stateMsg); //햄버거바 상단 사용자 정보

        linear_board_1 = (LinearLayout)findViewById(R.id.scroll_linear_board_1);
        linear_board_2 = (LinearLayout)findViewById(R.id.scroll_linear_board_2);
        linear_board_3 = (LinearLayout)findViewById(R.id.scroll_linear_board_3);

        board_image_2 = (ImageView)findViewById(R.id.scroll_vp_board_2);
        board_image_3 = (ImageView)findViewById(R.id.scroll_vp_board_3);

        final List<Board> boards = new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Board")
                .child("글글")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boards.clear();

                        boards.add(dataSnapshot.getValue(Board.class));

                        mGlide.load(boards.get(0).img_1)
                                .into(board_image_2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Board")
                .child("글글글")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boards.clear();

                        boards.add(dataSnapshot.getValue(Board.class));

                        mGlide.load(boards.get(0).img_1)
                                .into(board_image_3);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        mGlide = Glide.with(this);

        //DB에서 현재 사용자 정보 가져오기 위함 시작
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        final List<UserModel> userModel = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userModel.clear();

                        userModel.add(dataSnapshot.getValue(UserModel.class));

                        mGlide.load(userModel.get(0).profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(Nav_UserProfile);

                        Nav_UserName.setText(userModel.get(0).userName + " 님 환영합니다.");
                        Nav_UserEmail.setText(userModel.get(0).userEmail);
                        Nav_UserStateMsg.setText(userModel.get(0).stateMessage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); //DB정보 끝

        Nav_UserProfile.setOnClickListener(new View.OnClickListener() { //프로필 사진 눌렸을 때 이벤트 시작
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
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

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() { //햄버거 바 시작
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_main:
                        break;

                    case R.id.navigation_item_myPage:
                        intent = new Intent(getApplicationContext(), MypageActivity.class);
                        startActivity(intent);
                        finish();
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
                Intent intent = new Intent(MainActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
                finish();
            }
        }); //햄버거바 상태메시지 클릭 종료

        ViewPager main_banner_vp = (ViewPager) findViewById(R.id.scroll_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        main_banner_vp.setAdapter(pageAdapter); //스와이프 부분 끝

        ViewPager main_board_vp = (ViewPager) findViewById(R.id.scroll_vp_board_1);
        FragmentManager dbfm = getSupportFragmentManager();
        DB_FragmentPageAdapter boardAdapter = new DB_FragmentPageAdapter(dbfm);
        main_board_vp.setAdapter(boardAdapter);

        linear_board_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this,UserActivity.class);
               startActivity(intent);
               finish();
            }
        });

        close = new Close(this);
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

    @Override
    public void onBackPressed() {
        close.onBackPressed();
    }
}