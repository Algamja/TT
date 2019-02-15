package com.example.jmkim.nomad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RadioButton city;
    private RadioButton activity;

    private ImageView search;

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
    private LinearLayout recent;
    private LinearLayout focus;
    private ListView city_list;

    private BottomNavigationView Search_bottomNavigationView;

    private View black;

    private FrameLayout fb_layout;
    private FloatingActionButton write_plan;
    private FloatingActionButton write_review;

    private Boolean bottom_click = false;

    private List<String> list;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;

    private int click = 0;

    private DbOpenHelper mDbOpenHelper;
    private Cursor iCursor;

    private Close close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.searchActivity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar  = getSupportActionBar();

        getSupportActionBar().setTitle("");

        city = (RadioButton) findViewById(R.id.searchActivity_rb_city);
        activity = (RadioButton) findViewById(R.id.searchActivity_rb_activity);

        search = (ImageView)findViewById(R.id.searchActivity_iv_search);

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
        recent = (LinearLayout)findViewById(R.id.searchActivity_ll_recent);
        nonfocus = (LinearLayout)findViewById(R.id.searchActivity_ll_nonfocus);
        city_list = (ListView)findViewById(R.id.searchActivity_lv_city);

        black = (View)findViewById(R.id.searchActivity_view_black);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        black.setMinimumHeight(size.y);

        fb_layout = (FrameLayout)findViewById(R.id.searchActivity_ll_write_float);
        write_plan = (FloatingActionButton)findViewById(R.id.searchActivity_fb_write_plan);
        write_review = (FloatingActionButton)findViewById(R.id.searchActivity_fb_write_review);

        Search_bottomNavigationView = (BottomNavigationView) findViewById(R.id.searchActivity_bottomNavigation);
        Search_bottomNavigationView.setOnNavigationItemSelectedListener(search_navigationItemSelectedListner);

        Search_bottomNavigationView.setSelectedItemId(R.id.nav_search);

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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                if(click %2 == 0){
                    nonfocus.setVisibility(View.VISIBLE);
                    focus.setVisibility(View.GONE);
                }else{
                    nonfocus.setVisibility(View.GONE);
                    focus.setVisibility(View.VISIBLE);
                    recent.setVisibility(View.VISIBLE);
                    city_list.setVisibility(View.GONE);
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
                if(search_city.getText().toString().equals("")){
                    recent.setVisibility(View.VISIBLE);
                    city_list.setVisibility(View.GONE);
                }else{
                    recent.setVisibility(View.GONE);
                    city_list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_city.getText().toString();
                search(text);
            }
        });

        black.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Search_bottomNavigationView.setSelectedItemId(R.id.nav_search);
                fb_layout.setVisibility(View.INVISIBLE);
                bottom_click = false;
                black.setVisibility(View.GONE);
                return false;
            }
        });

        if (!bottom_click){
            Search_bottomNavigationView.setSelectedItemId(R.id.nav_search);
        }

        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        write_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, WritePlanActivity.class));
            }
        });

        close = new Close(this);
    }

    public void search(String charText){
        list.clear();

        if (charText.length() != 0) {
            for(int i=0;i<arrayList.size();i++){
                if(arrayList.get(i).toLowerCase().contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void settingList(){
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        iCursor = mDbOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            String tmpCity = iCursor.getString(iCursor.getColumnIndex("city"));
            list.add(tmpCity);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener search_navigationItemSelectedListner =
            new BottomNavigationView.OnNavigationItemSelectedListener(){

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){

                        case R.id.nav_home:
                            startActivity(new Intent(SearchActivity.this, MainActivity.class));
                            finish();
                            break;

                        case R.id.nav_search:
                            break;

                        case R.id.nav_add:
                            if(bottom_click){
                                fb_layout.setVisibility(View.GONE);
                                bottom_click = false;
                                black.setVisibility(View.GONE);
                            }else{
                                fb_layout.setVisibility(View.VISIBLE);
                                bottom_click = true;
                                black.setVisibility(View.VISIBLE);
                                black.bringToFront();
                            }
                            break;

                        case R.id.nav_profile:
                            startActivity(new Intent(SearchActivity.this, MypageActivity.class));
                            finish();
                            break;

                    }
                    return true;
                }
            };

    @Override
    public void onBackPressed() {
        close.onBackPressed();
    }
}
