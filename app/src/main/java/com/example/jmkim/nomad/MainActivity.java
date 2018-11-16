package com.example.jmkim.nomad;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Resources;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String tag = this.getClass().getSimpleName();

    TabHost tabHost;
    LocalActivityManager mLocalActivityManager;
    Button btn_sign, btn_login, btn_logout, btn_mypage;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
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
        });

        ViewPager pager = (ViewPager) findViewById(R.id.main_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        pager.setAdapter(pageAdapter); //스와이프 부분 끝

        initTabs(savedInstanceState);

        btn_sign = (Button) findViewById(R.id.main_btn_sign);
        btn_login = (Button) findViewById(R.id.main_btn_login);
        btn_logout = (Button) findViewById(R.id.main_btn_logout); //main 로그인, 회원가입 버튼
        btn_mypage = (Button) findViewById(R.id.main_btn_mypage);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        }); //sign버튼 눌렸을 때 intent

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivityForResult(intent, 0);
            }
        }); //login버튼 눌렸을 때 intent

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_logout.setVisibility(View.GONE);
                btn_login.setVisibility(View.VISIBLE);
            }
        });

        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MypageActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        mLocalActivityManager.dispatchResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mLocalActivityManager.dispatchPause(isFinishing());
        super.onPause();
    }

    private void initTabs(Bundle savedInstanceState) { //Tab사용하기 위함
        Resources res = getResources();
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec spec;
        spec = tabHost.newTabSpec("Join").setIndicator("파티")
                .setContent(R.id.main_tab_join);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Review").setIndicator("후기")
                .setContent(R.id.main_tab_review);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Free").setIndicator("자유")
                .setContent(R.id.main_tab_free);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int Log_val = data.getIntExtra("LOG", 0);
            if (Log_val == 1) {
                btn_logout.setVisibility(View.VISIBLE);
                btn_login.setVisibility(View.GONE);
                btn_sign.setVisibility(View.GONE);
                btn_mypage.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}