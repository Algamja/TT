package com.example.jmkim.nomad.added;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.Message.ChatActivity;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.ReportReadActivity;
import com.example.jmkim.nomad.prev.UserInfoEditActivity;
import com.example.jmkim.nomad.prev.WriterActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class MypageFragment extends Fragment {

    private FirebaseUser user;

    private ImageView userProfile;
    private TextView userName;
    private TextView userEmail;
    private TextView userStateMsg;

    private ImageView infoEdit;
    private LinearLayout my_board;
    private LinearLayout my_report;

    String uid;
    private int PICK_FROM_ALBUM_PROFILE = 10;

    String[] UserImageArray = new String[]{"기본 이미지로 설정", "앨범에서 사진 선택"};
    private Uri profileImageUri;

    private RequestManager mGlide;

    public static MypageFragment create() {
        MypageFragment fragment = new MypageFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.added_frag3, container, false);

        userProfile = rootView.findViewById(R.id.mypage_iv_back_profile);
        userName = rootView.findViewById(R.id.mypage_tv_name);
        userEmail = rootView.findViewById(R.id.mypage_tv_email);
        userStateMsg = rootView.findViewById(R.id.mypage_tv_stateMsg);

        infoEdit = rootView.findViewById(R.id.mypage_iv_infoedit);
        my_board = rootView.findViewById(R.id.mypage_ll_myboard);
        my_report = rootView.findViewById(R.id.mypage_ll_myhistory);

        mGlide = Glide.with(getContext());

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        final List<UserModel> userModel = new ArrayList<>(); //DB에서 읽어올 준비
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //읽어오기 시작
                        userModel.clear();

                        userModel.add(dataSnapshot.getValue(UserModel.class));

                        mGlide.load(userModel.get(0).profileImageUrl)
                                .into(userProfile);
                        userName.setText(userModel.get(0).userName);
                        userEmail.setText(userModel.get(0).userEmail);
                        userStateMsg.setText(userModel.get(0).stateMessage); //마이페이지 상단
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        userProfile.setOnClickListener(new View.OnClickListener() { //프로필사진 눌렸을 때
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setTitle("프로필 사진 수정");
                dlg.setItems(UserImageArray,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0: //기본 이미지로 설정
                                        final List<UserModel> userModel = new ArrayList<>();
                                        FirebaseDatabase.getInstance().getReference()
                                                .child("UserBasic")
                                                .child(uid)
                                                .child("profileImageUrl")
                                                .setValue("android.resource://com.example.jmkim.nomad/drawable/profile");
                                        break;

                                    case 1: //앨범에서 선택하기
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                        startActivityForResult(intent, PICK_FROM_ALBUM_PROFILE);
                                        break;
                                }
                            }
                        });
                dlg.setPositiveButton("닫기", null);
                dlg.show();
            }
        });

        userStateMsg.setOnClickListener(new View.OnClickListener() { //상태메시지 눌렸을 때
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInfoEditActivity.class);
                startActivity(intent);
            }
        });

        infoEdit.setOnClickListener(new View.OnClickListener() { //정보수정 버튼 눌렀을 때
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UserInfoEditActivity.class));
            }
        });

        my_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BoardsActivity.class);
                intent.putExtra("publisher",uid);
                startActivity(intent);
            }
        });

        my_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), ReportReadActivity.class);
                intent.putExtra("publisher",uid);
                startActivity(intent);
            }
        });

        return rootView;
    }
}