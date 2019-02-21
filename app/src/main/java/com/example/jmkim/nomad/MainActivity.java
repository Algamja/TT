package com.example.jmkim.nomad;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    private ScrollView main;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setTitle(""); //app이름 없애기 위함

        main = (ScrollView)findViewById(R.id.main_scroll);

        board_recycler = findViewById(R.id.recycler_view);
        board_recycler.setHasFixedSize(true);

        board_layoutManager = new LinearLayoutManager(this);
        board_recycler.setLayoutManager(board_layoutManager);

        view = (View)findViewById(R.id.main_black_view);

        fb_layout = (FrameLayout)findViewById(R.id.main_ll_write_float);
        write_plan = (FloatingActionButton)findViewById(R.id.main_fb_write_plan);
        write_review = (FloatingActionButton)findViewById(R.id.main_fb_write_review);

        Main_bottomNavigationView = (BottomNavigationView)findViewById(R.id.main_bottomNavigation);
        Main_bottomNavigationView.setOnNavigationItemSelectedListener(main_navigationItemSelectedListener);

        Main_bottomNavigationView.setSelectedItemId(R.id.nav_home);


        //main에서 어둡게 표시될 부분
        main.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                view.setMinimumHeight(main.getHeight() + main.getScrollY());
            }
        });

        //게시글을 List형태로 입력
        ArrayList<BoardInfo> boardInfos = new ArrayList<>();
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));
        boardInfos.add(new BoardInfo(R.drawable.profile, "글 제목", "국가"));

        //Adapter와 연결
        MyAdapter myAdapter = new MyAdapter(getApplication(), boardInfos);
        board_recycler.setAdapter(myAdapter);

        //상단 스와이프
        ViewPager main_banner_vp = (ViewPager) findViewById(R.id.main_scroll_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        main_banner_vp.setAdapter(pageAdapter);

        //게시글 작성시 어두워지는 view 클릭했을 때
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

        //BottomNavigation에서 Home을 선택하기 위함
        if(!is_clicked){
            Main_bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        //리뷰 작성하기 클릭되었을 때
        write_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //일정 작성하기 클릭되었을 때
        write_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WritePlanActivity로 이동
                startActivity(new Intent(MainActivity.this, WritePlanActivity.class));
            }
        });

        //뒤로가기 2번 눌러서 종료
        close = new Close(this);
    }

    //BottomNavigationView
    private BottomNavigationView.OnNavigationItemSelectedListener main_navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){

                        //home 클릭되었을 때
                        case R.id.nav_home:
                            //FAB과 black_view를 숨김
                            fb_layout.setVisibility(View.INVISIBLE);
                            is_clicked = false;
                            view.setVisibility(View.INVISIBLE);
                            break;

                        //search 클릭되었을 때
                        case R.id.nav_search:
                            //SearchActivity 시작 및 MainActivity 종료
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                            finish();
                            break;

                        //게시글 추가 버튼
                        case R.id.nav_add:
                            //이미 버튼이 눌린 상태이면
                            if(is_clicked){
                                //FAB과 view 숨김
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

                        //프로필 눌렸을 때
                        case R.id.nav_profile:
                            //마이페이지 시작 및 메인페이지 종료
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