package com.example.jmkim.nomad;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.jmkim.nomad.Fragment.FragmentPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private RecyclerView board_recycler;
    private RecyclerView.LayoutManager board_layoutManager;

    private BottomNavigationView Main_bottomNavigationView;

    private LinearLayout linear_board_1;
    private LinearLayout linear_board_2;
    private LinearLayout linear_board_3;

    private ImageView board_image_2;
    private ImageView board_image_3;

    private View view;

    private FrameLayout fb_layout;
    private FloatingActionButton write_plan;
    private FloatingActionButton write_review;

    private Boolean is_clicked = false;

    private Close close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setTitle(""); //app이름 없애기 위함

        board_recycler = findViewById(R.id.recycler_view);
        board_recycler.setHasFixedSize(true);

        board_layoutManager = new LinearLayoutManager(this);
        board_recycler.setLayoutManager(board_layoutManager);

        view = (View)findViewById(R.id.black_view);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        view.setMinimumHeight((size.y)*3);

        fb_layout = (FrameLayout)findViewById(R.id.main_ll_write_float);
        write_plan = (FloatingActionButton)findViewById(R.id.main_fb_write_plan);
        write_review = (FloatingActionButton)findViewById(R.id.main_fb_write_review);

        Main_bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_bottomNavigation);
        Main_bottomNavigationView.setOnNavigationItemSelectedListener(main_navigationItemSelectedListener);

        Main_bottomNavigationView.setSelectedItemId(R.id.nav_home);

        ArrayList<BoardInfo> boardInfos = new ArrayList<>();
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));

        MyAdapter myAdapter = new MyAdapter(boardInfos);

        board_recycler.setAdapter(myAdapter);

        ViewPager main_banner_vp = (ViewPager) findViewById(R.id.main_scroll_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        main_banner_vp.setAdapter(pageAdapter); //스와이프 부분 끝

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Main_bottomNavigationView.setSelectedItemId(R.id.nav_home);
                fb_layout.setVisibility(View.INVISIBLE);
                is_clicked = false;
                view.setVisibility(View.INVISIBLE);
                return false;
            }
        });

        if(!is_clicked){
            Main_bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        write_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WritePlanActivity.class));
            }
        });

        close = new Close(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener main_navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){

                        case R.id.nav_home:
                            fb_layout.setVisibility(View.INVISIBLE);
                            is_clicked = false;
                            view.setVisibility(View.INVISIBLE);
                            break;

                        case R.id.nav_search:
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                            finish();
                            break;

                        case R.id.nav_add:
                            if(is_clicked){
                                fb_layout.setVisibility(View.INVISIBLE);
                                is_clicked = false;
                                view.setVisibility(View.INVISIBLE);
                            }else {
                                fb_layout.setVisibility(View.VISIBLE);
                                is_clicked = true;
                                view.setVisibility(View.VISIBLE);
                                view.bringToFront();
                            }
                            break;

                        case R.id.nav_profile:
                            startActivity(new Intent(MainActivity.this, MypageActivity.class));
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