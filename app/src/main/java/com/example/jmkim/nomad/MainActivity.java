package com.example.jmkim.nomad;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String tag = this.getClass().getSimpleName();

    TabHost tabHost;
    LocalActivityManager mLocalActivityManager;
    Button btn_sign, btn_login, btn_logout, btn_mypage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}