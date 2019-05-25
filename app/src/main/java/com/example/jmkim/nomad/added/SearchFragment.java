package com.example.jmkim.nomad.added;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.RoundedTransformation;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {
    public static Fragment fragment_search;

    private RadioButton city;
    private RadioButton hashtag;
    private LinearLayout search;

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

    public static SearchFragment create() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment_search = SearchFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.added_frag2, container, false);

        city = (RadioButton) rootView.findViewById(R.id.search_rb_city);
        hashtag = (RadioButton) rootView.findViewById(R.id.search_rb_hashtag);

        search = (LinearLayout)rootView.findViewById(R.id.search_layout);

        recom_1 = (Button)rootView.findViewById(R.id.search_recom_word_1);
        recom_2 = (Button)rootView.findViewById(R.id.search_recom_word_2);
        recom_3 = (Button)rootView.findViewById(R.id.search_recom_word_3);
        recom_4 = (Button)rootView.findViewById(R.id.search_recom_word_4);
        recom_5 = (Button)rootView.findViewById(R.id.search_recom_word_5);
        recom_6 = (Button)rootView.findViewById(R.id.search_recom_word_6);
        recom_7 = (Button)rootView.findViewById(R.id.search_recom_word_7);
        recom_8 = (Button)rootView.findViewById(R.id.search_recom_word_8); //추천 검색어

        firstImg = (ImageView)rootView.findViewById(R.id.search_recom_plan_1);
        secondImg = (ImageView)rootView.findViewById(R.id.search_recom_plan_2);
        thirdImg = (ImageView)rootView.findViewById(R.id.search_recom_plan_3);
        fourthImg = (ImageView)rootView.findViewById(R.id.search_recom_plan_4);
        fifthImg = (ImageView)rootView.findViewById(R.id.search_recom_plan_5);
        sixthImg = (ImageView)rootView.findViewById(R.id.search_recom_plan_6); //국가 이미지

        recom_1.setText("도쿄");
        recom_2.setText("오사카");
        recom_3.setText("삿포로");
        recom_4.setText("오키나와");
        recom_5.setText("베이징");
        recom_6.setText("상하이");
        recom_7.setText("시안");
        recom_8.setText("마닐라");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(city.isChecked()){
                    Intent intent = new Intent(getContext(), SearchCity.class);
                    intent.putExtra("type","search");
                    startActivity(intent);
                }else if(hashtag.isChecked()){
                    Intent intent = new Intent(getContext(), SearchTag.class);
                    intent.putExtra("type","search");
                    startActivity(intent);
                }
            }
        });

        city.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (city.isChecked()) {
                    String color = "#000000";
                    city.setTextColor(Color.parseColor(color));
                    color = "#a6a6a6";
                    hashtag.setTextColor(Color.parseColor(color));

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
                    hashtag.setTextColor(Color.parseColor(color));

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

        hashtag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (city.isChecked()) {
                    String color = "#000000";
                    city.setTextColor(Color.parseColor(color));
                    color = "#a6a6a6";
                    hashtag.setTextColor(Color.parseColor(color));

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
                    hashtag.setTextColor(Color.parseColor(color));

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

        return rootView;
    }
}