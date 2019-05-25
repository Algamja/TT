package com.example.jmkim.nomad.added;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.DB.Review;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MainFragment extends Fragment {

    private Context mContext;
    private CircleAnimIndicator circleAnimIndicator;
    private int imsi=0;
    private List<Board> boards;

    public static MainFragment create() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.added_frag0, container, false);
        mContext = getContext();

        ViewPager slide = rootView.findViewById(R.id.slide);
        PagerAdapter adapter = new PagerAdapter(((Main) mContext).getSupportFragmentManager());
        slide.setAdapter(adapter);
        slide.addOnPageChangeListener(mOnPageChangeListener);

        circleAnimIndicator = rootView.findViewById(R.id.circleAnimIndicator);
        circleAnimIndicator.setItemMargin(15);
        circleAnimIndicator.setAnimDuration(300);
        circleAnimIndicator.createDotPanel(4, R.drawable.indicator_non , R.drawable.indicator_on);

        boards = new ArrayList<>();

        LinearLayout box = rootView.findViewById(R.id.box);

        List<String> user_key = new ArrayList<>();
        List<UserModel> users= new ArrayList<>();

        List<String> plan_keys = new ArrayList<>();
        List<Plan> plans = new ArrayList<>();

        List<String> review_keys = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user_key.clear();
                        users.clear();

                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            user_key.add(item.getKey());
                            users.add(item.getValue(UserModel.class));
                        }

                        for(int i=0;i<user_key.size();i++){
                            int I = i;
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("Plan")
                                    .child(user_key.get(i))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            plans.clear();
                                            plan_keys.clear();

                                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                               if(item.getValue(Plan.class).open) {
                                                   plans.add(item.getValue(Plan.class));
                                                   plan_keys.add(item.getKey());
                                               }
                                            }
                                            for(int k=0;k<plans.size();k++){
                                                ItemMain itemMain = new ItemMain(getContext(), plans.get(k),users.get(I),plan_keys.get(k));
                                                box.addView(itemMain);
                                                Log.e("PLAN_",plans.get(k).country);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }

                        for(int i=0;i<user_key.size();i++){
                            int I = i;
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("WriteReview")
                                    .child(user_key.get(i))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            reviews.clear();
                                            review_keys.clear();

                                            for(DataSnapshot item : dataSnapshot.getChildren()){
                                                reviews.add(item.getValue(com.example.jmkim.nomad.DB.Review.class));
                                                review_keys.add(item.getKey());
                                            }

                                            for(int k=0;k<reviews.size();k++){
                                                ItemMain itemMain = new ItemMain(getContext(), reviews.get(k),users.get(I),review_keys.get(k));
                                                box.addView(itemMain);
                                                Log.e("PLAN_",reviews.get(k).city);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return rootView;
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
            return FragSlide.create(position);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            circleAnimIndicator.selectDot(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
}