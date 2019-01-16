package com.example.jmkim.nomad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WritePlanActivity extends AppCompatActivity implements com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener {

    private LinearLayout layout_where;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_plan);

        layout_where = (LinearLayout)findViewById(R.id.WP_ll_where);
        layout_when = (LinearLayout)findViewById(R.id.WP_ll_when);
        ques_date = (TextView)findViewById(R.id.WP_tv_choose_date);
        range = (TextView)findViewById(R.id.WP_tv_range);
        day_night = (TextView)findViewById(R.id.WP_tv_DN);
        layout_ppl = (LinearLayout)findViewById(R.id.WP_ll_people);
        ques_ppl = (TextView)findViewById(R.id.WP_tv_howmanyppl);
        layout_go_ppl = (LinearLayout)findViewById(R.id.WP_ll_go_people);
        total_ppl = (TextView)findViewById(R.id.WP_tv_totalppl);
        cur_ppl = (TextView)findViewById(R.id.WP_tv_currentppl);

        layout_where.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WritePlanActivity.this, SearchActivity.class));
            }
        });

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
                dlg.setTitle("몇 명이서 가시나요?");
                dlg.setView(dlg_ppl);
                dlg_ppl_total = (EditText)dlg_ppl.findViewById(R.id.ppl_dlg_total);
                dlg_ppl_cur = (EditText)dlg_ppl.findViewById(R.id.ppl_dlg_cur);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String total = dlg_ppl_total.getText().toString();
                        String cur = dlg_ppl_cur.getText().toString();

                        total_ppl.setText(total + " 명과 함께 여행을 떠나요!");
                        cur_ppl.setText("현재 " + cur + " 명이 함께 해요!");

                        layout_go_ppl.setVisibility(View.VISIBLE);
                        ques_ppl.setVisibility(View.GONE);
                    }
                });

                dlg.show();
            }
        });

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
            String date = year + " / " + (monthOfYear + 1) + " / " + dayOfMonth + " ~ " + yearEnd + " / " + (monthOfYearEnd + 1) + " / " + dayOfMonthEnd;
            range.setText(date);

            day_night.setText(String.valueOf(Math.abs(night)) + "박" + String.valueOf(day) + "일");

            range.setVisibility(View.VISIBLE);
            day_night.setVisibility(View.VISIBLE);
            ques_date.setVisibility(View.GONE);
        }
    }
}
