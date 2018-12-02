package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.net.MalformedURLException;

public class SigninActivity extends Activity{

    private static final int PICK_FROM_ALBUM = 10;

    TextView check_pw;
    EditText id, pw, rigth_pw, name, phone;
    Button btn_next;
    ImageView profile;
    Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        profile = (ImageView)findViewById(R.id.signin_image_profile);
        id = (EditText)findViewById(R.id.signin_et_id);
        pw = (EditText)findViewById(R.id.signin_et_pw);
        rigth_pw = (EditText)findViewById(R.id.signin_et_right_pw);
        check_pw = (TextView)findViewById(R.id.signin_tv_check);
        name = (EditText)findViewById(R.id.signin_et_name);
        phone = (EditText)findViewById(R.id.signin_et_phonenumber);
        btn_next=(Button)findViewById(R.id.signin_btn_next);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent,PICK_FROM_ALBUM);
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), Gen_ageActivity.class);
                intent.putExtra("ID",id.getText().toString());
                intent.putExtra("PW",pw.getText().toString());
                intent.putExtra("NAME",name.getText().toString());
                intent.putExtra("PHONE",phone.getText().toString());
                intent.putExtra("IMAGE",imageUri.toString());
                startActivityForResult(intent,0);
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
