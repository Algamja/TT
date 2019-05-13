package com.example.jmkim.nomad.added;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.R;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class WritePlan extends AppCompatActivity implements SlyCalendarDialog.Callback {
    private LinearLayoutManager mLayoutManager;
    LinearLayout frame;
    LinearLayout frame_next;
    LinearLayout frame2;
    LinearLayout frame2_next;
    GridView cities;

    TextView info;
    TextView info1;
    TextView info2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_write_plan);

        frame = findViewById(R.id.frame);
        frame_next = findViewById(R.id.frame_next);
        frame2 = findViewById(R.id.frame2);
        frame2_next = findViewById(R.id.frame2_next);

        info = findViewById(R.id.info);
        info1 = findViewById(R.id.info1);
        info2 = findViewById(R.id.info2);

        // 검색
        EditText search = findViewById(R.id.search);
        search.setOnClickListener(view -> {
            startActivityForResult(new Intent(WritePlan.this, SearchCity.class), 0);
            overridePendingTransition(0, android.R.anim.fade_in);
        });

        // 도시목록
        cities = findViewById(R.id.cities);
        CitiesAdapter citiesAdapter = new CitiesAdapter(WritePlan.this);
        cities.setAdapter(citiesAdapter);

        // 날짜/인원
        info.setOnClickListener(view -> {
            if(frame.getVisibility() == View.VISIBLE) {
                Toast.makeText(WritePlan.this, "도시를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            new SlyCalendarDialog()
                    .setSingle(false)
                    .setCallback(this)
                    .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
        });
        info1.setOnClickListener(friends);

        // 취향저격일정
        LinearLayout listReco = findViewById(R.id.listReco);
        for (int i = 0; i < 5; i++) {
            ItemReco itemReco = new ItemReco(WritePlan.this);
            listReco.addView(itemReco);
        }

        // 요즘뜨는 여행지
        RecyclerView listHot = findViewById(R.id.listHot);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        listHot.setLayoutManager(mLayoutManager);
        String[] items = new String[]{"도쿄", "도쿄", "도쿄", "도쿄", "도쿄", "도쿄"};
        HotAdapter hotAdapter = new HotAdapter(items);
        listHot.setAdapter(hotAdapter);

        // 리뷰
        Intent intent = getIntent();
        if(intent.getStringExtra("review") != null) {
            String txt = "5월 25일 ~ 5월 27일(2박 3일)";
            info.setText(txt);
            setSchedule(3);

            frame.setVisibility(View.GONE);
            frame_next.setVisibility(View.VISIBLE);

            info1.setText("5명과 함께 떠나요!");
            info2.setText("현재 3명이 함께해요!");
            info2.setVisibility(View.VISIBLE);
        }
    }

    // 도시검색후
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) { // 도시검색
            if (data != null && data.getStringExtra("city") != null) {
                frame.setVisibility(View.GONE);
                frame_next.setVisibility(View.VISIBLE);

                String city = data.getStringExtra("city");
                if (city.equals("yes")) cities.setVisibility(View.VISIBLE);
                else cities.setVisibility(View.GONE);
            }
        }
    }

    // 날짜 선택
    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
        if (secondDate == null) secondDate = firstDate;
        String txt = firstDate.get(Calendar.MONTH) + "월 " + firstDate.get(Calendar.DATE) + "일 ~ ";
        txt += secondDate.get(Calendar.MONTH) + "월 " + secondDate.get(Calendar.DATE) + "일";

        int days = (int) (secondDate.getTimeInMillis() - firstDate.getTimeInMillis()) / (1000 * 60 * 60 * 24) + 1;
        txt += "(" + (days - 1) + "박 " + days + "일)";
        info.setText(txt);

        setSchedule(days);
    }

    @Override
    public void onCancelled() {
    }

    // 인원선택
    View.OnClickListener friends = view -> {
        if(frame.getVisibility() == View.VISIBLE) {
            Toast.makeText(WritePlan.this, "도시를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dlg = new Dialog(WritePlan.this);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.added_pop_friends);
        dlg.show();

        final EditText num0 = dlg.findViewById(R.id.num0);
        final EditText num1 = dlg.findViewById(R.id.num1);
        final TextView ok = dlg.findViewById(R.id.ok);

        ok.setOnClickListener(v -> {
            dlg.dismiss();

            info1.setText(num0.getText().toString() + "명과 함께 떠나요!");
            info2.setText("현재 " + num1.getText().toString() + "명이 함께해요!");
            info2.setVisibility(View.VISIBLE);
        });
    };

    // 도시목록
    public class CitiesAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inf;

        public CitiesAdapter(Context context) {
            this.context = context;
            inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) view = inf.inflate(R.layout.added_item_city, null);
            return view;
        }
    }

    // 요즘뜨는여행지목록
    public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater inflater;
        String[] items;

        public class HotViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView title;

            public HotViewHolder(@NonNull View itemView) {
                super(itemView);

                img = itemView.findViewById(R.id.img);
                title = itemView.findViewById(R.id.title);
            }
        }

        HotAdapter(String[] items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_item_hot, parent, false);

            return new HotViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            HotViewHolder planViewHolder = (HotViewHolder) holder;

            // planViewHolder.img
            planViewHolder.title.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }

    void setSchedule(int days) {
        for(int i=1; i<=days; i++) {
            ItemSchedule schedule = new ItemSchedule(WritePlan.this, i);
            frame2_next.addView(schedule);
        }

        for(int i=1; i<=days; i++) {
            ItemSchedule2 schedule2 = new ItemSchedule2(WritePlan.this, i);
            frame2_next.addView(schedule2);
        }

        cities.setVisibility(View.GONE);
        frame2.setVisibility(View.GONE);
        frame2_next.setVisibility(View.VISIBLE);
    }
}