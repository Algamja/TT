package com.example.jmkim.nomad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class WritePlanActivity extends AppCompatActivity implements com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener {

    private LinearLayout layout_when;
    private TextView ques_date;
    private TextView range;
    private TextView day_night;

    private LinearLayout layout_ppl;
    private TextView ques_ppl;
    private LinearLayout layout_go_ppl;
    private TextView total_ppl;
    private TextView cur_ppl;

    private View dlg_ppl;

    private EditText dlg_ppl_total;
    private EditText dlg_ppl_cur;

    private LinearLayout layout_recommend;
    private ImageButton btn_help;
    private View dlg_help;

    private RecyclerView planhot_recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_plan);

        layout_when = (LinearLayout)findViewById(R.id.WP_ll_when);
        ques_date = (TextView)findViewById(R.id.WP_tv_choose_date);
        range = (TextView)findViewById(R.id.WP_tv_range);
        day_night = (TextView)findViewById(R.id.WP_tv_DN);

        layout_ppl = (LinearLayout)findViewById(R.id.WP_ll_people);
        ques_ppl = (TextView)findViewById(R.id.WP_tv_howmanyppl);
        layout_go_ppl = (LinearLayout)findViewById(R.id.WP_ll_go_people);
        total_ppl = (TextView)findViewById(R.id.WP_tv_totalppl);
        cur_ppl = (TextView)findViewById(R.id.WP_tv_currentppl);
        layout_recommend = (LinearLayout)findViewById(R.id.WP_ll_recommend);
        btn_help = (ImageButton)findViewById(R.id.WP_ib_help);
        //layout_title = (LinearLayout)findViewById(R.id.WP_ll_title);

        planhot_recycler = findViewById(R.id.WP_recyclerview);
        planhot_recycler.setHasFixedSize(true);

        planhot_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<PlanHotInfo> planHotInfos = new ArrayList<>();
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));

        PlanAdapter planAdapter = new PlanAdapter(planHotInfos);
        planhot_recycler.setAdapter(planAdapter);

        layout_when.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                com.borax12.materialdaterangepicker.date.DatePickerDialog datepicker = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        (com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener) WritePlanActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                );
                datepicker.setAutoHighlight(true);
                datepicker.show(getFragmentManager(),"Datepickerdialog");
            }
        });

        layout_ppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_ppl = (View) View.inflate(WritePlanActivity.this, R.layout.with_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(WritePlanActivity.this);
                dlg.setTitle("인원을 설정해주세요!");
                dlg.setView(dlg_ppl);
                dlg_ppl_total = (EditText)dlg_ppl.findViewById(R.id.ppl_dlg_total);
                dlg_ppl_cur = (EditText)dlg_ppl.findViewById(R.id.ppl_dlg_cur);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String total = dlg_ppl_total.getText().toString();
                        String cur = dlg_ppl_cur.getText().toString();

                        total_ppl.setText(total + "명과 함께 여행을 떠나요!");
                        cur_ppl.setText("현재 " + cur + "명이 함께해요!");

                        layout_go_ppl.setVisibility(View.VISIBLE);
                        ques_ppl.setVisibility(View.GONE);
                    }
                });

                dlg.show();
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_help = (View) View.inflate(WritePlanActivity.this, R.layout.recom_help_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(WritePlanActivity.this);
                dlg.setView(dlg_help);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });//취향저격 일정 물음표 dialog

        /*btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sub layout_title = new Sub(getApplicationContext());
                layout_recommend.addView(layout_title);
            }
        });//취향저격 일정 더보기*/
    }

    @Override
    public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(year,monthOfYear,dayOfMonth);
        endDate.set(yearEnd,monthOfYearEnd,dayOfMonthEnd);

        long night = 0;
        long a = (endDate.getTimeInMillis() - startDate.getTimeInMillis())/1000;
        night = a/(60*60*24);
        long day = night + 1;

        if(night<0){
            Toast.makeText(this, "날짜를 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else {
            String date = (monthOfYear + 1) + "월 " + dayOfMonth + "일 ~ " + (monthOfYearEnd + 1) + "월 " + dayOfMonthEnd + "일";
            range.setText(date);

            day_night.setText("("+String.valueOf(Math.abs(night)) + "박" + String.valueOf(day) + "일)");

            ques_date.setVisibility(View.GONE);
            range.setVisibility(View.VISIBLE);
            day_night.setVisibility(View.VISIBLE);
        }
    }
}