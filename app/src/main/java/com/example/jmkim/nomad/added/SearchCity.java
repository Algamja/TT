package com.example.jmkim.nomad.added;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.jmkim.nomad.R;

import androidx.appcompat.app.AppCompatActivity;

public class SearchCity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_search_city);

        TextView itemCity = findViewById(R.id.itemCity);
        TextView itemCity1 = findViewById(R.id.itemCity1);
        TextView itemCity2 = findViewById(R.id.itemCity2);

        itemCity.setOnClickListener(l);
        itemCity1.setOnClickListener(l);
        itemCity2.setOnClickListener(l);
    }

    View.OnClickListener l = view -> {
        final Dialog dlg = new Dialog(SearchCity.this);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.added_pop_confirm);
        dlg.show();

        final TextView title = dlg.findViewById(R.id.title);
        final TextView content = dlg.findViewById(R.id.content);
        final TextView ok = dlg.findViewById(R.id.ok);
        final TextView cancel = dlg.findViewById(R.id.cancel);

        title.setText("일정이 있습니다!");
        content.setText("해당 국가에 관한 스크랩 일정이 8개 있습니다. 불러올까요?");

        ok.setOnClickListener(v -> {
            dlg.dismiss();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("city", "yes");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        cancel.setOnClickListener(v -> {
            dlg.dismiss();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("city", "no");
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    };
}