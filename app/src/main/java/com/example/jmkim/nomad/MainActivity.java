package com.example.jmkim.nomad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    String tag = this.getClass().getSimpleName();

    Menu mMenu,navigation;
    MenuItem item_sign_up,item_login,item_mypage;
    MenuItem nav_item_mypage,nav_item_logout;

    NavigationView navigationView;

    private DrawerLayout mDrawerLayout;

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

        navigation = navigationView.getMenu();
        nav_item_mypage = navigation.findItem(R.id.navigation_item_mypage);
        nav_item_logout = navigation.findItem(R.id.nav_sub_item_logout); //item 들을 개별적으로 사용하기 위함


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_main:
                        break;

                    case R.id.navigation_item_mypage:
                        intent = new Intent(getApplicationContext(), MypageActivity.class);
                        intent.putExtra("Mypage",1);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.navigation_item_location:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_sub_item_logout:
                        mMenu.setGroupVisible(R.id.at_login,false);
                        mMenu.setGroupVisible(R.id.at_logout,true);

                        nav_item_mypage.setVisible(false);
                        nav_item_logout.setVisible(false);
                        break;

                    case R.id.nav_sub_menu_item02:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        }); //햄버거바 끝

        ViewPager pager = (ViewPager) findViewById(R.id.scroll_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        pager.setAdapter(pageAdapter); //스와이프 부분 끝

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        item_sign_up = mMenu.findItem(R.id.item_sign_up);
        item_login = mMenu.findItem(R.id.item_login);
        item_mypage = mMenu.findItem(R.id.item_mypage);

        if (resultCode == RESULT_OK) {
            int Log_val = data.getIntExtra("LOG", 0); //로그인 버튼 눌렸을 때 return값 받음

            if (Log_val == 1) {
                item_sign_up.setVisible(false);
                item_login.setVisible(false);
                item_mypage.setVisible(true);

                nav_item_mypage.setVisible(true);
                nav_item_logout.setVisible(true); //햄버거바 로그아웃 구현
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //toolbar구성
        mMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        Intent intent = getIntent();
        int intToolbar = intent.getIntExtra("LOG",0);

        if(intToolbar == -1){
            mMenu.setGroupVisible(R.id.at_login,false);
            mMenu.setGroupVisible(R.id.at_logout,true);
            mMenu.setGroupVisible(R.id.at_mypage,false);
        }
        else if(intToolbar == 1){
            mMenu.setGroupVisible(R.id.at_login,true);
            mMenu.setGroupVisible(R.id.at_logout,false);
            mMenu.setGroupVisible(R.id.at_mypage,false);

            nav_item_mypage.setVisible(true);
            nav_item_logout.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //toolbar 버튼 눌렸을 때
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.item_sign_up:
                intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
                break;
            case R.id.item_login:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.item_mypage:
                intent = new Intent(getApplicationContext(), MypageActivity.class);
                intent.putExtra("Mypage",1);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}