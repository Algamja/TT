package com.example.jmkim.nomad.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class DB_FragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3; //스와이프 화면 개수

    public DB_FragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DB_FragmentActivity db_myFragment = new DB_FragmentActivity();
                Bundle data = new Bundle();
                data.putInt("current_page", position + 1);
                db_myFragment.setArguments(data);
                return db_myFragment; //스와이프 첫 화면

            case 1:
                return new DB_FirstFragment(); //스와이프 두번째 화면

            case 2:
                return new DB_SecondFragment(); //스와이프 세번째 화면

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
