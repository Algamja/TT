package com.example.jmkim.nomad;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class MainActivity extends Activity {

    TabHost tabHost;
    LocalActivityManager mLocalActivityManager;
    Button btn_sign, btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTabs(savedInstanceState);

        btn_sign=(Button)findViewById(R.id.main_btn_sign);
        btn_login=(Button)findViewById(R.id.main_btn_login);

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
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

    private void initTabs(Bundle savedInstanceState){ //Tab사용하기 위함
        Resources res=getResources();
        tabHost=(TabHost)findViewById(android.R.id.tabhost);
        mLocalActivityManager=new LocalActivityManager(this,false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec spec;
        spec=tabHost.newTabSpec("Join").setIndicator("파티")
                .setContent(R.id.main_tab_join);
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("Review").setIndicator("후기")
                .setContent(R.id.main_tab_review);
        tabHost.addTab(spec);

        spec=tabHost.newTabSpec("Free").setIndicator("자유")
                .setContent(R.id.main_tab_free);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}
