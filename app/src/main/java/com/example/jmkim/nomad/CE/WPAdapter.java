package com.example.jmkim.nomad.CE;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.WritePlanActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WPAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    private ArrayList<WritePlanInfo> writePlanInfos;
    WritePlanActivity writePlanActivity;

    private FirebaseUser user;
    private String uid;

    public WPAdapter(Context context, ArrayList<WritePlanInfo> writePlanInfos, WritePlanActivity writePlanActivity){
        this.context = context;
        this.writePlanInfos = writePlanInfos;
        this.writePlanActivity = writePlanActivity;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView hashtag;

        TextView dayN;
        ImageButton memo_plus;
        ImageButton memo;
        ImageButton memo_oneline;
        EditText memo_editText;
        TextView WPD_hashtag;
        LinearLayout WPD_ll_repeat;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hashtag = itemView.findViewById(R.id.WPD_hashtag);

            dayN = (TextView) itemView.findViewById(R.id.WPD_day);
            memo_plus = (ImageButton) itemView.findViewById(R.id.memo_plus);
            memo = (ImageButton) itemView.findViewById(R.id.memo);
            memo_oneline = (ImageButton) itemView.findViewById(R.id.memo_oneline);
            memo_editText = (EditText) itemView.findViewById(R.id.memo_editText);
            WPD_hashtag = (TextView) itemView.findViewById(R.id.WPD_hashtag);
            WPD_ll_repeat = (LinearLayout) itemView.findViewById(R.id.WPD_ll_repeat);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.writeplan_days, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.hashtag.setText(writePlanInfos.get(position).hashtag);

        myViewHolder.memo_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //memo 작성 페이지로 넘어가도록
                Intent intent = new Intent(view.getContext(), MemoActivity.class);
                view.getContext().startActivity(intent);

                //R.id.memo 이미지가 옆에 뜨도록
                MemoActivity memoActivity = (MemoActivity) context.getApplicationContext();
                if(memoActivity.saveBtn.callOnClick())
                    myViewHolder.memo.setVisibility(View.VISIBLE);


                //또 추가하면 또 옆에 뜨도록
                //if(memoActivity.saveBtn.callOnClick())

            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }

    @Override
    public int getItemCount() {
        return writePlanInfos.size();
    }
}
