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
                Intent intent = new Intent(getApplicationContext(), PartyselectActivity.class);
                startActivity(intent);
            }
        });
    }
}
