package com.example.jmkim.nomad.prev;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jmkim.nomad.R;

import java.util.HashMap;

import androidx.annotation.Nullable;

public class ActivitySelectActivity extends Activity {
    ImageView back;
    ToggleButton[] btns = new ToggleButton[28];

    Integer[] numBtnID = {R.id.activity_select_tg_kidult, R.id.activity_select_tg_gift, R.id.activity_select_tg_luxury, R.id.activity_select_tg_stationery, R.id.activity_select_tg_electronic, R.id.activity_select_tg_cosmetics, R.id.activity_select_tg_fashion,
    R.id.activity_select_tg_ski_board, R.id.activity_select_tg_extreme, R.id.activity_select_tg_camping, R.id.activity_select_tg_kayak, R.id.activity_select_tg_hiking, R.id.activity_select_tg_water_leisure, R.id.activity_select_tg_fishing,
    R.id.activity_select_tg_food, R.id.activity_select_tg_game, R.id.activity_select_tg_party, R.id.activity_select_tg_photo_attraction, R.id.activity_select_tg_tradition, R.id.activity_select_tg_concert, R.id.activity_select_tg_amusement_park,
    R.id.activity_select_tg_spa, R.id.activity_select_tg_beach, R.id.activity_select_tg_walk, R.id.activity_select_tg_food_h, R.id.activity_select_tg_temple_stay, R.id.activity_select_tg_hocance, R.id.activity_select_tg_massage};
    Button btn_next;

    public static Activity ActivitySelect;

   HashMap checks = new HashMap(); //선택된 버튼의 번호 저장

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_select);

        back = (ImageView)findViewById(R.id.activity_select_iv_back);

        for(int i=0;i<28;i++){
            btns[i] = (ToggleButton)findViewById(numBtnID[i]);
        }//각 버튼의 id와 이름 매핑

        btn_next = (Button)findViewById(R.id.activity_select_btn_next);

        ActivitySelect = ActivitySelectActivity.this;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for(int i=0;i<numBtnID.length;i++){
            final int index;
            index = i;
            checks.put("Activity_"+String.valueOf(index),"false");
            btns[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btns[index].isChecked()){
                        checks.put("Activity_"+String.valueOf(index),"true");
                    }else {
                        checks.put("Activity_"+String.valueOf(index),"false");
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

                int checked = 0;

                for(int i=0;i<numBtnID.length;i++){
                    if(btns[i].isChecked()){
                        checked++;
                    }
                }

                if(checked >= 10){
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
}
