package com.example.jmkim.nomad.prev;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jmkim.nomad.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WritePlanActivity extends AppCompatActivity implements com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener {

    private ScrollView WP;
    private View black;

    private LinearLayout layout_top;
    private View shadow_search;
    private LinearLayout layout_search;
    private EditText search;
    private ImageView close_search;
    private ImageView ic_search;
    private ListView list_city;

    private LinearLayout layout_when;
    private TextView ques_date;
    private TextView range;
    private TextView day_night;

    private LinearLayout layout_ppl;
    private LinearLayout ques_ppl;
    private LinearLayout layout_go_ppl;
    private TextView total_ppl;
    private TextView cur_ppl;

    private View dlg_ppl;

    private EditText dlg_ppl_total;
    private EditText dlg_ppl_cur;
    private Button with_btn;

    private LinearLayout layout_recommend;
    private ImageButton btn_help;
    private View dlg_help;

    private RecyclerView planhot_recycler;

    private List<String> list;
    private SearchAdapter adapter;
    private ArrayList<String> arrayList;

    private DbOpenHelper mDbOpenHelper;
    private Cursor iCursor;

    private Boolean isClicked = false;

    private RelativeLayout relativeLayout;
    private GridLayout gridLayout;
    private ImageView gridimage_1;
    private ImageView gridimage_2;
    private static final String IMAGEVIEW_TAG = "드래그 이미지";

    private AlertDialog dlg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_plan);

        WP = (ScrollView)findViewById(R.id.WP_scroll);
        black = (View)findViewById(R.id.WP_view_black);

        layout_top = (LinearLayout)findViewById(R.id.WP_ll_top);
        shadow_search = (View) findViewById(R.id.WP_view_shadow);
        layout_search = (LinearLayout)findViewById(R.id.WP_ll_search);
        search = (EditText) findViewById(R.id.WP_et_search);
        close_search = (ImageView)findViewById(R.id.WP_iv_close_search);
        ic_search = (ImageView) findViewById(R.id.WP_iv_search);
        list_city = (ListView) findViewById(R.id.WP_lv_city);

        layout_when = (LinearLayout)findViewById(R.id.WP_ll_when);
        ques_date = (TextView)findViewById(R.id.WP_tv_choose_date);
        range = (TextView)findViewById(R.id.WP_tv_range);
        day_night = (TextView)findViewById(R.id.WP_tv_DN);

        layout_ppl = (LinearLayout)findViewById(R.id.WP_ll_people);
        ques_ppl = (LinearLayout) findViewById(R.id.WP_ll_howmanyppl);
        layout_go_ppl = (LinearLayout)findViewById(R.id.WP_ll_go_people);
        total_ppl = (TextView)findViewById(R.id.WP_tv_totalppl);
        cur_ppl = (TextView)findViewById(R.id.WP_tv_currentppl);
        layout_recommend = (LinearLayout)findViewById(R.id.WP_ll_recommend);
        btn_help = (ImageButton)findViewById(R.id.WP_ib_help);
        //layout_title = (LinearLayout)findViewById(R.id.WP_ll_title);

        planhot_recycler = findViewById(R.id.WP_recyclerview);

        //드래그
        gridimage_1 = findViewById(R.id.gridimage1);
        gridimage_1.setTag(IMAGEVIEW_TAG);
        gridimage_1.setOnLongClickListener(new LongClickListener());

        gridimage_2 = findViewById(R.id.gridimage2);
        gridimage_2.setTag(IMAGEVIEW_TAG);
        gridimage_2.setOnLongClickListener(new LongClickListener());

        gridLayout = findViewById(R.id.gridlayout);
        gridLayout.setOnDragListener(new DragListener());
        relativeLayout = findViewById(R.id.dayplan);
        relativeLayout.setOnDragListener(new DragListener());

        //요즘뜨는 여행지 보여지는 부분
        planhot_recycler.setHasFixedSize(true);
        planhot_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<PlanHotInfo> planHotInfos = new ArrayList<>();
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));
        planHotInfos.add(new PlanHotInfo(R.drawable.one, "PLACE"));

        PlanAdapter planAdapter = new PlanAdapter(planHotInfos);
        planhot_recycler.setAdapter(planAdapter);

        WP.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                black.setMinimumHeight(WP.getHeight() + WP.getScrollY());
            }
        });

        //검색 아이콘 클릭되었을 때
        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked){

                    black.setVisibility(View.VISIBLE);

                    layout_top.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams control = (RelativeLayout.LayoutParams) layout_search.getLayoutParams();
                    control.topMargin = 10;
                    layout_search.setLayoutParams(control);

                    layout_search.setBackground(ContextCompat.getDrawable(WritePlanActivity.this, R.drawable.round_top_layout));
                    search.setBackground(ContextCompat.getDrawable(WritePlanActivity.this, R.drawable.round_top_left_layout));

                    close_search.setVisibility(View.VISIBLE);
                    list_city.setVisibility(View.VISIBLE);

                    String text = search.getText().toString();
                    if(text.length() == 0){
                        list.clear();
                        list_city.setMinimumHeight(500);
                    }
                }
            }
        });

        //도시 검색
        list = new ArrayList<String>();
        settingList();

        arrayList = new ArrayList<String>();
        arrayList.addAll(list);

        adapter = new SearchAdapter(list,this);
        list_city.setAdapter(adapter);

        //Search EditText에 글자 입력되었을 때
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search.getText().toString();
                search(text);

                //setListViewHeightBasedOnChildren(list_city);
            }
        });

        //언제 여행을 떠나시나요? 클릭되었을 때
        layout_when.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                com.borax12.materialdaterangepicker.date.DatePickerDialog datepicker = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        (com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener) WritePlanActivity.this,
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                );
                datepicker.setAutoHighlight(true);
                datepicker.show(getFragmentManager(),"Datepickerdialog");

                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

        //몇명이서 여행을 떠나시나요? 클릭되었을 때
        ques_ppl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg_ppl = (View) View.inflate(WritePlanActivity.this, R.layout.with_dialog, null);

                dlg = new AlertDialog.Builder(WritePlanActivity.this).create();
                dlg.setView(dlg_ppl);
                dlg_ppl_total = (EditText)dlg_ppl.findViewById(R.id.ppl_dlg_total);
                dlg_ppl_cur = (EditText)dlg_ppl.findViewById(R.id.ppl_dlg_cur);

                with_btn = (Button)dlg_ppl.findViewById(R.id.with_btn);

                dlg.show();

                with_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String total = dlg_ppl_total.getText().toString();
                        String cur = dlg_ppl_cur.getText().toString();

                        total_ppl.setText(total + "명과 함께 여행을 떠나요!");
                        cur_ppl.setText("현재 " + cur + "명을 구해요!");

                        layout_go_ppl.setVisibility(View.VISIBLE);
                        ques_ppl.setVisibility(View.GONE);
                        gridLayout.setVisibility(View.VISIBLE);
                        dlg.dismiss();
                    }
                });
            }
        });

        //도움말 클릭되었을 때
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_help = (View) View.inflate(WritePlanActivity.this, R.layout.recom_help_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(WritePlanActivity.this);
                dlg.setView(dlg_help);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dlg.show();
            }
        });//취향저격 일정 물음표 dialog

        /*btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Sub layout_title = new Sub(getApplicationContext());
                layout_recommend.addView(layout_title);
            }
        });//취향저격 일정 더보기*/
    }

    //드래그
    private class LongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View view) {
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return true;
        }
    }

    //드래그
    class DragListener implements View.OnDragListener{
        Drawable grey = getResources().getDrawable(R.drawable.grey);
        Drawable original = getResources().getDrawable(R.drawable.transparent);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackground(grey);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackground(original);
                    break;
                case DragEvent.ACTION_DROP:
                    if(v == findViewById(R.id.dayplan)){
                        View view = (View) event.getLocalState();
                        ViewGroup viewGroup = (ViewGroup) view.getParent();
                        viewGroup.removeView(view);

                        TextView textView = v.findViewById(R.id.tv_dayplan);
                        textView.setVisibility(View.GONE);

                        RelativeLayout containView = (RelativeLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);

                    } else if(v == findViewById(R.id.gridlayout)){
                        View view = (View) event.getLocalState();
                        ViewGroup viewGroup = (ViewGroup) view.getParent();
                        viewGroup.removeView(view);

                        GridLayout containView = (GridLayout) v;
                        containView.addView(view);
                        view.setVisibility(View.VISIBLE);

                    } else{
                        View view = (View) event.getLocalState();
                        view.setVisibility(View.VISIBLE);
                        Context context = getApplicationContext();
                        Toast.makeText(context, "이미지를 다른 지역에 드랍할 수 없습니다.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackground(original);
                default:
                    break;
            }
            return true;
        }
    }

    //여행기간 계산하는 부분
    @Override
    public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        startDate.set(year,monthOfYear,dayOfMonth);
        endDate.set(yearEnd,monthOfYearEnd,dayOfMonthEnd);

        long night = 0;
        long a = (endDate.getTimeInMillis() - startDate.getTimeInMillis())/1000;
        night = a/(60*60*24);
        long day = night + 1;

        if(night<0){
            Toast.makeText(this, "날짜를 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
        }else {
            String date = (monthOfYear + 1) + "월 " + dayOfMonth + "일 ~ " + (monthOfYearEnd + 1) + "월 " + dayOfMonthEnd + "일";
            range.setText(date);

            day_night.setText("("+String.valueOf(Math.abs(night)) + "박" + String.valueOf(day) + "일)");

            ques_date.setVisibility(View.GONE);
            range.setVisibility(View.VISIBLE);
            day_night.setVisibility(View.VISIBLE);
        }
    }

    //검색
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

    //listView에 정보 뿌려줌
    public void settingList(){
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();

        iCursor = mDbOpenHelper.selectColumns();
        while(iCursor.moveToNext()){
            String tmpCity = iCursor.getString(iCursor.getColumnIndex("city"));
            list.add(tmpCity);
        }
    }

    /*public void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        listView.setBackground(ContextCompat.getDrawable(WritePlanActivity.this, R.drawable.round_bottom_layout));
    }*/
}