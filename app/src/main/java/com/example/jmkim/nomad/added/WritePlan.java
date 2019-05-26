package com.example.jmkim.nomad.added;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jmkim.nomad.DB.Add_Tag;
import com.example.jmkim.nomad.DB.ChatModel;
import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.PlanReadActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.slybeaver.slycalendarview.SlyCalendarDialog;

public class WritePlan extends AppCompatActivity implements SlyCalendarDialog.Callback {
    private LinearLayoutManager mLayoutManager;
    private RecyclerView.LayoutManager layoutManager;

    private LinearLayout search_layout;
    private LinearLayout country_info_layout;
    private LinearLayout recommend_layout;
    private RecyclerView tag_layout;

    private EditText search;
    private ImageView flag;
    private TextView city;
    private TextView when;
    private TextView many;
    private TextView want;
    private LinearLayout recommend_list;
    private RecyclerView hot_list;
    private CheckBox agree;
    private Button submit;

    private int member;
    private ChatModel chatModel = new ChatModel();

    @Override
    public void onBackPressed() {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Imsi")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .removeValue();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.added_write_plan);

        mLayoutManager = new LinearLayoutManager(this);
        layoutManager = new LinearLayoutManager(this);

        search_layout = findViewById(R.id.plan_search_layout);
        country_info_layout = findViewById(R.id.plan_country_info_layout);
        recommend_layout = findViewById(R.id.plan_recom_layout);
        tag_layout = findViewById(R.id.plan_schedule_layout);

        search = findViewById(R.id.plan_search_editText);
        flag = findViewById(R.id.plan_flag);
        city = findViewById(R.id.plan_city);
        when = findViewById(R.id.plan_when);
        many = findViewById(R.id.plan_many);
        want = findViewById(R.id.plan_want);
        recommend_list = findViewById(R.id.plan_recom_list);
        hot_list = findViewById(R.id.plan_hot_list);
        agree = findViewById(R.id.plan_agree);
        submit = findViewById(R.id.plan_submit);

        tag_layout.setLayoutManager(layoutManager);

        String get_city = getIntent().getStringExtra("city");
        String get_country = getIntent().getStringExtra("country");

        if(get_city != null){
            search_layout.setVisibility(View.GONE);
            country_info_layout.setVisibility(View.VISIBLE);

            city.setText(get_city);

            if(get_country.equals("일본")) {
                flag.setImageDrawable(getResources().getDrawable(R.drawable.japan));
            }
        }

        // 검색
        search.setOnClickListener(view -> {
            Intent intent = new Intent(WritePlan.this, SearchCity.class);
            intent.putExtra("type","plan");
            startActivityForResult(intent, 0);
            overridePendingTransition(0, android.R.anim.fade_in);
        });

        // 날짜/인원
        when.setOnClickListener(view -> {
            if(search_layout.getVisibility() == View.VISIBLE) {
                Toast.makeText(WritePlan.this, "도시를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            new SlyCalendarDialog()
                    .setSingle(false)
                    .setCallback(this)
                    .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");
        });
        many.setOnClickListener(friends);

        // 취향저격일정
        String[] tag = new String[]{"도쿄돔","디즈니스토어","우에노동물원","요요기공원","도쿄디즈니씨"};
        String[] img = new String[]{"https://firebasestorage.googleapis.com/v0/b/trip-box-1be2a.appspot.com/o/hashtag%2F%EB%8F%84%EC%BF%84%EB%8F%94.jpg?alt=media&token=520af6aa-856b-4b9b-9f70-e71e5f049cc4",
                "https://firebasestorage.googleapis.com/v0/b/trip-box-1be2a.appspot.com/o/hashtag%2F%EB%94%94%EC%A6%88%EB%8B%88%EC%8A%A4%ED%86%A0%EC%96%B4.jpg?alt=media&token=3a08e6ba-c754-4e81-8b49-1e61f30edb1c",
                "https://firebasestorage.googleapis.com/v0/b/trip-box-1be2a.appspot.com/o/hashtag%2F%EC%9A%B0%EC%97%90%EB%85%B8%EB%8F%99%EB%AC%BC%EC%9B%90.jpg?alt=media&token=25c59e59-4b84-4ee3-aec9-c13182a3344f",
                "https://firebasestorage.googleapis.com/v0/b/trip-box-1be2a.appspot.com/o/hashtag%2F%EC%9A%94%EC%9A%94%EA%B8%B0%EA%B3%B5%EC%9B%90.jpg?alt=media&token=a3a4c654-0c42-4bfd-9de5-0a391f407b79",
                "https://firebasestorage.googleapis.com/v0/b/trip-box-1be2a.appspot.com/o/hashtag%2F%EB%8F%84%EC%BF%84%EB%94%94%EC%A6%88%EB%8B%88%EC%94%A8.jpg?alt=media&token=195526df-f8db-4e3b-a2ee-f4bde0af819c"};
        for (int i = 0; i < 5; i++) {
            ItemReco itemReco = new ItemReco(WritePlan.this,tag[i],img[i]);
            recommend_list.addView(itemReco);
        }

        // 요즘뜨는 여행지
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hot_list.setLayoutManager(mLayoutManager);
        String[] items = new String[]{"도쿄 도", "야마가타 현", "야마나시 현", "스자카 시", "오키나와 시", "다이센 시"};
        int[] imgs = new int[]{R.drawable.dokyo, R.drawable.yamagata, R.drawable.yamanashi, R.drawable.tsuzaka, R.drawable.okinawa, R.drawable.daisen};
        String[] publishers = new String[]{"9DT9bXFtbWeK9449mNeNPSIcMvm2", "9eDtcPCYIcg3uoyDw8BqRD9LJMF3", "Edn1YAyL0gc3oXpg1jGxoKk68223", "IX4niMXblUXdNfAJHfIgWi1T7uD3", "JKxyLrqXS7OfmpSKUa8Kj4ANQoB2", "Nvi25OVRWKWKBexNbOXqZfVbnAi2"};
        String[] plan_keys = new String[]{"-LfZdbhk5lzKmI8cIlqC", "-Lfp700Icpc-yFSvc_p6", "-Lfp9wvBSMKIuqs3m3D5", "-LfpC4OCSMiIQAAgp0ea", "-LfpEEwkuVhDZzImRE7S", "-LfpnILWcLS28dXfTfpG"};
        HotAdapter hotAdapter = new HotAdapter(items, imgs, publishers, plan_keys);
        hot_list.setAdapter(hotAdapter);

        agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(agree.isChecked()==false){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(WritePlan.this);
                    dlg.setMessage("비공개시 글이 다른 사용자에게 노출되지 않으며\n파티원 모집시 불이익이 있을 수 있습니다");
                    dlg.setPositiveButton("비공개", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            agree.setChecked(false);
                            Toast.makeText(WritePlan.this, "글이 비공개됩니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            agree.setChecked(true);
                        }
                    });
                    dlg.show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Tag tag = new Add_Tag();

                if(agree.isChecked()){
                    if(member > 1){
                        chatModel.users.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),true);
                        chatModel.type="group";
                        chatModel.king=FirebaseAuth.getInstance().getCurrentUser().getUid();
                        chatModel.member_count= String.valueOf(member);
                        chatModel.title = city.getText().toString();

                        String imsi_chat_key = FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("ChatRooms")
                                .push()
                                .getKey();

                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("ChatRooms")
                                .child(imsi_chat_key)
                                .setValue(chatModel);

                        List<Add_Tag> add_tags = new ArrayList<>();
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("Imsi")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hashtag")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        add_tags.clear();

                                        for(DataSnapshot item : dataSnapshot.getChildren()){
                                            add_tags.add(item.getValue(Add_Tag.class));
                                        }

                                        Plan plan = new Plan();
                                        plan.publisher = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        plan.country = city.getText().toString();
                                        plan.period = when.getText().toString();
                                        plan.open = agree.isChecked();
                                        plan.chat_key = imsi_chat_key;
                                        plan.total = String.valueOf(member);
                                        plan.want = String.valueOf(member - 1);

                                        for(int i=0;i<add_tags.size();i++){
                                            plan.hashtag.put(add_tags.get(i).tag_name, add_tags.get(i));
                                        }

                                        if(plan.hashtag.size() > 1){
                                            FirebaseDatabase
                                                    .getInstance()
                                                    .getReference()
                                                    .child("Plan")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .push()
                                                    .setValue(plan)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            FirebaseDatabase
                                                                    .getInstance()
                                                                    .getReference()
                                                                    .child("Imsi")
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .removeValue();

                                                            Toast.makeText(WritePlan.this, "일정 등록이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }else{
                        List<Add_Tag> add_tags = new ArrayList<>();
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("Imsi")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("hashtag")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        add_tags.clear();

                                        for(DataSnapshot item : dataSnapshot.getChildren()){
                                            add_tags.add(item.getValue(Add_Tag.class));
                                        }

                                        Plan plan = new Plan();
                                        plan.publisher = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        plan.country = city.getText().toString();
                                        plan.period = when.getText().toString();
                                        plan.open = agree.isChecked();

                                        for(int i=0;i<add_tags.size();i++){
                                            plan.hashtag.put(add_tags.get(i).tag_name, add_tags.get(i));
                                        }

                                        if(plan.hashtag.size() > 1){
                                            FirebaseDatabase
                                                    .getInstance()
                                                    .getReference()
                                                    .child("Plan")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .push()
                                                    .setValue(plan)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            FirebaseDatabase
                                                                    .getInstance()
                                                                    .getReference()
                                                                    .child("Imsi")
                                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                    .removeValue();

                                                            Toast.makeText(WritePlan.this, "일정 등록이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }
                }
                else if(!agree.isChecked()){
                    List<Add_Tag> add_tags = new ArrayList<>();
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Imsi")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("hashtag")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    add_tags.clear();

                                    for(DataSnapshot item : dataSnapshot.getChildren()){
                                        add_tags.add(item.getValue(Add_Tag.class));
                                    }

                                    Plan plan = new Plan();
                                    plan.publisher = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    plan.country = city.getText().toString();
                                    plan.period = when.getText().toString();
                                    plan.open = agree.isChecked();

                                    for(int i=0;i<add_tags.size();i++){
                                        plan.hashtag.put(add_tags.get(i).tag_name, add_tags.get(i));
                                    }

                                    if(plan.hashtag.size() > 1){
                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("Plan")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .push()
                                                .setValue(plan)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseDatabase
                                                                .getInstance()
                                                                .getReference()
                                                                .child("Imsi")
                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                                .removeValue();

                                                        Toast.makeText(WritePlan.this, "일정 등록이 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }
        });
    }

    // 도시검색후
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 0) { // 도시검색
            if (data != null && data.getStringExtra("city") != null) {
                search_layout.setVisibility(View.GONE);
                country_info_layout.setVisibility(View.VISIBLE);

                String imsi = data.getStringExtra("city");
                city.setText(imsi);

                imsi = data.getStringExtra("country");
                if(imsi.equals("일본")) {
                    flag.setImageDrawable(getResources().getDrawable(R.drawable.japan));
                }
            }
        }

        if(requestCode == 0){
            if(data != null && data.getStringExtra("tag") != null){
                String tag = data.getStringExtra("tag");

                final ItemSchedule.schedule input = new ItemSchedule.schedule();
                input.setHashtag(tag);
                input.setTagOk(true);

                Add_Tag add_tag = new Add_Tag();
                add_tag.tag_name = tag;
                add_tag.index = String.valueOf(input.getIndex());
                add_tag.position = String.valueOf(input.getPosition());

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Imsi")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("hashtag")
                        .child(tag)
                        .setValue(add_tag);
            }
        }
    }

    // 날짜 선택
    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
        if (secondDate == null) secondDate = firstDate;
        String txt = firstDate.get(Calendar.MONTH) + 1 + "월 " + firstDate.get(Calendar.DATE) + "일 ~ ";
        txt += secondDate.get(Calendar.MONTH) + 1 + "월 " + secondDate.get(Calendar.DATE) + "일";


        int days = (int) (secondDate.getTimeInMillis() - firstDate.getTimeInMillis()) / (1000 * 60 * 60 * 24) + 1;
        txt += "(" + (days - 1) + "박 " + days + "일)";
        when.setText(txt);
        setSchedule(days);
    }

    @Override
    public void onCancelled() {
    }

    // 인원선택
    View.OnClickListener friends = view -> {
        if(search_layout.getVisibility() == View.VISIBLE) {
            Toast.makeText(WritePlan.this, "도시를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Dialog dlg = new Dialog(WritePlan.this);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.added_pop_friends);
        dlg.show();

        final EditText num0 = dlg.findViewById(R.id.num_total);
        final EditText num1 = dlg.findViewById(R.id.num_want);
        final TextView ok = dlg.findViewById(R.id.ok);

        ok.setOnClickListener(v -> {
            dlg.dismiss();

            member= Integer.parseInt(num0.getText().toString());

            many.setText("총 " + num0.getText().toString() + "명이 함께 떠나요!");
            want.setText("현재 " + num1.getText().toString() + "명을 구해요!");
            want.setVisibility(View.VISIBLE);
        });
    };

    // 요즘뜨는여행지목록
    public class HotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        LayoutInflater inflater;
        String[] items;
        String[] publishers;
        String[] plan_keys;
        int[] imgs;

        public class HotViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView title;

            public HotViewHolder(@NonNull View itemView) {
                super(itemView);

                img = itemView.findViewById(R.id.img);
                title = itemView.findViewById(R.id.title);
            }
        }

        HotAdapter(String[] items, int[] imgs, String[] publishers, String[] plan_keys) {
            this.items = items;
            this.imgs = imgs;
            this.publishers = publishers;
            this.plan_keys = plan_keys;
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

            planViewHolder.title.setText(items[position]);
            planViewHolder.img.setImageDrawable(getResources().getDrawable(imgs[position]));

            planViewHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WritePlan.this, PlanReadActivity.class);
                    intent.putExtra("publisher", publishers[position]);
                    intent.putExtra("key",plan_keys[position]);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.length;
        }
    }

    void setSchedule(int days) {
        ItemSchedule schedule = new ItemSchedule(WritePlan.this, days, WritePlan.this);
        tag_layout.setAdapter(schedule);

        recommend_layout.setVisibility(View.GONE);
        tag_layout.setVisibility(View.VISIBLE);
    }
}