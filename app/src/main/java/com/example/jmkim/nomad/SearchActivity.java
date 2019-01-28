package com.example.jmkim.nomad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private final String dbName = "tripbbox";
    private final String tblName = "tb_city";

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

    private EditText search_city;

    private LinearLayout nonfocus;
    private LinearLayout focus;
    private ListView city_list;

    private List<String> list;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;

    int click = 0;

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
        recom_8 = (Button)findViewById(R.id.searchActivity_btn_recommend_8); //추천 검색어

        firstImg = (ImageView)findViewById(R.id.searchActivity_iv_1);
        secondImg = (ImageView)findViewById(R.id.searchActivity_iv_2);
        thirdImg = (ImageView)findViewById(R.id.searchActivity_iv_3);
        fourthImg = (ImageView)findViewById(R.id.searchActivity_iv_4);
        fifthImg = (ImageView)findViewById(R.id.searchActivity_iv_5);
        sixthImg = (ImageView)findViewById(R.id.searchActivity_iv_6); //국가 이미지

        search_city = (EditText)findViewById(R.id.searchActivity_et_search);

        focus = (LinearLayout)findViewById(R.id.searchActivity_ll_focus);
        nonfocus = (LinearLayout)findViewById(R.id.searchActivity_ll_nonfocus);
        city_list = (ListView)findViewById(R.id.searchActivity_lv_city);

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

        search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                if(click %2 == 0){
                    nonfocus.setVisibility(View.VISIBLE);
                    focus.setVisibility(View.GONE);
                }else{
                    nonfocus.setVisibility(View.GONE);
                    focus.setVisibility(View.VISIBLE);
                }
            }
        });

        list = new ArrayList<String>();
        settingList();

        arrayList = new ArrayList<String>();
        arrayList.addAll(list);

        adapter = new SearchAdapter(list,this);
        city_list.setAdapter(adapter);

        search_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_city.getText().toString();
                search(text);
            }
        });
    }

    public void search(String charText){
        list.clear();

        if (charText.length() == 0) {

        }else{
            for(int i=0;i<arrayList.size();i++){
                if(arrayList.get(i).toLowerCase().contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void settingList(){
        list.add("채수빈");
        list.add("박지현");
        list.add("수지");
        list.add("남태현");
        list.add("하성운");
        list.add("크리스탈");
        list.add("강승윤");
        list.add("손나은");
        list.add("남주혁");
        list.add("루이");
        list.add("진영");
        list.add("슬기");
        list.add("이해인");
        list.add("고원희");
        list.add("설리");
        list.add("공명");
        list.add("김예림");
        list.add("혜리");
        list.add("웬디");
        list.add("박혜수");
        list.add("카이");
        list.add("진세연");
        list.add("동호");
        list.add("박세완");
        list.add("도희");
        list.add("창모");
        list.add("허영지");
    }
}
