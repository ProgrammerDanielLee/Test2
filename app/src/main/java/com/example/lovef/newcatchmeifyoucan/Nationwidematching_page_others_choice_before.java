package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-12-07.
 */
public class Nationwidematching_page_others_choice_before extends AppCompatActivity{
    private ImageView row_core_img2;

    //결과인지 아닌지를 체크해주기 위한 string 시작
    private String result_or_not;
    private String result_of_json;
    //결과인지 아닌지를 체크해주기 위한 string 끝

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

    //데이터 시작
    class MyData {
        private String data_sex_left;//왼쪽_성별
        private String data_number_left;//왼쪽_번호
        private String data_proimg_left;//왼쪽_이미지

        public String middle_img;

        private String data_sex_right;//오른쪽_성별
        private String data_number_right;//오른쪽_번호
        private String data_proimg_right;//오른쪽_이미지

        public MyData(String middle_img,String img_name_left,String data_sex_left, String data_number_left,String img_name_right,String data_sex_right,String data_number_right) {
                this.data_sex_left = data_sex_left;
                this.data_number_left = data_number_left;
                this.data_proimg_left= img_name_left;

                this.middle_img=middle_img;

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
            public String getImage_middle(){return middle_img;}//가운데 이미지 반환
            //왼쪽 프로필 이름 가져오기
            public String getproImage_left(){return data_proimg_left;}//왼쪽 이미지 반환
            //오른쪽 프로필 이름 가져오기
            public String getproImage_right(){return data_proimg_right;}//오른쪽 이미지 반환

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
        public void Add_adapter(int index,String md_img,String img_i,String i1, String i2, String img_r,String i3,String i4) {
            MyarrData.add(index, new MyData(md_img,img_i,i1,i2,img_r,i3,i4));
            notifyDataSetChanged();
        }

        public void add_left_img(){
        }

        public void add_right_img(){

        }

        public void Edit_adapter(int index,String md_img,String img_i,String i1, String i2,String img_r,String i3,String i4) {
            MyarrData.set(index, new MyData(md_img,img_i,i1,i2,img_r,i3,i4));
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
                    row_core_img2 = (ImageView)convertView.findViewById(R.id.row_core_img5);
                    row_core_img2.setVisibility(View.VISIBLE);

                }else if(select_depth.equals("3")){
                    //나중에 원래 view값이 비어있지 않으면 바꾸는 것으로 예외처리하기
                    ImageView row_core_img = (ImageView)convertView.findViewById(R.id.row_core_img);
                    if(row_core_img.equals(row_core_img)){//여기 이따가 다시바바 만약 첫번째 이미지와 같다면!~
                        row_core_img.setVisibility(View.GONE);
                        ImageView row_core_img3 = (ImageView)convertView.findViewById(R.id.row_core_img6);
                        row_core_img3.setVisibility(View.VISIBLE);
                    }
                }
            }
            //row_core_img.setImageResource(R.drawable.core_icon_select_twodepth);
            //middle img end

            //left profile img start
            ImageView left_img = (ImageView)convertView.findViewById(R.id.row_img_left);//이미지 추가9
            change_url_left = server_url+MyarrData.get(position).getproImage_left();
            Glide.with(Nationwidematching_page_others_choice_before.this).load(change_url_left)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    //.thumbnail((float) 0.5)
                    .into(left_img);
            if(select_depth!=null){
                if(select_depth.equals("1")){
                    Glide.with(Nationwidematching_page_others_choice_before.this).load(R.drawable.default_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .into(left_img);
                }
            }
            //left profile img end

            //right profile img start
            ImageView right_img = (ImageView)convertView.findViewById(R.id.pro_img_right);//이미지 추가10
            change_url_right = server_url+MyarrData.get(position).getproImage_right();
            Glide.with(Nationwidematching_page_others_choice_before.this).load(change_url_right)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(right_img);
            //선택단계가 1이면 아무사진도 안뜨기 시작
            if(select_depth!=null) {
                if (select_depth.equals("1")) {
                    Glide.with(Nationwidematching_page_others_choice_before.this).load(R.drawable.default_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            .into(right_img);
                }
            }
            //선택단계가 1이면 아무사진도 안뜨기 끝
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
        //여기서 받아야 될 것은,
        //선택한사람,선택된사람,왼쪽이성번호,오른쪽이성번호,왼쪽이미지,오른쪽이미지

        //이전 페이지가 결과인지 아닌지의 여부 받기 , json 받기 시작
        result_or_not = getIntent().getStringExtra("result");//'결과'페이지에서 온건지 아니면 그냥 들어온건지 변수 받기
        result_of_json = getIntent().getStringExtra("result_json");
        Log.v("두개다 제대로2","?"+result_or_not+result_of_json);
        //이전 페이지가 결과인지 아닌지의 여부 받기 , json 받기 끝

        setContentView(R.layout.nationwidematching_page_others_choice_before);

        //어뎁터들 구성 시작
        myarrdata = new ArrayList<MyData>();
        core_adpater = new MyAdapter(this, myarrdata);
        core_one_list = (ListView) findViewById(R.id.core_one_list_select);
        core_one_list.setAdapter(core_adpater);
        //어뎁터들 구성 끝 + 마지막에 어뎁터 꽃아주기

        //옆에 탭(결과 탭)에서 넘어왔을 경우의 상황 시작
        if(result_of_json!=null){
            try {
                    JSONObject j_o = new JSONObject(result_of_json);
                    select_mode = j_o.getString("matching_result_mode");//결과
                    select_sex = j_o.getString("matching_result_sex");//선택한 사람의 성별
                    select_depth = j_o.getString("matching_result_depth");//깊이
                    select_human = j_o.getString("matching_result_s");//선택자
                    selected_human = j_o.getString("matching_result_st");//선택당한 사람
                    selected_left_human = j_o.getString("matching_result_left");//왼쪽 사람
                    selected_right_human = j_o.getString("matching_result_right");//오른쪽 사람
                    selected_left_human_img = j_o.getString("matching_result_left_img");//왼쪽사람 이미지
                    selected_right_human_img = j_o.getString("matching_result_right_img");//오른쪽사람 이미지
                } catch (JSONException e) {
                    e.printStackTrace();
            }

        }

        if( result_or_not!=null){
            if(result_or_not.equals("결과")){//list에 추가해주고 finish()
                if(select_sex.equals("남자")){
                        core_adpater.Add_adapter(0,select_depth,selected_left_human_img,"여자",selected_left_human,selected_right_human_img, "여자", selected_right_human);
                        Log.v("남자 리스트 추가","!");
                        onSave();
                        finish();
                }else if(select_sex.equals("여자")){
                        core_adpater.Add_adapter(0,select_depth,selected_left_human_img,"남자", selected_left_human,selected_right_human_img,"남자", selected_right_human);
                        Log.v("여자 리스트 추가","!");
                        onSave();
                        finish();
                    }
                }
            }else{
            //onLoad();
        }
        //옆에 탭(결과 탭)에서 넘어왔을 경우의 상황 끝
        onLoad();


        //리스트 클릭했을때 시작
        /*
        core_one_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//아이템 클릭 시
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String left_human_number = myarrdata.get(position).getNumber_left().toString();
                String right_human_number = myarrdata.get(position).getNumber_right().toString();
                String left_human_img = myarrdata.get(position).getproImage_left().toString();//이미지 추가11
                String right_human_img = myarrdata.get(position).getproImage_right().toString();

                Intent go_one_view_page = new Intent(Nationwidematching_page_others_choice_before.this,Nationwidematching_page_view.class);
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
        */
        //리스트 클릭했을때 끝
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case POSITION_FOR_DELETE:
                if(resultCode == Activity.RESULT_OK){
                    int position = data.getIntExtra("delete_position",0);
                    Log.v("position값","select"+position);
                   // core_adpater.Delete_adapter(position);
                    //여기서는 삭제하지말고 그냥 냅두기
                }
                break;

            //여기에 또다른 case적어주면 되지 만약에 활용할일이 있으면~
        }
    }
    //쉐어드 프리퍼런스 시작///////////////////////////////////////////////////
    @Override
    protected void onPause(){
        super.onPause();
        onSave();//pause상태일때 onSave()호출
        Log.v("퍼즈호출","!");
    }

    @Override
    protected void onStop() {
        super.onStop();
       //onSave();
        Log.v("스톱호출","!");
    }

    public void onSave(){
        SharedPreferences sf = getSharedPreferences("onsave2", 0);//위에서 보면 sfName에 파일값을 담았지!
        SharedPreferences.Editor editor1 = sf.edit();//저장하려면 editor가 필요
        int a = myarrdata.size();

        for(int i=0; i<myarrdata.size();i++) {
            String left_sex = myarrdata.get(i).getSex_left();
            String left_num = myarrdata.get(i).getNumber_left();
            String right_sex = myarrdata.get(i).getSex_right();
            String right_num = myarrdata.get(i).getNumber_right();
            String left_img = myarrdata.get(i).getproImage_left();//이미지 추가13
            String right_img = myarrdata.get(i).getproImage_right();
            select_depth = myarrdata.get(i).getImage_middle();

            editor1.putString("depth"+i, select_depth);
            editor1.putString("left_sex"+i,left_sex);//배열에서 제목의 위치에서 제목을 꺼낸다
            editor1.putString("left_num"+i,left_num);
            editor1.putString("right_sex"+i,right_sex);
            editor1.putString("right_num"+i,right_num);
            editor1.putString("left_img"+i,left_img);
            editor1.putString("right_img"+i,right_img);
            Log.v("저장될때 사이즈","????"+ a);
        }
        editor1.putInt("size",a);
        editor1.commit();
    }

    public void onLoad(){
        SharedPreferences sf = getSharedPreferences("onsave2", 0);//위에서 보면 sfName에 파일값을 담았지!
       /*
        String get_token = sf.getString("save_token","");
        Log.v("token test","-"+get_token);
        */
        int a =  sf.getInt("size",0);//myData안의 getTitle을 가져와서 셋팅함
        //MyData w = new MyData();
        myarrdata.clear();
        for (int i = 0; i < a; i++) {
            //Uri img_send = sf.getString()
            //String test = img_send.toString();
            String as = sf.getString("left_sex"+i,"");
            String bs = sf.getString("left_num"+i,"");
            String cs = sf.getString("right_sex"+i,"");
            String ds = sf.getString("right_num"+i,"");
            String l_i = sf.getString("left_img"+i,"");
            String r_i = sf.getString("right_img"+i,"");//이미지 추가14
            select_depth = sf.getString("depth"+i,"");
            Log.v("호출될때 사이즈","????"+a);
            myarrdata.add(i,new MyData(select_depth,l_i,as,bs,r_i,cs,ds));
        }
    }
    //쉐어드 프리퍼런스 끝///////////////////////////////////////////////////
}
