package com.example.jmkim.nomad.prev;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jmkim.nomad.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PlanReadActivity extends AppCompatActivity {
    private View pw_dlg;
    private AlertDialog dlg = null;

    private Button confirm_btn;
    private Button pw_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planread);

        confirm_btn = (Button)findViewById(R.id.btn_confirm);

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw_dlg = View.inflate(PlanReadActivity.this, R.layout.password_dialog, null);
                dlg = new AlertDialog.Builder(PlanReadActivity.this).create();
                dlg.setView(pw_dlg);
                dlg.show();

                pw_btn = (Button)findViewById(R.id.pass_btn);
                pw_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //단체 채팅방에 들어가는 과정
                    }
                });
            }
        });


    }
}
