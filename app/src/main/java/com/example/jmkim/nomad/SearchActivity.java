package com.example.jmkim.nomad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    private RadioButton city;
    private RadioButton activity;

    private Button recom_1;
    private Button recom_2;
    private Button recom_3;
    private Button recom_4;
    private Button recom_5;
    private Button recom_6;
    private Button recom_7;
    private Button recom_8;

    private ImageView firstImg;
    private ImageView secondImg;
    private ImageView thirdImg;
    private ImageView fourthImg;
    private ImageView fifthImg;
    private ImageView sixthImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        city = (RadioButton) findViewById(R.id.searchActivity_rb_city);
        activity = (RadioButton) findViewById(R.id.searchActivity_rb_activity);

        recom_1 = (Button)findViewById(R.id.searchActivity_btn_recommend_1);
        recom_2 = (Button)findViewById(R.id.searchActivity_btn_recommend_2);
        recom_3 = (Button)findViewById(R.id.searchActivity_btn_recommend_3);
        recom_4 = (Button)findViewById(R.id.searchActivity_btn_recommend_4);
        recom_5 = (Button)findViewById(R.id.searchActivity_btn_recommend_5);
        recom_6 = (Button)findViewById(R.id.searchActivity_btn_recommend_6);
        recom_7 = (Button)findViewById(R.id.searchActivity_btn_recommend_7);
        recom_8 = (Button)findViewById(R.id.searchActivity_btn_recommend_8);

        firstImg = (ImageView)findViewById(R.id.searchActivity_iv_1);
        secondImg = (ImageView)findViewById(R.id.searchActivity_iv_2);
        thirdImg = (ImageView)findViewById(R.id.searchActivity_iv_3);
        fourthImg = (ImageView)findViewById(R.id.searchActivity_iv_4);
        fifthImg = (ImageView)findViewById(R.id.searchActivity_iv_5);
        sixthImg = (ImageView)findViewById(R.id.searchActivity_iv_6);

        city.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (city.isChecked()) {
                    String color = "#000000";
                    city.setTextColor(Color.parseColor(color));
                    color = "#a6a6a6";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("도쿄");
                    recom_2.setText("도쿄");
                    recom_3.setText("도쿄");
                    recom_4.setText("도쿄");
                    recom_5.setText("도쿄");
                    recom_6.setText("도쿄");
                    recom_7.setText("도쿄");
                    recom_8.setText("도쿄");

                } else {
                    String color = "#a6a6a6";
                    city.setTextColor(Color.parseColor(color));
                    color = "#000000";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("온천");
                    recom_2.setText("온천");
                    recom_3.setText("온천");
                    recom_4.setText("온천");
                    recom_5.setText("온천");
                    recom_6.setText("온천");
                    recom_7.setText("온천");
                    recom_8.setText("온천");
                }
            }
        });

        activity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (city.isChecked()) {
                    String color = "#000000";
                    city.setTextColor(Color.parseColor(color));
                    color = "#a6a6a6";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("도쿄");
                    recom_2.setText("도쿄");
                    recom_3.setText("도쿄");
                    recom_4.setText("도쿄");
                    recom_5.setText("도쿄");
                    recom_6.setText("도쿄");
                    recom_7.setText("도쿄");
                    recom_8.setText("도쿄");
                } else {
                    String color = "#a6a6a6";
                    city.setTextColor(Color.parseColor(color));
                    color = "#000000";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("온천");
                    recom_2.setText("온천");
                    recom_3.setText("온천");
                    recom_4.setText("온천");
                    recom_5.setText("온천");
                    recom_6.setText("온천");
                    recom_7.setText("온천");
                    recom_8.setText("온천");
                }
            }
        });

        Picasso.with(getApplicationContext())
                .load(R.drawable.flag)
                .transform(new RoundedTransformation(100,0))
                .into(firstImg);

        Picasso.with(getApplicationContext())
                .load(R.drawable.england)
                .transform(new RoundedTransformation(100,0))
                .into(secondImg);

        Picasso.with(getApplicationContext())
                .load(R.drawable.canada)
                .transform(new RoundedTransformation(100,0))
                .into(thirdImg);

        Picasso.with(getApplicationContext())
                .load(R.drawable.canada)
                .transform(new RoundedTransformation(100,0))
                .into(fourthImg);

        Picasso.with(getApplicationContext())
                .load(R.drawable.flag)
                .transform(new RoundedTransformation(100,0))
                .into(fifthImg);

        Picasso.with(getApplicationContext())
                .load(R.drawable.england)
                .transform(new RoundedTransformation(100,0))
                .into(sixthImg);
    }
}
