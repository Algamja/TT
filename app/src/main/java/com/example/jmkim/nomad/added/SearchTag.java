package com.example.jmkim.nomad.added;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.SearchAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchTag extends AppCompatActivity {
    private EditText search;
    private ListView tags;

    private List<String> list;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tag);

        search = findViewById(R.id.tag_search);
        tags = findViewById(R.id.tag_list);

        list = new ArrayList<String>();
        settingList();

        arrayList = new ArrayList<String>();

        adapter = new SearchAdapter(list, getApplicationContext());
        tags.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(search.getText().toString().equals("")) {
                    tags.setVisibility(View.GONE);
                }
                else{
                    tags.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text= search.getText().toString();
                searching(text);
            }
        });

        tags.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = getIntent().getStringExtra("type");
                String tag = list.get(position);

                if(type.equals("search")){
                    Intent intent = new Intent(SearchTag.this, HashTagInfoActivity.class);
                    intent.putExtra("tag",tag);
                    startActivity(intent);
                }else{
                    Intent result = new Intent();
                    result.putExtra("tag",tag);
                    setResult(RESULT_OK, result);
                    finish();
                }
            }
        });
    }

    private void searching(String text){
        list.clear();

        if(text.length() != 0){
            for(int i=0; i<arrayList.size(); i++){
                if(arrayList.get(i).toLowerCase().contains(text)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void settingList(){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Hashtag")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String tmpTag = snapshot.getKey();
                    list.add(tmpTag);
                    arrayList.add(tmpTag);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
