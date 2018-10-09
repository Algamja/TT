//메인페이지 중간 스와이프(광고) 넣기 위한 Adapter
package com.example.jmkim.nomad;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentPageAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3; //스와이프 화면 개수

    public FragmentPageAdapter(FragmentManager fm){
        super (fm);
    }

    @Override
    public Fragment getItem(int position) {

       switch (position) {
           case 0:
               FragmentActivity myFragment = new FragmentActivity();
               Bundle data = new Bundle();
               data.putInt("current_page", position + 1);
               myFragment.setArguments(data);
               return myFragment; //스와이프 첫 화면

           case 1:
               return new FirstFragment(); //스와이프 두번째 화면

           case 2:
               return new SecondFragment(); //스와이프 세번째 화면

           default:
               return null;
       }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
