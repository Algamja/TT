package com.example.jmkim.nomad.prev;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SignUpActivity extends Activity{

    private FirebaseUser firebaseUser;

    private static final int PICK_FROM_ALBUM = 10;

    private ImageView back;
    private ImageView profile;
    private EditText email;
    private Button btn_check;
    private EditText pw;
    private EditText rigth_pw;
    private TextView check_pw;
    private EditText name;
    private EditText phone;
    private Button btn_next;

    Uri imageUri;

    int checking;

    public static Activity Signup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        back = (ImageView)findViewById(R.id.signUpActivity_iv_back);
        profile = (ImageView)findViewById(R.id.signUpActivity_iv_profile);
        email = (EditText)findViewById(R.id.signUpActivity_et_email);
        btn_check = (Button)findViewById(R.id.signUpActivity_btn_emailCheck);
        pw = (EditText)findViewById(R.id.signUpActivity_et_password);
        rigth_pw = (EditText)findViewById(R.id.signUpActivity_et_pwCheck);
        check_pw = (TextView)findViewById(R.id.signUpActivity_tv_pwCheck);
        name = (EditText)findViewById(R.id.signUpActivity_et_name);
        phone = (EditText)findViewById(R.id.signUpActivity_et_phone);
        btn_next=(Button)findViewById(R.id.signUpActivity_btn_next);

        Signup = SignUpActivity.this;

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,WelcomeActivity.class));
                finish();
            }
        });

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
                                    if(userModels.get(i).userEmail.equals(email.getText().toString())){
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

                if(firebaseUser != null){
                    Toast.makeText(SignUpActivity.this, "회원가입이 가능합니다.", Toast.LENGTH_SHORT).show();
                    btn_next.setVisibility(View.VISIBLE);
                }
                else if(checking == -1){
                    Toast.makeText(SignUpActivity.this, "아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
                }else if(checking == 1){
                    Toast.makeText(SignUpActivity.this, "회원가입이 가능합니다.", Toast.LENGTH_SHORT).show();
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
                String Semail = email.getText().toString();
                String Spw = pw.getText().toString();
                String Sname = name.getText().toString();
                String Sphone = phone.getText().toString();

                if(!(pw.getText().toString().equals(rigth_pw.getText().toString()))){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(imageUri == null){
                   imageUri = Uri.parse("android.resource://com.example.jmkim.nomad/drawable/profile");
                }
                if(Semail == null || Spw == null || Sname == null || Sphone == null){
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), GenAgeActivity.class);
                    intent.putExtra("EMAIL", Semail);
                    intent.putExtra("PW", Spw);
                    intent.putExtra("NAME", Sname);
                    intent.putExtra("PHONE", Sphone);
                    intent.putExtra("IMAGE", imageUri.toString());
                    startActivityForResult(intent, 0);
                }
            }
        }); //다음버튼 누르면 성별,나이 입력 페이지로 이동

    }
}
