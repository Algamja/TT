package com.example.jmkim.nomad.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DB_FragmentActivity extends Fragment {

    private RequestManager mGlide;


    String tag = this.getClass().getSimpleName();
    Button btn1,btn2,btn3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mGlide = Glide.with(this);

        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.db_view_page_1,container,false);//첫번째 스와이프 화면이랑 엮어줌
        final ImageView img = layout.findViewById(R.id.db_vp_first_iv);

        final List<Board> boards = new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Board")
                .child("글")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boards.clear();

                        boards.add(dataSnapshot.getValue(Board.class));

                        mGlide.load(boards.get(0).img_1)
                                .into(img);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return layout;
    }
}
