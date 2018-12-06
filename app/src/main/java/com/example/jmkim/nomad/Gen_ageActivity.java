package com.example.jmkim.nomad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Gen_ageActivity extends Activity{

    private RadioButton userMale;
    private RadioButton userFemale;

    private Spinner spinner;

    private CheckBox partyCheck;

    private LinearLayout partySex;
    private RadioButton partySameSex;
    private RadioButton partyDonCareSex;

    private LinearLayout partyAge;
    private RadioButton partyDoncareAge;
    private RadioButton party3Age;
    private RadioButton party5Age;

    private Button btn_nnext;

    String userSex;
    String SpartySex;
    String SpartyAge;
    String userAge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_age);

        userMale = (RadioButton)findViewById(R.id.genAgeActivity_rb_userMale);
        userFemale = (RadioButton)findViewById(R.id.genAgeActivity_rb_userFemale);

        spinner = (Spinner)findViewById(R.id.genAgeActivity_spinnerAge);

        partyCheck = (CheckBox) findViewById(R.id.genAgeActivity_cb_party);

        partySex = (LinearLayout) findViewById(R.id.genAgeActivity_partySex);
        partySameSex = (RadioButton)findViewById(R.id.genAgeActivity_rb_sameSex);
        partyDonCareSex = (RadioButton)findViewById(R.id.genAgeActivity_rb_donCareSex);

        partyAge = (LinearLayout) findViewById(R.id.genAgeActivity_partyAge);
        partyDoncareAge = (RadioButton)findViewById(R.id.genAgeActivity_rb_donCareAge);
        party3Age = (RadioButton)findViewById(R.id.genAgeActivity_rb_3Age);
        party5Age = (RadioButton)findViewById(R.id.genAgeActivity_rb_5Age);

        partyCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { //파티원 동의하면 동작
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(partyCheck.isChecked()){
                    partySex.setVisibility(View.VISIBLE);
                    partyAge.setVisibility(View.VISIBLE);
                }else{
                    partySex.setVisibility(View.GONE);
                    partyAge.setVisibility(View.GONE);
                }
            }
        }); //파티원 동의 동작 끝




        btn_nnext = (Button)findViewById(R.id.btn_nnext);

        btn_nnext.setOnClickListener(new View.OnClickListener() { //다음 버튼 눌렸을 때 동작 시작
            @Override
            public void onClick(View v) {
                if(userMale.isChecked()){
                    userSex = "남성";
                }else if(userFemale.isChecked()){
                    userSex = "여성";
                }else{
                    Toast.makeText(getApplicationContext(), "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                userAge = spinner.getSelectedItem().toString();

                if(partySameSex.isChecked()){
                    SpartySex = "동성만";
                }else if(partyDonCareSex.isChecked()){
                    SpartySex = "무관";
                }else{
                    if(partyCheck.isChecked()){
                        Toast.makeText(getApplicationContext(), "파티원 성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        SpartySex="";
                    }
                }

                if(partyDoncareAge.isChecked()){
                    SpartyAge = "무관";
                }else if(party3Age.isChecked()){
                    SpartyAge = "3살차이";
                }else if(party5Age.isChecked()){
                    SpartyAge = "5살차이";
                }else{
                    if(partyCheck.isChecked()){
                        Toast.makeText(getApplicationContext(), "파티원 나이를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        SpartyAge="";
                    }
                }

                Intent getintent = getIntent();
                String id = getintent.getStringExtra("ID");
                String pw = getintent.getStringExtra("PW");
                String name = getintent.getStringExtra("NAME");
                String phone = getintent.getStringExtra("PHONE");
                String img = getintent.getStringExtra("IMAGE");

                Intent intent = new Intent(getApplicationContext(), PartyselectActivity.class); //파티원 고르는 페이지로 이동
                intent.putExtra("ID",id);
                intent.putExtra("PW",pw);
                intent.putExtra("NAME",name);
                intent.putExtra("PHONE",phone);
                intent.putExtra("SEX",userSex);
                intent.putExtra("AGE",userAge);
                intent.putExtra("IMAGE",img);
                if(partyCheck.isChecked()){
                    intent.putExtra("PARTY","파티원 동의");
                }else{
                    intent.putExtra("PARTY","파티원 미동의");
                }

                intent.putExtra("PARTY_SEX",SpartySex);
                intent.putExtra("PARTY_AGE",SpartyAge);

                startActivityForResult(intent,0);
            }
        }); //다음 버튼 동작 끝
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            int MAIN=data.getIntExtra("END",0);
            if(MAIN==1){
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                int Main_Val=1;
                intent.putExtra("END",Main_Val);
                setResult(RESULT_OK,intent);
                finish();
            } //뒤로가기 안하려고
        }
    }
}
