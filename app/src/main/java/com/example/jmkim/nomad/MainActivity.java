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

public class MainActivity extends AppCompatActivity {

    String tag = this.getClass().getSimpleName();

    int Log_val;

    Menu mMenu;
    MenuItem item_sign_up,item_login,item_log_out,item_mypage;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true); //toolbar 사용하기 위함

        getSupportActionBar().setTitle(""); //app이름 없애기 위함

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view); //햄버거바 사용위함
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_attachment:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.navigation_item_images:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.navigation_item_location:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_sub_menu_item01:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;

                    case R.id.nav_sub_menu_item02:
                        Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        }); //햄버거바 끝

        ViewPager pager = (ViewPager) findViewById(R.id.main_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        pager.setAdapter(pageAdapter); //스와이프 부분 끝

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        item_sign_up = mMenu.findItem(R.id.item_sign_up);
        item_login = mMenu.findItem(R.id.item_login);
        item_log_out = mMenu.findItem(R.id.item_logout);
        item_mypage = mMenu.findItem(R.id.item_mypage);

        if (resultCode == RESULT_OK) {
            Log_val = data.getIntExtra("LOG", 0); //로그인 버튼 눌렸을 때 return값 받음
            if (Log_val == 1) {
                item_sign_up.setVisible(false);
                item_login.setVisible(false);
                item_mypage.setVisible(true);
                item_log_out.setVisible(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //toolbar구성
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
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
                startActivity(intent);
                break;
            case R.id.item_logout:
                mMenu.setGroupVisible(R.id.at_login,false);
                mMenu.setGroupVisible(R.id.at_logout,true);
                break;
        }
        return true;
    }
}