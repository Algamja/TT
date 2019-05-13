package com.example.jmkim.nomad.added;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.jmkim.nomad.DB.Board;
import com.example.jmkim.nomad.DB.UserModel;
import com.example.jmkim.nomad.R;
import com.example.jmkim.nomad.prev.WriterActivity;

import java.util.List;

public class ItemMain extends LinearLayout {
    Main parent;

    private RequestManager mGlide;

    public ItemMain(Context context, int num, Board board, UserModel userModel) {
        super(context);
        init(num, board, userModel);
        parent = (Main) context;
    }

    private void init(int num, Board board, UserModel userModel) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Context context = getContext();
        View rootView = inflater.inflate(R.layout.added_item_main, this, true);
        ImageView profile;
        TextView name;
        TextView country;
        ImageView img;
        TextView like;

        profile = rootView.findViewById(R.id.board_profile);
        name = rootView.findViewById(R.id.board_name);
        country = rootView.findViewById(R.id.board_country);
        img = rootView.findViewById(R.id.board_img);
        like = rootView.findViewById(R.id.board_tv_like);

        mGlide = Glide.with(context);

        mGlide.load(userModel.profileImageUrl)
                .apply(new RequestOptions().circleCrop())
                .into(profile);
        name.setText(userModel.userName);
        country.setText(board.country);
        Log.e("IMAGE",board.img);
        mGlide.load(board.img)
                .apply(new RequestOptions().fitCenter())
                .into(img);
        like.setText(board.like);

        profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WriterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("publisher",board.publisher);
                context.startActivity(intent);
            }
        });
    }
}