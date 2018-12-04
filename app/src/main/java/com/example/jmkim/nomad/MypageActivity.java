package com.example.jmkim.nomad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.START;

public class MypageActivity extends AppCompatActivity {

    Intent getintent;

    Menu navigation;
    MenuItem nav_item_logout,nav_item_mypage;

    NavigationView navigationView;

    int intToolbar;

    private DrawerLayout mDrawerLayout;


    View header;
    ImageView Nav_UserProfile;
    TextView Nav_UserText;
    FirebaseUser user;

    String uid;

    ImageView Mypage_UserProfile;
    TextView Mypage_UserName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        getintent = getIntent();
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
        Nav_UserText = (TextView) header.findViewById(R.id.drawerHeader_textView);

        Mypage_UserProfile = (ImageView)findViewById(R.id.mypage_iv_profile);
        Mypage_UserName = (TextView)findViewById(R.id.mypage_tv_name);

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

                        Glide.with(header)
                                .load(userModel.get(0).profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(Nav_UserProfile);
                        Nav_UserText.setText(userModel.get(0).userName + " 님 환영합니다.");

                        Glide.with(MypageActivity.this)
                                .load(userModel.get(0).profileImageUrl)
                                .apply(new RequestOptions().circleCrop())
                                .into(Mypage_UserProfile);
                        Mypage_UserName.setText(userModel.get(0).userName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        navigation = navigationView.getMenu();

        nav_item_mypage = navigation.findItem(R.id.navigation_item_mypage);
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

                    case R.id.navigation_item_mypage:
                        break;

                    case R.id.navigation_item_location:
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
}