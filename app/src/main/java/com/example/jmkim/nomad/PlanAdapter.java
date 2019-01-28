package com.example.jmkim.nomad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class PlanViewHolder extends RecyclerView.ViewHolder{

        ImageButton ib_hot;
        TextView tv_hot;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            ib_hot = itemView.findViewById(R.id.PA_ib_hot);
            tv_hot = itemView.findViewById(R.id.PA_tv_hot);
        }
    }

    private ArrayList<PlanHotInfo> planHotInfos;
    PlanAdapter(ArrayList<PlanHotInfo> planHotInfos){
        this.planHotInfos = planHotInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_recyclerview, parent, false);

        return new PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PlanViewHolder planViewHolder = (PlanViewHolder) holder;

        planViewHolder.ib_hot.setImageResource(planHotInfos.get(position).imageint);
        planViewHolder.tv_hot.setText(planHotInfos.get(position).place);
    }

    @Override
    public int getItemCount() {
        return planHotInfos.size();
    }
}
