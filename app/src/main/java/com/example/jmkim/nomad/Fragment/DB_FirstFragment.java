package com.example.jmkim.nomad.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DB_FirstFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.db_view_page_2,container,false); //두번째 스와이프 화면과 엮어줌
        return layout;
    }
}
