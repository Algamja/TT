package com.example.jmkim.nomad.prev;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.R;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private ChatModel chatModel = new ChatModel();

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btn = (Button)findViewById(R.id.test_btn);

        chatModel.users.put("nbofj9YLoogLEYr0uTbyvxFmWFz1", true);
        chatModel.users.put("Nvi25OVRWKWKBexNbOXqZfVbnAi2", true);
        chatModel.users.put("9DT9bXFtbWeK9449mNeNPSIcMvm2", true);
        chatModel.type = "group";
        chatModel.king="9DT9bXFtbWeK9449mNeNPSIcMvm2";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("ChatRooms")
                        .push()
                        .setValue(chatModel);
            }
        });
    }
}
