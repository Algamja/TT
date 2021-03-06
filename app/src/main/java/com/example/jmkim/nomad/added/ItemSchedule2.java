package com.example.jmkim.nomad.added;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.R;

public class ItemSchedule2 extends LinearLayout {
    WritePlan parent;

    public ItemSchedule2(Context context, int num) {
        super(context);
        init(num);
        parent = (WritePlan) context;
    }

    private void init(int num) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.added_schedule2, this, true);

        TextView day = rootView.findViewById(R.id.wPlan_day);
        day.setText("> DAY " + num);
    }
}