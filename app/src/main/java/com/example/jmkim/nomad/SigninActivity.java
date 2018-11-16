package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SigninActivity extends Activity{

    EditText id, pw, name, phone;
    Button btn_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        id = (EditText)findViewById(R.id.identify);
        pw = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.w_name);
        phone = (EditText)findViewById(R.id.phonenumber);
        btn_next=(Button)findViewById(R.id.btn_next);

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
