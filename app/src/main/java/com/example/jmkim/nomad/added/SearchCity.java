package com.example.jmkim.nomad.added;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.DbOpenHelper;
import com.example.jmkim.nomad.prev.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SearchCity extends AppCompatActivity {
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

        search = findViewById(R.id.plan_search);
        cities = findViewById(R.id.plan_city);

        list = new ArrayList<String>();
        settingList();

        arrayList = new ArrayList<String>();
        arrayList.addAll(list);

        adapter = new SearchAdapter(list,getApplicationContext());
        cities.setAdapter(adapter);

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

                Intent resultIntent = new Intent();
                resultIntent.putExtra("city", city);
                resultIntent.putExtra("country",country[country.length-1]);
                setResult(RESULT_OK, resultIntent);
                finish();
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