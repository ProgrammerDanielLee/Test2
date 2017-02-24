package com.example.lovef.newcatchmeifyoucan;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;
import android.widget.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-11-14.
 */
public class Main_page extends TabActivity{
    Context mContext;

    LinearLayout ln;

    //drawer를 위한 변수시작
    TextView drawer_sex,drawer_num,drawer_age,drawer_email,drawer_name,drawer_tall,drawer_job,drawer_local,drawer_mind,drawer_smoking,drawer_blood,drawer_hobby,drawer_final_oneword;
    ImageView drawer_img;
    //drawer를 위한 변수 끝

    //로그인 후 모든 정보를 가져온 후 담을 변수 시작
    String login_sex,login_num,login_age,login_email,login_name,login_tall,login_job,login_local,login_mind,login_smoking,login_blood,login_hobby,login_final_oneword,login_img;
    //여기에서 login_num은 전화면에서 넘어온 값을 받아서 조회하는 데에 쓰인다.
    //로그인 후 모든 정보를 가져온 후 담을 변수 끝

    //토큰값(사원번호와 matching 시킬) 시작
    String important_token;
    //토큰값 끝

    //tabhost 관련 메뉴 시작
    private Intent tab;
    private TabHost.TabSpec Main_page_tap;
    private TabHost tabhost;
    //tabhost 관련 메뉴 끝

    //토큰을 위한 변수 시작
    private String json_for_join_token,handover_json_to_nextpage;
    //토큰을 위한 변수 끝

    //초반에 전체정보를 조회하기 위해서 이전 layout으로 부터 번호 받기 시작
    private void Get_first_information_and_json(){
        login_num = getIntent().getStringExtra("login_num");
        important_token = getIntent().getStringExtra("set_token_mainact");
        JSONObject jo = new JSONObject();
        try {
            jo.put("json_num_core_page",login_num);
            jo.put("json_token_core_page",important_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json_for_join_token = jo.toString();
    }
    //초반에 전체정보를 조회하기 위해서 이전 layout으로 부터 번호 받기 끝

    //넘어온 정보들 json 해제 후에 변수에 대입 시작
    private void Insert_json_to_variable(){
        try {
            JSONObject j_o = new JSONObject(handover_json_to_nextpage);
            login_num = j_o.getString("user_num");
            login_email= j_o.getString("email");
            login_name = j_o.getString("name");
            login_sex= j_o.getString("sex");
            login_age = j_o.getString("age");
            login_tall = j_o.getString("tall");
            login_job = j_o.getString("job");
            login_local = j_o.getString("local");
            login_smoking = j_o.getString("smoking");
            login_mind = j_o.getString("mind");
            login_blood = j_o.getString("blood");
            login_hobby = j_o.getString("hobby");
            login_img = j_o.getString("img");
            login_final_oneword = j_o.getString("final_oneword");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //넘어온 정보들 json 해제 후에 변수에 대입 끝

    //drawer 객체화 시작
    private void Objectification() {

        drawer_sex = (TextView)findViewById(R.id.left_drawer_sex);
        drawer_local = (TextView)findViewById(R.id.left_drawer_local);
        drawer_num = (TextView)findViewById(R.id.left_drawer_number);
        drawer_email = (TextView)findViewById(R.id.left_drawer_email);
        drawer_name = (TextView)findViewById(R.id.left_drawer_name);
        drawer_age = (TextView)findViewById(R.id.left_drawer_age);
        drawer_tall = (TextView)findViewById(R.id.left_drawer_tall);
        drawer_job = (TextView)findViewById(R.id.left_drawer_job);
        drawer_smoking = (TextView)findViewById(R.id.left_drawer_smoking);
        drawer_mind = (TextView)findViewById(R.id.left_drawer_mind);
        drawer_blood = (TextView)findViewById(R.id.left_drawer_blood);
        drawer_hobby = (TextView)findViewById(R.id.left_drawer_hobby);
        drawer_img = (ImageView)findViewById(R.id.drawer_img);
        drawer_final_oneword = (TextView)findViewById(R.id.final_oneword);
    }
    //drawer 객체화 끝

    //drawer 에 정보 꽃아주기 시작
    private void Set_drawer(){
        drawer_sex.setText(login_sex);
        drawer_local.setText(login_local);
        drawer_num.setText(login_num);
        drawer_email.setText(login_email);
        drawer_name.setText(login_name);
        drawer_age.setText(login_age);
        drawer_tall.setText(login_tall);
        drawer_job.setText(login_job);
        drawer_smoking.setText(login_smoking);
        drawer_mind.setText(login_mind);
        drawer_blood.setText(login_blood);
        drawer_hobby.setText(login_hobby);
        drawer_final_oneword.setText(login_final_oneword);
        Get_image(login_img);
    }
    //drawer에 정보 꽃아주기 끝

    //이미지 가져오기 시작
    private void Get_image(String image_name){
        Glide.with(Main_page.this).load("http://cmic.dothome.co.kr/user_signup/uploads/"+image_name)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                //.centerCrop()
                //.crossFade()
                .into(drawer_img);
    }
    //이미지 가져오기 끝


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //모든 정보를 얻기 위한 json 시작
        Get_first_information_and_json();
        //모든 정보를 얻기 위한 json 끝

        setContentView(R.layout.main_page);

        //정보 가져오기 + 토큰 등록해주기 시작
        Inquire_all_information(json_for_join_token);
        //정보 가져오기 + 토큰 등록해주기 끝
    }

    //데이터 삽입 시작
    public void Inquire_all_information(final String json_result){
        class insertdata extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_for_send2 = (String)strings[0];
                    String data = "json_final"+"="+json_result_for_send2;
                    //Log.v("json결과2","json결과2"+data);
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_login/Inquire_all_information.php");//여기 수정
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line ;
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                        break;
                    }

                    return sb.toString();

                }catch(Exception e){
                    return new String("exeption :"+e.getMessage());
                }
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("모든정보 불러오기","drawer에 꽃기"+s);
                handover_json_to_nextpage = s;

                //우선 drawer에 위젯들 객체화 시작
                Objectification();
                //우선 drawer에 위젯들 객체화 끝

                //넘어온 정보들 json 해제 후에 변수에 대입 시작
                Insert_json_to_variable();
                //넘어온 정보들 json 해제 후에 변수에 대입 끝

                //drawer에 꽃아주기 시작
                Set_drawer();
                //drawer에 꽃아주기 끝
/*
                tabhost = getTabHost();
                TabWidget wt = tabhost.getTabWidget();
                for(int i=0; i< wt.getChildCount(); i++){
                    View v = wt.getChildAt(i);

                    TextView tv = (TextView)v.findViewById(android.R.id.title);
                    if(tv == null){continue;}
                    v.setBackgroundResource(R.drawable.tab_selector);
                }*/
                Animation main_1 = AnimationUtils.loadAnimation(Main_page.this,R.anim.fade_in_ver3);
                //tabhost.getTabWidget().setRightStripDrawable(R.drawable.tab_under_line);
                //tabhost.getTabWidget().setLeftStripDrawable(R.drawable.tab_under_line);
                Animation mini_fade_1 = AnimationUtils.loadAnimation(Main_page.this,R.anim.fade_mini_1);
                Animation mini_fade_2 = AnimationUtils.loadAnimation(Main_page.this,R.anim.fade_mini_2);
                Animation mini_fade_3 = AnimationUtils.loadAnimation(Main_page.this,R.anim.fade_mini_3);

                tabhost = getTabHost();
                tabhost.getTabWidget().setStripEnabled(false);
                //1.Nationwide로 보내는 곳 시작
                tab = new Intent().setClass(Main_page.this,Nationwidematching_page.class);
                //tab.putExtra("core_num", login_num);
                //tab.putExtra("core_local", login_local);
                //tab.putExtra("core_sex", login_sex);
                tab.putExtra("login_json",handover_json_to_nextpage);
                Main_page_tap = tabhost.newTabSpec("Nationwide_matching").setIndicator("",getResources().getDrawable(R.drawable.d_national_icon)).setContent(tab);
                tabhost.addTab(Main_page_tap);
                tabhost.startAnimation(mini_fade_1);
                //1.Nationwide로 보내는곳 끝

                //2.Room_matching으로 보내는 곳 시작
                tab = new Intent().setClass(Main_page.this,Roommatching_page.class);
                tab.putExtra("user_num",login_num);
                tab.putExtra("user_sex",login_sex);
                tab.putExtra("login_json",handover_json_to_nextpage);
                Main_page_tap = tabhost.newTabSpec("Roommatching_matching").setIndicator("",getResources().getDrawable(R.drawable.d_room_matching_icon)).setContent(tab);
                tabhost.addTab(Main_page_tap);
                tabhost.startAnimation(mini_fade_2);
                //2.Room_matching으로 보내는 곳 끝

                //3.Room_matching_chatting 보내는 곳 시작
                tab = new Intent().setClass(Main_page.this,Chatlist.class);
                tab.putExtra("login_json",handover_json_to_nextpage);
                Main_page_tap = tabhost.newTabSpec("Chatting").setIndicator("",getResources().getDrawable(R.drawable.d_chat_icon)).setContent(tab);
                tabhost.addTab(Main_page_tap);
                tabhost.startAnimation(mini_fade_3);
                //3.Room_matching_chatting 보내는 곳 끝

                //4.환경설정 시작
                /*
                tab = new Intent().setClass(Main_page.this,Preferences_page.class);
                tab.putExtra("login_json",handover_json_to_nextpage);
                Main_page_tap = tabhost.newTabSpec("Preferences").setIndicator("",getResources().getDrawable(R.drawable.d_sp_icon)).setContent(tab);
                tabhost.addTab(Main_page_tap);
                */
                //4.환경설정 끝


            }
        }
        insertdata task = new insertdata();
        task.execute(json_result);
    }


}
