package com.example.jmkim.nomad;

import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.ReportModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReportReadActivity extends AppCompatActivity {

    private TextView clean_user;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_read);
        String publisher = getIntent().getStringExtra("publisher");

        List<String> reasons = new ArrayList<>();
        List<ReportModel> reportModels = new ArrayList<>();

        clean_user = (TextView)findViewById(R.id.report_list_null);
        listView = (ListView)findViewById(R.id.report_list);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Report")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            reportModels.add(item.getValue(ReportModel.class));
                        }

                        Log.e("size", String.valueOf(reportModels.size()));
                        for(int i=0;i<reportModels.size();i++){
                            Log.e("IN","in");
                            if(reportModels.get(i).reportee.equals(publisher)){
                                Log.e("ININ","ininin");
                                reasons.add(reportModels.get(i).reason);
                            }
                        }

                        if(reasons.size()!=0){
                            clean_user.setVisibility(View.GONE);
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportReadActivity.this, android.R.layout.simple_list_item_1,reasons);
                            listView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
