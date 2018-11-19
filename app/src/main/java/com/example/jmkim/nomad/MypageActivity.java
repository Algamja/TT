package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MypageActivity extends AppCompatActivity {

    Button btn_logout, btn_infofix;

    //NavigationDrawer
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    boolean isDrawerOpend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        btn_logout=(Button)findViewById(R.id.mainbtn_logout);
        btn_infofix=(Button)findViewById(R.id.mainbtn_infofix);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        //NavigationDrawer
        //커스텀 타이틀을 사용하기 때문에 ActionBar()에 기본 타이틀을 표시하지 않도록 false로 지정
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //DrawerLayout 참조
        drawer=(DrawerLayout)findViewById(R.id.main_drawer);

        /* ActionBarDrawerToggle 생성
           - 3, 4번째 매개변수:ActionBarDrawerToggle가 열린 상태, 닫힌 상태를 표현하기 위한 문자열(임의의 문자열 지정)
           - 하지만 화면 출력과 무관함으로 임의로 작성하면 됨(sting.xml)
        */
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);

        //ActionBar(ToolBar)에서 기본 홈 버튼(<-)을 사용 가능하도록 설정6
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ActionBarDrawerToggle 의 상태를 sync(동기화)
        toggle.syncState();

        //NavigationView 참조
        NavigationView navigationView=(NavigationView)findViewById(R.id.main_drawer_view);

        //navigationView에 이벤트 설정
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();

                if(id==R.id.nav_account){
                    showToast("Navigation... Account...");
                }else if(id==R.id.nav_contact_mail){
                    showToast("Navigation... Contact Mail...");
                }else if(id==R.id.nav_contact_phone){
                    showToast("Navigation... Contact Phone...");
                }else if(id==R.id.nav_setting){
                    showToast("Navigation... Setting...");
                }else if(id==R.id.nav_search){
                    showToast("Navigation... Search...");
                }else if(id==R.id.nav_zoom_in){
                    showToast("Navigation... Zoom In...");
                }else if(id==R.id.nav_zoom_out){
                    showToast("Navigation... Zoom Out...");
                }else if(id==R.id.nav_help){
                    showToast("Navigation... Help...");
                }else if(id==R.id.nav_home){
                    showToast("Navigation... Home...");
                }
                return false;
            }
        });
    }

    //Navigation Drawer 설정
    /* ActionBarDrawerToggle 아이콘 이벤트 처리
       - 내부적으로 ActionBarDrawerToogle 아이콘 클릭이 메뉴 이벤트로 처리되기 때문에
         onOptionsItemSelected() 메서드를 정의해야 NavigationDrawer가 열리거나 닫힘
       - onOptionsItemSelected() 메서드를 정의하지 않으면 토글 아이콘은 표시되지만
         열리거나 닫히지 않음
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 이벤트가 toggle에서 발생한 거라면 메뉴 이벤트 로직에서 벗어나게 처리
        if(toggle.onOptionsItemSelected(item)){
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    //Toast 메시지 메서드
    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}