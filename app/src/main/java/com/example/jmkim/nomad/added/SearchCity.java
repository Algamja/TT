package com.example.jmkim.nomad.added;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.DbOpenHelper;
import com.example.jmkim.nomad.prev.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SearchCity extends AppCompatActivity {
    private LinearLayout top;
    private LinearLayout search_layout;

    private EditText search;
    private ListView cities;

    private List<String> list;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;

    private DbOpenHelper mDbOpenHelper;
    private Cursor iCursor;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_search_city);

        SearchFragment parent = (SearchFragment) SearchFragment.fragment_search;

        top = (LinearLayout)findViewById(R.id.search_city_top);
        search_layout = (LinearLayout)findViewById(R.id.search_city_search);

        search = findViewById(R.id.plan_search);
        cities = findViewById(R.id.plan_city);

        list = new ArrayList<String>();
        settingList();

        arrayList = new ArrayList<String>();
        arrayList.addAll(list);

        adapter = new SearchAdapter(list,getApplicationContext());
        cities.setAdapter(adapter);

        String type = getIntent().getStringExtra("type");
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)search_layout.getLayoutParams();
        params.topMargin = 0;

        if(type.equals("search")){
            top.setVisibility(View.GONE);
            search_layout.setLayoutParams(params);
        }

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(search.getText().toString().equals("")) {
                    cities.setVisibility(View.GONE);
                }
                else{
                    cities.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search.getText().toString();
                searching(text);
            }
        });

        cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String city = list.get(position);
                String country[] = city.split(" ");

                if(type.equals("search")){
                    Toast.makeText(SearchCity.this, "IN", Toast.LENGTH_SHORT).show();

                    final Dialog dlg = new Dialog(SearchCity.this);
                    dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dlg.setContentView(R.layout.added_pop_confirm);
                    dlg.show();

                    final TextView title = (TextView)dlg.findViewById(R.id.confirm_title);
                    final TextView content = (TextView)dlg.findViewById(R.id.confirm_content);
                    final TextView ok = dlg.findViewById(R.id.confirm_ok);
                    final TextView cancel = dlg.findViewById(R.id.confirm_cancel);

                    title.setText(city+" (을)를 선택하셨습니다");
                    content.setText("일정 작성을 시작할까요?");

                    ok.setOnClickListener(v -> {
                        dlg.dismiss();

                        Intent intent = new Intent(SearchCity.this, WritePlan.class);
                        intent.putExtra("city", city);
                        intent.putExtra("country",country[country.length-1]);
                        startActivity(intent);
                        finish();
                    });

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.dismiss();
                        }
                    });
                }else if(type.equals("plan")){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("city", city);
                    resultIntent.putExtra("country",country[country.length-1]);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }

    private void searching(String charText){
        list.clear();

        if(charText.length() != 0){
            for(int i=0;i<arrayList.size();i++){
                if(arrayList.get(i).toLowerCase().contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void settingList(){
        mDbOpenHelper = new DbOpenHelper(getApplicationContext());
        mDbOpenHelper.open();

        iCursor = mDbOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            String tmpCity = iCursor.getString(iCursor.getColumnIndex("city"));
            list.add(tmpCity);
        }
    }
}