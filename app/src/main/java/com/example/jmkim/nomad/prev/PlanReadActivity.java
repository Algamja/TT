package com.example.jmkim.nomad.prev;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.added.ItemSchedule;
import com.example.jmkim.nomad.added.ReadPlanItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlanReadActivity extends AppCompatActivity {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView.LayoutManager layoutManager;

    private View pw_dlg;
    private AlertDialog dlg = null;

    private ImageView flag;
    private TextView city;
    private TextView period;
    private TextView total;
    private TextView want;
    private RecyclerView schedule;

    private Button confirm_btn;
    private Button pw_btn;
    String chat_key = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planread);

        String publisher = getIntent().getStringExtra("publisher");
        String plan_key = getIntent().getStringExtra("key");

        layoutManager = new LinearLayoutManager(this);
        layoutManager = new LinearLayoutManager(this);

        flag = (ImageView)findViewById(R.id.read_plan_flag);
        city = (TextView)findViewById(R.id.read_plan_city);
        period = (TextView)findViewById(R.id.read_plan_period);
        total = (TextView)findViewById(R.id.read_plan_total);
        want = (TextView)findViewById(R.id.read_plan_want);
        schedule = (RecyclerView)findViewById(R.id.read_plan_schedule);
        confirm_btn = (Button)findViewById(R.id.read_plan_confirm);

        schedule.setLayoutManager(layoutManager);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(publisher)
                .child(plan_key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Plan plan = dataSnapshot.getValue(Plan.class);
                        String imsi = plan.country;
                        String imsi_array[] = imsi.split(" ");

                        if(imsi_array[imsi_array.length-1].equals("일본")){
                            flag.setImageDrawable(getResources().getDrawable(R.drawable.japan));
                        }
                        city.setText(plan.country);
                        period.setText(plan.period);
                        total.setText("총 "+plan.total+"명이 함께 떠나요!");
                        want.setText("현재 "+plan.want+"명을 구해요!");

                        imsi = plan.period;
                        imsi_array = imsi.split(" ");
                        imsi = imsi_array[imsi_array.length-1];
                        imsi_array = imsi.split("일");
                        imsi = imsi_array[0];
                        chat_key = plan.chat_key;

                        ReadPlanItem readPlanItem = new ReadPlanItem(PlanReadActivity.this, Integer.parseInt(imsi), plan, PlanReadActivity.this);
                        schedule.setAdapter(readPlanItem);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dlg = new Dialog(PlanReadActivity.this);
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dlg.setContentView(R.layout.password_dialog);
                dlg.show();

                final EditText dlg_pw = (EditText)dlg.findViewById(R.id.dlg_pass);
                final Button dlg_btn = (Button)dlg.findViewById(R.id.dlg_btn);

                Map<String, Boolean> member = new HashMap<>();

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("ChatRooms")
                        .child(chat_key)
                        .child("users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot item : dataSnapshot.getChildren()){
                                    member.put(item.getKey(),true);
                                    Log.e("item.getKey",item.getKey());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                dlg_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dlg_pw.getText().toString().equals(chat_key)){
                            member.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
                            Log.e("true","true");

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference()
                                    .child("ChatRooms")
                                    .child(chat_key)
                                    .child("users")
                                    .setValue(member);
                            Toast.makeText(PlanReadActivity.this, "신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            dlg.dismiss();
                        }else{
                            Toast.makeText(PlanReadActivity.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
