package com.example.jmkim.nomad.prev;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //Adapter가 작용할 위치 및 사용할 정보
    Context mContext;
    private ArrayList<MainBoardInfo> mainBoardInfos;

    private RequestManager mGlide;

    public MainAdapter(Context mContext, ArrayList<MainBoardInfo> mainBoardInfos) {
        this.mContext = mContext;
        this.mainBoardInfos = mainBoardInfos;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_board_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        mGlide = Glide.with(mContext);

        //받아온 정보를 xml에 적용
        mGlide.load(mainBoardInfos.get(position).profileId)
                .apply(new RequestOptions().circleCrop())
                .into(myViewHolder.iv_profile);
        myViewHolder.tv_title.setText(mainBoardInfos.get(position).title);
        myViewHolder.tv_country.setText(mainBoardInfos.get(position).country);

        //프로필이 클릭되었을 때
        myViewHolder.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i <= mainBoardInfos.size(); i++){
                    if(position == i){
                        Intent intent = new Intent(v.getContext(), WriterActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("publisher", mainBoardInfos.get(position).publisher);
                        v.getContext().startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainBoardInfos.size();
    }
}
