package com.example.jmkim.nomad.added;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.jmkim.nomad.DB.Hashtag;
import com.example.jmkim.nomad.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HashTagInfoActivity extends AppCompatActivity {

    private TextView name;
    private ImageView img;
    private TextView introduce;
    private TextView addr;
    private TextView time;
    private TextView price;

    private RequestManager mGlide;

    Hashtag hashtag = new Hashtag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tag_info);

        name = (TextView)findViewById(R.id.hashTagInfo_name);
        img = (ImageView) findViewById(R.id.hashTagInfo_img);
        introduce = (TextView)findViewById(R.id.hashTagInfo_introduce);
        addr = (TextView)findViewById(R.id.hashTagInfo_addr);
        time = (TextView)findViewById(R.id.hashTagInfo_time);
        price = (TextView)findViewById(R.id.hashTagInfo_price);

        mGlide = Glide.with(this);

        String get_tag = getIntent().getStringExtra("tag");


        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Hashtag")
                .child(get_tag)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        hashtag = dataSnapshot.getValue(Hashtag.class);
                        init(hashtag, get_tag);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void init(Hashtag hashtag, String tag) {
        name.setText("#"+tag);
        mGlide.load(hashtag.Url).into(img);
        introduce.setText(hashtag.Introduce);
        addr.setText(hashtag.Addr);
        time.setText(hashtag.Time);
        price.setText(hashtag.Price);
    }
}
