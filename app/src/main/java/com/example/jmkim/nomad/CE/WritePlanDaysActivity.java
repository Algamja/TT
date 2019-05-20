package com.example.jmkim.nomad.CE;

import android.os.Bundle;
import android.widget.TextView;

import com.example.jmkim.nomad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WritePlanDaysActivity extends AppCompatActivity {
    private TextView day_num;
    private TextView hashtag;

    private List<DBHashtag> dbHashtagList = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeplan_days);

        database = FirebaseDatabase.getInstance();

        day_num = (TextView) findViewById(R.id.WPD_day);
        hashtag = (TextView) findViewById(R.id.WPD_hashtag);

        database.getReference().child("Hashtag").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbHashtagList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    DBHashtag dbHashtag = snapshot.getValue(DBHashtag.class);
                    dbHashtagList.add(dbHashtag);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}