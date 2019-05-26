package com.example.jmkim.nomad.added;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.DB.Add_Tag;
import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.PlanReadActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReadPlanItem extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int num;
    Plan plan;
    PlanReadActivity planReadActivity;
    int days[];

    public ReadPlanItem(Context context, int num, Plan plan, PlanReadActivity planReadActivity) {
        this.context = context;
        this.num = num;
        this.plan = plan;
        this.planReadActivity = planReadActivity;
    }

    public ReadPlanItem(Context context) {this.context = context;}

    private static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        LinearLayout imsi;
        LinearLayout tag_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = (TextView)itemView.findViewById(R.id.wPlan_day);
            tag_layout = (LinearLayout)itemView.findViewById(R.id.add_tag_layout);
            imsi = (LinearLayout)itemView.findViewById(R.id.layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_schedule,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder)holder;

        days = new int[num];
        for(int i=0;i<num;i++){
            days[i] = i+1;
        }

        myViewHolder.imsi.setVisibility(View.GONE);
        myViewHolder.day.setText("DAY " + days[position]);

        Plan imsi_plan = plan;
        List<Add_Tag> tag = new ArrayList<>();

        for(int i=0;i<imsi_plan.hashtag.size();i++){
            Set set = imsi_plan.hashtag.keySet();
            Iterator iterator = set.iterator();

            tag.clear();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                tag.add(imsi_plan.hashtag.get(key));
            }
        }

        for(int i=0;i<tag.size();i++){
            for(int pos = 0;pos<num;pos++){
                if(position == pos && Integer.parseInt(tag.get(i).position) == position){
                    final TagAdd layout = new TagAdd(context);
                    myViewHolder.tag_layout.addView(layout);
                }
            }
        }

        for(int i=0;i<tag.size();i++){
            for(int pos = 0 ; pos < num ; pos++){
                if(position == pos && Integer.parseInt(tag.get(i).position) == position){
                    final TextView new_tag = myViewHolder.tag_layout.getChildAt(Integer.parseInt(tag.get(i).index)).findViewById(R.id.tag_item_tag);
                    new_tag.setText(tag.get(i).tag_name);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return num;
    }
}
