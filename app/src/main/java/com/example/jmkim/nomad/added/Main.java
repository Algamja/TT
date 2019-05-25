package com.example.jmkim.nomad.added;

import android.content.Intent;
import android.os.Bundle;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.Close;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Main extends AppCompatActivity {

    ViewPager pager;
    Close close;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_main);
        close = new Close(this);

        pager = findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mPagerAdapter);
        pager.setOffscreenPageLimit(4);
    }

    @Override
    public void onBackPressed() {
        close.onBackPressed();
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return MainFragment.create();
                case 1:
                    return ChatFragment.create();
                case 2:
                    return SearchFragment.create();
                default :
                    return MypageFragment.create();
            }
        }
    }

    void setCurrentItem(int i) {
        pager.setCurrentItem(i);
    }

    void showPop() {
        startActivity(new Intent(Main.this, Pop.class));
        overridePendingTransition(0, android.R.anim.fade_in);
    }
}