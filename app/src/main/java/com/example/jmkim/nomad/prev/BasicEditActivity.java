package com.example.jmkim.nomad.prev;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class BasicEditActivity extends AppCompatActivity {

    private FirebaseUser user;

    String uid;

    private EditText email;
    private EditText name;
    private EditText pw;
    private EditText pw_check;
    private TextView tv_check;
    private EditText phone;
    private Button change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_edit);

        email = (EditText)findViewById(R.id.basicEdit_et_email);
        name = (EditText)findViewById(R.id.basicEdit_et_name);
        pw = (EditText)findViewById(R.id.basicEdit_et_password);
        pw_check = (EditText)findViewById(R.id.basicEdit_et_password_check);
        tv_check = (TextView) findViewById(R.id.basicEdit_tv_pwCheck);
        phone = (EditText)findViewById(R.id.basicEdit_et_phone);
        change = (Button)findViewById(R.id.basicEdit_btn_change);

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

                        email.setText(userModel.get(0).userEmail);
                        name.setText(userModel.get(0).userName);
                        phone.setText(userModel.get(0).userPhone);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }); //DB에서 읽어오기 종료

        pw_check.addTextChangedListener(new TextWatcher() { //비밀번호 확인 시작
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(pw.getText().toString().equals(pw_check.getText().toString())){
                    tv_check.setVisibility(View.INVISIBLE);
                }else{
                    tv_check.setVisibility(View.VISIBLE);
                    tv_check.setText("비밀번호가 일치하지 않습니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }); //비밀번호 확인 끝

        change.setOnClickListener(new View.OnClickListener() { //변경 버튼 클릭했을 때
            @Override
            public void onClick(View v) {
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("UserBasic")
                        .child(uid)
                        .child("userPhone")
                        .setValue(phone.getText().toString());

                if(pw.getText().toString().equals(pw_check.getText().toString())){
                   user.updatePassword(pw.getText().toString())
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   Toast.makeText(getApplicationContext(), "사용자 정보가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                   Intent intent = new Intent(BasicEditActivity.this,UserInfoEditActivity.class);
                                   startActivity(intent);
                                   finish();
                               }
                           });
                }
            }
        });
    }
}
