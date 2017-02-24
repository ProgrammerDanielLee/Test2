package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-12-02.
 */
public class Nationwidematching_page_view extends AppCompatActivity {
    String hurry_num;



    Integer THIS_VIEW_POSITION;
    String json_result_for_send;
    String json_result_for_send_left;
    String json_result_for_send_right;
    //이미지 server url + context 시작
    private Context mContext = this;//이미지 처리 때문에..glide
    private String server_url = "http://cmic.dothome.co.kr/user_signup/uploads/";//이미지 추가 3
    //이미지 server url+ context 끝

    //choice_before에 넘겨줄 변수
    private String result_json;
    //choice_before에 넘겨줄 변수

    //서버말고, 전 화면에서 넘어온 변수 시작
    String get_select_view
    ,get_select_human_depth//전화면(선택/결과)에서 넘어온 현재 단계
    ,get_select_human_sex//전화면(선택/결과)에서 넘어온 선택한사람의 성별
    ,get_select_human_num //전 화면(선택/결과)에서 넘어온 선택한 사람의 번호
    ,get_selected_human_num //전화면 (결과)에서 넘어온 선택된 사람의 번호
    ,get_left_human_num//전 화면(선택/결과)에서 넘어온 왼쪽사람의 번호
    ,get_right_human_num//전화면(선택/결과)에서 넘어온 오른쪽 사람의 번호
    ,get_left_img_name
    ,get_right_img_name;
    //서버말고, 전 화면에서 넘어온 변수 끝

    //서버에서 가져온 변수 + 그것들을 객체화 시킬 안드로이드 뷰 시작
    //layout 객체화 관련 변수 시작
    TextView view_title,view_depth,two_depth_block_left,two_depth_block_right,three_depth_block_left,three_depth_block_right;
    AppCompatImageView view_left_imageview,view_right_imageview;
    ImageView left_img,right_img;
    View left_ln_blood,left_ln_tall,left_ln_job,right_ln_blood,right_ln_tall,right_ln_job;
    //layout 객체화 관련 변수 끝
        //왼쪽 시작
            //1단계
                TextView sex_textview_left,number_textview_left,age_textview_left,local_textview_left,smoking_textview_left,mind_textview_left,hobby_textview_left;
                String view_email_left,view_name_left,view_sex_left,view_num_left,view_age_left,view_local_left,view_smoking_left,view_mind_left,view_hobby_left,view_final_left;
            //2단계
                TextView blood_textview_left,tall_textview_left,job_textview_left;
                String view_blood_left,view_tall_left,view_job_left;
            //3단계
                TextView oneword_textview_left;
        Button button_left;//왼쪽 버튼
        //왼쪽 끝

        //오른쪽 시작
            //1단계
                TextView sex_textview_right,number_textview_right,age_textview_right,local_textview_right,smoking_textview_right,mind_textview_right,hobby_textview_right;
                String view_email_right,view_name_right,view_sex_right,view_num_right,view_age_right,view_local_right,view_smoking_right,view_mind_right,view_hobby_right,view_final_right;
            //2단계
                TextView blood_textview_right,tall_textview_right,job_textview_right;
                String view_blood_right,view_tall_right,view_job_right;
            //3단계
                TextView oneword_textview_right;
        Button button_right;//오른쪽 버튼
        //오른쪽 끝
    //서버에서 가져온 변수 끝

    //객체화 구현 시작
    private void Objectification(){
        view_title = (TextView)findViewById(R.id.view_title_id);
        view_depth = (TextView)findViewById(R.id.depth_image_text);

        //왼쪽 객체화 시작
        left_img = (ImageView)findViewById(R.id.pro_img_left_view);//왼쪽 이미지
        left_ln_blood = (View) findViewById(R.id.left_blood_linear_id);
        left_ln_job = (View) findViewById(R.id.left_job_linear_id);
        left_ln_tall = (View) findViewById(R.id.left_tall_linear_id);
        view_left_imageview = (AppCompatImageView)findViewById(R.id.left_selected_id);//왼쪽 이미지 뷰
        sex_textview_left = (TextView)findViewById(R.id.left_sex_id);
        number_textview_left = (TextView)findViewById(R.id.left_number_id);
        age_textview_left = (TextView)findViewById(R.id.left_age_id);
        local_textview_left = (TextView)findViewById(R.id.left_local_id);
        smoking_textview_left = (TextView)findViewById(R.id.left_smoking_id);
        mind_textview_left = (TextView)findViewById(R.id.left_mind_id);
        hobby_textview_left = (TextView)findViewById(R.id.left_hobby_id);
        blood_textview_left = (TextView)findViewById(R.id.left_blood_id);
        job_textview_left = (TextView)findViewById(R.id.left_job_id);
        tall_textview_left = (TextView)findViewById(R.id.left_tall_id);
        oneword_textview_left = (TextView)findViewById(R.id.final_oneword_left);

        two_depth_block_left = (TextView)findViewById(R.id.left_secret_twodepth_textview);//2단계 비공개입니다 푯말 객체화
        three_depth_block_left = (TextView)findViewById(R.id.left_secret_threedepth_textview);//왼쪽 공개됩니다 객체연결
        //왼쪽 객체화 끝

        //오른쪽 객체화 시작
        right_img = (ImageView)findViewById(R.id.pro_img_right_view);//오른쪽 이미지
        right_ln_blood = (View) findViewById(R.id.right_blood_linear_id);
        right_ln_job = (View) findViewById(R.id.right_job_linear_id);
        right_ln_tall = (View) findViewById(R.id.right_tall_linear_id);
        view_right_imageview = (AppCompatImageView)findViewById(R.id.right_selected_id);//오른쪽 이미지 뷰
        sex_textview_right = (TextView)findViewById(R.id.right_sex_id);
        number_textview_right = (TextView)findViewById(R.id.right_number_id);
        age_textview_right = (TextView)findViewById(R.id.right_age_id);
        local_textview_right = (TextView)findViewById(R.id.right_local_id);
        smoking_textview_right = (TextView)findViewById(R.id.right_smoking_id);
        mind_textview_right = (TextView)findViewById(R.id.right_mind_id);
        hobby_textview_right = (TextView)findViewById(R.id.right_hobby_id);
        blood_textview_right = (TextView)findViewById(R.id.right_blood_id);
        job_textview_right = (TextView)findViewById(R.id.right_job_id);
        tall_textview_right = (TextView)findViewById(R.id.right_tall_id);
        oneword_textview_right = (TextView)findViewById(R.id.final_oneword_right);

        two_depth_block_right = (TextView)findViewById(R.id.right_secret_twodepth_textview);
        three_depth_block_right = (TextView)findViewById(R.id.right_secret_threedepth_textview);//오른쪽 공개됩니다 객체 연결
        //오른쪽 객체화 끝

        button_left = (Button)findViewById(R.id.left_button_id);//왼쪽버튼
        button_right = (Button)findViewById(R.id.right_button_id);//오른쪽 버튼
    }
    //객체화 구현 끝

    //1단계,2단계,3단계 선택/결과로 부터 넘어온 변수를 담는 메소드 시작
    private void Get_select_or_result(){
        //선택으로 넘어왔을 경우
        get_select_view = getIntent().getStringExtra("page_view");//현재 화면(선택인지 결과인지 알려준다)
        get_select_human_depth = getIntent().getStringExtra("page_one_select_depth");//현재 단계(1,2,3단계를 알려준다)
        get_select_human_sex = getIntent().getStringExtra("page_one_select_sex");//선택한 사람의 성별
        get_select_human_num = getIntent().getStringExtra("page_one_select_human");//선택한 사람의 번호
        get_selected_human_num = getIntent().getStringExtra("page_one_selected_human");//선택된 사람의 번호
        get_left_human_num = getIntent().getStringExtra("page_one_left_number");//왼쪽 사람
        get_right_human_num = getIntent().getStringExtra("page_one_right_number");//오른쪽 사람
        get_left_img_name = getIntent().getStringExtra("page_one_left_human_img");
        get_right_img_name = getIntent().getStringExtra("page_one_right_human_img");
        THIS_VIEW_POSITION = getIntent().getIntExtra("page_position",0);
            Log.v("선택으로 넘어왔나?","-"+get_select_view);
        //결과로 넘어왔을 경우

    }
    //1단계,2단계,3단계 선택/결과로 부터 넘어온 변수를 담는 메소드 끝

    //다이얼로그 메소드 시작
    private void Get_dialog(String title,String contents){
        AlertDialog.Builder builder = new AlertDialog.Builder(Nationwidematching_page_view.this);
        // 다이얼로그의 제목을 설정한다.
        TextView tt = new TextView(Nationwidematching_page_view.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그의 제목을 설정한다.
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Nationwidematching_page_view.this);
        tm.setText(contents);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        //tm.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그에 출력될 메세지 내용을 적는다.
        builder.setView(tm);
        // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
        //builder.setIcon(R.drawable.core_icon_image_1);
        //사용자가 취소 못하게 막는다
        builder.setCancelable(false);

        // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
        builder.setNegativeButton("확인",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result ="결과";
                try {
                JSONObject j_o = new JSONObject();
                    j_o.put("matching_result_mode",get_select_view);
                    j_o.put("matching_result_sex",get_select_human_sex);
                    j_o.put("matching_result_depth",get_select_human_depth);
                    j_o.put("matching_result_s",get_select_human_num);
                    j_o.put("matching_result_st",get_selected_human_num);
                    j_o.put("matching_result_left",get_left_human_num);
                    j_o.put("matching_result_right",get_right_human_num);
                    j_o.put("matching_result_left_img",get_left_img_name);
                    j_o.put("matching_result_right_img",get_right_img_name);
                   result_json = j_o.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Intent return_page_one = new Intent(Nationwidematching_page_view.this,Nationwidematching_page_others_choice.class);
                //return_page_one.putExtra("delete_positoin",THIS_VIEW_POSITION);
                //Log.v("position값","one_view"+THIS_VIEW_POSITION);
                //setResult(RESULT_OK,return_page_one);
                Intent return_page_one = new Intent(Nationwidematching_page_view.this,Nationwidematching_page_others_choice.class);
                return_page_one.putExtra("delete_positoin",THIS_VIEW_POSITION);
                //Log.v("position값","one_view"+THIS_VIEW_POSITION);
                setResult(RESULT_OK,return_page_one);

                //확인을 누른순간, 지금 view에 있는 사용자 정보 + 결과를 확인했다는 여부 체크하여 변수넣어서 다른 activity로 넘김
                Intent go_choice_before = new Intent(Nationwidematching_page_view.this,Nationwidematching_page_others_choice_before.class);
                go_choice_before.putExtra("result",result);
                go_choice_before.putExtra("result_json",result_json);
                Log.v("두개다 제대로","?"+result+result_json);
                startActivity(go_choice_before);
                finish();
            }});
        builder.show();
    }
    //다이얼로그 메소드 끝

    //왼쪽,오른쪽 글라이드 이미지 가져오는 메소드 시작
    private void Get_glide_img_left_right(){
        //left profile img
        left_img = (ImageView)findViewById(R.id.pro_img_left_view);//이미지 추가9
        if(get_select_human_depth!=null){
            if(get_select_human_depth.equals("1")){
                Glide.with(Nationwidematching_page_view.this).load(R.drawable.default_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(left_img);
            }
        }
        Glide.with(Nationwidematching_page_view.this).load(server_url+get_left_img_name)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                //.thumbnail((float) 0.5)
                .into(left_img);
        //left profile img end

        //right profile img
        right_img = (ImageView)findViewById(R.id.pro_img_right_view);//이미지 추가10
        if(get_select_human_depth!=null){
            if(get_select_human_depth.equals("1")){
                Glide.with(Nationwidematching_page_view.this).load(R.drawable.default_img)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(right_img);
            }
        }
        Glide.with(Nationwidematching_page_view.this).load(server_url+get_right_img_name)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(right_img);
        //right profile img end
    }
    //왼쪽,오른쪽 글라이드 이미지 가져오는 메소드 끝

    //왼쪽,오른쪽 고정이미지 가져오는 메소드 시작
    private void Set_default_img_left_right(){
        if(get_select_human_sex.equals("여자")){
            Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_man_icon_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(left_img);
            Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_man_icon_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(right_img);
        }else{
            Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_girl_icon_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(left_img);
            Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_girl_icon_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(right_img);
        }
        //결과화면 1단계 남녀사진 안보이기 끝
    }
    //왼쪽,오른쪽 고정이미지 가져오는 메소드 끝

    //하단에 선택결과 보이게끔 하는 메소드 시작
    private void appear_check_animation(){
        Animation slowly_appear;
        slowly_appear = AnimationUtils.loadAnimation(Nationwidematching_page_view.this,R.anim.fadein_result);
        view_left_imageview.setAnimation(slowly_appear);
    }
    //하단에 선택결과 보이게끔 하는 메소드 끝

    //결과 화면을 볼때에, 버튼들 가리는 메소드 시작
    private void hide_buttons(){
        button_left.setVisibility(View.GONE);
        button_right.setVisibility(View.GONE);
    }
    //결과 화면을 볼떄에, 버튼들 가리는 메소드 끝

    //초반에 서버로 두개의 번호를 보내서 양쪽 정보 모두 가져오는 메소드 시작
    private void get_json_for_information(){
        JSONObject user_info = new JSONObject();
        try {
            user_info.put("get_left_human_num",get_left_human_num);
            user_info.put("get_right_human_num",get_right_human_num);
            json_result_for_send = user_info.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //초반에 서버로 두개의 번호를 보내서 양쪽 정보 모두 가져오는 메소드 끝

    //선택버튼 눌렀을때, 확정 다이얼로그 시작
    private void get_confirm_of_left_dialog(String title,String contents){
        AlertDialog.Builder builder = new AlertDialog.Builder(Nationwidematching_page_view.this);
        TextView tt = new TextView(Nationwidematching_page_view.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그의 제목을 설정한다.
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Nationwidematching_page_view.this);
        tm.setText(contents);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        //tm.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그에 출력될 메세지 내용을 적는다.
        builder.setView(tm);
        // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
        //사용자가 취소 못하게 막는다
        builder.setCancelable(false);

        // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //스레드 발동
                inserttodata_left_button(json_result_for_send_left);//왼쪽 선택된 사람 서버로 전송
                //여기서 startforactivityresult해야할듯...
                Intent return_page_one = new Intent(Nationwidematching_page_view.this,Nationwidematching_page.class);
                return_page_one.putExtra("delete_positoin",THIS_VIEW_POSITION);
                //Log.v("position값","one_view"+THIS_VIEW_POSITION);
                setResult(RESULT_OK,return_page_one);
                finish();
            }
        });
        builder.setPositiveButton("취소", null);

        // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.
        builder.show();
    }
    //선택버튼 눌렀을때, 확정 다이얼로그 끝

    private void get_confirm_of_right_dialog(String title,String contents){
        AlertDialog.Builder builder = new AlertDialog.Builder(Nationwidematching_page_view.this);
        TextView tt = new TextView(Nationwidematching_page_view.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그의 제목을 설정한다.
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Nationwidematching_page_view.this);
        tm.setText(contents);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        //tm.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그에 출력될 메세지 내용을 적는다.
        builder.setView(tm);
        // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
        //builder.setIcon(R.drawable.core_icon_image_1);
        //사용자가 취소 못하게 막는다
        builder.setCancelable(false);

        // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //스레드 발동
                inserttodata_right_button(json_result_for_send_right);//왼쪽 선택된 사람 서버로 전송

                //여기서 startforactivityresult해야할듯...
                Intent return_page_one = new Intent(Nationwidematching_page_view.this,Nationwidematching_page.class);
                return_page_one.putExtra("delete_position",THIS_VIEW_POSITION);
                Log.v("position값","one_view"+THIS_VIEW_POSITION);
                setResult(RESULT_OK,return_page_one);
                finish();
            }
        });
        builder.setPositiveButton("취소", null);

        // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nationwidematching_page_view);

        Get_select_or_result();
        Objectification();
        get_json_for_information();
        inserttodata(json_result_for_send);

        if (get_select_view.equals("결과")) {
            if (get_select_human_depth.equals("1")) {
                //Log.v("1에 들어왔지롱","결과"+get_selected_human_num+get_left_human_num);
                view_title.setText("1단계 매칭 결과");
                hide_buttons();

                //왼쪽 오른쪽 이미지 불러오기 시작
                //Get_glide_img_left_right();
                //왼쪽 오른쪽 이미지 불러오기 끝

                //결과화면 1단계 남녀사진 안보이기 시작
                if(get_select_human_sex.equals("여자")){
                    Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_man_icon_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .into(left_img);
                    Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_man_icon_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .into(right_img);
                }else{
                    Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_girl_icon_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .into(left_img);
                    Glide.with(Nationwidematching_page_view.this).load(R.drawable.d_girl_icon_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .into(right_img);
                }
                //결과화면 1단계 남녀사진 안보이기 끝

                if (get_selected_human_num.equals(get_left_human_num)) {//선택된 사람과 왼쪽사람이 같으면
                    view_left_imageview.setVisibility(View.VISIBLE);
                    //하단에 결과 보이는 에니메이션 시작
                    appear_check_animation();
                    //하단에 결과 보이는 에니메이션 끝

                    //다이얼로그 삽입 시작
                    Get_dialog("매칭에 성공하였습니다", "누군가가 당신을 또 선택하게 되면 \n 2단계 매칭이 시작됩니다");
                    //다이얼로그 삽입 끝
                } else if (get_selected_human_num.equals(get_right_human_num)) {//선택된 사람과 오른쪽 사람이 같으면
                    view_right_imageview.setVisibility(View.VISIBLE);
                    //하단에 결과 보이는 에니메이션 시작
                    appear_check_animation();
                    //하단에 결과 보이는 에니메이션 끝

                    //다이얼로그 삽입 시작
                    Get_dialog("매칭에 성공하였습니다", "누군가가 당신을 또 선택하게 되면 \n 2단계 매칭이 시작됩니다");
                    //다이얼로그 삽입 끝
                }
            } else if (get_select_human_depth.equals("2")) {
                view_title.setText("2단계 매칭 결과");
                hide_buttons();

                //왼쪽 오른쪽 이미지 불러오기 시작
                Get_glide_img_left_right();
                //왼쪽 오른쪽 이미지 불러오기 끝

                two_depth_block_left.setVisibility(View.GONE);//2단계때 공게됩니다 푯말 가리기
                two_depth_block_right.setVisibility(View.GONE);

                left_ln_blood.setVisibility(View.VISIBLE);//왼쪽 혈액형 리니어 보이게
                right_ln_blood.setVisibility(View.VISIBLE);//오른쪽 혈액형 리니어 보이게
                left_ln_job.setVisibility(View.VISIBLE);//왼쪽 직업 리니어 보이게
                right_ln_job.setVisibility(View.VISIBLE);//오른쪽 직업 리니어 보이게
                left_ln_tall.setVisibility(View.VISIBLE);//왼쪽 키 리니어 보이게
                right_ln_tall.setVisibility(View.VISIBLE);//오른쪽 키 리니어 보이게


                if (get_selected_human_num.equals(get_left_human_num)) {//선택된 사람과 왼쪽사람이 같으면
                    view_left_imageview.setVisibility(View.VISIBLE);//선택된거 체크

                    //하단에 결과 보이는 에니메이션 시작
                    appear_check_animation();
                    //하단에 결과 보이는 에니메이션 끝

                    //다이얼로그 삽입 시작
                    Get_dialog("매칭에 성공하였습니다", "누군가가 당신을 또 선택하게 되면 \n 최종단계 매칭이 시작됩니다");
                    //다이얼로그 삽입 끝
                } else if (get_selected_human_num.equals(get_right_human_num)) {//선택된 사람과 오른쪽 사람이 같으면
                    view_right_imageview.setVisibility(View.VISIBLE);//선택된거 체크

                    //하단에 결과 보이는 에니메이션 시작
                    appear_check_animation();
                    //하단에 결과 보이는 에니메이션 끝

                    //다이얼로그 삽입 시작
                    Get_dialog("매칭에 성공하였습니다", "누군가가 당신을 또 선택하게 되면 \n 최종단계 매칭이 시작됩니다");
                    //다이얼로그 삽입 끝
                }

            } else if (get_select_human_depth.equals("3")) {//여기 처리해줘야지
                view_title.setText("최종 단계 매칭 결과");
                hide_buttons();

                //왼쪽 오른쪽 이미지 불러오기 시작
                Get_glide_img_left_right();
                //왼쪽 오른쪽 이미지 불러오기 끝

                left_ln_blood.setVisibility(View.VISIBLE);//왼쪽 혈액형 리니어 보이게
                right_ln_blood.setVisibility(View.VISIBLE);//오른쪽 혈액형 리니어 보이게
                left_ln_job.setVisibility(View.VISIBLE);//왼쪽 직업 리니어 보이게
                right_ln_job.setVisibility(View.VISIBLE);//오른쪽 직업 리니어 보이게
                left_ln_tall.setVisibility(View.VISIBLE);//왼쪽 키 리니어 보이게
                right_ln_tall.setVisibility(View.VISIBLE);//오른쪽 키 리니어 보이게

                three_depth_block_left.setVisibility(View.GONE);
                three_depth_block_right.setVisibility(View.GONE);

                oneword_textview_left.setVisibility(View.VISIBLE);//마지막 한마디는 보이게 하고~
                oneword_textview_right.setVisibility(View.VISIBLE);

                if (get_selected_human_num.equals(get_left_human_num)) {//선택된 사람과 왼쪽사람이 같으면
                    view_left_imageview.setVisibility(View.VISIBLE);
                    //에니메이션 체크 후 다이얼로그

                    //하단에 결과 보이는 에니메이션 시작
                    appear_check_animation();
                    //하단에 결과 보이는 에니메이션 끝

                    AlertDialog.Builder builder = new AlertDialog.Builder(Nationwidematching_page_view.this);

                    TextView tt = new TextView(Nationwidematching_page_view.this);
                    tt.setText("매칭에 성공하였습니다");
                    tt.setPadding(10,15,10,15);
                    tt.setGravity(Gravity.CENTER);
                    tt.setTextSize(20);
                    tt.setTextColor(Color.parseColor("#FFFFFF"));
                    tt.setBackgroundColor(Color.parseColor("#FC6265"));
                    // 다이얼로그의 제목을 설정한다.
                    builder.setCustomTitle(tt);

                    TextView tm = new TextView(Nationwidematching_page_view.this);
                    tm.setText("상대방과 대화를 시작하시겠습니까?");
                    tm.setPadding(10,60,10,60);
                    tm.setGravity(Gravity.CENTER);
                    tm.setTextColor(Color.parseColor("#FC6265"));
                    //tm.setBackgroundColor(Color.parseColor("#FC6265"));
                    // 다이얼로그에 출력될 메세지 내용을 적는다.
                    builder.setView(tm);
                    //사용자가 취소 못하게 막는다
                    builder.setCancelable(false);

                    // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
                    builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //대화시작
                            // 무엇을 넘겨야 할까요? 내 번호, 상대방 번호
                            /*
                            Intent go_chat = new Intent(Nationwidematching_page_view.this, Chat_page.class);
                            go_chat.putExtra("chat_select_num", get_select_human_num);//선택한 사람의 성
                            go_chat.putExtra("chat_select_sex",get_select_human_sex);//선택한 사람의 성
                            go_chat.putExtra("chat_selected_num", get_selected_human_num);//선택된사람
                            startActivity(go_chat);
                            finish();
                            */
                            //대화 끝
                            //왼쪽과 오른쪽 둘다 처리해주기 시작
                            Log.v("(왼쪽)대화시작되기전 '네'","!");
                            //테그
                            //서버로 넘겨줄 정보(채팅을 위한) 시작
                            JSONObject j_o = new JSONObject();
                            try {
                                j_o.put("hurry_num",get_select_human_num);//최초에 나를 선택해준 사람
                                j_o.put("chat_selected_num",get_selected_human_num);//나
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hurry_num = j_o.toString();
                            Log.v("json으로 묶은 채팅상대","-"+hurry_num);
                            //서버로 넘겨줄 정보(채팅을 위한) 끝
                            if(hurry_num !=null) {
                                Log.v("hurry_num이 null이 아니면","!");
                                go_server_hurry(hurry_num);
                            }
                            //왼쪽과 오른쪽 둘다 처리해주기 끝
                        }
                    });

                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //다시한번 재차 묻는 dialog 작성하기 시작

                            //다시한번 재차 묻는 dialog 작성하기 끝
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    // alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.
                    builder.show();
                    //다이얼로그 삽입 끝
                } else if (get_selected_human_num.equals(get_right_human_num)) {//선택된 사람과 오른쪽 사람이 같으면
                    view_right_imageview.setVisibility(View.VISIBLE);//오른쪽에 이미지 체크
                    //에니메이션 체크 후 다이얼로그




                    //하단에 결과 보이는 에니메이션 시작
                    appear_check_animation();
                    //하단에 결과 보이는 에니메이션 끝

                    //다이얼로그 삽입 시작

                    AlertDialog.Builder builder = new AlertDialog.Builder(Nationwidematching_page_view.this);
                    // 다이얼로그의 제목을 설정한다.
                    TextView tt = new TextView(Nationwidematching_page_view.this);
                    tt.setText("매칭에 성공하였습니다");
                    tt.setPadding(10,15,10,15);
                    tt.setGravity(Gravity.CENTER);
                    tt.setTextSize(20);
                    tt.setTextColor(Color.parseColor("#FFFFFF"));
                    tt.setBackgroundColor(Color.parseColor("#FC6265"));
                    // 다이얼로그의 제목을 설정한다.
                    builder.setCustomTitle(tt);

                    TextView tm = new TextView(Nationwidematching_page_view.this);
                    tm.setText("상대방과 대화를 시작하시겠습니까?");
                    tm.setPadding(10,60,10,60);
                    tm.setGravity(Gravity.CENTER);
                    tm.setTextColor(Color.parseColor("#FC6265"));
                    //tm.setBackgroundColor(Color.parseColor("#FC6265"));
                    // 다이얼로그에 출력될 메세지 내용을 적는다.
                    builder.setView(tm);
                    // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
                    //사용자가 취소 못하게 막는다
                    builder.setCancelable(false);
                    // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
                    builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //왼쪽과 오른쪽 둘다 처리해주기 시작
                            Log.v("대화시작되기전 '네'","!");
                            //테그
                            //서버로 넘겨줄 정보(채팅을 위한) 시작
                            JSONObject j_o = new JSONObject();
                            try {
                                j_o.put("hurry_num",get_select_human_num);
                                j_o.put("chat_selected_num",get_selected_human_num);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            hurry_num = j_o.toString();
                            Log.v("json으로 묶은 채팅상대","-"+hurry_num);
                            //서버로 넘겨줄 정보(채팅을 위한) 끝
                            if(hurry_num !=null) {
                                Log.v("hurry_num이 null이 아니면","!");
                                go_server_hurry(hurry_num);
                            }
                            //왼쪽과 오른쪽 둘다 처리해주기 끝
                        }
                    });
                    builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //다시한번 재차 묻는 dialog 작성하기 시작

                            //다시한번 재차 묻는 dialog 작성하기 끝
                            finish();
                        }
                    });
                    // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.

                    AlertDialog alertDialog = builder.create();
                    // alertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;

                    builder.show();
                    //다이얼로그 삽입 끝
                }
            }
        } else if (get_select_view.equals("선택")) {
            //결과 이외의 것이 오면
            if (get_select_human_depth.equals("1")) {//선택이면서 1단계이면
                view_title.setText("1단계 선택하기");
                view_depth = (TextView) findViewById(R.id.depth_image_text);
                view_depth.setVisibility(View.VISIBLE);
                //Get_glide_img_left_right();
                Set_default_img_left_right();
            } else if (get_select_human_depth.equals("2")) {//선택이면서 2단계이면
                view_title.setText("2단계 선택하기");
                view_depth.setVisibility(View.GONE);
                //사진 보여주고 시작
                Get_glide_img_left_right();
                //사진 보여주고 끝

                //2단계 도움말 block 처리 시작
                two_depth_block_left.setVisibility(View.GONE);
                two_depth_block_right.setVisibility(View.GONE);
                //2단계 도움말 block 처리 끝

                left_ln_blood.setVisibility(View.VISIBLE);//왼쪽 혈액형 리니어 보이게
                right_ln_blood.setVisibility(View.VISIBLE);//오른쪽 혈액형 리니어 보이게
                left_ln_job.setVisibility(View.VISIBLE);//왼쪽 직업 리니어 보이게
                right_ln_job.setVisibility(View.VISIBLE);//오른쪽 직업 리니어 보이게
                left_ln_tall.setVisibility(View.VISIBLE);//왼쪽 키 리니어 보이게
                right_ln_tall.setVisibility(View.VISIBLE);//오른쪽 키 리니어 보이게
                //버튼은 따로 작업 안해도 괜찮은듯
            } else if (get_select_human_depth.equals("3")) {
                view_title.setText("최종 단계 선택하기");
                view_depth.setVisibility(View.GONE);
                //사진 보여주고 시작
                Get_glide_img_left_right();
                //사진 보여주고 끝

                //2단계 도움말 block 처리 시작
                two_depth_block_left.setVisibility(View.GONE);
                two_depth_block_right.setVisibility(View.GONE);
                //2단계 도움말 block 처리 끝

                left_ln_blood.setVisibility(View.VISIBLE);//왼쪽 혈액형 리니어 보이게
                right_ln_blood.setVisibility(View.VISIBLE);//오른쪽 혈액형 리니어 보이게
                left_ln_job.setVisibility(View.VISIBLE);//왼쪽 직업 리니어 보이게
                right_ln_job.setVisibility(View.VISIBLE);//오른쪽 직업 리니어 보이게
                left_ln_tall.setVisibility(View.VISIBLE);//왼쪽 키 리니어 보이게
                right_ln_tall.setVisibility(View.VISIBLE);//오른쪽 키 리니어 보이게

                //버튼은 따로 작업 안해도 괜찮은듯
                three_depth_block_left.setVisibility(View.GONE);
                three_depth_block_right.setVisibility(View.GONE);

                //3단계 도움말 blcok 처리 끝
                three_depth_block_left.setVisibility(View.VISIBLE);
                three_depth_block_right.setVisibility(View.VISIBLE);
                //마지막 한마디 보이게 하기

                oneword_textview_left.setVisibility(View.VISIBLE);//마지막 한마디는 보이게 하고~
                oneword_textview_right.setVisibility(View.VISIBLE);
            }
        }
        //왼쪽 사람 선택 시작
        button_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_left_button_json_for_send();
                if(get_select_human_depth.equals("1")){
                        get_confirm_of_left_dialog("이성이 선택할 차례입니다","상대방을 선택한 또다른 이성이 나타나면 \n상대방의 "+(Integer.parseInt(get_select_human_depth)+1)+"단계 매칭이 시작됩니다 \n그때까지 기다려 주세요");
                    }else if(get_select_human_depth.equals("2")){
                        get_confirm_of_left_dialog("이성이 선택할 차례입니다","상대방을 선택한 또다른 이성이 나타나면 \n상대방의 "+(Integer.parseInt(get_select_human_depth)+1)+"단계 매칭이 시작됩니다 \n그때까지 기다려 주세요");
                    }else if(get_select_human_depth.equals("3")){
                        get_confirm_of_left_dialog("마지막 매칭입니다","최종 선택에 대한 결과를 \n 상대방이 수락하면 1:1대화가 시작됩니다!\n"+"선택하시겠습니까?");
                }
            }
        });
        //왼쪽 사람 선택 시작

        //오른쪽 사람 선택 시작
        button_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_right_button_json_for_send();
                if(get_select_human_depth.equals("1")){
                    get_confirm_of_right_dialog("이성이 선택할 차례입니다","상대방을 선택한 또다른 이성이 나타나면 \n상대방의 "+(Integer.parseInt(get_select_human_depth)+1)+"단계 매칭이 시작됩니다 \n그때까지 기다려 주세요");
                }else if(get_select_human_depth.equals("2")){
                    get_confirm_of_right_dialog("이성이 선택할 차례입니다","상대방을 선택한 또다른 이성이 나타나면 \n상대방의 "+(Integer.parseInt(get_select_human_depth)+1)+"단계 매칭이 시작됩니다 \n그때까지 기다려 주세요");
                }else if(get_select_human_depth.equals("3")){
                    get_confirm_of_right_dialog("마지막 매칭입니다","최종 선택에 대한 결과를 상대방이 수락하면 1:1대화가 시작됩니다!\n"+"선택하시겠습니까?");
                }
            }
        });
        //오른쪽 사람 선택 끝
    }
    //nationwidematching_page_others_choice에 있는 listview를 삭제해주기 위해서 인텐트 넘겨주기 시작
    @Override
    protected void onStop() {
        super.onStop();
    }
    //nationwidematching_page_others_choice에 있는 listview를 삭제해주기 위해서 인텐트 넘겨주기 끝

    private void get_left_button_json_for_send(){
        JSONObject go_girl_db = new JSONObject();
        try {
            go_girl_db.put("json_standard_depth",get_select_human_depth);//현재의 깊이
            go_girl_db.put("json_standard_man",get_select_human_num);//선택한 사람의 번호
            go_girl_db.put("json_standard_sex",get_select_human_sex);//선택한 사람의 성
            go_girl_db.put("json_selected_woman",get_left_human_num);//선택된 사람의 번호
            go_girl_db.put("json_left_woman",get_left_human_num);//왼쪽 사람의 번호
            go_girl_db.put("json_right_woman",get_right_human_num);//오른쪽 사람의 번호
        } catch (JSONException e) {
            e.printStackTrace();
        }
            json_result_for_send_left = go_girl_db.toString();//json으로 묶기
        Log.v("왼쪽버튼 보낼json_in_one_view","왼쪽버튼 보낼json"+ json_result_for_send_right);
    }

    private void get_right_button_json_for_send(){
        JSONObject go_girl_db = new JSONObject();
        try {
            go_girl_db.put("json_standard_depth",get_select_human_depth);//현재의 깊이
            go_girl_db.put("json_standard_man",get_select_human_num);//선택한 사람의 번호
            go_girl_db.put("json_standard_sex",get_select_human_sex);//선택한 사람의 성
            go_girl_db.put("json_selected_woman",get_right_human_num);//선택된 사람의 번호
            go_girl_db.put("json_left_woman",get_left_human_num);//왼쪽 사람의 번호
            go_girl_db.put("json_right_woman",get_right_human_num);//오른쪽 사람의 번호
        } catch (JSONException e) {
            e.printStackTrace();
        }
        json_result_for_send_right = go_girl_db.toString();
        Log.v("오른버튼 보낼json_in_one_view","오른버튼 보낼json"+ json_result_for_send_right);
    }

    //데이터 삽입 시작
    private void inserttodata(final String json_result){
        class insertdata extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_for_send = (String)strings[0];
                    String data = "json_final"+"="+json_result_for_send;
                    Log.v("json결과2","json결과2"+data);
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_meeting/user_matching_view.php");//여기 수정
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));
                    //my_url.setDoInput(true);
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
                Log.v("json_recieve","json_recieve"+s);
                try{
                    JSONObject j_o = new JSONObject(s);
                    //왼쪽 시작
                    view_email_left = j_o.getString("email_left");
                    view_name_left = j_o.getString("name_left");
                    view_sex_left= j_o.getString("sex_left");
                    view_num_left = get_left_human_num;
                    view_age_left = j_o.getString("age_left");
                    view_tall_left = j_o.getString("tall_left");
                    view_job_left = j_o.getString("job_left");
                    view_local_left = j_o.getString("local_left");
                    view_smoking_left = j_o.getString("smoking_left");
                    view_mind_left = j_o.getString("mind_left");
                    view_hobby_left = j_o.getString("hobby_left");
                    view_blood_left = j_o.getString("blood_left");
                    view_final_left = j_o.getString("final_oneword_left");
                    //왼쪽 끝

                    //오른쪽 시작
                    view_email_right = j_o.getString("email_right");
                    view_name_right = j_o.getString("name_right");
                    view_sex_right= j_o.getString("sex_right");
                    view_num_right = get_right_human_num;
                    view_age_right = j_o.getString("age_right");
                    view_tall_right = j_o.getString("tall_right");
                    view_job_right = j_o.getString("job_right");
                    view_local_right = j_o.getString("local_right");
                    view_smoking_right = j_o.getString("smoking_right");
                    view_mind_right = j_o.getString("mind_right");
                    view_hobby_right = j_o.getString("hobby_right");
                    view_blood_right = j_o.getString("blood_right");
                    view_final_right = j_o.getString("final_oneword_right");
                    //오른쪽 끝


                    //TextView에 넣어주긔~
                    sex_textview_left.setText(view_sex_left);
                    number_textview_left.setText(view_num_left);
                    age_textview_left.setText(view_age_left);
                    local_textview_left.setText(view_local_left);
                    smoking_textview_left.setText(view_smoking_left);
                    mind_textview_left.setText(view_mind_left);
                    hobby_textview_left.setText(view_hobby_left);
                    //2단계
                    blood_textview_left.setText(view_blood_left);
                    job_textview_left.setText(view_job_left);
                    tall_textview_left.setText(view_tall_left);
                    //3단계
                    oneword_textview_left.setText(view_final_left);

                    //TextView에 넣어주긔~
                    sex_textview_right.setText(view_sex_right);
                    number_textview_right.setText(view_num_right);
                    age_textview_right.setText(view_age_right);
                    local_textview_right.setText(view_local_right);
                    smoking_textview_right.setText(view_smoking_right);
                    mind_textview_right.setText(view_mind_right);
                    hobby_textview_right.setText(view_hobby_right);
                    //2단계 뿌려줄 ...
                    blood_textview_right.setText(view_blood_right);
                    job_textview_right.setText(view_job_right);
                    tall_textview_right.setText(view_tall_right);
                    //3단계
                    oneword_textview_right.setText(view_final_right);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }
        insertdata task = new insertdata();
        task.execute(json_result);
    }
    //데이터 삽입 끝

    //왼쪽 버튼 선택 스레드 시작
    public void inserttodata_left_button(final String insert_json_result_left){
        class insertdata_left_button extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_for_send_left_button = (String)strings[0];
                    String data = "json_final"+"="+json_result_for_send_left_button;
                    Log.v("left_button_result","-"+data);//선택한사람,선택된사람,왼쪽사람,오른쪽사람
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_meeting/user_matching_women_selected.php");//여기 수정
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));
                    //my_url.setDoInput(true);
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
                // loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

        }
        insertdata_left_button task = new insertdata_left_button();
        task.execute(insert_json_result_left);
    }
    //왼쪽 버튼 선택 스레드 끝

    //오른쪽 버튼 선택 스레드 시작
    public void inserttodata_right_button(final String insert_json_result_right){
        class insertdata_right_button extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                //loading = ProgressDialog.show(catch_core_page_one_view.this,"기다려 주세요..",null,true,true);
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_for_send_left_button = (String)strings[0];
                    String data = "json_final"+"="+json_result_for_send_left_button;
                    Log.v("left doInback result","left doInback result"+data);//선택한사람,선택된사람,왼쪽사람,오른쪽사람
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_meeting/user_matching_women_selected.php");//여기 수정
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));
                    //my_url.setDoInput(true);
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
                // loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

        }
        insertdata_right_button task = new insertdata_right_button();
        task.execute(insert_json_result_right);
    }
    //오른쪽 버튼 선택 스레드 끝

    //서버로 가서 fcm 시작
    public void go_server_hurry(final String insert_json_result_right){
        class insertdata_right_button extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_for_send_left_button = (String)strings[0];
                    String data = "json_final"+"="+json_result_for_send_left_button;
                    Log.v("left doInback result","left doInback result"+data);//선택한사람,선택된사람,왼쪽사람,오른쪽사람
                    URL my_server = new URL("http://cmic.dothome.co.kr/fcm/hurry.php");//여기 수정
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));
                    //my_url.setDoInput(true);
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
                Log.v("마지막 chat post","-"+s);
                // loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                // 대화 시작 시에 무엇을 넘겨야 할까요? 시작

                //테그
                Intent return_page_one = new Intent(Nationwidematching_page_view.this,Nationwidematching_page.class);
                return_page_one.putExtra("delete_positoin",THIS_VIEW_POSITION);
                //Log.v("position값","one_view"+THIS_VIEW_POSITION);
                setResult(RESULT_OK,return_page_one);

                Intent go_chat = new Intent(Nationwidematching_page_view.this, Chat_page.class);
                go_chat.putExtra("chat_select_num", get_select_human_num);//최초에 나를 선택한사람
                if(get_select_human_sex.equals("남자")){
                    get_select_human_depth="여자";
                }else{
                    get_select_human_sex="남자";
                }
                go_chat.putExtra("chat_select_sex",get_select_human_sex);
                go_chat.putExtra("chat_selected_num", get_selected_human_num);//선택된사람
                //여기선 남자를 넘겨야함
                startActivity(go_chat);
                //대화 끝
                finish();
                //대화 시작 시에 무엇을 넘겨야 할까요? 끝
            }

        }
        insertdata_right_button task = new insertdata_right_button();
        task.execute(insert_json_result_right);
    }
    //오른쪽 버튼 선택 스레드 끝

}
