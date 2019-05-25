package com.example.jmkim.nomad.added;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.ChatPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class ChatFragment extends Fragment {
    private TabLayout mTabLayout;

    private ViewPager mViewPager;
    private ChatPagerAdapter mChatPagerAdater;

    public static ChatFragment create() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        //args.putInt("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //image = getArguments().getInt("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.added_frag1, container, false);

        mTabLayout = (TabLayout)rootView.findViewById(R.id.chat_tab);
        mTabLayout.addTab(mTabLayout.newTab().setText("일반채팅"));
        mTabLayout.addTab(mTabLayout.newTab().setText("그룹채팅"));

        mViewPager = (ViewPager)rootView.findViewById(R.id.chat_vp);
        mChatPagerAdater = new ChatPagerAdapter(getFragmentManager(), mTabLayout.getTabCount());
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

        return rootView;
    }
}