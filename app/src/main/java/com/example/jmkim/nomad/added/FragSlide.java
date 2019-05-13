package com.example.jmkim.nomad.added;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jmkim.nomad.R;

import androidx.fragment.app.Fragment;

public class FragSlide extends Fragment {

    int[] imgs = {R.drawable.slide0, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
    int position;

    public static FragSlide create(int position) {
        FragSlide fragment = new FragSlide();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.added_fragslide, container, false);
        ImageView img = rootView.findViewById(R.id.img);
        img.setImageResource(imgs[position]);

        return rootView;
    }
}