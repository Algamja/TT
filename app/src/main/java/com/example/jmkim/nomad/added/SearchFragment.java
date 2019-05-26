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
import android.widget.TextView;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.PlanReadActivity;
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

    private TextView city_1;
    private TextView city_2;
    private TextView city_3;
    private TextView city_4;
    private TextView city_5;
    private TextView city_6;

    private TextView name_1;
    private TextView name_2;
    private TextView name_3;
    private TextView name_4;
    private TextView name_5;
    private TextView name_6;

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

        city_1 = (TextView)rootView.findViewById(R.id.search_recom_city_1);
        city_2 = (TextView)rootView.findViewById(R.id.search_recom_city_2);;
        city_3 = (TextView)rootView.findViewById(R.id.search_recom_city_3);;
        city_4 = (TextView)rootView.findViewById(R.id.search_recom_city_4);;
        city_5 = (TextView)rootView.findViewById(R.id.search_recom_city_5);;
        city_6 = (TextView)rootView.findViewById(R.id.search_recom_city_6);;

        name_1 = (TextView)rootView.findViewById(R.id.search_recom_name_1);
        name_2 = (TextView)rootView.findViewById(R.id.search_recom_name_2);
        name_3 = (TextView)rootView.findViewById(R.id.search_recom_name_3);
        name_4 = (TextView)rootView.findViewById(R.id.search_recom_name_4);
        name_5 = (TextView)rootView.findViewById(R.id.search_recom_name_5);
        name_6 = (TextView)rootView.findViewById(R.id.search_recom_name_6);

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
                .load(R.drawable.dokyo)
                .transform(new RoundedTransformation(100,0))
                .into(firstImg);

        Picasso.with(getContext())
                .load(R.drawable.yamagata)
                .transform(new RoundedTransformation(100,0))
                .into(secondImg);

        Picasso.with(getContext())
                .load(R.drawable.yamanashi)
                .transform(new RoundedTransformation(100,0))
                .into(thirdImg);

        Picasso.with(getContext())
                .load(R.drawable.tsuzaka)
                .transform(new RoundedTransformation(100,0))
                .into(fourthImg);

        Picasso.with(getContext())
                .load(R.drawable.okinawa)
                .transform(new RoundedTransformation(100,0))
                .into(fifthImg);

        Picasso.with(getContext())
                .load(R.drawable.daisen)
                .transform(new RoundedTransformation(100,0))
                .into(sixthImg);

        city_1.setText("도쿄 도");
        city_2.setText("야마가타 현");
        city_3.setText("야마나시 현");
        city_4.setText("스자카 시");
        city_5.setText("오키나와 시");
        city_6.setText("다이센 시");

        name_1.setText("김자명");
        name_2.setText("여행자");
        name_3.setText("김자명");
        name_4.setText("최부탁");
        name_5.setText("테스트");
        name_6.setText("HJM");

        firstImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanReadActivity.class);
                intent.putExtra("publisher","9DT9bXFtbWeK9449mNeNPSIcMvm2");
                intent.putExtra("key","-LfZdbhk5lzKmI8cIlqC");
                startActivity(intent);
            }
        });

        secondImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanReadActivity.class);
                intent.putExtra("publisher","9eDtcPCYIcg3uoyDw8BqRD9LJMF3");
                intent.putExtra("key","-Lfp700Icpc-yFSvc_p6");
                startActivity(intent);
            }
        });

        thirdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanReadActivity.class);
                intent.putExtra("publisher","Edn1YAyL0gc3oXpg1jGxoKk68223");
                intent.putExtra("key","-Lfp9wvBSMKIuqs3m3D5");
                startActivity(intent);
            }
        });

        fourthImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanReadActivity.class);
                intent.putExtra("publisher","IX4niMXblUXdNfAJHfIgWi1T7uD3");
                intent.putExtra("key","-LfpC4OCSMiIQAAgp0ea");
                startActivity(intent);
            }
        });

        fifthImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanReadActivity.class);
                intent.putExtra("publisher","JKxyLrqXS7OfmpSKUa8Kj4ANQoB2");
                intent.putExtra("key","-LfpEEwkuVhDZzImRE7S");
                startActivity(intent);
            }
        });

        sixthImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PlanReadActivity.class);
                intent.putExtra("publisher","Nvi25OVRWKWKBexNbOXqZfVbnAi2");
                intent.putExtra("key","-LfpnILWcLS28dXfTfpG");
                startActivity(intent);
            }
        });

        return rootView;
    }
}