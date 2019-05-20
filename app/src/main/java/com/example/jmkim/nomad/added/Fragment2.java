package com.example.jmkim.nomad.added;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.DbOpenHelper;
import com.example.jmkim.nomad.prev.RoundedTransformation;
import com.example.jmkim.nomad.prev.SearchAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;

public class Fragment2 extends Fragment {

    private ScrollView sv;

    private RadioButton city;
    private RadioButton activity;

    private ImageView search;

    private Button recom_1;
    private Button recom_2;
    private Button recom_3;
    private Button recom_4;
    private Button recom_5;
    private Button recom_6;
    private Button recom_7;
    private Button recom_8;

    private ImageView firstImg;
    private ImageView secondImg;
    private ImageView thirdImg;
    private ImageView fourthImg;
    private ImageView fifthImg;
    private ImageView sixthImg;

    private EditText search_city;

    private LinearLayout nonfocus;
    private LinearLayout recent;
    private LinearLayout focus;
    private ListView city_list;

    private Boolean bottom_click = false;

    private List<String> list;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;

    private int click = 0;

    private DbOpenHelper mDbOpenHelper;
    private Cursor iCursor;

    public static Fragment2 create() {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        //args.putInt("image", image);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //image = getArguments().getInt("image");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.added_frag2, container, false);

        sv = (ScrollView)rootView.findViewById(R.id.searchActivity_sv);

        city = (RadioButton) rootView.findViewById(R.id.searchActivity_rb_city);
        activity = (RadioButton) rootView.findViewById(R.id.searchActivity_rb_activity);

        search = (ImageView)rootView.findViewById(R.id.searchActivity_iv_search);

        recom_1 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_1);
        recom_2 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_2);
        recom_3 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_3);
        recom_4 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_4);
        recom_5 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_5);
        recom_6 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_6);
        recom_7 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_7);
        recom_8 = (Button)rootView.findViewById(R.id.searchActivity_btn_recommend_8); //추천 검색어

        firstImg = (ImageView)rootView.findViewById(R.id.searchActivity_iv_1);
        secondImg = (ImageView)rootView.findViewById(R.id.searchActivity_iv_2);
        thirdImg = (ImageView)rootView.findViewById(R.id.searchActivity_iv_3);
        fourthImg = (ImageView)rootView.findViewById(R.id.searchActivity_iv_4);
        fifthImg = (ImageView)rootView.findViewById(R.id.searchActivity_iv_5);
        sixthImg = (ImageView)rootView.findViewById(R.id.searchActivity_iv_6); //국가 이미지

        search_city = (EditText)rootView.findViewById(R.id.searchActivity_et_search);

        focus = (LinearLayout)rootView.findViewById(R.id.searchActivity_ll_focus);
        recent = (LinearLayout)rootView.findViewById(R.id.searchActivity_ll_recent);
        nonfocus = (LinearLayout)rootView.findViewById(R.id.searchActivity_ll_nonfocus);
        city_list = (ListView)rootView.findViewById(R.id.searchActivity_lv_city);

        recom_1.setText("도쿄");
        recom_2.setText("오사카");
        recom_3.setText("삿포로");
        recom_4.setText("오키나와");
        recom_5.setText("베이징");
        recom_6.setText("상하이");
        recom_7.setText("시안");
        recom_8.setText("마닐라");

        city.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (city.isChecked()) {
                    String color = "#000000";
                    city.setTextColor(Color.parseColor(color));
                    color = "#a6a6a6";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("도쿄");
                    recom_2.setText("오사카");
                    recom_3.setText("삿포로");
                    recom_4.setText("오키나와");
                    recom_5.setText("베이징");
                    recom_6.setText("상하이");
                    recom_7.setText("시안");
                    recom_8.setText("마닐라");

                } else {
                    String color = "#a6a6a6";
                    city.setTextColor(Color.parseColor(color));
                    color = "#000000";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("온천");
                    recom_2.setText("스키");
                    recom_3.setText("맛집");
                    recom_4.setText("야시장");
                    recom_5.setText("호캉스");
                    recom_6.setText("명품");
                    recom_7.setText("쇼핑");
                    recom_8.setText("키덜트");
                }
            }
        });

        activity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (city.isChecked()) {
                    String color = "#000000";
                    city.setTextColor(Color.parseColor(color));
                    color = "#a6a6a6";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("도쿄");
                    recom_2.setText("오사카");
                    recom_3.setText("삿포로");
                    recom_4.setText("오키나와");
                    recom_5.setText("베이징");
                    recom_6.setText("상하이");
                    recom_7.setText("시안");
                    recom_8.setText("마닐라");
                } else {
                    String color = "#a6a6a6";
                    city.setTextColor(Color.parseColor(color));
                    color = "#000000";
                    activity.setTextColor(Color.parseColor(color));

                    recom_1.setText("온천");
                    recom_2.setText("스키");
                    recom_3.setText("맛집");
                    recom_4.setText("야시장");
                    recom_5.setText("호캉스");
                    recom_6.setText("명품");
                    recom_7.setText("쇼핑");
                    recom_8.setText("키덜트");
                }
            }
        });

        Picasso.with(getContext())
                .load(R.drawable.flag)
                .transform(new RoundedTransformation(100,0))
                .into(firstImg);

        Picasso.with(getContext())
                .load(R.drawable.england)
                .transform(new RoundedTransformation(100,0))
                .into(secondImg);

        Picasso.with(getContext())
                .load(R.drawable.canada)
                .transform(new RoundedTransformation(100,0))
                .into(thirdImg);

        Picasso.with(getContext())
                .load(R.drawable.canada)
                .transform(new RoundedTransformation(100,0))
                .into(fourthImg);

        Picasso.with(getContext())
                .load(R.drawable.flag)
                .transform(new RoundedTransformation(100,0))
                .into(fifthImg);

        Picasso.with(getContext())
                .load(R.drawable.england)
                .transform(new RoundedTransformation(100,0))
                .into(sixthImg);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                if(click %2 == 0){
                    nonfocus.setVisibility(View.VISIBLE);
                    focus.setVisibility(View.GONE);
                    recent.setVisibility(View.GONE);
                }else{
                    nonfocus.setVisibility(View.GONE);
                    focus.setVisibility(View.VISIBLE);
                    recent.setVisibility(View.VISIBLE);
                    city_list.setVisibility(View.GONE);
                }
            }
        });

        list = new ArrayList<String>();
        settingList();

        arrayList = new ArrayList<String>();
        arrayList.addAll(list);

        adapter = new SearchAdapter(list,getContext());
        city_list.setAdapter(adapter);

        search_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(click %2 != 0){
                    if(search_city.getText().toString().equals("")){
                        recent.setVisibility(View.VISIBLE);
                        city_list.setVisibility(View.GONE);
                    }else{
                        recent.setVisibility(View.GONE);
                        city_list.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_city.getText().toString();
                search(text);
            }
        });

        return rootView;
    }

    public void search(String charText){
        list.clear();

        if (charText.length() != 0) {
            for(int i=0;i<arrayList.size();i++){
                if(arrayList.get(i).toLowerCase().contains(charText)){
                    list.add(arrayList.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void settingList(){
        mDbOpenHelper = new DbOpenHelper(getContext());
        mDbOpenHelper.open();

        iCursor = mDbOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            String tmpCity = iCursor.getString(iCursor.getColumnIndex("city"));
            list.add(tmpCity);
        }
    }
}