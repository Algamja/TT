package com.example.jmkim.nomad;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BottomSheetDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    public static BottomSheetDialog getInstance() {return new BottomSheetDialog();}

    public LinearLayout schedule;
    public LinearLayout review;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog,container,false);
        schedule = (LinearLayout) view.findViewById(R.id.bottomSheet_schedule);
        review = (LinearLayout) view.findViewById(R.id.bottomSheet_review);

        schedule.setOnClickListener(this);
        review.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        dismiss();
    }
}
