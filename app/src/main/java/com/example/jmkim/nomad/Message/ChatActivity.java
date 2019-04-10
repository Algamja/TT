package com.example.jmkim.nomad.Message;

import android.os.Bundle;

import com.example.jmkim.nomad.ChatPagerAdapter;
import com.example.jmkim.nomad.R;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class ChatActivity extends AppCompatActivity {
    private TabLayout mTabLayout;

    private ViewPager mViewPager;
    private ChatPagerAdapter mChatPagerAdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //상위 탭
        mTabLayout = (TabLayout)findViewById(R.id.chat_tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("일반채팅"));
        mTabLayout.addTab(mTabLayout.newTab().setText("그룹채팅"));

        mViewPager = (ViewPager)findViewById(R.id.chat_vp);
        mChatPagerAdater = new ChatPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mChatPagerAdater);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
