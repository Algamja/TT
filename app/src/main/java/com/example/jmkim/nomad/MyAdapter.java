package com.example.jmkim.nomad;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Adapter가 작용할 위치 및 사용할 정보
    Context mContext;
    private ArrayList<BoardInfo> boardInfos;

    public MyAdapter(Context mContext, ArrayList<BoardInfo> boardInfos) {
        this.mContext = mContext;
        this.boardInfos = boardInfos;
    }

    //Adapter가 연결될 layout정보
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_profile;
        TextView tv_title;
        TextView tv_country;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_profile = itemView.findViewById(R.id.scroll_iv_profile);
            tv_title = itemView.findViewById(R.id.scroll_tv_title);
            tv_country = itemView.findViewById(R.id.scroll_tv_country);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        myViewHolder.iv_profile.setImageResource(boardInfos.get(position).profileId);
        myViewHolder.tv_title.setText(boardInfos.get(position).title);
        myViewHolder.tv_country.setText(boardInfos.get(position).country);

        //프로필이 클릭되었을 때
        myViewHolder.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    Intent intent = new Intent(v.getContext(), WriterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardInfos.size();
    }
}
