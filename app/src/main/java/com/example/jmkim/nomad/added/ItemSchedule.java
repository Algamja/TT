package com.example.jmkim.nomad.added;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jmkim.nomad.R;

public class ItemSchedule extends LinearLayout {
    WritePlan parent;

    public ItemSchedule(Context context, int num) {
        super(context);
        init(num);
        parent = (WritePlan) context;
    }

    private void init(int num) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.added_schedule, this, true);

        TextView day = rootView.findViewById(R.id.day);
        day.setText("DAY " + num);

    }
}