package com.example.jmkim.nomad.added;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jmkim.nomad.R;

public class Navigation extends LinearLayout {

    Main parent;

    public Navigation(Context context) {
        super(context);
        init();
        parent = (Main) context;
    }

    public Navigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        parent = (Main) context;
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.added_navigation, this, true);

        ImageView navi0 = rootView.findViewById(R.id.navi0);
        ImageView navi1 = rootView.findViewById(R.id.navi1);
        ImageView navi2 = rootView.findViewById(R.id.navi2);
        ImageView navi3 = rootView.findViewById(R.id.navi3);
        ImageView navi4 = rootView.findViewById(R.id.navi4);

        navi0.setOnClickListener(l);
        navi1.setOnClickListener(l);
        navi2.setOnClickListener(l);
        navi3.setOnClickListener(l);
        navi4.setOnClickListener(l);
    }

    OnClickListener l = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.navi0:
                    parent.setCurrentItem(0);
                    break;
                case R.id.navi1:
                    parent.setCurrentItem(1);
                    break;
                case R.id.navi2:
                    parent.showPop();
                    break;
                case R.id.navi3:
                    parent.setCurrentItem(2);
                    break;
                case R.id.navi4:
                    parent.setCurrentItem(3);
                    break;
            }
        }
    };
}