package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SigninActivity extends Activity{

    private static final int PICK_FROM_ALBUM = 10;

    TextView check_pw;
    EditText id, pw, rigth_pw, name, phone;
    Button btn_next,btn_check;
    ImageView profile;
    Uri imageUri;

    int checking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        profile = (ImageView)findViewById(R.id.signin_image_profile);
        id = (EditText)findViewById(R.id.signin_et_id);
        btn_check = (Button)findViewById(R.id.signin_btn_check);
        pw = (EditText)findViewById(R.id.signin_et_pw);
        rigth_pw = (EditText)findViewById(R.id.signin_et_right_pw);
        check_pw = (TextView)findViewById(R.id.signin_tv_check);
        name = (EditText)findViewById(R.id.signin_et_name);
        phone = (EditText)findViewById(R.id.signin_et_phonenumber);
        btn_next=(Button)findViewById(R.id.signin_btn_next);

        profile.setOnClickListener(new View.OnClickListener() { //가운데 프로필 사진 클릭 이벤트 시작
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        }); //프로필 이벤트 끝

        btn_check.setOnClickListener(new View.OnClickListener() { //중복확인 클릭 이벤트 시작
            @Override
            public void onClick(View v) {
                final List<UserModel> userModels;
                userModels = new ArrayList<>(); //UserModel형의 List만듦
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("UserBasic")
                        .addValueEventListener(new ValueEventListener() { //UserBasic하위의 모든 데이터를 List에 저장하기 위함
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                                    userModels.add(snapshot.getValue(UserModel.class));
                                }

                                for(int i=0;i<userModels.size();i++){
                                    if(userModels.get(i).userEmail.equals(id.getText().toString())){
                                        checking = -1;
                                        break;
                                    }else{
                                        checking = 1;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                if(checking == -1){
                    Toast.makeText(SigninActivity.this, "아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
                }else if(checking == 1){
                    Toast.makeText(SigninActivity.this, "회원가입이 가능합니다.", Toast.LENGTH_SHORT).show();
                    btn_next.setVisibility(View.VISIBLE);
                }
            }
        }); //중복확인 이벤트 끝

        rigth_pw.addTextChangedListener(new TextWatcher() { //비밀번호 확인 시작
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pw.getText().toString().equals(rigth_pw.getText().toString())){
                    check_pw.setVisibility(View.INVISIBLE);
                }else{
                    check_pw.setVisibility(View.VISIBLE);
                    check_pw.setText("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }); //비밀번호 확인 끝

        btn_next.setOnClickListener(new View.OnClickListener() { //다음버튼 시작
            @Override
            public void onClick(View v) {
                String Sid = id.getText().toString();
                String Spw = pw.getText().toString();
                String Sname = name.getText().toString();
                String Sphone = phone.getText().toString();

                if(imageUri == null){
                   imageUri = Uri.parse("android.resource://com.example.jmkim.nomad/drawable/profile");
                }
                if(Sid == null || Spw == null || Sname == null || Sphone == null){
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Gen_ageActivity.class);
                    intent.putExtra("ID", Sid);
                    intent.putExtra("PW", Spw);
                    intent.putExtra("NAME", Sname);
                    intent.putExtra("PHONE", Sphone);
                    intent.putExtra("IMAGE", imageUri.toString());
                    startActivityForResult(intent, 0);
                }
            }
        }); //다음버튼 누르면 성별,나이 입력 페이지로 이동
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            profile.setImageURI(data.getData());
            imageUri = data.getData();
        }

        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                Intent intent = new Intent(SigninActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    } //뒤로가기 버튼 안되게
}
