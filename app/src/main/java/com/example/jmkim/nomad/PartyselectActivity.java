package com.example.jmkim.nomad;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.Stack;

public class PartyselectActivity extends Activity{
    Button[] btns = new Button[20];
    Integer[] numBtnID = {R.id.party_btn_1,R.id.party_btn_2,R.id.party_btn_3,R.id.party_btn_4,R.id.party_btn_5,R.id.party_btn_6,R.id.party_btn_7,R.id.party_btn_8,R.id.party_btn_9,R.id.party_btn_10,
            R.id.party_btn_11,R.id.party_btn_12,R.id.party_btn_13,R.id.party_btn_14,R.id.party_btn_15,R.id.party_btn_16,R.id.party_btn_17,R.id.party_btn_18,R.id.party_btn_19,R.id.party_btn_20};
    Button btn_next;

    Hashtable checks = new Hashtable(); //선택된 버튼의 번호 저장

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_select);

        for(int i=0;i<20;i++){
            btns[i]=(Button)findViewById(numBtnID[i]);
        }//각 버튼의 id와 이름 매핑

        btn_next = (Button)findViewById(R.id.party_btn_next);

        final int click[] = new int[20]; //선택된 버튼 표시하기 위한 배열

        for(int i=0;i<numBtnID.length;i++){
            click[i]=0;
            final int index;
            index = i;
            btns[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click[index]++; //클릭될 때마다 click[i]증가
                    if(click[index]%2==1){
                        btns[index].setBackgroundResource(R.drawable.button); //홀수번 클릭시 버튼에 테두리표시
                        checks.put(index,index); //홀수번 클릭시 hashtable에 버튼번호 저장
                    }else{
                        checks.remove(index); //짝수번 클릭시 hashtable에서 버튼 제거
                        btns[index].setBackgroundColor(000000);
                    }
                }
            });
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getintent = getIntent();
                String id = getintent.getStringExtra("ID");
                String pw = getintent.getStringExtra("PW");
                String name = getintent.getStringExtra("NAME");
                String phone = getintent.getStringExtra("PHONE");
                String sex = getintent.getStringExtra("SEX");
                String age = getintent.getStringExtra("AGE");

                if(checks.size()>=10) { //최소 10개버튼 이상 선택시
                    Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class); //가입완료 페이지로 이동
                    intent.putExtra("ID",id);
                    intent.putExtra("PW",pw);
                    intent.putExtra("NAME",name);
                    intent.putExtra("PHONE",phone);
                    intent.putExtra("SEX",sex);
                    intent.putExtra("AGE",age);
                    startActivityForResult(intent, 0);
                }
                else{
                    Toast.makeText(getApplication(),"최소 10개 이상 선택하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                Intent intent=new Intent(getApplicationContext(),Gen_ageActivity.class);
                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    } //뒤로가기 버튼 안되게 하기 위함
}
