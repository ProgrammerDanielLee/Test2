package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.RequestQueue;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.*;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by lovef on 2016-11-14.
 */
public class Join_page extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,View.OnClickListener{
    //implements 받은 것 :

    //전화면으로부터 받은 변수들 시작
    private String facebook_email,facebook_name,facebook_sex;
    private String google_email,google_name,google_sex;
    private String twitter_name;
    private String naver_email,naver_name,naver_sex;
    //전화면으로부터 받은 변수들 끝

    //이메일 시작
    private EditText join_page_edittext_email;//이메일
    private TextView under_join_page_edittext_email_wrongframe;//이메일 형식 틀렸을때
    private TextView under_join_page_edittext_email_overlap;//이메일 중복됬을때
    private TextView under_join_page_edittext_email_success;//이메일 성공했을때

    private String final_email_check_overlap;//중복이메일 최종검사를 위한 String..이게 ok여야지 통과...
    private String wrong_email_form;
    private Integer email_check_return;//email 최종 검사

    private Button join_page_email_check_button;
    //이메일 끝

    //이름 시작
    private EditText join_page_edittext_name;//이름입력
    private TextView under_join_page_edittext_name;//이름 아래 텍스트 예외처리
    //이름 끝

    //비밀번호 + 비밀번호 확인 시작
    private EditText join_page_edittext_password;//비밀번호입력하는 곳
    private TextView under_join_page_edittext_password;//비밀번호 아래 예외처리
    private EditText join_page_edittext_passwordcf;//비밀번호 확인
    private String before_password; //비밀번호 초기화를 위한 변수 쓸지 안쓸지 모름
    private String first_password, second_password;
    //비밀번호 + 비밀번호 확인 끝

    //성별 시작
    private RadioGroup join_page_radiogroup_sex;
    private RadioButton join_page_radiobutton_man;//남자 라디오 버튼
    private RadioButton join_page_radiobutton_women;//여자 라디오 버튼
    //성별 끝

    //나이 시작
    private EditText join_page_edittext_age;//나이 에딧텍스트
    private TextView under_join_page_edittext_age;//나이 아래 텍스트 뷰
    //나이 끝

    //키 시작
    private EditText join_page_edittext_tall;//키 에딧텍스트
    private TextView under_join_page_edittext_tall;
    //키 끝

    //직업 시작
    private AutoCompleteTextView join_page_autoct_job;//직업 오토컴플리트 텍스트뷰
    private ArrayAdapter join_page_arrayadapter_job;
    static final String[] join_page_job_information = new String[]{"프로그래머","시스템 프로그래머","네트워크 프로그래머","웹 프로그래머","앱 프로그래머","풀스택 프로그래머","전업주부","비서","가정부","까페아르바이트생","옷매장아르바이트생","학생"};//직업을 담을 String배열
    //직업 끝

    //지역 시작
    private Spinner join_page_spinner_local;
    private String[] join_page_local_information = new String[]{"-", "서울","광주","대구","대전","세종","부산","울산","인천","제주","강원","경기","경남","경북","전남","전북","충남","충북"};
    private ArrayList<String> join_page_arraylist_local;
    private ArrayAdapter<String> join_page_arrayadapter_local;
    //지역 끝

    //혈액형 시작
    private Spinner join_page_spinner_blood;
    private String[] join_page_blood_information = new String[]{"-", "A형", "B형", "O형", "AB형", "RH-A형", "RH-B형", "RH-O형", "RH-AB형", "Weak A형", "Weak B형", "Weak -D형", "Cis-AB형", "MkMk형", "바디바(-D/-D-)혈액형", "밀텐버거형"};
    private ArrayList<String> join_page_arraylist_blood;
    private ArrayAdapter<String> join_page_arrayadapter_blood;
    //혈액형 끝

    //흡연여부 시작
    private RadioGroup join_page_radiogroup_smoke;
    private RadioButton join_page_radiobutton_smoke_yes;
    private RadioButton join_page_radiobutton_smoke_no;
    //흡연여부 끝

    //성격 시작
    private AutoCompleteTextView join_page_autoct_character;//성격오토컴플리트 텍스트뷰
    private ArrayAdapter join_page_arrayadapter_character;
    static final String[] join_page_character_information = new String[]{"평범한","비범한","성질있는","조용한"};//성격을 담을 String배열
    private TextView under_join_page_autoct_character;
    private TextView under_join_page_autoct_character_length;
    //성격 끝

    //취미 시작
    private AutoCompleteTextView join_page_autoct_hobby;
    private ArrayAdapter join_page_arrayadapter_hobby;
    static final String[] join_page_hobby_information = new String[]{"낚시", "스포츠", "야구", "축구","프로그래밍","영화감상","만화감상"};
    private TextView under_join_page_autoct_hobby;
    private TextView under_join_page_autoct_hobby_length;
    //취미 끝

    //한마디 시작
    private EditText join_page_edittext_myself;
    private TextView under_join_page_edittext_myself;
    //한마디 끝

    //개인정보 이용 내역 시작
    private Button private_information;
    private int private_information_ok;
    //개인정보 이용 내역 끝


    //완료 시작
    private Button join_page_finish_button;
    //완료 끝

    private Pattern pattern;//정규표현식 관련 변수
    private Matcher matcher;//정규표현식 관련 변수2

    //쉐어드 프리퍼런스를 저정하기위해서 뿌려놓은 값 시작
    private String email;
    private String name;
    private String sex;
    //쉐어드 프리퍼런스를 저장하기 위해서 뿌려놓은값 끝

    //이메일 정규표현식 시작
    public boolean email_validate(String hex2) {
        String mail = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern p = Pattern.compile(mail);
        Matcher m = p.matcher(hex2);
        return m.matches();
    }
    //이메일 정규표현식 끝


    //비밀번호 정규 표현식 시작
    private static final String password_pattern = "^(?=.*[0-9])(?=.*[a-z])(?=[\\S]+$).{6,12}$";

    public boolean password_validate(final String hex) {//정규표현식 넣고 돌리는 메소드
        pattern = Pattern.compile(password_pattern);//import java.util.regex.Pattern; 하였음
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    //비밀번호 정규 표현식 끝

    //회원가입 완료 결과 모아두는 곳ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    private String email_result;//이메일 - 완료
    private String name_result;//이름 - 완료
    private String password_result;//비번 - 완료
    private String sex_result;//성별 - 완료(어째서인지 중복체크 개짱남)
    private int age_result;//나이 - 완료
    private Integer k = new Integer(age_result);
    private int tall_result;//키 - 완료
    private String job_result;//직업 - 완료
    private String local_result;//지역 - 완료
    private String blood_result;//혈액형 - 완료
    private String smoking_result;//흡연여부 - 완료
    private String mind_result;//성격 - 완료
    private String hobby_result;//취미 - 완료
    private String myself_result;//자기소개 - 완료
    private String pic_result;//사진 - 진행중
    //회원가입 완료 결과 모아두는 곳 끝ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

    //이미지 시작
    public static final String UPLOAD_URL = "http://cmic.dothome.co.kr/img_upload/upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";
    private int PICK_IMAGE_REQUEST = 1;

    private Button join_page_button_choosing; //선택할 버튼
    private Button join_page_button_sending;
    private AppCompatImageView join_page_imageview_view; //뿌려줄 ImageView
    //서버로 보내주는 단은 완료버튼 눌렀을때 같이 할꺼라서..

    private Bitmap join_page_image_bitmap;
    private Uri join_page_file_path;
    private String encoded_string;
    private String image_name;
    //이미지 끝

    //나중에 추가시킨
    private ImageView basic_check_button,basic_cancel_button,special_check_button,special_cancel_button;
    //oncreate 시작
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        SharedPreferences pref = getSharedPreferences("pf", Activity.MODE_PRIVATE);
        String save_email = pref.getString("save_join_email", "");
        String save_name = pref.getString("save_join_name", "");
        String save_sex = pref.getString("save_join_sex", "");
        Log.v("넘길 pref 값", "넘길 pref 값" + save_email);

        if (save_email != null && save_name != null && save_sex != null) {
            //조건에 부합하면 main_page로 이동
            Intent go_main_activity = new Intent(Join_page.this, Main_page.class);
            go_main_activity.putExtra("catch_join_page_email", save_email);
            go_main_activity.putExtra("catch_join_page_name", save_name);
            go_main_activity.putExtra("catch_join_page_sex", save_sex);
            startActivity(go_main_activity);
            Log.v("넘길 email 값", "넘길 email 값" + save_email);
            //finish();
        }*/
        setContentView(R.layout.join_page);

        //전체 객체화 시작

        //이메일 객체화 시작
        join_page_edittext_email = (EditText) findViewById(R.id.join_page_edittext_email);//이메일 뽑기 위한 edittext
        under_join_page_edittext_email_wrongframe = (TextView) findViewById(R.id.under_join_page_edittext_email_wrongframe);//이메일 형식 틀렸을때
        under_join_page_edittext_email_overlap = (TextView)findViewById(R.id.under_join_page_edittext_email_overlap);//중복 된 값
        under_join_page_edittext_email_success = (TextView)findViewById(R.id.under_join_page_edittext_email_success);//성공했을때의 TextView
        under_join_page_edittext_email_wrongframe.setTextColor(Color.parseColor("#FFFBFB"));
        under_join_page_edittext_email_overlap.setTextColor(Color.parseColor("#FFFBFB"));
        under_join_page_edittext_email_success.setTextColor(Color.parseColor("#FFFBFB"));
        join_page_email_check_button = (Button)findViewById(R.id.join_page_email_check_button);
        //이메일 객체화 끝

        //이름 객체화 시작
        join_page_edittext_name = (EditText) findViewById(R.id.join_page_edittext_name);
        under_join_page_edittext_name = (TextView) findViewById(R.id.under_join_page_edittext_name);
        under_join_page_edittext_name.setTextColor(Color.parseColor("#FFFBFB"));
        //이름 객체화 끝

        //비밀번호 객체화 시작
        join_page_edittext_password = (EditText) findViewById(R.id.join_page_edittext_password);
        //비밀번호 객채화 끝

        //비밀번호 확인 객체화 시작
        join_page_edittext_passwordcf = (EditText) findViewById(R.id.join_page_edittext_passwordcf);
        //비밀번호 확인 객체화 끝

        //성별 객체화 시작
        join_page_radiogroup_sex = (RadioGroup) findViewById(R.id.join_page_radiogroup_sex);
        join_page_radiobutton_man = (RadioButton) findViewById(R.id.join_page_radiobutton_sex_man);
        join_page_radiobutton_women = (RadioButton) findViewById(R.id.join_page_radiobutton_sex_women);
        //성별 객체화 끝

        //나이 객체화 시작
        join_page_edittext_age = (EditText) findViewById(R.id.join_page_edittext_age);
        under_join_page_edittext_age = (TextView) findViewById(R.id.under_join_page_edittext_age);
        //나이 객체화 끝

        //키 객체화 시작
        join_page_edittext_tall = (EditText) findViewById(R.id.join_page_edittext_tall);
        under_join_page_edittext_tall = (TextView) findViewById(R.id.under_join_page_edittext_tall);
        //키 객체화 끝

        //직업 객체화 시작
        join_page_autoct_job = (AutoCompleteTextView) findViewById(R.id.join_page_autocompletetextview_job);
        //직업 객체화 끝

        //지역 객체화 시작
        join_page_spinner_local = (Spinner) findViewById(R.id.join_page_spinner_local);
        //지역 객체화 끝

        //혈액형 객체화 시작
        join_page_spinner_blood = (Spinner) findViewById(R.id.join_page_spinner_blood);
        //혈액형 객체화 끝

        //흡연여부 객체화 시작
        join_page_radiogroup_smoke = (RadioGroup) findViewById(R.id.join_page_radiogroup_smoke);
        join_page_radiobutton_smoke_yes = (RadioButton) findViewById(R.id.join_page_radiobutton_smoke);
        join_page_radiobutton_smoke_no = (RadioButton) findViewById(R.id.join_page_radiobutton_nosmoke);
        //흡연여부 객체화 끝

        //성격 객체화 시작
        join_page_autoct_character = (AutoCompleteTextView) findViewById(R.id.join_page_autocompletetextview_character);
        under_join_page_autoct_character = (TextView)findViewById(R.id.under_join_page_autocompletetextview_character);
        under_join_page_autoct_character_length = (TextView)findViewById(R.id.under_join_page_autocompletetextview_character_length);
        //성격 객체화 끝

        /*
        //facebook 객체화 시작
        facebook_email_textview = (TextView) findViewById(R.id.facebook_email);
        facebook_name_textview = (TextView) findViewById(R.id.facebook_name);
        facebook_sex_textview = (TextView) findViewById(R.id.facebook_sex);
        //facebook 객체화 끝

        //google 객체화 시작
        google_email_textview = (TextView) findViewById(R.id.google_email);
        google_name_textview = (TextView) findViewById(R.id.google_name);
        google_sex_textview = (TextView) findViewById(R.id.google_sex);
        //google 객체화 끝
        */

        //취미 객체화 시작
        join_page_autoct_hobby = (AutoCompleteTextView) findViewById(R.id.join_page_autocompletetextview_hobby);
        under_join_page_autoct_hobby = (TextView)findViewById(R.id.under_join_page_autocompletetextview_hobby);
        under_join_page_autoct_hobby_length = (TextView)findViewById(R.id.under_join_page_autocompletetextview_hobby_length);
        //취미 객체화 끝

        //한마디 객체화 시작
        join_page_edittext_myself = (EditText)findViewById(R.id.join_page_edittext_myself);

        //한마디 객체화 끝

        ///개인정보 버튼 시작
        private_information=(Button)findViewById(R.id.join_page_private_information_button);
        //개인정보 버튼 끝

        //이미지 객체화 시작
        join_page_button_choosing = (Button)findViewById(R.id.join_page_button_imageupload);
        join_page_button_sending = (Button)findViewById(R.id.join_page_button_imagesend);//test
        join_page_button_choosing.setOnClickListener(this);
        join_page_button_sending.setOnClickListener(this);//test
        //보이는거 자동화, 보내는거는 완료버튼 누르면 되게끔 커스텀
        join_page_imageview_view = (AppCompatImageView)findViewById(R.id.join_page_imageview_view);

        //이미지 객체화 끝

        //따로 추가된 체크버튼 객체화 시작
        //기본 password 시작
        basic_check_button = (ImageView)findViewById(R.id.android_password_check_icon_good);
        basic_cancel_button = (ImageView)findViewById(R.id.android_password_check_icon_bad);
        //기본 password 끝
        special_check_button = (ImageView)findViewById(R.id.android_password_checkcf_icon_good);
        special_cancel_button = (ImageView)findViewById(R.id.android_password_checkcf_icon_bad);
        //따로 추가된 체크버튼 객체화 끝

        //전체 객체화 끝

        //본격 코드 시작

        //facebook이랑 google 예외처리 시작ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
        /*
        //facebook 값들 받기
        facebook_email = getIntent().getStringExtra("facebook_email");
        facebook_name = getIntent().getStringExtra("facebook_name");
        facebook_gender = getIntent().getStringExtra("facebook_gender");
        //facebook 값들 받기 끝

        //google 값들 받기
        google_email = getIntent().getStringExtra("google_email");
        google_name = getIntent().getStringExtra("google_name");
        google_gender = getIntent().getStringExtra("google_sex");
        //google 값들 받기 끝

        if(facebook_email != null && facebook_name != null){//만약 facebook 이메일과 이름이 감지가 되면,
            if(facebook_gender.equals("male")){
                facebook_gender = "";
                facebook_gender = "남자";
            }else if(facebook_gender.equals("female")){
                facebook_gender = "";
                facebook_gender = "여자";
            }
            Log.v("TAG","email"+facebook_email);
            Log.v("TAG","name"+facebook_name);
            Log.v("TAG","gender"+facebook_gender);

            catch_join_page_email.setVisibility(View.GONE);//원래 이메일 지우기
            LinearLayout pw = (LinearLayout)findViewById(R.id.password_linear);
            LinearLayout pwcf = (LinearLayout)findViewById(R.id.passwordcf_linear);
            catch_join_page_name.setVisibility(View.GONE);
            pw.setVisibility(View.GONE);//비밀번호 지우기
            pwcf.setVisibility(View.GONE);//비밀번호 확인 지우기
            catch_join_page_sexs_radiogroup.setVisibility(View.GONE);//성별지우기


            facebook_email_textview.setVisibility(View.VISIBLE);//이메일 보이기
            facebook_name_textview.setVisibility(View.VISIBLE);//이름보이기
            facebook_sex_textview.setVisibility(View.VISIBLE);//성별 보이기

            facebook_email_textview.setText(facebook_email);//이메일 셋팅
            facebook_name_textview.setText(facebook_name);//이름 셋팅
            facebook_sex_textview.setText(facebook_gender);//성별 셋팅

        }else if(google_email != null && google_name != null){//만약 google 이메일과 이름이 감지가 되면,
            //String google_gender_str = String.valueOf(google_gender);
            //Log.v("test_google_gender","test_google_gender"+google_gender_str);
            if(google_gender.equals("0")){
                google_gender = "";
                google_gender = "남자";
            }else{
                google_gender = "";
                google_gender = "여자";
            }
            Log.v("TAG","email"+google_email);
            Log.v("TAG","name"+google_name);
            Log.v("TAG","gender"+google_gender);

            catch_join_page_email.setVisibility(View.GONE);//원래 이메일 지우기
            LinearLayout pw = (LinearLayout)findViewById(R.id.password_linear);
            LinearLayout pwcf = (LinearLayout)findViewById(R.id.passwordcf_linear);
            catch_join_page_name.setVisibility(View.GONE);
            pw.setVisibility(View.GONE);//비밀번호 지우기
            pwcf.setVisibility(View.GONE);//비밀번호 확인 지우기
            catch_join_page_sexs_radiogroup.setVisibility(View.GONE);//성별지우기

            google_email_textview.setVisibility(View.VISIBLE);//이메일 보이기
            google_name_textview.setVisibility(View.VISIBLE);//이름보이기
            google_sex_textview.setVisibility(View.VISIBLE);//성별 보이기

            google_email_textview.setText(google_email);//이메일 셋팅
            google_name_textview.setText(google_name);//이름 셋팅
            google_sex_textview.setText(google_gender);//성별 셋팅

        }else {//그냥 회원가입을 눌렀을때에
            */



        //facebook 값 받고 예외처리 시작
        facebook_email = getIntent().getStringExtra("facebook_email");
        facebook_name = getIntent().getStringExtra("facebook_name");
        facebook_sex = getIntent().getStringExtra("facebook_gender");
        if(facebook_email!=null && facebook_name !=null && facebook_sex != null){
        join_page_edittext_email.setText(facebook_email);
        join_page_email_check_button.setVisibility(View.GONE);
        join_page_edittext_name.setText(facebook_name);
        if(facebook_sex.equals("male")){join_page_radiobutton_man.setChecked(true);}else{join_page_radiobutton_women.setChecked(true);};
        }
        //facebook 값 받고 예외처리 끝

        //google 값 받고 예외처리 시작
        google_email = getIntent().getStringExtra("google_email");
        google_name = getIntent().getStringExtra("google_name");
        google_sex = getIntent().getStringExtra("google_gender");
        if(google_email!=null && google_name !=null && google_sex != null){
            join_page_edittext_email.setText( google_email);
            join_page_email_check_button.setVisibility(View.GONE);
            join_page_edittext_name.setText(google_name);
            if(google_sex.equals("남자")){join_page_radiobutton_man.setChecked(true);}else{join_page_radiobutton_women.setChecked(true);};
        }
        //google 값 받고 예외처리 끝

        //twitter 트위터 값 받고 예외처리 시작
        twitter_name = getIntent().getStringExtra("twitter_name");
        if(twitter_name != null){
            join_page_edittext_name.setText(twitter_name);
        }

        //twitter 트위터 값 받고 예외처리 끝

        //네이버 값 받고 예외처리 시작
        naver_email = getIntent().getStringExtra("naver_email");
        naver_name = getIntent().getStringExtra("naver_name");
        naver_sex = getIntent().getStringExtra("naver_gender");
        if(naver_email!=null && naver_name !=null && naver_sex != null){
            join_page_edittext_email.setText(naver_email);
            join_page_email_check_button.setVisibility(View.GONE);
            join_page_edittext_name.setText(naver_name);
            if(naver_sex.equals("남자")){join_page_radiobutton_man.setChecked(true);}else{join_page_radiobutton_women.setChecked(true);};
        }
        //네이버 값 받고 예외처리 끝




        //이메일 정규표현식 적용 및 이메일 최종값 전달 시작
        join_page_edittext_email.addTextChangedListener(new TextWatcher() {//이메일에 리스너..!
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {//다입력되고 난 후
                if (s.toString().length() > 60 || !email_validate(s.toString()) || s.toString().equals(null)) {//60자 이상이거나 유효성 검사에 걸리면
                    wrong_email_form = s.toString();
                    if(under_join_page_edittext_email_success!=null){under_join_page_edittext_email_success.setVisibility(View.GONE);}
                    under_join_page_edittext_email_wrongframe.setVisibility(View.VISIBLE);
                    email_check_return = 0;
                }

                if (s.toString().length() < 60) {//60자 이하이고 null이 아니며..
                    if (email_validate(s.toString())) {//이메일 형식에 맞을 경우
                        under_join_page_edittext_email_wrongframe.setVisibility(View.GONE);//wronframe을 숨기고
                       under_join_page_edittext_email_success.setVisibility(View.VISIBLE);//맞다는 이메일 textview를 setting
                        email_result = s.toString();//이메일 최종값
                        Log.v("이메일 최종값", "-" + email_result);//최종값 전달 완료
                        email_check_return = 1;
                    }
                }
            }
        });
        //이메일 정규 표현식 적용 및 이메일 최종값 전달 끝

        //이메일 중복검사 시작
        join_page_email_check_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //값이 비었으면 값이 비었다고 다이얼로그
                //값이 차 있으나 형식에 맞지 않을경우 맞지 않는다고 알림
                if(wrong_email_form == null){
                    Simple_push_dialog("알림","아무것도 입력하지 않았습니다");
                }else if(email_check_return == 0){
                    Simple_push_dialog("알림","형식에 맞지 않는 이메일입니다");
                }else if(email_check_return == 1){
                    //Simple_push_dialog("알림","사용 가능한 이메일 계정입니다");
                    Log.v("이메일 최종 전달값", "-" + email_result);
                    Check_everything(email_result);//서버로 보내는 메소드 발동
                }
            }//버튼 클릭된거 끝
        });
        //이메일 중복검사 끝

        //이름 길이 제한 예외처리 시작
        join_page_edittext_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 10) {
                    under_join_page_edittext_name.setVisibility(View.VISIBLE);
                    if(name_result!=null){
                        name_result=null;
                    }
                }
                if ((s.toString().length() < 11) && s.toString() != null) {//애초에 여기 들어오지도 못해..
                    under_join_page_edittext_name.setVisibility(View.GONE);
                    name_result = s.toString();
                    Log.v("name_validate", "name_validate" + name_result);//최종값 전달 완료
                }
            }
        });
        //이름 길이 제한 예외처리 끝


        //비밀번호 에딧텍스트에게 리스너 달아줘서 길이 검사 후 아래 빨간색 표시뜨게 검사
        join_page_edittext_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {before_password = s.toString();}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 12 || !password_validate(s.toString())) {//서버에 한글이 들어가면 안되니까
                    under_join_page_edittext_password = (TextView) findViewById(R.id.under_join_page_edittext_password);
                    under_join_page_edittext_password.setVisibility(View.VISIBLE);
                }

                if (s.toString().length() < 13) {//13자 이하면 12자 부터 이 표시가 남
                    if (password_validate(s.toString())) {
                        Log.v("pass_validate", "pass_validate true");
                        under_join_page_edittext_password.setVisibility(View.GONE);
                        first_password = s.toString();//첫번째 비밀번호값을 넣고
                    }
                }

            }
        });
        //비밀번호 에딧텍스트에게 리스너 달아줘서 길이 검사 후 아래 빨간색 표시뜨게 끝

        //비밀번호 확인버튼에게 리스너 달아줘서 색깔 바꾸기 시작
        join_page_edittext_passwordcf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = join_page_edittext_password.getText().toString();
                String password_cf = join_page_edittext_passwordcf.getText().toString();
                Log.v("ontextchanged", "charsequence" + s.toString());

                if (password.equals(password_cf)) {//기존의 비밀번호와 같을 시
                    //join_page_edittext_password.setBackgroundColor(getResources().getColor(R.color.color_green));//둘다 green~
                    //join_page_edittext_passwordcf.setBackgroundColor(getResources().getColor(R.color.color_green));
                    //테그
                    basic_check_button.setVisibility(View.VISIBLE);
                    special_check_button.setVisibility(View.VISIBLE);

                    if(basic_cancel_button != null || special_cancel_button !=null){
                        basic_cancel_button.setVisibility(View.GONE);
                        special_cancel_button.setVisibility(View.GONE);
                    }

                    second_password = s.toString();
                    password_result = second_password;
                    Log.v("최종 비밀번호 확인", "-" + password_result);
                } else {
                    //join_page_edittext_password.setBackgroundColor(Color.RED);
                    //join_page_edittext_passwordcf.setBackgroundColor(Color.RED);
                    basic_cancel_button.setVisibility(View.VISIBLE);
                    special_cancel_button.setVisibility(View.VISIBLE);

                    if( basic_check_button != null ||  special_check_button !=null){
                        basic_check_button.setVisibility(View.GONE);
                        special_check_button.setVisibility(View.GONE);
                    }
                }

            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        //비밀번호 확인버튼에게 리스너 달아줘서 색깔 바꾸기 끝

        //성별 시작(맨 아래 onchangedcheck listner 메소드 확인하기)
        join_page_radiobutton_man.setOnCheckedChangeListener(this);
        join_page_radiobutton_women.setOnCheckedChangeListener(this);
        //성별 끝

        //}
    //여기까지 facebook 이랑 google 로그인 예외처리 끝ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        //나이 시작
        join_page_edittext_age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 2) {
                    under_join_page_edittext_age = (TextView) findViewById(R.id.under_join_page_edittext_age);
                    under_join_page_edittext_age.setVisibility(View.VISIBLE);//100세 이상이신분들 죄송하다는 text..
                }
                if (s.toString().length() < 3) {
                    String age = s.toString();//String age에 넣기...
                    age_result = Integer.parseInt(age);//근데 다시 parseInt해서 age_result에 넣기
                    Log.v("나이", "나이결과 : " + age_result);
                    under_join_page_edittext_age.setVisibility(View.GONE);//text 감추기
                }
                if(s.toString().length() == 3){
                    age_result = 0;
                }
            }
        });
        //나이 끝

        //키 시작
        join_page_edittext_tall.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 3) {
                    String tall = s.toString();
                    tall_result = Integer.parseInt(tall);
                    under_join_page_edittext_tall.setVisibility(View.GONE);//*키를 제대로 입력했는지 확인해주세요에 있는 textview표시
                    Log.v("키 확인", "키 확인 결과" + tall_result);
                }
                if (s.toString().length() == 2) {
                    under_join_page_edittext_tall.setVisibility(View.VISIBLE);
                    if(tall_result!=0){
                        tall_result=0;
                    }
                    //2자리 이상이면 키를 잘보이게끔 해달라고 ....
                }
            }
        });
        //키 끝

        //직업 시작
        //catch_join_page_job_id.addTextChangedListener();
        join_page_arrayadapter_job = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, join_page_job_information);//직업 어뎁터에, 직업 string배열 연결
        join_page_autoct_job.setAdapter(join_page_arrayadapter_job);
        join_page_autoct_job.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.equals(null)){
                    if(job_result!=null){
                        job_result=null;
                    }
                }
                job_result = s.toString();
                Log.v("직업 축출 결과", "-" + job_result);
            }
        });
        //직업 끝

        //지역 시작
        join_page_arraylist_local = new ArrayList<>(Arrays.asList(join_page_local_information));
        join_page_arrayadapter_local= new ArrayAdapter<String>(this, R.layout.join_page_spinner, join_page_local_information) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.parseColor("#FC6265"));

                if (position == 0) {
                    tv.setTextColor(Color.WHITE);//0번째꺼는 gray로 한다
                } else {
                    tv.setTextColor(Color.WHITE);//나머지는 black
                }
                return view;
            }
        };
        join_page_arrayadapter_local.setDropDownViewResource(R.layout.join_page_spinner);
        join_page_spinner_local.setAdapter(join_page_arrayadapter_local);

        join_page_spinner_local.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((join_page_arrayadapter_local.getItemId(position)) == 0)) {
                    local_result = join_page_arrayadapter_local.getItem(position);
                    Log.v("지역 확인 축출", "- " + local_result);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v("onNothingSelected_local", "어떨때 작동되는거지");
            }
        });
        //지역 끝

        //혈액형 시작
        join_page_arraylist_blood = new ArrayList<>(Arrays.asList(join_page_blood_information));
        join_page_arrayadapter_blood = new ArrayAdapter<String>(this, R.layout.join_page_spinner, join_page_arraylist_blood) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.parseColor("#FC6265"));

                if (position == 0) {
                    tv.setTextColor(Color.WHITE);
                } else {
                    tv.setTextColor(Color.WHITE);
                }
                return view;
            }
        };

        join_page_arrayadapter_blood.setDropDownViewResource(R.layout.join_page_spinner);//이 레이아웃에 있는 리소스를 연결시킨다
        join_page_spinner_blood.setAdapter(join_page_arrayadapter_blood);//spinner에 setAdapter해야한다
        join_page_spinner_blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (!((join_page_arrayadapter_blood.getItemId(position)) == 0)) {//0번째 position이 아니면
                    blood_result = join_page_arrayadapter_blood.getItem(position);
                    Log.v("혈액형 확인", "혈액형 확인결과 - " + blood_result);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.v("onNothingSelected_local", "어떨때 작동되는거지");
            }
        });
        //혈액형 끝

        //흡연여부 시작
        join_page_radiobutton_smoke_yes.setOnCheckedChangeListener(this);
        join_page_radiobutton_smoke_no.setOnCheckedChangeListener(this);
        //흡연여부 끝

        //성격 시작
        join_page_arrayadapter_character = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, join_page_character_information);//직업 어뎁터에, 직업 string배열 연결
        join_page_autoct_character.setAdapter(join_page_arrayadapter_character);
        join_page_autoct_character.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                //10자 이하로 입력해달라고 하기
                /*
                if(s.toString().length() == 0){
                    //성격을 입력해주세요
                    under_join_page_autoct_character.setVisibility(View.VISIBLE);
                    under_join_page_autoct_character_length.setVisibility(View.GONE);
                }*/
                if(s.toString().length() > 15){
                    //15자 이하로 입력해주세요
                    under_join_page_autoct_character.setVisibility(View.GONE);
                    under_join_page_autoct_character_length.setVisibility(View.VISIBLE);
                    if(mind_result!=null){mind_result=null;}
                }else{
                mind_result = s.toString();
                Log.v("성격 결과", "성격 결과" + mind_result);
                }
            }
        });
        //성격 끝

        //취미 시작
        join_page_arrayadapter_hobby = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, join_page_hobby_information);
        join_page_autoct_hobby.setAdapter(join_page_arrayadapter_hobby);
        join_page_autoct_hobby.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() > 15) {
                    under_join_page_autoct_hobby.setVisibility(View.GONE);
                    under_join_page_autoct_hobby_length.setVisibility(View.VISIBLE);
                    if(hobby_result!=null){hobby_result=null;}
                }else{
                    under_join_page_autoct_hobby.setVisibility(View.GONE);
                    under_join_page_autoct_hobby_length.setVisibility(View.GONE);

                    hobby_result = s.toString();
                    Log.v("취미 결과", "취미 결과 - " + hobby_result);
                }
            }
        });
        //취미 끝

        //한마디 시작
        join_page_edittext_myself = (EditText)findViewById(R.id.join_page_edittext_myself);
        under_join_page_edittext_myself = (TextView)findViewById(R.id.under_join_page_edittext_myself);
        join_page_edittext_myself.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 30) {

                    //myself_result = Integer.parseInt(myself);
                    under_join_page_edittext_myself.setVisibility(View.GONE);//*키를 제대로 입력했는지 확인해주세요에 있는 textview표시
                    myself_result = s.toString();
                    Log.v("한마디 결과", " - " + myself_result);
                }

                if (s.toString().length() > 30) {
                    under_join_page_edittext_myself.setVisibility(View.VISIBLE);
                }
            }
        });
        //한마디 끝

        //개인정보 이용 내역 시작
        private_information = (Button)findViewById(R.id.join_page_private_information_button);
        private_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_private_information_page = new Intent(Join_page.this,Privateinformation_page.class);
                //startActivity(go_private_information_page);
                startActivityForResult(go_private_information_page,1);
                //requestcode을 두번째 인자값으로 전달
            }
        });
        //개인정보 이용 내역 끝


        //가입완료(테그) 버튼 이벤트 처리 시작
        join_page_finish_button = (Button) findViewById(R.id.join_page_finish_button);
        join_page_finish_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //빈칸검사 시작
                /*
                if((facebook_email != null && facebook_name != null) || (google_email != null && google_name != null)){
                }else {*/
                    //꼭 .trim()을 해야합니다!
                    //이메일 검사 시작
                    if ((join_page_edittext_email.getText().toString().trim().length() == 0)){
                        //이메일 이벤트 처리 시작
                        Toast.makeText(Join_page.this, "E-mail을 입력하세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_email.requestFocus();//focus를 돌려주는 작업
                        join_page_edittext_email.setText("");
                    }else if(email_check_return != 1){
                        Toast.makeText(Join_page.this, "E-mail 형식을 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_email.requestFocus();
                    }else if(final_email_check_overlap != "ok"){
                        Toast.makeText(Join_page.this, "E-mail 중복체크 버튼 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_email.requestFocus();
                    }else if((name_result == null)||(join_page_edittext_name.getText().toString().trim().length() == 0)){//이름이 비었으면
                        //이름 검사 시작
                        Toast.makeText(Join_page.this, "이름을 제대로 입력했는지 확인하세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_name.requestFocus();
                        //이름 검사 끝
                    }else if((first_password == null)||(join_page_edittext_password.getText().toString().trim().length() == 0)){
                        //비밀번호 이벤트 처리 시작
                        Toast.makeText(Join_page.this, "비밀번호를 제대로 입력했는지 확인하세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_password.requestFocus();//focus를 돌려주는 작업
                        return;
                    }else if((password_result == null)||(join_page_edittext_passwordcf.getText().toString().trim().length() == 0)) {
                        //비밀번호 이벤트 처리 시작
                        Toast.makeText(Join_page.this, "비밀번호 확인을 제대로 입력했는지 확인하세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_passwordcf.requestFocus();//focus를 돌려주는 작업
                        return;
                        //비밀번호 검사 끝
                    }else if(sex_result == null){
                        //성별 검사 시작
                        Toast.makeText(Join_page.this, "성별을 제대로 선택했는지 확인하세요", Toast.LENGTH_SHORT).show();
                        join_page_radiogroup_sex.requestFocus();//focus를 돌려주는 작업
                        //성별 검사 끝
                    }else if(age_result == 0){//여기서부터 다시 보면 되 만약 오류가 나타나면
                        //나이 검사 시작
                        Toast.makeText(Join_page.this, "나이를 제대로 선택했는지 확인하세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_age.requestFocus();//focus를 돌려주는 작업
                        //나이 검사 끝
                    }else if(tall_result == 0){
                        //키 검사 시작
                        Toast.makeText(Join_page.this, "키를 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_tall.requestFocus();//focus를 돌려주는 작업
                        //키 검사 끝
                    }else if(job_result == null || (join_page_autoct_job.getText().toString().trim().length() == 0)){
                        //직업 검사 시작
                        Toast.makeText(Join_page.this, "직업을 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_autoct_job.requestFocus();//focus를 돌려주는 작업
                        //직업 검사 끝
                    }else if(local_result == null){
                        //지역 검사 시작
                        Toast.makeText(Join_page.this, "지역을 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_spinner_local.requestFocus();//focus를 돌려주는 작업
                        //지역 검사 끝
                    }else if(blood_result == null){
                        //혈액형 검사 시작
                        Toast.makeText(Join_page.this, "혈액형을 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_spinner_blood.requestFocus();//focus를 돌려주는 작업
                        //혈액형 검사 끝
                    }else if(smoking_result == null){
                        //흡연여부 시작
                        Toast.makeText(Join_page.this, "흡연여부 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_radiogroup_smoke.requestFocus();
                        //흡연여부 끝
                    }else if(mind_result == null || (join_page_autoct_character.getText().toString().trim().length() == 0)){
                        //성격 검사 시작
                        Toast.makeText(Join_page.this, "성격을 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_autoct_character.requestFocus();//focus를 돌려주는 작업
                        //성격 검사 끝
                    }else if(hobby_result == null || (join_page_autoct_hobby.getText().toString().trim().length() == 0)){
                        //취미 검사 시작
                        Toast.makeText(Join_page.this, "취미를 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_autoct_hobby.requestFocus();//focus를 돌려주는 작업
                        //취미 검사 끝
                    }else if(myself_result == null || (join_page_edittext_myself.getText().toString().trim().length() == 0)){
                        //소개 시작
                        Toast.makeText(Join_page.this, "한마디 어필을 제대로 입력했는지 확인해주세요", Toast.LENGTH_SHORT).show();
                        join_page_edittext_myself.requestFocus();//focus를 돌려주는 작업
                        //소개 끝
                    }else if(image_name == null || join_page_image_bitmap == null){//여기에 이미지
                        Toast.makeText(Join_page.this, "이미지를 선택해주셨는지 확인해주세요", Toast.LENGTH_SHORT).show();
                    }else{
                        //이미지 먼저 서버로 보내자
                        if(join_page_image_bitmap != null){//한번 더 확인ㅇㅇ
                            new Encoding_image().execute();//이미지 보내는 method
                        }
                                        /*
                if(facebook_email != null && facebook_name != null){//페이스북일 경우
                    email=facebook_email;
                    name = facebook_name;
                    sex = facebook_gender;
                    password_result = "facebooklogin";
                }else if(google_email != null && google_name != null){//구글일 경우
                    email=google_email;
                    name =google_name;
                    sex = google_gender;
                    password_result = "googlelogin";
                }else{
                */
                        email = email_result;
                        name = name_result;
                        sex = sex_result;
                    //}
                        String password = password_result;
                        String age = String.valueOf(age_result);
                        String tall = String.valueOf(tall_result);
                        String job = job_result;
                        String local = local_result;
                        String smoking = smoking_result;
                        String mind = mind_result;
                        String blood = blood_result;
                        String hobby = hobby_result;
                        String myself = myself_result;//여기서부터 다시 보기
                        String pic = pic_result;

                        Log.v("완료테스트","-"+email+name+password+sex+age+tall+job+local+smoking+mind+blood+hobby+myself+image_name);
                        //생성자 발동
                        insertToDatabase(email,name,password,sex,age,tall,job,local,smoking,mind,blood,hobby,myself,image_name);
                }
            }
        });
        //가입완료 버튼 이벤트 처리 끝
    }//oncreate 끝

    //Dialog 알람 메소드 시작
    public void Simple_push_dialog(String title,String maintext){
        AlertDialog.Builder builder = new AlertDialog.Builder(Join_page.this);

        TextView tt = new TextView(Join_page.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Join_page.this);
        tm.setText(maintext);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        builder.setView(tm);

        builder.setCancelable(false);
        // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
        builder.setNegativeButton("확인",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                }});
        builder.show();
    }
    //Dialog 알람 메소드 끝

    //이메일 중복확인(체크)하는 메소드
    private void Check_everything(String result) {
        class Check_everything extends AsyncTask<String, Void, String>{

            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String result = strings[0];
                    Log.v("async로 들어온 result","-"+result);

                    String data = "data"+"="+result;
                    URL my_server = new URL("http://cmic.dothome.co.kr/check_overlap/overlap_email.php");
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();

                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));//읽는 통로를 여는 것
                    StringBuilder sb = new StringBuilder();//이건 String이랑 똑같은거
                    String line;
                    while((line=reader.readLine())!=null){
                        sb.append(line);//sb는 저장공간이다. 그곳에 line을 올린다. line속에는 reader에서 읽어들인 글이 있다
                        //이게 1혹은 0이 되겠지...ㅎㅎ
                    }
                    Log.v("이메일 검사 return","-"+sb.toString());
                    return sb.toString();//받아들인 Stringbuilder값을 String으로 다시 바꾼다. 그걸 postexecute의 인자값으로 보낸다.

                }catch(Exception e){
                    return new String("exeption :"+e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                //Toast.makeText(Join_page.this,s.toString(), Toast.LENGTH_SHORT).show();
                //이따가 이거 꺼내
                Integer test_result = Integer.parseInt(s);
                if(test_result == 0){//값이 비면
                    Log.v("이메일 빈 값 진입","!");
                    Simple_push_dialog("알림","사용가능한 이메일 값입니다");
                    under_join_page_edittext_email_wrongframe.setVisibility(View.GONE);
                    under_join_page_edittext_email_overlap.setVisibility(View.GONE);
                    under_join_page_edittext_email_success.setVisibility(View.VISIBLE);
                    final_email_check_overlap = "ok";
                }else{//해당 이메일이 있다는 것이다
                    Log.v("이메일 유효값 진입","!");
                    Simple_push_dialog("알림","중복된 이메일 값입니다, 해당 이메일은 사용하실 수 없습니다");
                    under_join_page_edittext_email_wrongframe.setVisibility(View.GONE);
                    under_join_page_edittext_email_success.setVisibility(View.GONE);
                    under_join_page_edittext_email_overlap.setVisibility(View.VISIBLE);//중복된 값이라는 것을 보여준다
                    if(final_email_check_overlap!=null){
                        final_email_check_overlap=null;
                    }
                }

                    /*
                    //다이얼로그 띄우기 - 이메일 형식에 맞게 써주세요 하고 finish
                    //다이얼로그 삽입 시작
                    AlertDialog.Builder builder = new AlertDialog.Builder(Join_page.this);

                    // 다이얼로그의 제목을 설정한다.
                    builder.setTitle("알 림");
                    // 다이얼로그에 출력될 메세지 내용을 적는다.
                    builder.setMessage("이미 존재하는 메일입니다");
                    // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
                    //builder.setIcon(R.drawable.core_icon_image_1);
                    //사용자가 취소 못하게 막는다
                    builder.setCancelable(false);

                    // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
                    builder.setNegativeButton("확인", null);
                    builder.show();
                    finish();
                    //다이얼로그 삽입 끝

                    //edittext 비워주기 시작
                    join_page_edittext_email.setText("");
                    //edittext 비워주기 끝

                    finish();
                */
            }
        }
        Check_everything task = new Check_everything();
        task.execute(result);
    }
    //이메일 중복체크하는 메소드 끝

    @Override
    public void onCheckedChanged(CompoundButton a, boolean b) {
        //라디오 버튼 관련된 건 다 여기서 감지! 감지하는 메소드
        //성별 버튼 메소드 시작
        if(a == join_page_radiobutton_man){
            sex_result = join_page_radiobutton_man.getText().toString();
            Log.v("남자확인","남자확인결과"+sex_result);
        }else if(a == join_page_radiobutton_women){
            sex_result = join_page_radiobutton_women.getText().toString();
            Log.v("여자확인","여자확인결과"+sex_result);
            //성별 버튼 메소드 끝
        }


        //흡연 여부 메소드 시작
        if(a == join_page_radiobutton_smoke_yes){
            smoking_result = join_page_radiobutton_smoke_yes.getText().toString();
        } else if(a == join_page_radiobutton_smoke_no){
            smoking_result = join_page_radiobutton_smoke_no.getText().toString();
        }
        //흡연 여부 메소드 끝
    }

    @Override
    protected void onResume() {//키 자동으로 안뜨게 하는거)
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SharedPreferences pref=getSharedPreferences("pf", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("save_join_email",email);
        editor.putString("save_join_name",name);
        editor.putString("save_join_sex",sex);
        Log.v("test_resume","test_resume"+email);

        editor.commit();
    }

    @Override
    protected void onStop() {//기기가 꺼질때 shared 프리퍼런스의 에디터를 꺼내서, 값들을 넣어준다
        super.onStop();
        SharedPreferences pref=getSharedPreferences("pf", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("save_join_email",email);
        editor.putString("save_join_name",name);
        editor.putString("save_join_sex",sex);
        Log.v("test_onstop","test_ontop"+email);

        editor.commit();
    }

    private void insert_join(View view) {//xml에 있는 insert()의 onclick 발동
        //문자 얻어와서, String에 넣고

    }

    private void insertToDatabase(String email,String name,String password,String sex,String age,String tall,String job,String local,String smoking,String mind,String blood,String hobby,String myself,String pic){
        class InsertData extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;

            @Override
            protected void onPreExecute() {}

            @Override
            protected void onPostExecute(String s) {
                Log.v("회원가입","php return"+s);
                Toast.makeText(getApplicationContext(),"가입이 완료되었습니다",Toast.LENGTH_SHORT).show();

                //정보들이 다 들어가면, 화면 전환 시작
                Intent go_main_page = new Intent(Join_page.this,MainActivity.class);
                // go_main_page.putExtra("catch_join_page_email",email);
                //go_main_page.putExtra("catch_join_page_sex",sex);
                //go_main_page.putExtra("catch_join_page_name",name);
                //Log.v("가입페이지 테스트"," - "+email+sex+name);
                //go_main_page.putExtra("catch_join_page_")
                startActivity(go_main_page);
                finish();
                //정보들이 다 들어가면, 화면 전환 끝
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String email = strings[0];
                    String name = strings[1];
                    String password = strings[2];
                    String sex = strings[3];
                    String age = strings[4];
                    String tall = strings[5];
                    String job = strings[6];
                    String local = strings[7];
                    String smoking = strings[8];
                    String mind = strings[9];
                    String blood = strings[10];
                    String hobby = strings[11];
                    String myself = strings[12];
                    String pic = strings[13];

                    String data = "email" + "=" + email + "&" + "name" + "=" + name + "&" + "password" + "=" + password + "&" + "sex" + "=" + sex + "&" + "age" + "=" + age + "&" + "tall" + "=" + tall + "&" + "job" + "=" + job + "&" + "local" + "=" + local + "&" + "smoking" + "=" + smoking + "&" + "mind" + "=" + mind + "&" + "blood" + "=" +blood+ "&" + "hobby" + "=" + hobby +"&" + "myself" + "=" + myself +"&" + "pic" + "=" + pic;
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_signup/user_join.php");
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));

                    return "가입이 완료되었습니다";
                }catch(Exception e){
                    return new String("exeption :"+e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(email, name, password, sex, age, tall, job, local, smoking, mind,blood,hobby,myself,pic);
    }
/*
    private void image_upload(){
        // Create MultipartEntityBuilder
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // Set String Params
        builder.addTextBody("body_test", "body_test2", ContentType.create("Multipart/related", "UTF-8"));

        // Set File Params
        builder.addPart("part_test", new FileBody(new File("File 경로")));
    }
*/



    //이미지 경로로부터 파일 이름을 얻는 메소드
    //나중에 공부할 일을 대비하여 얼려둠
    /*
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.v("이미지 encoding","-"+encodedImage);
        return encodedImage;
    }*/
    //이미지 경로로부터 파일 이름을 얻는 메소드

    //이미지 업로드(upload)를 위한 asynctesk 시작
    //나중에 공부할 때를 대비하여 얼려둠
    /*
    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {
            Join_page_requestHandler rh = new Join_page_requestHandler();//요청하는 핸들러 객체화(네트워크에 접촉하는....)

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                Log.v("uploadImage찍어보기","-"+uploadImage);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL, data);//q
                Log.v("url이랑 data찍어보기", "-" + UPLOAD_URL + "," + data);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(join_page_image_bitmap);
    }
    */
    //이미지 업로드를 위한 asynctestk 끝



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){//최초에 보낸 값, 숫자 1,2,3같은거를 switch로 받아준다
            case 1://1일때
                if(resultCode == Activity.RESULT_OK){//RESULT OK는 제대로 받았다 안받았다의 표시....
                    Log.v("개인정보 이용 내역이 넘어왔나","?");
                    private_information_ok = 0;
                    Log.v("private_information 확인","-"+private_information_ok);
                }
                break;
        }

        //이미지 선택후 받기 시작
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //이미지를 고른것에 대한 조건이 충족되면

            join_page_file_path = data.getData();//파일경로
            //File[] image_name = new File(String.valueOf(join_page_file_path)).listFiles();
            Uri join_page_file_uri = data.getData();
           // getPath(join_page_file_uri);
            getName(join_page_file_uri);//휴 드디어 이미지 네임 구함 ㅠㅠ

            Log.v("image_name","-"+image_name);
            Log.v("file_path","-"+join_page_file_path);


            try {
                join_page_image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), join_page_file_path);
                Log.v("bitmap","-"+join_page_image_bitmap);
                //일단 bitmap으로 이미지를 담아주는 것 까진 했지..
                join_page_imageview_view.setImageBitmap(join_page_image_bitmap);//이미지 setting 끝

                new Encoding_image().execute();
                //getStringImage(join_page_image_bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //이미지 선택후 받기 끝
    }


    @Override
    public void onClick(View v) {
        if (v == join_page_button_choosing) {
            showFileChooser();//choosing 버튼을 누르면 choosing method가 시작된다. 선택되는 파일을 꺼내온다
        }

        if(v == join_page_button_sending){
            //uploadImage();
            //이미지 보내는 메소드 시작
            if(join_page_image_bitmap != null){
                new Encoding_image().execute();//이미지 보내는 method
            }
            //이미지 보내는 메소드 끝
        }
    }

    //uri이미지에서 실제경로를 축출하는 방법 시작
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }//uri이미지에서 실제경로를 축출하는 방법끝


    //uri이미지에서 이름을축출하는 방법 시작
    private String getName(Uri uri)
    {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return image_name = cursor.getString(column_index);
    }
    //uri이미지에서 이름을축출하는 방법 끝



    //선택된 파일을 imageview에 뿌려주기 위한 메소드 시작
    private void showFileChooser() {//파일을 imageview에 뿌려주기 위한 메소드
        Intent intent = new Intent();
        intent.setType("image/*");//image끌고오기
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);//PICK_IMAGE_REQUEST는 1이다
    }


    //선택된 파일을 imageview에 뿌려주기 위한 메소드 끝

    //이미지를 Jpg 100의 화질로 바꾸는 메소드 시작
    private class Encoding_image extends AsyncTask<Void,Void,Void>{
            @Override
            protected void onPreExecute() {}

            @Override
            protected Void doInBackground(Void... params) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream(); // image를 bytearray로 바꾸어 주기 위해서 선언
                join_page_image_bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);//비트맵을 옆에와 같은 옵션으로 compress해준다. 이것도 찾아봐야됨..모르는거 많네

                byte[] array = stream.toByteArray();
                encoded_string = Base64.encodeToString(array,0);//base64 format에 대해서도 알아봐야 할듯 웹서버에 저장하기에 적합하게 만드는 매소드라고 한다
                Log.v("encoded_string","-"+encoded_string);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                makeRequest();//이미지를 다 변환 시켰으면(encoded string에...) 요청 메소드 시작
            }
        }
    //이미지를 Jpg 100의 화질로 바꾸는 메소드 끝

    //이미지 서버로 보내는 메소드 시작
        private void makeRequest() {
            com.android.volley.RequestQueue requestqueue = Volley.newRequestQueue(this);
            //volley library의 requestqueue를 이용

            StringRequest request = new StringRequest(Request.Method.POST, "http://cmic.dothome.co.kr/img_upload/upload_img.php",

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {}
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("endcoded_string",encoded_string);
                    map.put("image_name",image_name);
                    return map;
                }
            };
            //part2/2 7:16초
            requestqueue.add(request);
        }
    //이미지 서버로 보내는 메소드 끝
}

