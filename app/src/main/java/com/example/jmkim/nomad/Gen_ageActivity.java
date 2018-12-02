package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

public class Gen_ageActivity extends Activity{

    RadioButton male,female;
    Spinner spinner;
    Button btn_nnext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_age);

        male = (RadioButton)findViewById(R.id.male);
        female = (RadioButton)findViewById(R.id.female);
        spinner = (Spinner)findViewById(R.id.spinnerage);
        btn_nnext = (Button)findViewById(R.id.btn_nnext);

        btn_nnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String age = spinner.getSelectedItem().toString();
                String sex;
                if(male.isChecked()){
                    sex = "남성";
                }else{
                    sex = "여성";
                }

                Intent getintent = getIntent();
                String id = getintent.getStringExtra("ID");
                String pw = getintent.getStringExtra("PW");
                String name = getintent.getStringExtra("NAME");
                String phone = getintent.getStringExtra("PHONE");
                String img = getintent.getStringExtra("IMAGE");

                Intent intent = new Intent(getApplicationContext(), PartyselectActivity.class); //파티원 고르는 페이지로 이동
                intent.putExtra("ID",id);
                intent.putExtra("PW",pw);
                intent.putExtra("NAME",name);
                intent.putExtra("PHONE",phone);
                intent.putExtra("SEX",sex);
                intent.putExtra("AGE",age);
                intent.putExtra("IMAGE",img);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                Intent intent=new Intent(getApplicationContext(),SigninActivity.class);
                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish();
            } //뒤로가기 안하려고
        }
    }
}
