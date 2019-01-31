package com.example.jmkim.nomad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.DB.UserParty;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PartyEditActivity extends AppCompatActivity {

    private FirebaseUser user;

    String uid;

    private CheckBox partyCheck;
    private LinearLayout linear_sex;
    private LinearLayout linear_age;
    private RadioButton rb_sameSex;
    private RadioButton rb_donCareSex;
    private RadioButton rb_sameAge;
    private RadioButton rb_donCareAge;
    private RadioButton rb_3Age;
    private RadioButton rb_5Age;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_edit);

        partyCheck = (CheckBox)findViewById(R.id.partyEdit_cb_party);
        linear_sex = (LinearLayout) findViewById(R.id.partyEdit_partySex);
        linear_age = (LinearLayout) findViewById(R.id.partyEdit_partyAge);
        rb_sameSex = (RadioButton) findViewById(R.id.partyEdit_rb_sameSex);
        rb_donCareSex = (RadioButton) findViewById(R.id.partyEdit_rb_donCareSex);
        rb_sameAge = (RadioButton) findViewById(R.id.partyEdit_rb_sameAge);
        rb_donCareAge = (RadioButton) findViewById(R.id.partyEdit_rb_donCareAge);
        rb_3Age = (RadioButton) findViewById(R.id.partyEdit_rb_3Age);
        rb_5Age = (RadioButton) findViewById(R.id.partyEdit_rb_5Age);
        change = (Button)findViewById(R.id.partyEdit_btn_change);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        final List<UserModel> userModel = new ArrayList<>(); //DB에서 읽어올 준비
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("UserBasic")
                .child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //읽어오기 시작
                        userModel.clear();

                        userModel.add(dataSnapshot.getValue(UserModel.class));

                        if(userModel.get(0).party.equals("파티원 동의")){
                            partyCheck.setChecked(true);
                        }else{
                            partyCheck.setChecked(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); //DB에서 읽어오기 종료

        partyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(partyCheck.isChecked()){
                    linear_sex.setVisibility(View.VISIBLE);
                    linear_age.setVisibility(View.VISIBLE);
                }else{
                    linear_sex.setVisibility(View.GONE);
                    linear_age.setVisibility(View.GONE);
                }
            }
        });


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserParty userParty = new UserParty();
                if(rb_sameSex.isChecked()){
                    userParty.partySex = "동성만";
                }else if(rb_donCareSex.isChecked()){
                    userParty.partySex = "무관";
                }else{
                    if(partyCheck.isChecked()){
                        Toast.makeText(getApplicationContext(), "파티원 성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(rb_sameAge.isChecked()){
                    userParty.partyAge = "동갑";
                }else if(rb_donCareAge.isChecked()){
                    userParty.partyAge = "무관";
                }else if(rb_3Age.isChecked()){
                    userParty.partyAge = "3살차이";
                }else if(rb_5Age.isChecked()){
                    userParty.partyAge = "5살차이";
                }else{
                    if(partyCheck.isChecked()){
                        Toast.makeText(getApplicationContext(), "파티원 나이를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(partyCheck.isChecked()){
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("UserBasic")
                            .child(uid)
                            .child("party")
                            .setValue("파티원 동의");

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("UserParty")
                            .child(uid)
                            .setValue(userParty);
                }else{
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("UserBasic")
                            .child(uid)
                            .child("party")
                            .setValue("파티원 미동의");

                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("UserParty")
                            .child(uid)
                            .removeValue();
                }

                Intent intent = new Intent(PartyEditActivity.this,UserInfoEditActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
