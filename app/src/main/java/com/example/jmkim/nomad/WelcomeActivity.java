package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class WelcomeActivity extends Activity{

    private Button mainpage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        mainpage=(Button)findViewById(R.id.mainpage);

        final Intent intent = getIntent();

        final String email = intent.getStringExtra("ID");
        final String password = intent.getStringExtra("PW");
        final String name = intent.getStringExtra("NAME");
        final String phone = intent.getStringExtra("PHONE");
        final String sex = intent.getStringExtra("SEX");
        final String age = intent.getStringExtra("AGE");
        final Uri img = Uri.parse(intent.getStringExtra("IMAGE"));

        final HashMap activities = (HashMap)intent.getSerializableExtra("Activities");

        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(WelcomeActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();
                                FirebaseStorage
                                        .getInstance()
                                        .getReference()
                                        .child("userImages")
                                        .child(uid)
                                        .putFile(img)
                                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                            final StorageReference profileImageRef = FirebaseStorage
                                                    .getInstance()
                                                    .getReference()
                                                    .child("userImages")
                                                    .child(uid);
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                Task<Uri> uriTask = profileImageRef.getDownloadUrl();
                                                while(!uriTask.isSuccessful());
                                                Uri downloadUrl = uriTask.getResult();
                                                String imageUrl = String.valueOf(downloadUrl);

                                                UserModel userModel = new UserModel();
                                                userModel.userEmail = email;
                                                userModel.userName = name;
                                                userModel.userPhone = phone;
                                                userModel.userSex = sex;
                                                userModel.userAge=age;
                                                userModel.profileImageUrl = imageUrl;

                                                FirebaseDatabase
                                                        .getInstance()
                                                        .getReference()
                                                        .child("UserActivity")
                                                        .child(uid)
                                                        .setValue(activities);

                                                FirebaseDatabase
                                                        .getInstance()
                                                        .getReference()
                                                        .child("UserBasic")
                                                        .child(uid)
                                                        .setValue(userModel)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getApplicationContext(), "가입완료", Toast.LENGTH_SHORT).show();

                                                                Intent backintent = new Intent(getApplicationContext(), PartyselectActivity.class);

                                                                int Main_Val=1;
                                                                backintent.putExtra("END",Main_Val);
                                                                setResult(RESULT_OK,backintent);
                                                                finish(); //뒤로가기 안하려고
                                                            }
                                                        });
                                            }
                                        });
                            }
                        });

            }
        });
    }
}