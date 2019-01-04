package com.example.jmkim.nomad;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

import androidx.annotation.Nullable;

public class ActivitySelectActivity extends Activity {
    Button[] btns = new Button[20];
    Integer[] numBtnID = {R.id.ActivitySelect_btn1,R.id.ActivitySelect_btn2,R.id.ActivitySelect_btn3,R.id.ActivitySelect_btn4,R.id.ActivitySelect_btn5,R.id.ActivitySelect_btn6,R.id.ActivitySelect_btn7,R.id.ActivitySelect_btn8,R.id.ActivitySelect_btn9,R.id.ActivitySelect_btn10,
            R.id.ActivitySelect_btn11,R.id.ActivitySelect_btn12,R.id.ActivitySelect_btn13,R.id.ActivitySelect_btn14,R.id.ActivitySelect_btn15,R.id.ActivitySelect_btn16,R.id.ActivitySelect_btn17,R.id.ActivitySelect_btn18,R.id.ActivitySelect_btn19,R.id.ActivitySelect_btn20};
    Button btn_next;

   HashMap checks = new HashMap(); //선택된 버튼의 번호 저장

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_select);

        for(int i=0;i<20;i++){
            btns[i]=(Button)findViewById(numBtnID[i]);
        }//각 버튼의 id와 이름 매핑

        btn_next = (Button)findViewById(R.id.ActivitySelect_btn_next);

        final int click[] = new int[20]; //선택된 버튼 표시하기 위한 배열

        for(int i=0;i<numBtnID.length;i++){
            click[i]=0;
            final int index;
            index = i;
            checks.put("Activity_"+String.valueOf(index),"false");
            btns[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click[index]++; //클릭될 때마다 click[i]증가
                    if(click[index]%2==1){
                        btns[index].setBackgroundResource(R.drawable.select_button_red); //홀수번 클릭시 버튼에 테두리표시
                        checks.put("Activity_"+String.valueOf(index),"true"); //홀수번 클릭시 hashtable에 버튼번호 저장
                    }else{
                        checks.put("Activity_"+String.valueOf(index),"false"); //짝수번 클릭시 hashtable에서 버튼 제거
                        btns[index].setBackgroundColor(000000);
                    }
                }
            });
        }

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getintent = getIntent();
                String email = getintent.getStringExtra("EMAIL");
                String pw = getintent.getStringExtra("PW");
                String name = getintent.getStringExtra("NAME");
                String phone = getintent.getStringExtra("PHONE");
                String sex = getintent.getStringExtra("SEX");
                String age = getintent.getStringExtra("AGE");
                String img = getintent.getStringExtra("IMAGE");
                String party = getintent.getStringExtra("PARTY");

                String partySex = getintent.getStringExtra("PARTY_SEX");
                String partyAge = getintent.getStringExtra("PARTY_AGE");

                if(checks.size()>=10) { //최소 10개버튼 이상 선택시
                    Intent intent = new Intent(getApplicationContext(), SignEndActivity.class); //가입완료 페이지로 이동
                    intent.putExtra("EMAIL",email);
                    intent.putExtra("PW",pw);
                    intent.putExtra("NAME",name);
                    intent.putExtra("PHONE",phone);
                    intent.putExtra("SEX",sex);
                    intent.putExtra("AGE",age);
                    intent.putExtra("IMAGE",img);
                    intent.putExtra("PARTY",party);

                    intent.putExtra("PARTY_SEX",partySex);
                    intent.putExtra("PARTY_AGE",partyAge);

                    intent.putExtra("Activities",checks);
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
                Intent intent=new Intent(getApplicationContext(),GenAgeActivity.class);
                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    } //뒤로가기 버튼 안되게 하기 위함
}
