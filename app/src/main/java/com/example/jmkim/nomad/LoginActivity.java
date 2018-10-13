package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity{

    Button btn_login, btn_sign, btn_find;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        btn_login=(Button)findViewById(R.id.login_btn_login);
        btn_find=(Button)findViewById(R.id.login_btn_find);
        btn_sign=(Button)findViewById(R.id.login_btn_sign);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                int LogIn_Val=1;
                intent.putExtra("LOG",LogIn_Val);
                setResult(RESULT_OK,intent);
                finish(); //로그인 버튼 누르면 바로 메인으로 감
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FindIdPwActivity.class);
                startActivity(intent); //아이디/비밀번호찾기 페이지
            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SigninActivity.class);
                startActivity(intent); //회원가입 페이지
            }
        });
    }

}
