package com.example.jmkim.nomad.added;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.MainAdapter;
import com.example.jmkim.nomad.prev.MainBoardInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Fragment0 extends Fragment {

    private Context mContext;
    private CircleAnimIndicator circleAnimIndicator;
    private int imsi=0;
    private List<Board> boards;

    public static Fragment0 create() {
        Fragment0 fragment = new Fragment0();
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

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Board");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boards.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Board board = snapshot.getValue(Board.class);
                    boards.add(board);

                    for(;imsi<boards.size();imsi++){
                        String publisher;
                        final List<UserModel> userModels = new ArrayList<>();

                        publisher = boards.get(imsi).publisher;

                        final int final_i = imsi;

                        FirebaseDatabase.getInstance().getReference()
                                .child("UserBasic")
                                .child(publisher)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                        userModels.clear();

                                        userModels.add(dataSnapshot2.getValue(UserModel.class));

                                        ItemMain item = new ItemMain(getContext(), final_i, board, userModels.get(0));
                                        box.addView(item);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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