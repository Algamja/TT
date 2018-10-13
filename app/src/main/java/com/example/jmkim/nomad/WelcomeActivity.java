package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity{

    Button mainpage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        mainpage=(Button)findViewById(R.id.mainpage);
        mainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PartyselectActivity.class);

                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish(); //뒤로가기 안하려고
            }
        });
    }
}
