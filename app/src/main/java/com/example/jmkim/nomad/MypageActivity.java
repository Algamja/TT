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
import android.view.MenuInflater;
import android.view.MenuItem;

public class MypageActivity extends AppCompatActivity {

    Intent getintent;

    Menu navigation;
    MenuItem nav_item_logout,nav_item_mypage;

    NavigationView navigationView;

    int intToolbar;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

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
                        intent.putExtra("LOG",1);
                        startActivity(intent);
                        finish();
                        break;

                    case R.id.navigation_item_mypage:
                        break;

                    case R.id.navigation_item_location:
                        break;

                    case R.id.nav_sub_item_logout:
                        intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.putExtra("LOG",-1);
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
    public boolean onCreateOptionsMenu(Menu menu) { //toolbar구성
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //toolbar 버튼 눌렸을 때

        return true;
    }
}