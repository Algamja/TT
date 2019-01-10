package com.example.jmkim.nomad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_profile;
        TextView tv_title;
        TextView tv_country;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.scroll_iv_profile_1);
            tv_title = itemView.findViewById(R.id.scroll_tv_title_1);
            tv_country = itemView.findViewById(R.id.scroll_tv_country_1);
        }
    }

    private ArrayList<BoardInfo> boardInfos;
    MyAdapter(ArrayList<BoardInfo> boardInfos){
        this.boardInfos = boardInfos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.iv_profile.setImageResource(boardInfos.get(position).profileId);
        myViewHolder.tv_title.setText(boardInfos.get(position).title);
        myViewHolder.tv_country.setText(boardInfos.get(position).country);
    }

    @Override
    public int getItemCount() {
        return boardInfos.size();
    }
}
