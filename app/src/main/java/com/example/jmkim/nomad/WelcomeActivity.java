package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends Activity {

    private Button signIn;
    private Button singUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        signIn = (Button)findViewById(R.id.welcomeActivity_btn_signIn);
        singUp = (Button)findViewById(R.id.welcomeActivity_btn_signUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,SignInsActivity.class));
                finish();
            }
        });

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,SignUpActivity.class));
                finish();
            }
        });
    }
}
