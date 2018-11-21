package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;

public class SigninActivity extends Activity{

    TextView check_pw;
    EditText id, pw, rigth_pw, name, phone;
    Button btn_next,check;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        NetworkUtil.setNetWorkPlicy();

        id = (EditText)findViewById(R.id.signin_et_id);
        pw = (EditText)findViewById(R.id.signin_et_pw);
        rigth_pw = (EditText)findViewById(R.id.signin_et_right_pw);
        name = (EditText)findViewById(R.id.signin_et_name);
        phone = (EditText)findViewById(R.id.signin_et_phonenumber);

        check_pw = (TextView)findViewById(R.id.signin_tv_check_pw);


        btn_next=(Button)findViewById(R.id.signin_btn_next);
        check = (Button)findViewById(R.id.signin_btn_idcheck); //중복확인 버튼

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PHPRequest request = new PHPRequest("http://nomad.dothome.co.kr/query.php");
                    String result = request.PhPtest(id.getText().toString());
                    if(result.equals("1")){
                        Toast.makeText(getApplication(),"ID가 중복되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplication(),"회원가입이 가능합니다.",Toast.LENGTH_SHORT).show();
                        btn_next.setVisibility(View.VISIBLE); //id중복이 아닐때만 다음으로 가는 버튼 보이도록 설정
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        });

        rigth_pw.addTextChangedListener(new TextWatcher() {
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
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Gen_ageActivity.class);
                intent.putExtra("ID",id.getText().toString());
                intent.putExtra("PW",pw.getText().toString());
                intent.putExtra("NAME",name.getText().toString());
                intent.putExtra("PHONE",phone.getText().toString());
                startActivityForResult(intent,0);
            }
        }); //다음버튼 누르면 성별,나이 입력 페이지로 이동
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                finish();
            }
        }
    } //뒤로가기 버튼 안되게
}
