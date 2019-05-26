package com.example.jmkim.nomad.added;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import com.example.jmkim.nomad.DB.Hashtag;
import com.example.jmkim.nomad.R;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HashTagInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView name;
    private ImageView img;
    private TextView introduce;
    private TextView addr;
    private TextView time;
    private TextView price;

    private RequestManager mGlide;

    Hashtag hashtag = new Hashtag();

    private GoogleMap mMap;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tag_info);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.hashTagInfo_map);
        mapFragment.getMapAsync(this);

        name = (TextView) findViewById(R.id.hashTagInfo_name);
        img = (ImageView) findViewById(R.id.hashTagInfo_img);
        introduce = (TextView) findViewById(R.id.hashTagInfo_introduce);
        addr = (TextView) findViewById(R.id.hashTagInfo_addr);
        time = (TextView) findViewById(R.id.hashTagInfo_time);
        price = (TextView) findViewById(R.id.hashTagInfo_price);

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
        name.setText("#" + tag);
        mGlide.load(hashtag.Url).into(img);
        introduce.setText(hashtag.Introduce);
        addr.setText(hashtag.Addr);
        time.setText(hashtag.Time);
        price.setText(hashtag.Price);

        String str = "일본 " +tag;
        List<Address> addressList = new ArrayList<>();
        addressList.clear();
        try {
            addressList = geocoder.getFromLocationName(str,10);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(addressList.get(0).toString());
        String []splitStr = addressList.get(0).toString().split(",");
        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
        System.out.println(address);

        String latitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 위도
        String longitude = splitStr[14].substring(splitStr[14].indexOf("=") + 1); // 경도
        System.out.println(latitude);
        System.out.println(longitude);

        LatLng point = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        MarkerOptions mOptions2 = new MarkerOptions();
        mOptions2.title("search result");
        mOptions2.snippet(address);
        mOptions2.position(point);

        mMap.addMarker(mOptions2);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

    }
}