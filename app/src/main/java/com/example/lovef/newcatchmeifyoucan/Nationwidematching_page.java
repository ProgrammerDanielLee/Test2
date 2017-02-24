package com.example.lovef.newcatchmeifyoucan;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-11-16.
 */
public class Nationwidematching_page extends AppCompatActivity {
    //테그 : 쉐어드 프리퍼런스 추가할 것 - OK (24시간 내에 action이 없으면 파괴할 것) - DONT!
    //테그 : 8명의 끌고오는 이성들을 끌고 오는데 , 중복되지 않고 , 24시간 이내로 끌고오는 조건을 추가할 것(쉬는동안에)...

    //Mainpage에서 넘어온 값들 시작
    private String get_num_from_join;//="163";
    private String get_local_from_join;//="인천";
    private String get_sex_from_join;//="남자";
    private String get_json;
    String login_sex,login_num,login_age,login_email,login_name,login_tall,login_job,login_local,login_mind,login_smoking,login_blood,login_hobby,login_final_oneword,login_img;
    //Mainpage에서 넘어온 값들 끝

    //이성소개를 위한 객체 시작
    private TextView nw_page_textview_meeting;
    private TextView nw_page_textview_lookup_my_choice;
    private TextView nw_page_textview_lookup_others_choice;
    private ListView nw_page_listview_meet;
    private MyAdapter nw_page_myadapter;
    private ArrayList<MyData> nw_page_myarrdata;
    //이성소개를 위한 객체 끝

    //----이성소개를 위한 변수 값 시작

    //이미지를 위한 변수 시작
    private Context mContext = this;
    private String server_url = "http://cmic.dothome.co.kr/user_signup/uploads/";
    //이미지를 위한 변수 끝

    //여자를 소개 받았을 경우 시작
    private String women_num1,women_num2,women_num3,women_num4,women_num5,women_num6,women_num7,women_num8,women_img1,women_img2,women_img3,women_img4,women_img5,women_img6,women_img7,women_img8;
    //여자를 소개 받았을 경우 끝
    //남자를 소개 받았을 경우 시작
    private String man_num1,man_num2,man_num3,man_num4,man_num5,man_num6,man_num7,man_num8,man_img1,man_img2,man_img3,man_img4,man_img5,man_img6,man_img7,man_img8;
    //여자를 소개 받았을 경우 끝

    //소개받았을때 둘다에 대해서 시작
    private String everybody[];
    //소개받았을때 둘다에 대해서 끝

    //json을 위한 변수 시작
    private URL nw_page_url_for_matching;
    private String mans_matching_url="http://cmic.dothome.co.kr/user_meeting/user_matching_women.php";
    private String womens_matcing_url="http://cmic.dothome.co.kr/user_meeting/user_matching_man.php";
    private String test_url="http://cmic.dothome.co.kr/user_meeting/get_meeting_people.php";
    private String json_result_for_matching;
    private JSONObject nw_page_givemeeting_post;
    //json을 위한 변수 끝

    //----이성소개를 위한 변수 값 끝

    //현재 page의 상태를 알려주기 위한 변수 시작(선택/깊이/왼쪽이미지/왼쪽번호/오른쪽이미지/오른쪽번호/선택하는사람의성별/선택하는사람의번호/위치값
    private String nw_page_view,nw_page_depth,nw_page_left_img,nw_page_left_num,nw_page_right_img,nw_page_right_num,nw_page_select_sex,nw_page_select_num,nw_page_position;
    //현재 page의 상태를 알려주기 위한 변수 끝

    //쉐어드 프리퍼런스 키값 시작
    private String sfName="save_listview";
    static final int position_for_delete_listview=2;
    //쉐어드 프리퍼런스 키값 끝

    //데이터 관리 시작
    class MyData{
        //private을 써서, get함수로만 접근 가능하게 끔 만들어서 data의 보안을 유지한다.
        private String data_sex_left;//성별
        private String data_number_left;//번호 선언
        private String data_proimg_left;//왼쪽 이미지
        //이미지 추가5
        private String data_sex_right;
        private String data_number_right;
        private String data_proimg_right;//오른쪽 이미지

        public MyData(String img_name_left,String data_sex_left, String data_number_left,String img_name_right,String data_sex_right,String data_number_right){
            this.data_proimg_left = img_name_left;//왼쪽 이미지 setting
            this.data_sex_left = data_sex_left;
            this.data_number_left = data_number_left;
            //이미지 추가6
            this.data_proimg_right = img_name_right;//오른쪽 이미지 setting
            this.data_sex_right = data_sex_right;
            this.data_number_right = data_number_right;
        }
        //왼쪽 프로필 이름 가져오기
        public String getproImage_left(){return data_proimg_left;}//이미지추가7

        //왼쪽 성별 가져오기
        public String getSex_left(){
            return data_sex_left;
        }

        //왼쪽 번호 가져오기
        public String getNumber_left(){
            return data_number_left;
        }

        //오른쪽 프로필 이름 가져오기
        public String getproImage_right(){return data_proimg_right;}//이미지 추가 7.5

        //오른쪽 성별 가져오기
        public String getSex_right(){
            return data_sex_right;
        }

        //오른쪽 번호 가져오기
        public String getNumber_right(){
            return data_number_right;
        }


        //왼쪽 성별 멤버변수 셋팅(갱신)
        public String setSex_left(String sex_left){
            return this.data_sex_left = sex_left;
        }

        //왼쪽 번호 멤버변수 셋팅(갱신)
        public String setNumber_left(String number_left){
            return this.data_number_left = number_left;
        }

        //왼쪽 성별 멤버변수 셋팅(갱신)
        public String setSex_right(String sex_right){
            return this.data_sex_left = sex_right;
        }

        //왼쪽 번호 멤버변수 셋팅(갱신)
        public String setNumber_right(String number_right){return this.data_number_left = number_right;}

    }
    //데이터 끝

    //어뎁터 구현 시작
    //2.어뎁터 시작 - 나의 어뎁터 구현
    class MyAdapter extends BaseAdapter implements Serializable {
        Context context; //현재 화면의 시작
        ArrayList<MyData> MyarrData; //Arraylist
        LayoutInflater inflater; // 인플레이터
        ListView List;//리스트 뷰 리스트

        //인플레이터 시작
        public MyAdapter(Context c, ArrayList<MyData> arr) {
            this.context = c;
            this.MyarrData = arr;
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        //인플레이터 끝

        @Override
        public int getCount() {
            return MyarrData.size();
        }

        @Override
        public Object getItem(int position) {
            return MyarrData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //배열을 직접 손보지 않고(어자피 notifyDatasetChanged있으니까 adapter에 추가,수정,삭제 시키면 된다
        public void Add_adapter(int index,String img_i,String i1, String i2, String img_r,String i3,String i4){
            MyarrData.add(index,new MyData(img_i,i1,i2,img_r,i3,i4));//이미지 추가8
            notifyDataSetChanged();
        }

        public void Edit_adapter(int index,String img_i,String i1, String i2,String img_r,String i3,String i4){
            MyarrData.set(index,new MyData(img_i,i1,i2,img_r,i3,i4));
            notifyDataSetChanged();
        }

        public void Delete_adapter(int index){
            MyarrData.remove(index);
            notifyDataSetChanged();
        }

        public void ALL_Delete_adapter(){
            MyarrData.clear();
            notifyDataSetChanged();
        }



        @Override
        public View getView(final int position , View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = inflater.inflate(R.layout.nationwidematching_page_row,parent,false);
            }

            //left profile img
            ImageView left_img = (ImageView)convertView.findViewById(R.id.row_img_left);//이미지 추가9
            if(login_sex.equals("여자")){
                Glide.with(Nationwidematching_page.this).load(R.drawable.d_man_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(left_img);
            }else{
                Glide.with(Nationwidematching_page.this).load(R.drawable.d_girl_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(left_img);
            }
            //Glide.with(Nationwidematching_page.this).load(server_url+MyarrData.get(position).getproImage_left())
            //left profile img end

            //right profile img
            ImageView right_img = (ImageView)convertView.findViewById(R.id.pro_img_right);//이미지 추가10
            if(login_sex.equals("여자")){
                Glide.with(Nationwidematching_page.this).load(R.drawable.d_man_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(right_img);
            }else{
                Glide.with(Nationwidematching_page.this).load(R.drawable.d_girl_icon)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(right_img);
            }
            //Glide.with(Nationwidematching_page.this).load(server_url+MyarrData.get(position).getproImage_right())
            //right profile img end
            //left sex
            TextView left_sex = (TextView)convertView.findViewById(R.id.row_sex_left);
            left_sex.setText(MyarrData.get(position).getSex_left());

            //left sex end

            //left number
            TextView left_number = (TextView)convertView.findViewById(R.id.row_number_left);
            left_number.setText(MyarrData.get(position).getNumber_left());

            //left numebr end

            //right sex
            TextView right_sex = (TextView)convertView.findViewById(R.id.row_sex_right);
            right_sex.setText(MyarrData.get(position).getSex_right());
            //right sex end

            //right number
            TextView right_number = (TextView)convertView.findViewById(R.id.row_number_right);
            right_number.setText(MyarrData.get(position).getNumber_right());
            //right number end


            //글자색 바꾸기 시작
            TextView left_ho = (TextView)convertView.findViewById(R.id.row_ho_left);
            TextView right_ho = (TextView)convertView.findViewById(R.id.row_ho_right);

            if(MyarrData.get(position).getSex_left().equals("여자")){
                left_sex.setTextColor(Color.parseColor("#FC6265"));
                left_number.setTextColor(Color.parseColor("#FC6265"));
                left_ho.setTextColor(Color.parseColor("#FC6265"));

                right_sex.setTextColor(Color.parseColor("#FC6265"));
                right_number.setTextColor(Color.parseColor("#FC6265"));
                right_ho.setTextColor(Color.parseColor("#FC6265"));
            }else{
                left_sex.setTextColor(Color.parseColor("#4268BD"));
                left_number.setTextColor(Color.parseColor("#4268BD"));
                left_ho.setTextColor(Color.parseColor("#4268BD"));

                right_sex.setTextColor(Color.parseColor("#4268BD"));
                right_number.setTextColor(Color.parseColor("#4268BD"));
                right_ho.setTextColor(Color.parseColor("#4268BD"));
            }
            //글자색 바꾸기 끝
            //left local
            // TextView left_local = (TextView)convertView.findViewById(R.id.row_local_left);
            //left_local.setText(MyarrData.get(position).getLocal());
            //주소 setting 끝

            return convertView;
        }
    }
    //어뎁터 구현 끝

    //객체화 구현 시작
    private void Objectification(){
        nw_page_textview_meeting = (TextView)findViewById(R.id.nw_page_textview_meeting);
        //nw_page_textview_lookup_my_choice = (TextView)findViewById(R.id.nw_page_textview_lookup_my_choice);
        nw_page_textview_lookup_others_choice = (TextView)findViewById(R.id.nw_page_textview_lookup_others_choice);
        nw_page_listview_meet = (ListView)findViewById(R.id.nw_page_listview_meeting);

        //data 꼽는거 시작
        nw_page_myarrdata = new ArrayList<MyData>();//Arraydata에 mydata를 집어넣고
        nw_page_myadapter = new MyAdapter(this,nw_page_myarrdata);//adapter에 data를 넣는다
        nw_page_listview_meet.setAdapter(nw_page_myadapter);
        //data 꼽는거 끝
    }
    //객체화 구현 끝

    //json해제 시작
    //넘어온 정보들 json 해제 후에 변수에 대입 시작
    private void Insert_json_to_variable(){
        if(get_json != null) {
            try {
                JSONObject j_o = new JSONObject(get_json);
                login_num = j_o.getString("user_num");
                login_email = j_o.getString("email");
                login_name = j_o.getString("name");
                login_sex = j_o.getString("sex");
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
    }
    //넘어온 정보들 json 해제 후에 변수에 대입 끝
    //json 해제 끝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nationwidematching_page);
        //step0.버튼들, 리스트뷰들을 객체화 시킨다(listview 연결까지)
        Objectification();
        Animation main_1 = AnimationUtils.loadAnimation(Nationwidematching_page.this,R.anim.fade_mini_1);
        Animation main_2 = AnimationUtils.loadAnimation(Nationwidematching_page.this,R.anim.fade_mini_2);

        nw_page_textview_meeting.startAnimation(main_1);
        nw_page_textview_lookup_others_choice.startAnimation(main_2);

        //step1.join_page에서 넘어온 번호+지역+성별을 받는다
        //get_num_from_join = getIntent().getStringExtra("core_num");//번호
        //get_local_from_join = getIntent().getStringExtra("core_local");//지역
        //get_sex_from_join = getIntent().getStringExtra("core_sex");//성별
        get_json = getIntent().getStringExtra("login_json");
        Log.v("nationwide..","json"+get_json);

        //step1.5 json을 해독한다
        Insert_json_to_variable();

        //step2.버튼 기능을 달아준다
        nw_page_textview_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getjsonformatching();
                //json이 비어있는 것이 아니면 메소드 시작
                if(json_result_for_matching != null){
                    //step3.json(번호+지역+성별)을 인자값으로 한 meeting 메소드를 호출한다
                        nw_page_give_meeting_people(json_result_for_matching);
                    Log.v("json완성","-"+json_result_for_matching);
                }
            }
        });

        nw_page_listview_meet.startAnimation(main_1);
        //리스트 뷰 시작
        nw_page_listview_meet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nw_page_view = "선택";
                nw_page_depth = "1";
                nw_page_left_img = nw_page_myarrdata.get(position).getproImage_left().toString();
                nw_page_left_num = nw_page_myarrdata.get(position).getNumber_left().toString();
                nw_page_right_img = nw_page_myarrdata.get(position).getproImage_right().toString();
                nw_page_right_num = nw_page_myarrdata.get(position).getNumber_right().toString();
                overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
                //인텐트 시작
                Intent go_view_page = new Intent(Nationwidematching_page.this,Nationwidematching_page_view.class);
                go_view_page.putExtra("page_view",nw_page_view);
                go_view_page.putExtra("page_one_select_depth",nw_page_depth);
                go_view_page.putExtra("page_one_left_human_img",nw_page_left_img);//왼쪽 사람 이미지
                go_view_page.putExtra("page_one_left_number", nw_page_left_num);//왼쪽 사람 번호
                go_view_page.putExtra("page_one_right_human_img", nw_page_right_img);//오른쪽 사람 이미지
                go_view_page.putExtra("page_one_right_number",nw_page_right_num);//오른쪽 사람 번호
                go_view_page.putExtra("page_one_select_sex",login_sex);//선택하는 사람의 성
                go_view_page.putExtra("page_one_select_human",login_num);//선택하는 사람의 번호
                go_view_page.putExtra("page_position",position);//선택한 값의 position
                startActivityForResult(go_view_page,position_for_delete_listview);
                //인텐트 끝
            }
        });
        //리스트뷰 끝

        //Look up my choice 시작
        /*
        nw_page_textview_lookup_my_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_mychoice_page = new Intent(Nationwidematching_page.this,Nationwidematching_page_my_choice.class);
                go_mychoice_page.putExtra("page_view",nw_page_view);
                go_mychoice_page.putExtra("page_one_select_depth",nw_page_depth);
                go_mychoice_page.putExtra("page_one_select_sex",login_sex);//선택하는 사람의 성
                go_mychoice_page.putExtra("page_one_select_human",login_num);//선택하는 사람의 번호
                startActivity(go_mychoice_page);//선택에는 1단계 2단계 3단계 다 있겠지, 그거 별로 저장이 되어있어야 겠지.
            }
        });
        */
        //Look up my choice 끝

        //Look up other's choice 시작
        nw_page_textview_lookup_others_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_others_choice_page = new Intent(Nationwidematching_page.this,Nationwidematching_page_others_choice.class);
                startActivity(go_others_choice_page);
                overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
            }
        });
        //look up other's choice 끝

        //기존의 pause나 stop상태에서 저장되어있던 값을 불러와서 화면 시작 시에 onload에서 뿌려주기 시작
        onLoad();
        //기존의 pause나 stop상태에서 저장되어있던 값을 불러와서 화면 시작 시에 onload에서 뿌려주기 끝
    }

    //소개를 위해서 json을 얻을 메소드 시작
    private void getjsonformatching(){
        JSONObject j_o = new JSONObject();
        try {
                j_o.put("json_user_num_for_matching",login_num);//현재나의 번호
                j_o.put("json_user_local_for_matching",login_local);//지역
                j_o.put("json_user_sex_for_matching",login_sex);//성별
                json_result_for_matching = j_o.toString();//위 세개를 json으로 묶어준다음
            Log.v("checkjsonformatching","-"+ json_result_for_matching);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //소개를 위해서 json을 얻을 메소드 끝

    //서버로 부터 받아온 8명의 이성(여성과 남성)의 json을 얻는 메소드 시작
    private void decode_json_meeting_people(){
        if(get_sex_from_join.equals("남자")){
            try {
                //번호 시작
                women_num1 = nw_page_givemeeting_post.getString("women_num1");
                women_num2 = nw_page_givemeeting_post.getString("women_num2");
                women_num3 = nw_page_givemeeting_post.getString("women_num3");
                women_num4 = nw_page_givemeeting_post.getString("women_num4");
                women_num5 = nw_page_givemeeting_post.getString("women_num5");
                women_num6 = nw_page_givemeeting_post.getString("women_num6");
                women_num7 = nw_page_givemeeting_post.getString("women_num7");
                women_num8 = nw_page_givemeeting_post.getString("women_num8");
                //번호 끝

                //이미지 이름 시작
                women_img1 = nw_page_givemeeting_post.getString("women_img1");
                women_img2 = nw_page_givemeeting_post.getString("women_img2");
                women_img3 = nw_page_givemeeting_post.getString("women_img3");
                women_img4 = nw_page_givemeeting_post.getString("women_img4");
                women_img5 = nw_page_givemeeting_post.getString("women_img5");
                women_img6 = nw_page_givemeeting_post.getString("women_img6");
                women_img7 = nw_page_givemeeting_post.getString("women_img7");
                women_img8 = nw_page_givemeeting_post.getString("women_img8");
                //이미지 이름 끝
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(get_sex_from_join.equals("여자")){
            try {
                //번호 시작
                man_num1 = nw_page_givemeeting_post.getString("man_num1");
                man_num2 = nw_page_givemeeting_post.getString("man_num2");
                man_num3 = nw_page_givemeeting_post.getString("man_num3");
                man_num4 = nw_page_givemeeting_post.getString("man_num4");
                man_num5 = nw_page_givemeeting_post.getString("man_num5");
                man_num6 = nw_page_givemeeting_post.getString("man_num6");
                man_num7 = nw_page_givemeeting_post.getString("man_num7");
                man_num8 = nw_page_givemeeting_post.getString("man_num8");
                //번호 끝

                //이미지 이름 시작
                man_img1 = nw_page_givemeeting_post.getString("man_img1");
                man_img2 = nw_page_givemeeting_post.getString("man_img2");
                man_img3 = nw_page_givemeeting_post.getString("man_img3");
                man_img4 = nw_page_givemeeting_post.getString("man_img4");
                man_img5 = nw_page_givemeeting_post.getString("man_img5");
                man_img6 = nw_page_givemeeting_post.getString("man_img6");
                man_img7 = nw_page_givemeeting_post.getString("man_img7");
                man_img8 = nw_page_givemeeting_post.getString("man_img8");
                //이미지 이름 끝
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //서버로 부터 받아온 8명의 이성(여성과 남성)의 json을 얻는 메소드 끝

    //json으로 할 경는 저렇게 하고 , 구분자로 구분하여 간편하게 땡겨왔을 때에는 이런식으로 배열에 담고, 꺼내써도 된다 시작
    private void get_meeting_people(){
        if(login_sex.equals("남자")){
                //번호 시작
                women_num1 = everybody[0];
                women_num2 = everybody[2];
                women_num3 = everybody[4];
                women_num4 = everybody[6];
                women_num5 = everybody[8];
                women_num6 = everybody[10];
                women_num7 = everybody[12];
                women_num8 = everybody[14];
                //번호 끝

                //이미지 이름 시작
                women_img1 = everybody[1];
                women_img2 = everybody[3];
                women_img3 = everybody[5];
                women_img4 = everybody[7];
                women_img5 = everybody[9];
                women_img6 = everybody[11];
                women_img7 = everybody[13];
                women_img8 = everybody[15];
                //이미지 이름 끝
            }else if(login_sex.equals("여자")){
                //번호 시작
                man_num1 = everybody[0];
                man_num2 = everybody[2];
                man_num3 = everybody[4];
                man_num4 = everybody[6];
                man_num5 = everybody[8];
                man_num6 = everybody[10];
                man_num7 = everybody[12];
                man_num8 = everybody[14];
                //번호 끝

                //이미지 이름 시작
                man_img1 = everybody[1];
                man_img2 = everybody[3];
                man_img3 = everybody[5];
                man_img4 = everybody[7];
                man_img5 = everybody[9];
                man_img6 = everybody[11];
                man_img7 = everybody[13];
                man_img8 = everybody[15];
                //이미지 이름 끝
        }
    }
    //json으로 할 경는 저렇게 하고 , 구분자로 구분하여 간편하게 땡겨왔을 때에는 이런식으로 배열에 담고, 꺼내써도 된다 끝

    //본인이 남성이라고 하면, 여성 소개 메소드 시작
    public void nw_page_give_meeting_people(String result){//서버로 가는 메소드 시작
        class give_meeting_data extends AsyncTask<String,Void,String> {
            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_women = (String)strings[0];

                    String data = "json_final"+"="+json_result_women;//이 값 안에는 자신의 이름+성별+지역이 들어있다.

                    //step4.남자와 여자에 따라서 다른 php를 호출한다
                    //자신이 남자인지 여자인지에 따라서 보내는 php가 나누어진다. php까지 손대기는 조금 그래서 이렇게 나누었다
                   /*
                    if(login_sex.equals("남자")) {
                         nw_page_url_for_matching = new URL(mans_matching_url);//url setting
                    }else if(login_sex.equals("여자")){
                         nw_page_url_for_matching = new URL(womens_matcing_url);//url setting
                    }*/

                    //step4.이제 하나의 호출 메서드로 바뀌었고, 서버쪽 코드도 훨씬 간단해졌다.
                    nw_page_url_for_matching = new URL(test_url);

                    HttpURLConnection my_url = (HttpURLConnection) nw_page_url_for_matching.openConnection();
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
                Log.v("nw_page_givewomen_post","-"+s);
                //step5.넘어온 값을 구분자로 나눠준다(번호와 이미지 이름)
               everybody = s.split("\\|");
  //              String everybody_img[] = s.split("\\?");
                Log.v("배열에 성공적으로","-"+everybody[0]+everybody[1]+everybody[2]+everybody[3]+everybody[4]+everybody[5]+everybody[6]+everybody[7]);
//                Log.v("이미지가 성공적으로","-"+everybody_img[0]+everybody_img[1]+everybody_img[2]+everybody_img[3]);

                //step6.구분자를 분해하여 넣어준 배열값을, 다시 변수에 넣어준다
                get_meeting_people();
                //json 해독 시작(남자와 여자가 나누어져 있음)
                /*
                try {
                    //step7.서버에서 넘어온 값들이 있을 것이다. 그것을 json으로 감싸준다
                    nw_page_givemeeting_post = new JSONObject(s);
                    //step8.이 메소드 안에는 남여가 나누어져서 번호8개, 이미지8개를 decoding하는 코드가 들어있다
                    decode_json_meeting_people();//남여 나누어서 변수에 번호,이미지 이름 decoding하기
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */
                //json 해독 끝(남자와 여자가 나누어져 있음)

                //step7.각각의 변수들을 메소드에 꽃아준다
                //어뎁터에 추가 시작
                if(women_num1 != null && women_num2 != null){//여자 값이 넘어왔다면
                    Log.v("여자 소개 adapter","-"+women_num1+women_num2);
                    nw_page_myadapter.Add_adapter(0, women_img1,"여자", women_num1,women_img2, "여자", women_num2);
                    nw_page_myadapter.Add_adapter(0,women_img3,"여자", women_num3,women_img4, "여자", women_num4);
                    nw_page_myadapter.Add_adapter(0,women_img5,"여자", women_num5,women_img6, "여자", women_num6);
                    nw_page_myadapter.Add_adapter(0,women_img7,"여자", women_num7,women_img8, "여자", women_num8);
                }else if(man_num1 != null && man_num2 != null){//남자값이 넘어왔다면
                    Log.v("남자 소개 adapter","-"+man_num1+man_num2);
                    nw_page_myadapter.Add_adapter(0, man_img1,"남자", man_num1,man_img2, "남자", man_num2);
                    nw_page_myadapter.Add_adapter(0, man_img3,"남자", man_num3, man_img4,"남자", man_num4);
                    nw_page_myadapter.Add_adapter(0, man_img5,"남자", man_num5, man_img6,"남자", man_num6);
                    nw_page_myadapter.Add_adapter(0, man_img7,"남자", man_num7, man_img8, "남자", man_num8);
                }
                //어뎁터에 추가 끝

            }
        }
        give_meeting_data task = new give_meeting_data();
        task.execute(result);
    }
    //본인이 남성이라고 하면, 여성 소개 메소드 끝


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case position_for_delete_listview:
                    if(resultCode == Activity.RESULT_OK){
                        int position = data.getIntExtra("delete_position",0);
                        Log.v("position값","one"+position);
                        nw_page_myadapter.Delete_adapter(position);
                    }
                    break;
            //여기에 또다른 case적어주면 되지 만약에 활용할일이 있으면~
        }
    }

    //생명주기 세트 시작
    @Override
    protected void onPause(){
        super.onPause();
        onSave();//pause상태일때 onSave()호출
    }

    @Override
    protected void onStop() {
        super.onStop();
        //onSave();
    }
    //onSave,onLoad는 자체적으로 만든 함수, onPause,onStop은 오버라이딩한것
    protected void onSave(){
        SharedPreferences sf = getSharedPreferences(sfName, 0);//위에서 보면 sfName에 파일값을 담았지!
        SharedPreferences.Editor editor1 = sf.edit();//저장하려면 editor가 필요
        int a = nw_page_myarrdata.size();

        for(int i=0; i<nw_page_myarrdata.size();i++) {
            String left_img = nw_page_myarrdata.get(i).getproImage_left();//이미지 추가13
            String left_sex = nw_page_myarrdata.get(i).getSex_left();
            String left_num = nw_page_myarrdata.get(i).getNumber_left();
            String right_img = nw_page_myarrdata.get(i).getproImage_right();
            String right_sex = nw_page_myarrdata.get(i).getSex_right();
            String right_num = nw_page_myarrdata.get(i).getNumber_right();

            editor1.putString("left_img"+i,left_img);
            editor1.putString("left_sex"+i,left_sex);//배열에 넣어준다
            editor1.putString("left_num"+i,left_num);
            editor1.putString("right_img"+i,right_img);
            editor1.putString("right_sex"+i,right_sex);
            editor1.putString("right_num"+i,right_num);
        }
        editor1.putInt("size",a);
        editor1.commit();
    }

    protected void onLoad(){
        SharedPreferences sf = getSharedPreferences(sfName, 0);//위에서 보면 sfName에 파일값을 담았지!

        int a =  sf.getInt("size",0);//myData안의 getTitle을 가져와서 셋팅함
        nw_page_myarrdata.clear();
        for (int i = 0; i < a; i++) {
            String l_i = sf.getString("left_img"+i,"");
            String as = sf.getString("left_sex"+i,"");
            String bs = sf.getString("left_num"+i,"");
            String r_i = sf.getString("right_img"+i,"");//이미지 추가14
            String cs = sf.getString("right_sex"+i,"");
            String ds = sf.getString("right_num"+i,"");
            //MyarrData.add(i,new MyData(img_send2,e,b,c));
            nw_page_myarrdata.add(i,new MyData(l_i,as,bs,r_i,cs,ds));
        }
    }

    //생명주기 세트 끝
}
