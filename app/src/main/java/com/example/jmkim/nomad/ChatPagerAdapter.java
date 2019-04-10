package com.example.jmkim.nomad;

import com.example.jmkim.nomad.Message.GroupChatFragment;
import com.example.jmkim.nomad.Message.ChatFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ChatPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public ChatPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;

            case 1:
                GroupChatFragment groupChatFragment = new GroupChatFragment();
                return groupChatFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
