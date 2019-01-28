package com.example.jmkim.nomad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.Fragment.FragmentPageAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        ViewPager main_banner_vp = (ViewPager) findViewById(R.id.scroll_vp);
        FragmentManager fm = getSupportFragmentManager();
        FragmentPageAdapter pageAdapter = new FragmentPageAdapter(fm);
        main_banner_vp.setAdapter(pageAdapter); //스와이프 부분 끝

        close = new Close(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener main_navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){

                        case R.id.nav_home:
                            break;

                        case R.id.nav_search:
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                            finish();
                            break;

                        case R.id.nav_add:
                            BottomSheetDialog bottomSheetDialog = BottomSheetDialog.getInstance();
                            bottomSheetDialog.show(getSupportFragmentManager(),"bottomSheet");
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