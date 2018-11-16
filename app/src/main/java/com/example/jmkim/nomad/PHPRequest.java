package com.example.jmkim.nomad;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PHPRequest {
    private URL url;

    public PHPRequest(String url) throws MalformedURLException{
        this.url = new URL(url);
    }

    private String readStream(InputStream in) throws IOException{
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }
    public String PhPtest(final String data1, final String data2, final String data3, final String data4, final String data5, final String data6) {
        try {
            //String postData = "Name="+data1;
            String postData = "U_ID=" + data1 + "&" + "U_PW=" + data2 + "&" + "U_NAME=" + data3 + "&" + "U_PHONE=" + data4 + "&" + "U_SEX=" + data5 + "&" + "U_Age=" +data6;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result = readStream(conn.getInputStream());
            conn.disconnect();
            return result;
        } catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }
}
