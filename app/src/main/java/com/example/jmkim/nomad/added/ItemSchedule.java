package com.example.jmkim.nomad.added;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.DB.Add_Tag;
import com.example.jmkim.nomad.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemSchedule extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    int num;
    WritePlan writePlan;
    int days[];

    public ItemSchedule(Context context, int num, WritePlan writePlan) {
        this.context = context;
        this.num = num;
        this.writePlan = writePlan;
    }

    public ItemSchedule(Context mContext){
        this.context = mContext;
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView day;
        ImageView add;
        LinearLayout tag_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = (TextView)itemView.findViewById(R.id.wPlan_day);
            add = (ImageView)itemView.findViewById(R.id.once_add_btn);
            tag_layout = (LinearLayout)itemView.findViewById(R.id.add_tag_layout);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.added_schedule, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;

        days = new int[num];
        for (int i = 0; i < num; i++) {
            days[i] = i + 1;
        }

        myViewHolder.day.setText("DAY " + days[position]);

        myViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TagAdd layout = new TagAdd(context);
                myViewHolder.tag_layout.addView(layout);

                TextView tag = layout.findViewById(R.id.tag_item_tag);
                tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SearchTag.class);
                        intent.putExtra("type","plan");
                        ((WritePlan) writePlan).startActivityForResult(intent, 0);
                        ((WritePlan) writePlan).overridePendingTransition(0, android.R.anim.fade_in);

                        schedule input = new schedule();
                        input.setPosition(position);
                        input.setIndex(myViewHolder.tag_layout.indexOfChild(layout));
                        input.setPosOk(true);
                        notifyDataSetChanged();
                    }
                });

                ImageView memo = layout.findViewById(R.id.tag_item_memo);
                memo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dlg = new Dialog(context);
                        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dlg.setContentView(R.layout.pop_memo);
                        dlg.show();

                        final EditText memo_editText = dlg.findViewById(R.id.memo_editText);
                        final TextView ok = dlg.findViewById(R.id.memo_ok);

                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child("Imsi")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(String.valueOf(position))
                                .child(String.valueOf(myViewHolder.tag_layout.indexOfChild(layout)))
                                .child("memo")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue() != null) {
                                            String db_memo = dataSnapshot.getValue(String.class);
                                            memo_editText.setText(db_memo);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("Imsi")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(String.valueOf(position))
                                        .child(String.valueOf(myViewHolder.tag_layout.indexOfChild(layout)))
                                        .child("memo")
                                        .setValue(memo_editText.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dlg.dismiss();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        final schedule in = new schedule();
        final int pos;
        final int ind;
        final String tag;

        pos = in.getPosition();
        ind = in.getIndex();
        tag = in.getHashtag();

        if (in.getHashtag() != null) {
            if (in.isPosOk()) {
                final List<Add_Tag> add_tags = new ArrayList<>();
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child("Imsi")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("hashtag")
                        .child(in.getHashtag())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                add_tags.clear();

                                add_tags.add(dataSnapshot.getValue(Add_Tag.class));

                                if (pos == position) {
                                    final TextView new_tag = myViewHolder.tag_layout.getChildAt(ind).findViewById(R.id.tag_item_tag);
                                    if(add_tags.get(0)!=null)
                                    new_tag.setText("#" + add_tags.get(0).tag_name);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }
    }


    @Override
    public int getItemCount() {
        return num;
    }

    public static class schedule {
        private static String hashtag;
        private static int position;
        private static int index;
        private static boolean posOk = false;
        private static boolean tagOk = false;

        public static String getHashtag() {
            return hashtag;
        }

        public static void setHashtag(String hashtag) {
            schedule.hashtag = hashtag;
        }

        public static int getPosition() {
            return position;
        }

        public static void setPosition(int position) {
            schedule.position = position;
        }

        public static int getIndex() {
            return index;
        }

        public static void setIndex(int index) {
            schedule.index = index;
        }

        public static boolean isPosOk() {
            return posOk;
        }

        public static void setPosOk(boolean posOk) {
            schedule.posOk = posOk;
        }

        public static boolean isTagOk() {
            return tagOk;
        }

        public static void setTagOk(boolean tagOk) {
            schedule.tagOk = tagOk;
        }
    }
}