package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class Gen_ageActivity extends Activity{

    Button btn_nnext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gen_age);

        btn_nnext = (Button)findViewById(R.id.btn_nnext);

        btn_nnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PartyselectActivity.class); //파티원 고르는 페이지로 이동
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                Intent intent=new Intent(getApplicationContext(),SigninActivity.class);
                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish();
            } //뒤로가기 안하려고
        }
    }
}
