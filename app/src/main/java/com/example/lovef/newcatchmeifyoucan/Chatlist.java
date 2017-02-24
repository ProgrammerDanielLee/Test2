package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-12-11.
 */
public class Chatlist extends Activity {
    private ListView lv;
    private MyAdapter myAdapter;
    private ArrayList<listItem> list;

    String server_url="http://cmic.dothome.co.kr/user_signup/uploads/";
    String mate_img;
    String mate_name;

    Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlist);

        //상대방 이미지, 이름 받기
        mate_img = getIntent().getStringExtra("mate_img");
        mate_name = getIntent().getStringExtra("mate_name");

        Log.v("Chatlist 진입","-"+mate_img+mate_name);
        list = new ArrayList<listItem>();//Arraylist로 생성

        lv=(ListView)findViewById(R.id.chat_list);
        myAdapter=new MyAdapter(list);
        lv.setAdapter(myAdapter);


        if(mate_img != null && mate_name != null){
            Log.v("추가하는부분 들어오나","-"+mate_img+mate_name);
            list.add(new listItem(mate_img,mate_name));
            onSave();
            finish();
        }else {
            onLoad();
        }
        Animation main_1 = AnimationUtils.loadAnimation(this,R.anim.fade_in_ver3);
        lv.startAnimation(main_1);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent return_chat_room = new Intent(Chatlist.this,Chat_page.class);
                startActivity(return_chat_room);
                overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
            }
        });
    }

    class listItem{
        private String mate_img1;
        private String mate_name1;

        public listItem(String mate_img2, String mate_name2){
            this.mate_img1 = mate_img2;
            this.mate_name1 = mate_name2;
        }

        public String  Get_img(){
            return mate_img1;
        }

        public String Get_name(){
            return mate_name1;
        }
    }

    class MyAdapter extends BaseAdapter {
        private  ArrayList<listItem> list;
        LayoutInflater inflater;

        public MyAdapter(ArrayList<listItem> list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public listItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos=position;


            if(convertView == null){
                inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView=inflater.inflate(R.layout.chat_list_item,parent,false);

                ImageView mate_profile = (ImageView)convertView.findViewById(R.id.chat_list_profile);
                TextView mate_name = (TextView)convertView.findViewById(R.id.chat_list_name);

                Log.v("getview에 들어 왔을까?","?");

                Glide.with(Chatlist.this).load(server_url+getItem(pos).mate_img1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .bitmapTransform(new CropCircleTransformation(mContext))
                        .into(mate_profile);

                mate_name.setText(getItem(pos).mate_name1+"님과의 채팅방");

                Log.v("getview에서 본 img,name","!"+getItem(pos).mate_img1+getItem(pos).mate_name1);
            }
            return convertView;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onSave(){
        SharedPreferences sf = getSharedPreferences("onsave3", 0);//위에서 보면 sfName에 파일값을 담았지!
        SharedPreferences.Editor editor1 = sf.edit();//저장하려면 editor가 필요
        int a = list.size();

        for(int i=0; i<list.size();i++) {
            String title = list.get(i).Get_name();
            String img_name = list.get(i).Get_img();

            editor1.putString("title"+i,title);//배열에서 제목의 위치에서 제목을 꺼낸다
            editor1.putString("img_name"+i,img_name);
        }


        editor1.putInt("size",a);
        editor1.commit();
    }
    public void onLoad(){
        SharedPreferences sf = getSharedPreferences("onsave3", 0);
        int a =  sf.getInt("size",0);
        list.clear();
            for (int i = 0; i < a; i++) {
                String b = sf.getString("title"+i,"");
                String c = sf.getString("img_name"+i,"");
                list.add(new listItem(c,b));
            }
    }
}
