package com.example.jmkim.nomad.added;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.Close;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
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
                    return Fragment0.create();
                case 1:
                    return Fragment1.create();
                case 2:
                    return Fragment2.create();
                default :
                    return Fragment3.create();
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