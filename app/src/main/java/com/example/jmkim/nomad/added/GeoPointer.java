package com.example.jmkim.nomad.added;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class GeoPointer extends AsyncTask<String, Void, GeoPointer.Point[]> {
    private final static String NAVER_CLIENT_ID = "hnmhcn0rp8";
    private final static String NAVER_CLIENT_SECRET = "KwjPsLvHujpOVs6V8H81KRdH0VUSFYbj6LTmiEby";

    private OnGeoPointListener onGeoPointListener;

    private Context context;

    public GeoPointer(Context context,OnGeoPointListener onGeoPointListener) {
        this.onGeoPointListener = onGeoPointListener;
        this.context = context;
    }

    public interface OnGeoPointListener {
        void onPoint(Point[] p);

        void onProgress(int progress, int max);
    }

    class Point{
        public double x;
        public double y;
        public String addr;
        public boolean havePoint;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("x : ");
            builder.append(x);
            builder.append(" y : ");
            builder.append(y);
            builder.append(" addr : ");
            builder.append(addr);

            return builder.toString();
        }
    }

    @Override
    protected Point[] doInBackground(String... strings) {
        Point[] points = new Point[strings.length];
        for (int i = 0; i < strings.length; i++) {
            onGeoPointListener.onProgress(i + 1, strings.length);

            final String addr = strings[i];
            Point point = getPointFromGeoCoder(addr);

            if (!point.havePoint) point = getPointFromNaver(addr);

            points[i] = point;
        }
        return points;
    }

    private Point getPointFromNaver(String addr) {
        Point point = new Point();
        point.addr = addr;

        String json = null;
        String clientId = NAVER_CLIENT_ID;
        String clientSecret = NAVER_CLIENT_SECRET;

        try {
            addr = URLEncoder.encode(addr, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr;
            URL url = new URL(apiURL);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            json = response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json == null) {
            return point;
        }

        Gson gson = new Gson();
        NaverData data = new NaverData();
        try {
            data = gson.fromJson(json, NaverData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data.result != null) {
            point.x = data.result.items.get(0).point.x;
            point.y = data.result.items.get(0).point.y;
            point.havePoint = true;
        }

        return point;
    }

    @Override
    protected void onPostExecute(Point[] point) {
        onGeoPointListener.onPoint(point);
    }

    private Point getPointFromGeoCoder(String addr) {
        Point point = new Point();
        point.addr = addr;

        Geocoder geocoder = new Geocoder(context);
        List<Address> listAddress;
        try {
            listAddress = geocoder.getFromLocationName(addr, 1);
        } catch (IOException e) {
            e.printStackTrace();
            point.havePoint = false;
            return point;
        }

        if (listAddress.isEmpty()) {
            point.havePoint = false;
            return point;
        }

        point.havePoint = true;
        point.x = listAddress.get(0).getLongitude();
        point.y = listAddress.get(0).getLatitude();
        return point;
    }
}
