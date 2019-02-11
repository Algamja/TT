package com.example.jmkim.nomad;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

public class UserInfoEditActivity extends AppCompatActivity {

    private FirebaseUser user;

    String uid;
    private int PICK_FROM_ALBUM = 10;

    String[] UserImageArray = new String[] {"기본 이미지로 설정", "앨범에서 사진 선택"};
    private Uri imageUri;

    private ImageView InfoEdit_UserProfile;
    private TextView InfoEdit_UserName;
    private TextView InfoEdit_UserEmail;
    private TextView InfoEdit_UserStateMsg;

    private LinearLayout linear_stateMsg;
    private LinearLayout linear_editBasic;
    private LinearLayout linear_editParty;
    private LinearLayout linear_editActivity;
    private LinearLayout linear_logout;
    private LinearLayout linear_leave;

    private TextView tv_stateMsg;

    private View dlg_stateMsg;

    private RequestManager mGlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.editInfo_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();//toolbar 사용하기 위함

        getSupportActionBar().setTitle("정보 수정"); //app이름

        InfoEdit_UserProfile = (ImageView)findViewById(R.id.editInfo_iv_profile);
        InfoEdit_UserName = (TextView)findViewById(R.id.editInfo_tv_name);
        InfoEdit_UserEmail = (TextView)findViewById(R.id.editInfo_tv_email);
        InfoEdit_UserStateMsg = (TextView)findViewById(R.id.editInfo_tv_stateMsg);

        linear_stateMsg = (LinearLayout)findViewById(R.id.editInfo_linearLayout_stateMsg);
        linear_editBasic = (LinearLayout)findViewById(R.id.editInfo_linearLayout_basicEdit);
        linear_editParty = (LinearLayout)findViewById(R.id.editInfo_linearLayout_partyEdit);
        linear_editActivity = (LinearLayout)findViewById(R.id.editInfo_linearLayout_activityEdit);
        linear_logout = (LinearLayout)findViewById(R.id.editInfo_linearLayout_logout);
        linear_leave = (LinearLayout)findViewById(R.id.editInfo_linearLayout_leave);

        tv_stateMsg = (TextView)findViewById(R.id.editInfo_tv_linear_stateMsg);

        mGlide = Glide.with(this);

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
                                .apply(new RequestOptions().circleCrop())
                                .into(InfoEdit_UserProfile);
                        InfoEdit_UserName.setText(userModel.get(0).userName);
                        InfoEdit_UserEmail.setText(userModel.get(0).userEmail);
                        InfoEdit_UserStateMsg.setText(userModel.get(0).stateMessage); //정보수정페이지 상단

                        tv_stateMsg.setText(userModel.get(0).stateMessage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); //DB에서 읽어오기 종료

        InfoEdit_UserProfile.setOnClickListener(new View.OnClickListener() { //상단 프로필 사진 눌렸을 때 이벤트 시작
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(UserInfoEditActivity.this);
                dlg.setTitle("프로필 사진 수정");
                dlg.setItems(UserImageArray,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which){
                                    case 0: //기본 이미지로 설정
                                        final List<UserModel> userModel = new ArrayList<>();
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("UserBasic")
                                                .child(uid)
                                                .child("profileImageUrl")
                                                .setValue("android.resource://com.example.jmkim.nomad/drawable/profile");
                                        break;

                                    case 1: //앨범에서 선택하기
                                        Intent intent = new Intent(Intent.ACTION_PICK);
                                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                                        startActivityForResult(intent,PICK_FROM_ALBUM);
                                        break;
                                }
                            }
                        });
                dlg.setPositiveButton("닫기",null);
                dlg.show();
            }
        }); //상단 프로필 선택 종료

        linear_stateMsg.setOnClickListener(new View.OnClickListener() { //상태메시지 레이아웃 클릭 이벤트
            @Override
            public void onClick(View v) {
                dlg_stateMsg = (View) View.inflate(UserInfoEditActivity.this,R.layout.statemsg_dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(UserInfoEditActivity.this);
                dlg.setTitle("상태메시지 수정");
                dlg.setView(dlg_stateMsg);
                dlg.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlg_et_stateMsg = (EditText)dlg_stateMsg.findViewById(R.id.stateMsg_et);

                        final List<UserModel> userModel = new ArrayList<>();
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("UserBasic")
                                .child(uid)
                                .child("stateMessage")
                                .setValue(dlg_et_stateMsg.getText().toString());
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }
        }); //상메 변경 끝


        linear_editBasic.setOnClickListener(new View.OnClickListener() { //기본정보 변경 시작
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoEditActivity.this, BasicEditActivity.class));
            }
        }); //기본정보 변경 끝

        linear_editParty.setOnClickListener(new View.OnClickListener() { //파티원 변경 시작
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoEditActivity.this, PartyEditActivity.class));
            }
        }); //파티원 변경 끝

        linear_editActivity.setOnClickListener(new View.OnClickListener() { //액티비티 변경 시작
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoEditActivity.this, ActivityEditActivity.class));
            }
        }); //액티비티 변경 끝

        linear_logout.setOnClickListener(new View.OnClickListener() { //로그아웃 시작
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoEditActivity.this, SplashActivity.class));
                finish();

                MypageActivity mypageActivity = (MypageActivity)MypageActivity.Mypage;
                mypageActivity.finish(); //마이페이지 종료

                FirebaseAuth.getInstance().signOut();
            }
        }); //로그아웃 끝

        linear_leave.setOnClickListener(new View.OnClickListener() { //탈퇴 시작
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(UserInfoEditActivity.this);
                dlg.setTitle("회원 탈퇴");
                dlg.setMessage("정말 탈퇴하시겠습니까?");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        finish();

                                        MypageActivity mypageActivity = (MypageActivity)MypageActivity.Mypage;
                                        mypageActivity.finish();

                                        FirebaseAuth.getInstance().signOut();

                                        FirebaseDatabase.getInstance().getReference().child("UserParty").child(uid).removeValue();
                                        FirebaseDatabase.getInstance().getReference().child("UserBasic").child(uid).removeValue();
                                        FirebaseDatabase.getInstance().getReference().child("UserActivity").child(uid).removeValue();

                                        ActivityCompat.finishAffinity(UserInfoEditActivity.this);
                                        System.runFinalizersOnExit(true);
                                        System.exit(0);
                                    }
                                });
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();
            }
        }); //탈퇴 끝
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            imageUri = data.getData();

            FirebaseStorage
                    .getInstance()
                    .getReference()
                    .child("userImages")
                    .child(uid)
                    .putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                        final StorageReference profileImageRef = FirebaseStorage
                                .getInstance()
                                .getReference()
                                .child("userImages")
                                .child(uid);

                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Task<Uri> uriTask = profileImageRef.getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadUrl = uriTask.getResult();
                            String imageUrl = String.valueOf(downloadUrl);

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("UserBasic")
                                    .child(uid)
                                    .child("profileImageUrl")
                                    .setValue(imageUrl);
                        }
                    });
        }
    }
}
