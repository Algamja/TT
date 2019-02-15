package com.example.jmkim.nomad;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WelcomeActivity extends Activity {

    private Button signIn;
    private Button singUp;

    DbOpenHelper mDbOpenHelper;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mDbOpenHelper = new DbOpenHelper(this);

        File files = new File("/data/data/com.example.jmkim.nomad/databases/InnerDatabase(SQLite).db");

        if(files.exists() == false){
            mDbOpenHelper.open();
            mDbOpenHelper.create();

            AlertDialog.Builder dlg = new AlertDialog.Builder(WelcomeActivity.this);
            dlg.setTitle("DB 다운로드가 필요합니다.");
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    pd = new ProgressDialog(WelcomeActivity.this);
                    
                    pd.show();
                    new getData().execute();
                }
            });
            dlg.setNegativeButton("취소",null);
            dlg.show();
        }

        signIn = (Button)findViewById(R.id.welcomeActivity_btn_signIn);
        singUp = (Button)findViewById(R.id.welcomeActivity_btn_signUp);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,SignInActivity.class));
                finish();
            }
        });

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,SignUpActivity.class));
                finish();
            }
        });
    }

    class getData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... Params) {
            try{
                URL server = new URL("http://192.168.0.18/getdb.php");
                HttpURLConnection urlConnection = (HttpURLConnection) server.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                br.close();
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result){
            if(result.startsWith("Exception")){
                Log.e("getPeriod onPost: ", "Error: " + result);
            }else{
                Log.d("getPeriod onPost: ", "Get Result: " + result);
                ArrayList<String> list = new ArrayList<String>();
                String[] list_DB = result.split("<br>");
                for (int i=0; i<list_DB.length; i++){
                    String[] tempResult = list_DB[i].split(",");

                    mDbOpenHelper.open();
                    String ID, City ,Code;
                    ID = tempResult[0];
                    City = tempResult[1];
                    Code = tempResult[2];
                    mDbOpenHelper.insertColumn(ID,City,Code);
                    pd.dismiss();
                }
            }
        }
    }
}
