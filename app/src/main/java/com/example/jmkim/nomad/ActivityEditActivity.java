package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jmkim.nomad.DB.UserActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityEditActivity extends Activity {

    private FirebaseUser user;

    private ToggleButton[] btns = new ToggleButton[28];
    Integer[] numBtnID = {R.id.activity_edit_tg_kidult, R.id.activity_edit_tg_gift, R.id.activity_edit_tg_luxury, R.id.activity_edit_tg_stationery, R.id.activity_edit_tg_electronic, R.id.activity_edit_tg_cosmetics, R.id.activity_edit_tg_fashion,
            R.id.activity_edit_tg_ski_board, R.id.activity_edit_tg_extreme, R.id.activity_edit_tg_camping, R.id.activity_edit_tg_kayak, R.id.activity_edit_tg_hiking, R.id.activity_edit_tg_water_leisure, R.id.activity_edit_tg_fishing,
            R.id.activity_edit_tg_food, R.id.activity_edit_tg_game, R.id.activity_edit_tg_party, R.id.activity_edit_tg_photo_attraction, R.id.activity_edit_tg_tradition, R.id.activity_edit_tg_concert, R.id.activity_edit_tg_amusement_park,
            R.id.activity_edit_tg_spa, R.id.activity_edit_tg_beach, R.id.activity_edit_tg_walk, R.id.activity_edit_tg_food_h, R.id.activity_edit_tg_temple_stay, R.id.activity_edit_tg_hocance, R.id.activity_edit_tg_massage};
    private Button btn_end;

    private HashMap checks = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_edit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        for(int i=0;i<28;i++){
            btns[i]=(ToggleButton) findViewById(numBtnID[i]);
        }//각 버튼의 id와 이름 매핑

        btn_end = (Button)findViewById(R.id.activity_edit_btn_end);

        final List<UserActivity> userActivity = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserActivity")
                .child(user.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userActivity.clear();
                        userActivity.add(dataSnapshot.getValue(UserActivity.class));
                        String Activities[] = {userActivity.get(0).Activity_0, userActivity.get(0).Activity_1, userActivity.get(0).Activity_2, userActivity.get(0).Activity_3, userActivity.get(0).Activity_4, userActivity.get(0).Activity_5, userActivity.get(0).Activity_6,
                                userActivity.get(0).Activity_7, userActivity.get(0).Activity_8, userActivity.get(0).Activity_9, userActivity.get(0).Activity_10, userActivity.get(0).Activity_11, userActivity.get(0).Activity_12, userActivity.get(0).Activity_13,
                                userActivity.get(0).Activity_14, userActivity.get(0).Activity_15, userActivity.get(0).Activity_16, userActivity.get(0).Activity_17, userActivity.get(0).Activity_18, userActivity.get(0).Activity_19, userActivity.get(0).Activity_20,
                                userActivity.get(0).Activity_21, userActivity.get(0).Activity_22, userActivity.get(0).Activity_23, userActivity.get(0).Activity_24, userActivity.get(0).Activity_25, userActivity.get(0).Activity_26,userActivity.get(0).Activity_27};

                        for(int i=0;i<Activities.length;i++){
                            if(Activities[i].equals("true")){
                                btns[i].setChecked(true); //홀수번 클릭시 버튼에 테두리표시
                                checks.put("Activity_"+String.valueOf(i),"true");
                            }else{
                                btns[i].setChecked(false);
                                checks.put("Activity_"+String.valueOf(i),"false");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        for(int i=0;i<numBtnID.length;i++){
            final int index;
            index = i;
            btns[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btns[index].isChecked()){
                        checks.put("Activity_"+String.valueOf(index),"true"); //홀수번 클릭시 hashtable에 버튼번호 저장
                    }else{
                        checks.put("Activity_"+String.valueOf(index),"false"); //짝수번 클릭시 hashtable에서 버튼 제거
                    }
                }
            });
        }

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checked = 0;

                for(int i=0;i<numBtnID.length;i++){
                    if(btns[i].isChecked()){
                        checked++;
                    }
                }

                if(checked < 10){
                    Toast.makeText(ActivityEditActivity.this, "최소 10개 이상 선택하세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("UserActivity")
                            .child(user.getUid())
                            .setValue(checks)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    Toast.makeText(ActivityEditActivity.this, "액티비티 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ActivityEditActivity.this, UserInfoEditActivity.class));
                                    finish();
                                }
                            });
                }
            }
        });
    }
}
