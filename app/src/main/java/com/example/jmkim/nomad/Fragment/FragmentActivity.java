package com.example.jmkim.nomad.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentActivity extends Fragment {

    String tag = this.getClass().getSimpleName();
    Button btn1,btn2,btn3;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.view_page_1,container,false); //첫번째 스와이프 화면이랑 엮어줌
        return layout;
    }
}
