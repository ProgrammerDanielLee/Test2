package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-11-19.
 */
public class Room_matching_page_minihome extends Activity{
    //쉐어드 시작
    private  String sfName="sfsf";
    //쉐어드 끝

    //서버관련 default ip 시작
    private String image_server_ip="http://cmic.dothome.co.kr/user_signup/uploads/";
    private String music_server_ip = "http://cmic.dothome.co.kr/new_cmic/user_room_matching/user_minihome/user_mp3_folder/";
    //서버관련 default ip 끝

    //이미지 재생 관련 변수 시작
    private Button minihome_music_btn;//이미지 재생/중지 똑딱이 버튼
    private boolean minihome_music_play_pause;//중지 됬는지 안됬는지의 여부를 알아보기 위한 pause
    private MediaPlayer minihome_music_mediaplayer;
    private boolean minihome_music_intialstage = true;
    /**
     * remain false till media is not completed, inside OnCompletionListener make it true.
     * meida가 끝나지 않았다면, OnCompletionListener가 그것을 가능하게 해준다.
     */
    private String music_name;
    //mp3확장자는 아래에 표현해주었음
    //이미지 재생 관련 변수 끝

    TextView tv;

    //키값 시작
    private String user_sex;
    private String user_num;
    private String other_num;
    private String other_sex;
    //키값 끝

    //프로필 관련 변수 선언 시작
    private Context mContext =  this;
    private ImageView pro_img;
    private TextView pro_sex,pro_num,pro_age,pro_local,pro_smoking,pro_hobby,pro_mind,pro_tall,pro_job,pro_blood;
    private EditText pro_edittext;
    private Button pro_button;
    //프로필 관련 변수 선언 끝

    //게시판 관련 시작
    private String board_result;
    private ListView board_page_listview_meet;
    private MyAdapter board_page_myadapter;
    private ArrayList<MyData> board_page_myarrdata;
    //게시판 관련 끝
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //데이터 관리 시작
    class MyData{
        //private을 써서, get함수로만 접근 가능하게 끔 만들어서 data의 보안을 유지한다.
        private String sex;//성별
        private String num;//번호
        private String contents;//대화 내용

        public MyData(String s,String n,String c){
            this.sex = s;
            this.num = n;
            this.contents = c;
        }
        //왼쪽 프로필 이름 가져오기
        public String Get_sex(){return sex;}//이미지추가7

        //왼쪽 성별 가져오기
        public String Get_num(){
            return num;
        }

        //왼쪽 번호 가져오기
        public String Get_contents(){
            return contents;
        }
    }


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
        public void Add_adapter(int i,String s,String n,String c){
            MyarrData.add(i,new MyData(s,n,c));//이미지 추가8
            notifyDataSetChanged();
        }

        public void Edit_adapter(int i,String s,String n,String c){
            MyarrData.set(i,new MyData(s,n,c));;
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
                convertView = inflater.inflate(R.layout.room_matching_board_row,parent,false);
            }

            //성별
            TextView board_sex= (TextView)convertView.findViewById(R.id.board_sex);
            board_sex.setText(MyarrData.get(position).Get_sex());
            //성별 끝

            //번호
            TextView board_num = (TextView)convertView.findViewById(R.id.board_num);
            board_num.setText(MyarrData.get(position).Get_num());
            //번호 끝

            //내용 시작
            TextView board_contents = (TextView)convertView.findViewById(R.id.board_contents);
            board_contents.setText(MyarrData.get(position).Get_contents());
            //내용 끝

            return convertView;
        }
    }
    //어뎁터 구현 끝
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void Pro_Objection(){

        pro_num=(TextView)findViewById(R.id.minihome_pro_num);
        pro_sex = (TextView)findViewById(R.id.minihome_pro_sex);
        pro_age = (TextView)findViewById(R.id.minihome_pro_age);
        pro_local = (TextView)findViewById(R.id.minihome_pro_local);
        pro_smoking = (TextView)findViewById(R.id.minihome_pro_smoking);
        pro_hobby = (TextView)findViewById(R.id.minihome_pro_hobby);
        pro_mind = (TextView)findViewById(R.id.minihome_pro_mind);
        pro_tall = (TextView)findViewById(R.id.minihome_pro_tall);
        pro_job = (TextView)findViewById(R.id.minihome_pro_job);
        pro_blood = (TextView)findViewById(R.id.minihome_pro_blood);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_sex = getIntent().getStringExtra("user_sex");
        user_num = getIntent().getStringExtra("user_num");
        other_sex = getIntent().getStringExtra("other_sex");
        other_num = getIntent().getStringExtra("other_num");
        setContentView(R.layout.room_matching_page_minihome1);//미니홈피 호출
        Animation main_1 = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        LinearLayout ln = (LinearLayout)findViewById(R.id.minihome_title);
        LinearLayout Ml = (LinearLayout)findViewById(R.id.minihome_main_frame);
        ListView lv = (ListView)findViewById(R.id.minihome_main_listview);
        TextView txl = (TextView)findViewById(R.id.minihome_line);
        ImageView imv = (ImageView)findViewById(R.id.minihome_pro_img);
        pro_edittext = (EditText)findViewById(R.id.minihome_main_edittext);
        pro_button = (Button)findViewById(R.id.minihome_main_button);

        //게시판 관련 시작
        board_page_listview_meet = (ListView)findViewById(R.id.minihome_main_listview);
        board_page_myarrdata = new ArrayList<MyData>();//Arraydata에 mydata를 집어넣고
        board_page_myadapter= new MyAdapter(this,board_page_myarrdata);//adapter에 data를 넣는다
        board_page_listview_meet.setAdapter(board_page_myadapter);

        imv.startAnimation(main_1);
        ln.startAnimation(main_1);
        Ml.startAnimation(main_1);
        lv.startAnimation(main_1);
        txl.startAnimation(main_1);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pro_edittext.getWindowToken(), 0);
        //음악관련 객체화 시작
        tv = (TextView)findViewById(R.id.minihome_page_textview_music_view);//음악 제목 setting
        minihome_music_btn = (Button) findViewById(R.id.minihome_page_button_music);
        tv.startAnimation(main_1);
        //음악 관련 객체화 끝

        Pro_Objection();//프로필 객체화

        if(user_num!=null){
        Start_to_server_get_pro_and_music(user_num);
        }else{
            Toast.makeText(getApplicationContext(), "유저의 번호가 넘어오지 않았습니다", Toast.LENGTH_SHORT).show();
        }

        //mp3 setting 시작
        minihome_music_mediaplayer = new MediaPlayer();
        minihome_music_mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);//Audio Streamtype을 스트림 뮤직으로 지정한다
        minihome_music_btn.setOnClickListener(pausePlay);//여기서부터 다시
        minihome_music_btn.startAnimation(main_1);
        //minihome_music_mediaplayer.isPlaying();
        //mp3 setting 끝

        final ImageView bottom_button = (ImageView)findViewById(R.id.bottom_button_for_pro);
        final ImageView topside_button = (ImageView)findViewById(R.id.topside_button_for_pro);
        final TableLayout tl = (TableLayout)findViewById(R.id.pro_tab);

        bottom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_button.setVisibility(View.GONE);
                topside_button.setVisibility(View.VISIBLE);
                tl.setVisibility(View.VISIBLE);

            }
        });

        topside_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottom_button.setVisibility(View.VISIBLE);
                topside_button.setVisibility(View.GONE);
                tl.setVisibility(View.GONE);
            }
        });

        //방명록  시작
        pro_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 30) {

                    //myself_result = Integer.parseInt(myself);
                   // under_join_page_edittext_myself.setVisibility(View.GONE);//*키를 제대로 입력했는지 확인해주세요에 있는 textview표시
                   board_result = s.toString();
                    Log.v("게시글 결과", " - " + board_result);
                }

                if (s.toString().length() > 30) {
                    Toast.makeText(Room_matching_page_minihome.this, "30자 이상은 보내지지 않습니다", Toast.LENGTH_SHORT).show();
                    board_result = null;
                }
            }
        });
        //방명록 끝

        //방명록 전송 버튼 시작
        pro_button.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
                if(pro_edittext.getText().toString().trim().length() != 0 && board_result != null && board_result !=""){
                    //테그 : 리스트뷰 add
                    if(other_num !=null && other_sex !=null){
                        user_sex=other_sex;
                        user_num=other_num;
                    }

                    board_page_myadapter.Add_adapter(0,"- "+user_sex,user_num+"호",board_result);
                    pro_edittext.setText("");
                }else{
                    Toast.makeText(Room_matching_page_minihome.this, "제대로 입력되었는지 확인해주세요", Toast.LENGTH_SHORT).show();
                }

            }
          }
        );
        //방명록 전송 버튼 끝
       //onLoad();
       // board_page_myarrdata.add(0,new MyData("- 여자","155호","뭐해?"));
        //board_page_myarrdata.add(1,new MyData("- 여자","159호","밥먹었어?"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        minihome_music_mediaplayer.pause();
        //onSave();
    }

    protected void onSave(){
        SharedPreferences sf = getSharedPreferences(sfName, 0);//위에서 보면 sfName에 파일값을 담았지!
        SharedPreferences.Editor editor1 = sf.edit();//저장하려면 editor가 필요
        int a = board_page_myarrdata.size();

        for(int i=0; i<board_page_myarrdata.size();i++) {
            String left_img = board_page_myarrdata.get(i).Get_sex();
            String left_sex = board_page_myarrdata.get(i).Get_num();
            String left_num = board_page_myarrdata.get(i).Get_contents();

            editor1.putString("sex"+i,left_img);
            editor1.putString("num"+i,left_sex);//배열에 넣어준다
            editor1.putString("contents"+i,left_num);
        }
        editor1.putInt("size",a);
        editor1.commit();
    }

    protected void onLoad(){
        SharedPreferences sf = getSharedPreferences(sfName, 0);//위에서 보면 sfName에 파일값을 담았지!

        int a =  sf.getInt("size",0);//myData안의 getTitle을 가져와서 셋팅함
        board_page_myarrdata.clear();
        for (int i = 0; i < a; i++) {
            String s = sf.getString("sex"+i,"");
            String n = sf.getString("num"+i,"");
            String c = sf.getString("contents"+i,"");
            //MyarrData.add(i,new MyData(img_send2,e,b,c));
            board_page_myarrdata.add(i,new MyData(s,n,c));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.room_matching_page_minihome1, menu);
        return true;
    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!minihome_music_play_pause) {//중지 시에는...
                minihome_music_btn.setBackgroundResource(R.drawable.stop_button);
                if (minihome_music_intialstage)
                    new Player().execute(music_server_ip+music_name+".mp3");//실제로 음악 경로.
                else {
                    if (!minihome_music_mediaplayer.isPlaying())
                        minihome_music_mediaplayer.start();
                }
                minihome_music_play_pause = true;

            } else {//재생 시에는...
                minihome_music_btn.setBackgroundResource(R.drawable.play_button);
                if (minihome_music_mediaplayer.isPlaying())
                    minihome_music_mediaplayer.pause();
                minihome_music_play_pause = false;
            }
        }
    };


    class Player extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progress;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {
                minihome_music_mediaplayer.setDataSource(params[0]);

                minihome_music_mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        minihome_music_intialstage = true;
                        minihome_music_play_pause = false;
                        minihome_music_btn.setBackgroundResource(R.drawable.play_button);
                        minihome_music_mediaplayer.stop();
                        minihome_music_mediaplayer.reset();
                    }
                });
                minihome_music_mediaplayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progress.isShowing()) {
                progress.cancel();
            }
            Log.d("Prepared", "//" + result);
            minihome_music_mediaplayer.start();

            minihome_music_intialstage = false;
        }

        public Player() {
            progress = new ProgressDialog(Room_matching_page_minihome.this);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
          //  this.progress.setMessage("Buffering...");
           // this.progress.show();

        }
    }

    private void Start_to_server_get_pro_and_music(String user_num) {
        class insertdata_right_button extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... strings) {
                try{
                    String user_num1 = (String)strings[0];
                    String data = "user_num"+"="+user_num1;
                        Log.v("user_num제대로 확인","-"+data);//선택한사람,선택된사람,왼쪽사람,오른쪽사람
                    URL my_server = new URL("http://cmic.dothome.co.kr/new_cmic/user_room_matching/user_minihome/get_info_and_mp3.php");//여기 수정
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

                try {
                    JSONObject j_o = new JSONObject(s);
                    String login_num = j_o.getString("user_num");
                    String login_email= j_o.getString("email");
                    String login_name = j_o.getString("name");
                    String login_sex= j_o.getString("sex");
                    String login_age = j_o.getString("age");
                    String login_tall = j_o.getString("tall");
                    String login_job = j_o.getString("job");
                    String login_local = j_o.getString("local");
                    String login_smoking = j_o.getString("smoking");
                    String login_mind = j_o.getString("mind");
                    String login_blood = j_o.getString("blood");
                    String login_hobby = j_o.getString("hobby");
                    String login_img = j_o.getString("img");
                    //login_final_oneword = j_o.getString("final_oneword");
                    music_name = j_o.getString("music");

                    //프로필 setting 시작
                    //이미지 시작
                    pro_img = (ImageView)findViewById(R.id.minihome_pro_img);//이미지 추가9
                   // change_url_left = server_url+MyarrData.get(position).getproImage_left();
                    Glide.with(Room_matching_page_minihome.this).load(image_server_ip+login_img)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            //.thumbnail((float) 0.5)
                            .into(pro_img);
                    //이미지 끝
                    pro_sex.setText(login_sex);
                    pro_num.setText(login_num);
                    pro_local.setText(login_local);
                    //pro_email.setText(login_email);
                    //drawer_name.setText(login_name);
                    pro_age.setText(login_age);
                    pro_tall.setText(login_tall);
                    pro_job.setText(login_job);
                    pro_smoking.setText(login_smoking);
                    pro_mind.setText(login_mind);
                    pro_blood.setText(login_blood);
                    pro_hobby.setText(login_hobby);
                    tv.setText(music_name);
                    //drawer_final_oneword.setText(login_final_oneword);
                    //프로필 setting 끝
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        }
        insertdata_right_button task = new insertdata_right_button();
        task.execute(user_num);

    }
}










/*
class Room_matching_page_minihome_fragment extends Fragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.room_matching_page_minihome_fragment ,container,false);
        return view;
    }

    public void setText(String item){
        TextView view = (TextView)getView().findViewById(R.id.room_matching_page_minihome_textview_contents);
        view.setText(item);
    }

}
/*
class Room_matching_page_minihome_fragment_contents extends ListFragment{

    //세가지 메소드를 호출하고 나머지 한가지는 정의한다
    //세가지 : onCreate,onActivityCreated,onListItemClick

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] menus = new String[]{"프로필","사진첩","방명록"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,menus);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        String item = (String) getListAdapter().getItem(position);//클릭한 위치의 메뉴 땡겨오기
        Room_matching_page_minihome_fragment frag = (Room_matching_page_minihome_fragment) getFragmentManager().findFragmentById(R.id.room_matching_page_minihome_contents);
        if(frag != null && frag.isInLayout()){
            frag.setText(getContents(item));
        }
    }

    private String getContents(String item) {
        if(item.toLowerCase().contains("프로필")){
            return " 프로필 test";
        }
        if(item.toLowerCase().contains("사진첩")){
            return " 사진첩 test";
        }
        if(item.toLowerCase().contains("방명록")){
            return " 방명록 test";
        }
        return "??";
    }
}
*/