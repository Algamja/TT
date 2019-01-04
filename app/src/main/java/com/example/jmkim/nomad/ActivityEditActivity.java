package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

    private Button[] btns = new Button[20];
    private Integer[] numBtnID = {R.id.ActivityEdit_btn1,R.id.ActivityEdit_btn2,R.id.ActivityEdit_btn3,R.id.ActivityEdit_btn4,R.id.ActivityEdit_btn5,R.id.ActivityEdit_btn6,R.id.ActivityEdit_btn7,R.id.ActivityEdit_btn8,R.id.ActivityEdit_btn9,R.id.ActivityEdit_btn10,
            R.id.ActivityEdit_btn11,R.id.ActivityEdit_btn12,R.id.ActivityEdit_btn13,R.id.ActivityEdit_btn14,R.id.ActivityEdit_btn15,R.id.ActivityEdit_btn16,R.id.ActivityEdit_btn17,R.id.ActivityEdit_btn18,R.id.ActivityEdit_btn19,R.id.ActivityEdit_btn20};
    private Button btn_end;

    private HashMap checks = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_edit);

        user = FirebaseAuth.getInstance().getCurrentUser();

        for(int i=0;i<20;i++){
            btns[i]=(Button)findViewById(numBtnID[i]);
        }//각 버튼의 id와 이름 매핑

        btn_end = (Button)findViewById(R.id.ActivityEdit_btn_end);

        final int click[] = new int[20]; //선택된 버튼 표시하기 위한 배열

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
                        String Activities[] = {userActivity.get(0).Activity_0, userActivity.get(0).Activity_1, userActivity.get(0).Activity_2, userActivity.get(0).Activity_3, userActivity.get(0).Activity_4, userActivity.get(0).Activity_5, userActivity.get(0).Activity_6, userActivity.get(0).Activity_7, userActivity.get(0).Activity_8, userActivity.get(0).Activity_9, userActivity.get(0).Activity_10,
                                userActivity.get(0).Activity_11, userActivity.get(0).Activity_12, userActivity.get(0).Activity_13, userActivity.get(0).Activity_14, userActivity.get(0).Activity_15, userActivity.get(0).Activity_16, userActivity.get(0).Activity_17, userActivity.get(0).Activity_18, userActivity.get(0).Activity_19};

                        for(int i=0;i<Activities.length;i++){
                            if(Activities[i].equals("true")){
                                btns[i].setBackgroundResource(R.drawable.select_button_red); //홀수번 클릭시 버튼에 테두리표시
                                checks.put("Activity_"+String.valueOf(i),"true");
                                click[i]=1;
                            }else{
                                click[i]=0;
                                checks.put("Activity_"+String.valueOf(i),"false");
                                btns[i].setBackgroundColor(000000);
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

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}
