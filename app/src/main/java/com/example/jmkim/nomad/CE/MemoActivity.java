package com.example.jmkim.nomad.CE;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MemoActivity extends AppCompatActivity {

    public Button saveBtn;
    private EditText title;
    private EditText content;
    private FirebaseAuth auth;
    private FirebaseStorage storage;

    private FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_plan);
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        saveBtn = (Button)findViewById(R.id.MP_btn_save);
        title = (EditText)findViewById(R.id.MP_et_title);
        content = (EditText)findViewById(R.id.MP_et_content);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Plan")
                        .child(uid)
                        .child("memo")
                        .setValue(title.getText().toString());

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Plan")
                        .child(uid)
                        .child("memo")
                        .setValue(content.getText().toString());
            }
        });
    }
}
