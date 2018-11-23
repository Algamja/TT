package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.MalformedURLException;

public class WelcomeActivity extends Activity{

    Button mainpage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        NetworkUtil.setNetWorkPlicy();

        mainpage=(Button)findViewById(R.id.mainpage);
        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getintent = getIntent();

                try {
                    PHPRequest request = new PHPRequest("http://nomad.dothome.co.kr/insert.php");
                    String result = request.PhPtest(getintent.getStringExtra("ID"),getintent.getStringExtra("PW"),getintent.getStringExtra("NAME"),getintent.getStringExtra("PHONE"),getintent.getStringExtra("SEX"),getintent.getStringExtra("AGE"));
                    if(result.equals("1")){
                        Toast.makeText(getApplication(),"회원가입이 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplication(),"회원가입에 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                    }
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), PartyselectActivity.class);

                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish(); //뒤로가기 안하려고
            }
        });
    }
}