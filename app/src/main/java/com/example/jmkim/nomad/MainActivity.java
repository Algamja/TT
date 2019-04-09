package com.example.jmkim.nomad;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.Fragment.FragmentPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private List<Board> boards;

    private Close close;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
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

        boards = new ArrayList<>();

        //main에서 어둡게 표시될 부분
        main.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                view.setMinimumHeight(main.getHeight() + main.getScrollY());
            }
        });

        passPushTokenToServer();

        //main에서 보일 글 부분
        //Board의 정보를 받아옴
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Board");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boards.clear();
                //boards에 Board DB 삽입
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Board board = snapshot.getValue(Board.class);

                    boards.add(board);

                    //BoardInfos List생성
                    final ArrayList<MainBoardInfo> mainBoardInfos = new ArrayList<>();

                    //boards 정보를 바탕으로 BoardInfos 입력
                    for(int i = 0; i < boards.size(); i++){
                        String publisher;
                        final List<UserModel> userModel = new ArrayList<>();

                        //글 작성자의 UID를 받아옴
                        publisher = boards.get(i).publisher;

                        //get()에서 사용하기 위한 final_i
                        final int final_i = i;

                        //작성자 UID를 사용하여 user의 프로필 사진을 가져옴
                        FirebaseDatabase.getInstance().getReference()
                                .child("UserBasic")
                                .child(publisher)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        userModel.clear();
                                        //userModel에 작성자 정보 입력
                                        userModel.add(dataSnapshot.getValue(UserModel.class));
                                        //boardInfos에 사용자 프로필, 글 제목, 국가 입력
                                        mainBoardInfos.add(new MainBoardInfo(boards.get(final_i).publisher, userModel.get(0).profileImageUrl, boards.get(final_i).title, boards.get(final_i).country));

                                        //boardInfos의 내용을 Adapter에 연결
                                        MainAdapter mainAdapter = new MainAdapter(getApplication(), mainBoardInfos);
                                        board_recycler.setAdapter(mainAdapter);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                //WriteReviewActivity로 이동
                startActivity(new Intent(MainActivity.this, WriteReviewActivity.class));
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

    void passPushTokenToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();
        HashMap<String,Object> map = new HashMap<>();
        map.put("pushToken",token);

        FirebaseDatabase.getInstance().getReference()
                .child("UserBasic")
                .child(uid).updateChildren(map);
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

                        case R.id.nav_heart:
                            startActivity(new Intent(MainActivity.this, TestActivity.class));
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