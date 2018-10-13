package com.example.jmkim.nomad;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class PartyselectActivity extends Activity{

    Button btn_nnnext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.party_select);

        btn_nnnext=(Button)findViewById(R.id.next);
        btn_nnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WelcomeActivity.class); //환영합니다 페이지로 이동
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                Intent intent=new Intent(getApplicationContext(),Gen_ageActivity.class);
                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    } //뒤로가기 버튼 안되게 하기 위함
}
