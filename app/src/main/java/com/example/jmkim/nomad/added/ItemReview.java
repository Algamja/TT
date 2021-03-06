package com.example.jmkim.nomad.added;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.DB.Plan;
import com.example.jmkim.nomad.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemReview extends LinearLayout {
    WriteReview parent;
    ArrayList<Plan> plans = new ArrayList<>();
    List<String> keys = new ArrayList<>();

    public ItemReview(Context context, int num) {
        super(context);
        parent = (WriteReview) context;

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Plan")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        plans.clear();

                        for(DataSnapshot item : dataSnapshot.getChildren()){
                            plans.add(item.getValue(Plan.class));
                            keys.add(item.getKey());
                        }
                        init(num);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void init(int num) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.added_item_boards, this, true);

        List<Plan> plans = new ArrayList<>();
        for(int i=0;i<keys.size();i++){
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Plan")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(keys.get(num))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            plans.clear();

                            plans.add(dataSnapshot.getValue(Plan.class));

                            TextView city = rootView.findViewById(R.id.list_country);
                            TextView period = rootView.findViewById(R.id.list_period);

                            city.setText("#"+plans.get(0).country);
                            period.setText(plans.get(0).period);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}