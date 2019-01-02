package com.example.jmkim.nomad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FindIdPwActivity extends Activity{

    private EditText findEmail_name;
    private EditText findEmail_phone;
    private Button findEmail;
    private EditText findPw_email;
    private EditText findPw_name;
    private EditText findPw_phone;
    private Button findPw;
    private Button back;

    int checking = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id_pw);

        findEmail_name = (EditText)findViewById(R.id.findEmail_et_name);
        findEmail_phone = (EditText)findViewById(R.id.findEmail_et_phone);
        findEmail = (Button)findViewById(R.id.findEmail_btn_find);
        findPw_email = (EditText)findViewById(R.id.findPw_et_email);
        findPw_name = (EditText)findViewById(R.id.findPw_et_name);
        findPw_phone = (EditText)findViewById(R.id.findEmail_et_phone);
        findPw = (Button) findViewById(R.id.findPw_btn_find);
        back = (Button)findViewById(R.id.find_btn_back);

        checking = 0;
        findEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final List<UserModel> userModel = new ArrayList<>(); //UserModel형의 List만듦
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("UserBasic")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                                    userModel.add(snapshot.getValue(UserModel.class));
                                }

                                for(int i=0;i<userModel.size();i++){
                                    if((userModel.get(i).userName.equals(findEmail_name.getText().toString())) && (userModel.get(i).userPhone.equals(findEmail_phone.getText().toString()))){
                                        AlertDialog.Builder dlg = new AlertDialog.Builder(FindIdPwActivity.this);
                                        dlg.setTitle("이메일 찾기");
                                        dlg.setMessage("귀하의 이메일은 " + userModel.get(i).userEmail + " 입니다");
                                        dlg.setPositiveButton("확인",null);
                                        dlg.setNegativeButton("로그인 하기", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(FindIdPwActivity.this,SignInActivity.class));
                                                finish();
                                            }
                                        });
                                        dlg.show();
                                        return;
                                    }else{
                                        checking = -1;
                                    }
                                }
                                if(checking == -1){
                                    Toast.makeText(getApplicationContext(), "일치하는 사용자가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });

        findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<UserModel> userModel = new ArrayList<>(); //UserModel형의 List만듦
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("UserBasic")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                                    userModel.add(snapshot.getValue(UserModel.class));
                                }

                                for(int i=0;i<userModel.size();i++){
                                    if((userModel.get(i).userEmail.equals(findPw_email.getText().toString()))
                                            && (userModel.get(i).userName.equals(findPw_name.getText().toString()))
                                            && (userModel.get(i).userPhone.equals(findPw_phone.getText().toString()))){
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        auth.sendPasswordResetEmail(findPw_email.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(), "입력된 이메일로 비밀번호 변경 메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "비밀번호 변경 메일 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        return;
                                    }else{
                                        checking = -2;
                                    }
                                }
                                if(checking == -2){
                                    Toast.makeText(getApplicationContext(), "일치하는 사용자가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }); //DB정보 끝
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FindIdPwActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}
