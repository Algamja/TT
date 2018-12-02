package com.example.jmkim.nomad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
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

    Menu navigation;
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

        ViewPager main_banner_vp = (ViewPager) findViewById(R.id.scroll_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        main_banner_vp.setAdapter(pageAdapter); //스와이프 부분 끝

        ViewPager main_board_vp = (ViewPager) findViewById(R.id.scroll_vp_board);
        FragmentPageAdapter boardAdapter = new FragmentPageAdapter(fm);
        main_board_vp.setAdapter(boardAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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