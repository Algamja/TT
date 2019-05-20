package com.example.jmkim.nomad.CE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DaysActivity extends AppCompatActivity {

    private TextView dayN;
    private ImageButton memo_plus;
    private ImageButton memo;
    private ImageButton memo_oneline;
    private EditText memo_editText;
    private TextView WPD_hashtag;
    private LinearLayout WPD_ll_repeat;

    private MemoActivity memoActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.writeplan_days);

        dayN = (TextView) findViewById(R.id.WPD_day);
        memo_plus = (ImageButton) findViewById(R.id.memo_plus);
        memo = (ImageButton) findViewById(R.id.memo);
        memo_oneline = (ImageButton) findViewById(R.id.memo_oneline);
        memo_editText = (EditText) findViewById(R.id.memo_editText);
        WPD_hashtag = (TextView) findViewById(R.id.WPD_hashtag);
        WPD_ll_repeat = (LinearLayout) findViewById(R.id.WPD_ll_repeat);

        memo_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //memo 작성 페이지로 넘어가도록
                Intent intent = new Intent(DaysActivity.this, MemoActivity.class);
                startActivity(intent);

                //R.id.memo 이미지가 옆에 뜨도록
                memoActivity = (MemoActivity)getApplicationContext();
                if(memoActivity.saveBtn.callOnClick())
                    memo.setVisibility(View.VISIBLE);


                //또 추가하면 또 옆에 뜨도록
            }
        });

        memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //memo 저장된 페이지로 넘어가도록
            }
        });

        memo_oneline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //클릭하면 memo_editText가 보여지고 memo_oneline은 사라짐
            }
        });

        //memo_editText 자동 저장..


    }
}
