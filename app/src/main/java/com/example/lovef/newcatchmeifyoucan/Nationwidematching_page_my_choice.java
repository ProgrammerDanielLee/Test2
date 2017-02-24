package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-12-02.
 */
public class Nationwidematching_page_my_choice extends Activity{
    private ImageView row_core_img2;

    //setOnitemclicklistner 위치값때문에 만든 변수 시작
    static final int POSITION_FOR_DELETE = 2;
    //setOnItemclicklistner 위치값 때문에 만든 변수 끝

    //listview를 위한 변수 선언 시작
    private String select_mode, select_depth, select_human, selected_human, selected_left_human, selected_right_human, select_sex , selected_left_human_img , selected_right_human_img;
    //선택 noti니까 선택된사람 없지요~
    private ListView core_one_list;
    private MyAdapter core_adpater;
    private ArrayList<MyData> myarrdata;
    //listview를 위한 변수 선언 끝

    //glide를 위한 변수 선언
    private Context mContext = this;//이미지 처리 때문에..glide
    private String server_url = "http://cmic.dothome.co.kr/user_signup/uploads/";//이미지 추가 3
    private String change_url_left;
    private String change_url_right;
    //glide를 위한 변수 선언 끝

    class MyData {
        private String data_sex_left;//왼쪽_성별
        private String data_number_left;//왼쪽_번호
        private String data_proimg_left;//왼쪽_이미지

        private String data_sex_right;//오른쪽_성별
        private String data_number_right;//오른쪽_번호
        private String data_proimg_right;//오른쪽_이미지

        public MyData(String img_name_left,String data_sex_left, String data_number_left,String img_name_right,String data_sex_right,String data_number_right) {
            this.data_sex_left = data_sex_left;
            this.data_number_left = data_number_left;
            this.data_proimg_left= img_name_left;

            this.data_sex_right = data_sex_right;
            this.data_number_right = data_number_right;
            this.data_proimg_right = img_name_right;
        }

        //왼쪽 성별 가져오기
        public String getSex_left() {
            return data_sex_left;
        }

        //왼쪽 번호 가져오기
        public String getNumber_left() {
            return data_number_left;
        }

        //오른쪽 성별 가져오기
        public String getSex_right() {
            return data_sex_right;
        }

        //오른쪽 번호 가져오기
        public String getNumber_right() {
            return data_number_right;
        }


        //왼쪽 성별 멤버변수 셋팅(갱신)
        public String setSex_left(String sex_left) {
            return this.data_sex_left = sex_left;
        }

        //왼쪽 번호 멤버변수 셋팅(갱신)
        public String setNumber_left(String number_left) {
            return this.data_number_left = number_left;
        }

        //왼쪽 성별 멤버변수 셋팅(갱신)
        public String setSex_right(String sex_right) {
            return this.data_sex_left = sex_right;
        }

        //왼쪽 번호 멤버변수 셋팅(갱신)
        public String setNumber_right(String number_right) {
            return this.data_number_left = number_right;
        }
        //왼쪽 프로필 이름 가져오기
        public String getproImage_left(){return data_proimg_left;}//이미지추가7
        //오른쪽 프로필 이름 가져오기
        public String getproImage_right(){return data_proimg_right;}

    }
//데이터 끝

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
        public void Add_adapter(int index,String img_i,String i1, String i2, String img_r,String i3,String i4) {
            MyarrData.add(index, new MyData(img_i,i1,i2,img_r,i3,i4));
            notifyDataSetChanged();
        }

        public void add_left_img(){
        }

        public void add_right_img(){

        }

        public void Edit_adapter(int index,String img_i,String i1, String i2,String img_r,String i3,String i4) {
            MyarrData.set(index, new MyData(img_i,i1,i2,img_r,i3,i4));
            notifyDataSetChanged();
        }

        public void Delete_adapter(int index) {
            MyarrData.remove(index);
            notifyDataSetChanged();
        }

        public void ALL_Delete_adapter() {
            MyarrData.clear();
            notifyDataSetChanged();
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.nationwidematching_page_row, parent, false);
            }
            //middle img
            if(select_depth!=null){
                if(select_depth.equals("1")) {
                    ImageView row_core_img = (ImageView) convertView.findViewById(R.id.row_core_img);
                    row_core_img.setVisibility(View.GONE);
                    row_core_img2 = (ImageView) convertView.findViewById(R.id.row_core_img4);
                    row_core_img2.setVisibility(View.VISIBLE);

                }else if(select_depth.equals("2")){
                    //나중에 원래 view값이 비어있지 않으면 바꾸는 것으로 예외처리하기
                    ImageView row_core_img = (ImageView)convertView.findViewById(R.id.row_core_img);
                    row_core_img.setVisibility(View.GONE);
                    row_core_img2 = (ImageView)convertView.findViewById(R.id.row_core_img2);
                    row_core_img2.setVisibility(View.VISIBLE);

                }else if(select_depth.equals("3")) {
                    //나중에 원래 view값이 비어있지 않으면 바꾸는 것으로 예외처리하기
                    ImageView row_core_img = (ImageView) convertView.findViewById(R.id.row_core_img);
                    if (row_core_img.equals(row_core_img)) {//여기 이따가 다시바바 만약 첫번째 이미지와 같다면!~
                        row_core_img.setVisibility(View.GONE);
                        ImageView row_core_img3 = (ImageView) convertView.findViewById(R.id.row_core_img3);
                        row_core_img3.setVisibility(View.VISIBLE);
                    }
                }
            }
            //row_core_img.setImageResource(R.drawable.core_icon_select_twodepth);
            //middle img end

            //left profile img start
            ImageView left_img = (ImageView)convertView.findViewById(R.id.row_img_left);//이미지 추가9
            change_url_left = server_url+MyarrData.get(position).getproImage_left();
            Glide.with(Nationwidematching_page_my_choice.this).load(change_url_left)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    //.thumbnail((float) 0.5)
                    .into(left_img);
            if(select_depth!=null) {
                if (select_depth.equals("1")) {
                        if(select_sex.equals("여자")){
                            Glide.with(Nationwidematching_page_my_choice.this).load(R.drawable.d_man_icon)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .bitmapTransform(new CropCircleTransformation(mContext))
                                    .into(left_img);
                        }else{
                            Glide.with(Nationwidematching_page_my_choice.this).load(R.drawable.d_girl_icon)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .bitmapTransform(new CropCircleTransformation(mContext))
                                    .into(left_img);
                        }
                }
            }
            //left profile img end

            //right profile img start
            ImageView right_img = (ImageView)convertView.findViewById(R.id.pro_img_right);//이미지 추가10
            change_url_right = server_url+MyarrData.get(position).getproImage_right();
            Glide.with(Nationwidematching_page_my_choice.this).load(change_url_right)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(right_img);
            if(select_depth!=null) {
                if (select_depth.equals("1")) {
                    if(select_sex.equals("여자")){
                        Glide.with(Nationwidematching_page_my_choice.this).load(R.drawable.d_man_icon)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .bitmapTransform(new CropCircleTransformation(mContext))
                                .into(right_img);
                    }else{
                        Glide.with(Nationwidematching_page_my_choice.this).load(R.drawable.d_girl_icon)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .bitmapTransform(new CropCircleTransformation(mContext))
                                .into(right_img);
                    }
                }
            }
            //right profile img end

            //left sex
            TextView left_sex = (TextView) convertView.findViewById(R.id.row_sex_left);
            left_sex.setText(MyarrData.get(position).getSex_left());
            //left sex end

            //left number
            TextView left_number = (TextView) convertView.findViewById(R.id.row_number_left);
            left_number.setText(MyarrData.get(position).getNumber_left());
            //left numebr end

            //right sex
            TextView right_sex = (TextView) convertView.findViewById(R.id.row_sex_right);
            right_sex.setText(MyarrData.get(position).getSex_right());
            //right sex end

            //right number
            TextView right_number = (TextView) convertView.findViewById(R.id.row_number_right);
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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nationwidematching_page_my_choice);
        Animation main_1 = AnimationUtils.loadAnimation(this,R.anim.fade_in_ver3);

        TextView title = (TextView)findViewById(R.id.title_my_choice);
        ImageView title_underline=(ImageView)findViewById(R.id.underline_my_choice);

        title.startAnimation(main_1);
        title_underline.startAnimation(main_1);

        select_mode = getIntent().getStringExtra("matching_result_mode");//결과
        select_depth = getIntent().getStringExtra("matching_result_depth");//깊이
        select_human = getIntent().getStringExtra("matching_result_s");//선택자
        selected_left_human = getIntent().getStringExtra("matching_result_left");//왼쪽사람
        selected_right_human = getIntent().getStringExtra("matching_result_right");//오른쪽사람
        selected_left_human_img = getIntent().getStringExtra("matching_result_left_img");
        selected_right_human_img = getIntent().getStringExtra("matching_result_right_img");

        Log.v("탭에서 넘어왔을까요", "?" + select_mode +selected_human+ select_depth+selected_left_human);
        Log.v("탭에서 넘어왔을까요(img)", "?" +  selected_left_human_img+selected_right_human_img);

        //성별을 꺼내기 위한 json+method 시작
        JSONObject jo = new JSONObject();
        try {
            jo.put("json_select_sex", select_human);//번호를 토대로 자신의 성별을 꺼냄
        }catch(JSONException e) {
            e.printStackTrace();
        }
        String result_select_human = jo.toString();
        select_number(result_select_human);
        //성별을 꺼내기 위한 json+method 끝
        //어뎁터들 구성 시작
        myarrdata = new ArrayList<MyData>();
        core_adpater = new MyAdapter(this, myarrdata);
        core_one_list = (ListView) findViewById(R.id.core_one_list_select);
        core_one_list.startAnimation(main_1);
        core_one_list.setAdapter(core_adpater);
        //어뎁터들 구성 끝 + 마지막에 어뎁터 꽃아주기

        //리스트 클릭했을때 시작
        core_one_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//아이템 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String left_human_number = myarrdata.get(position).getNumber_left().toString();
                String right_human_number = myarrdata.get(position).getNumber_right().toString();
                String left_human_img = myarrdata.get(position).getproImage_left().toString();//이미지 추가11
                String right_human_img = myarrdata.get(position).getproImage_right().toString();

                Intent go_one_view_page = new Intent(Nationwidematching_page_my_choice.this,Nationwidematching_page_view.class);
                go_one_view_page.putExtra("page_view",select_mode);
                go_one_view_page.putExtra("page_one_select_depth",select_depth);//현재 깊이

                go_one_view_page.putExtra("page_one_left_human_img",left_human_img);//왼쪽 사람 이미지
                go_one_view_page.putExtra("page_one_right_human_img",right_human_img);//오른쪽 사람 이미지

                go_one_view_page.putExtra("page_one_select_sex",select_sex);//선택하는 사람의 성
                go_one_view_page.putExtra("page_one_select_human",select_human);//선택하는 사람의 번호

                //결과일때는 아래 한줄을 block처리해도 되겠지~
                go_one_view_page.putExtra("page_one_selected_human",selected_human);//선택된 사람의 번호

                go_one_view_page.putExtra("page_one_left_number",left_human_number);//왼쪽 사람 번호
                go_one_view_page.putExtra("page_one_right_number",right_human_number);//오른쪽 사람 번호
                startActivityForResult(go_one_view_page,POSITION_FOR_DELETE);
            }
        });
        //리스트 클릭했을때 끝
        //onLoad();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case POSITION_FOR_DELETE:
                if(resultCode == Activity.RESULT_OK){
                    int position = data.getIntExtra("delete_position",0);
                    Log.v("position값","select"+position);
                    core_adpater.Delete_adapter(position);
                    finish();
                }
                break;

            //여기에 또다른 case적어주면 되지 만약에 활용할일이 있으면~
        }
    }
    //성별 꺼내오기 시작
    public void select_number(String result_sex) {//서버로 가는 메소드 시작
        class select_num extends AsyncTask<String, Void, String> {
            //  ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                //    loading = ProgressDialog.show(catch_core_page_one.this,"기다려 주세요..",null,true,true);
                //걍 progressbar 없애기...
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String json_result_man = (String) strings[0];
                    String data = "json_final" + "=" + json_result_man;
                    Log.v("json결과(give_man)", "json결과(give_man)" + data);
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_meeting/select_for_get_sex.php");//여기 수정
                    HttpURLConnection my_url = (HttpURLConnection) my_server.openConnection();
                    my_url.setDoOutput(true);//output(출력)을 전용으로 하는 메소드이다. 한마디로 서버로 쓴다.

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(my_url.getOutputStream()));
                    bw.write(data);
                    bw.flush();
                    bw.close();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(my_url.getInputStream()));
                    //my_url.setDoInput(true);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    return new String("exeption :" + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
                //Toast.makeText(getApplicationContext(),"값이 성공적으로 전달되었습니다",Toast.LENGTH_LONG).show();
                Log.v("성별 꺼내오기", "성별꺼내오기" + s);

                try {
                    JSONObject j_o = new JSONObject(s);
                    // for(int i = 0; i<j_o.length();i++){
                    select_sex = j_o.getString("select_sex");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(select_sex.equals("남자")){
                    core_adpater.Add_adapter(0,selected_left_human_img,"여자",selected_left_human,selected_right_human_img, "여자", selected_right_human);
                }else if(select_sex.equals("여자")){
                    core_adpater.Add_adapter(0,selected_left_human_img,"남자", selected_left_human,selected_right_human_img,"남자", selected_right_human);
                }
            }
        }
        select_num task = new select_num();
        task.execute(result_sex);
    }
    //성별 꺼내오기 끝
}
