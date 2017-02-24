package com.example.lovef.newcatchmeifyoucan;

import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.*;

/**
 * Created by lovef on 2016-11-21.
 */
public class Privateinformation_page extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    CheckBoxPreference join_page_check_personalinfo;
    CheckBoxPreference join_page_check_useinfo;

    CheckBox privateinformation_page_personal;
    CheckBox privateinformation_page_useinfo;

    Integer isCheked_list=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privateinformation_page);
        privateinformation_page_personal = (CheckBox)findViewById(R.id.join_page_check_personalinfo);//개인정보 객체화
        privateinformation_page_useinfo = (CheckBox)findViewById(R.id.join_page_check_useinfo);//이용정보 객체화

        privateinformation_page_personal.setOnCheckedChangeListener(this);
        privateinformation_page_useinfo.setOnCheckedChangeListener(this);

        //Log.v("체크가 몇개 됬는지...","-"+isCheked_list);


        /*
        else{
            Join_page jp = new Join_page();
            jp.Simple_push_dialog("알림","");
        }*/

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
       // isCheked_list = 0; //체크된 리스트 갯수 구하기. 여기서는 2개다 체크되어야겠지
            if(privateinformation_page_personal.isChecked()){
                isCheked_list = isCheked_list+1;
                Log.v("개인정보...","-"+isCheked_list);
                Log.v("개인정보 체크됬나","?");
            }

            if(privateinformation_page_useinfo.isChecked()){
                isCheked_list = isCheked_list+1;
                Log.v("이용내역...","-"+isCheked_list);
                Log.v("이용내역 체크됬나","?");
            }

        if(isCheked_list>2){//두개다 체크가 되면,엑티비티가 꺼지면서 result_ok를 보낸다
            Intent return_join_page = new Intent(Privateinformation_page.this,Join_page.class);
            setResult(RESULT_OK,return_join_page);
            finish();
        }
    }
}
