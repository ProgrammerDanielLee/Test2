package com.example.lovef.newcatchmeifyoucan;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lovef on 2016-11-16.
 */
public class Chat_page extends ActionBarActivity {
    SQLiteDatabase db;
    MySQLiteOpenHelper dbhelper;


    //내이름,너이름
    //String me="남자1호";//처음들어온 사람
    //String you="여자1호";//두번째에 들어온 사람
    String me;
    String you;
    String my_sex;
    String your_sex;
    //아마도 처음 들어온사람,나중에 들어온사람, 그리고 intent로 넘겨준 두 번호 아마 다 다르게 받아야될 것 같다. 입장과 입장 의 차이...
    String left_human;
    String right_human;
    String other;
//위 초록색 4개만 바꿔주면 됨


    private ListView messagesContainer;
    private EditText messageET;
    private Button sendBtn;

    private ChatAdapter adapter;
    private ArrayList<ChatMessage> chatHistory;

    //소켓통신 부분 시작
    Socket chatting_socket;
    String server_ip = "172.30.1.9";

    //Thread 시작
    Thread chatting_sender_thread;
    Thread chatting_receive_thread;
    //Thread 끝
    Handler chat_handler;
    //소켓통신 부분 끝

    //보내기 버튼 눌렀을때 보내는 String
    String messageText;//(계속 보내는 msg)
    //가만히 받는 String
    String mesageReceive;
    Boolean me_or_you;

    //이미지 server url + context 시작
    private Context mContext = this;//이미지 처리 때문에..glide
    private String server_url = "http://cmic.dothome.co.kr/user_signup/uploads/";//이미지 추가 3
    String json_user_img;

    String left_imgr,right_imgr;
    //이미지 server url+ context 끝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        left_human=getIntent().getStringExtra("chat_select_num");//나
        right_human=getIntent().getStringExtra("chat_selected_num");//최초에 나를 선택해준 사람
        my_sex=getIntent().getStringExtra("chat_select_sex");//left = my
        my_sex="여자";//이거 나중에 꼭 수정할 것, 아마 이거 전 페이지에서 성별을 잘못 넘겨준것 같다
        //성별이 자꾸 남자로 넘어오느 이슈가 17.1.24일날 발생함
        Log.v("chat을 위해 넘어온 값","-"+left_human+right_human+my_sex);


        if(my_sex!=null) {
            if (my_sex.equals("남자")) {
                your_sex = "여자";
            }else{
                your_sex = "남자";
            }
        }

        if(left_human == null || right_human == null){
            onLoad();
        }


        me=my_sex+right_human+"호";
        you=your_sex+left_human+"호";

        Log.v("me,you에 들어간 값","-"+me+you);

        setContentView(R.layout.chat_page);

        dbhelper = new  MySQLiteOpenHelper(Chat_page.this,"chat_message.db",null,1);
        //화면이 열리면 현재 화면의 context,파일명,커서팩토리,버전번호 객체화

        //사진 조회를 위한 json 시작
        JSONObject user_img = new JSONObject();
        try{
            user_img.put("left_human",left_human);
            user_img.put("right_human",right_human);
            json_user_img=user_img.toString();
            Log.v("보내기 직전 json","-"+json_user_img);
        }catch (JSONException e){e.printStackTrace();}
        //사진조회를 위한 json 끝

        call_img(json_user_img);//step2.사진조회 발동

        initControls();//1.initControls() 발동(2번포함,
        startconnection();//연결 스레드 발동
        db_select();//데이터 조회(point)

        /*
        Button back_button = (Button)findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_back = new Intent(Chat_page.this,Chatlist.class);
                go_back.putExtra("mate_img",right_imgr);
                go_back.putExtra("mate_name",you);
                startActivity(go_back);
                finish();
            }
        });
        */
        //if(left_human==null || right_human==null){

        //}
    }


    /*
    @Override
    protected void onPause() {
        super.onPause();

        //finish();
    }
        */
    public void onSave(){
        SharedPreferences sf = getSharedPreferences("onsave4", 0);
        SharedPreferences.Editor editor1 = sf.edit();
        editor1.putString("left_hm",left_human);//테그
        editor1.putString("left_sex",my_sex);
        editor1.putString("left_img",left_imgr);

        editor1.putString("right_hm",right_human);
        editor1.putString("right_sex",your_sex);
        editor1.putString("right_img",right_imgr);

        Log.v("제대로 저장한거마장?","-"+left_human+my_sex+left_imgr);
        editor1.commit();
    }

    public void onLoad(){
        SharedPreferences sf = getSharedPreferences("onsave4", 0);
        String b = sf.getString("left_hm","");//나
        String c = sf.getString("left_img","");
        String d = sf.getString("left_sex","");

        String e = sf.getString("right_hm","");//상대방
        String f = sf.getString("right_img","");
        String g = sf.getString("right_sex","");

        Log.v("제대로 불러온거마장?","-"+b+c+d+e+f+g);

        left_human = b;
        left_imgr = c;
        my_sex = d;

        right_human = e;
        right_imgr = f;
        your_sex = g;


        /*
        TextView meLabel = (TextView) findViewById(R.id.meLbl);//왼쪽 위 텍스트 뷰
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);//오른쪽 위 텍스트 뷰


        meLabel.setText(b);//왼쪽 위 셋팅
        companionLabel.setText(c);// Hard Coded(오른쪽 위 셋팅)

        //
        //왼쪽사진 시작
        ImageView left_iv = (ImageView)findViewById(R.id.left_img);
        Glide.with(Chat_page.this).load(server_url+left_imgr)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                //.thumbnail((float) 0.5)
                .into(left_iv);
        //왼쪽사진 끝

        //오른쪽 사진 시작
        ImageView right_iv = (ImageView)findViewById(R.id.right_img);
        Glide.with(Chat_page.this).load(server_url+right_imgr)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new CropCircleTransformation(mContext))
                //.thumbnail((float) 0.5)
                .into(right_iv);
        //오른쪽 사진 끝
        */
    }


    @Override
    public void onBackPressed() {
        Log.v("백버튼소환","!");
        super.onBackPressed();
        onStop();
        //finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        onSave();//chat contents save
        Intent go_back = new Intent(Chat_page.this,Chatlist.class);
        go_back.putExtra("mate_img",right_imgr);
        go_back.putExtra("mate_name",you);
        startActivity(go_back);
    }

    private void initControls() {
        messagesContainer = (ListView) findViewById(R.id.messagesContainer);//리스트 뷰 객체
        messageET = (EditText) findViewById(R.id.messageEdit);//에딧텍스트 객체
        sendBtn = (Button) findViewById(R.id.chatSendButton);//버튼 객체

        TextView meLabel = (TextView) findViewById(R.id.meLbl);//왼쪽 위 텍스트 뷰
        TextView companionLabel = (TextView) findViewById(R.id.friendLabel);//오른쪽 위 텍스트 뷰

        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);//전체화면 container

        meLabel.setText(me);//왼쪽 위 셋팅
        if(you!=null) {
            companionLabel.setText(you);// Hard Coded(오른쪽 위 셋팅)
        }

        loadDummyHistory();//2.찌꺼기 흔적을 부른다

        sendBtn.setOnClickListener(new View.OnClickListener() {//보내기 버튼을 클릭했을때
            @Override
            public void onClick(View v) {
                messageText = messageET.getText().toString();//에딧텍스트에서 String 뽑아서
                if (TextUtils.isEmpty(messageText)) {//String의 null을 체크하게 해주는 util.... TextUtils.isEmpty();, isEmpty()안에 인자값에 String을 넣으면 됨
                    return;//무언가를 return하면 되겠지
                }

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setId(122);//dummy
                chatMessage.setMessage(messageText);
                chatMessage.setDate(DateFormat.getDateTimeInstance().format(new Date()));
                chatMessage.setMe(true);

                messageET.setText("");

                displayMessage(chatMessage);
            }
        });
    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messagesContainer.setSelection(messagesContainer.getCount() - 1);
    }

    private void loadDummyHistory(){//2.찌꺼기 흔적 , 더미 메세지

        chatHistory = new ArrayList<ChatMessage>();

        /*
        ChatMessage msg = new ChatMessage();//메세지 객체 만들고
        msg.setId(1);//위치를 set한다
        msg.setMe(false);//나는 아니고 상대쪽
        //msg.setMessage("Hi");//Message를 Hi로 setting
        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));//날짜 셋팅인것같다
        chatHistory.add(msg);//chat을 관리하는 chatHistory에 위에걸 추가한다

        ChatMessage msg1 = new ChatMessage();
        msg1.setId(2);
        msg1.setMe(false);
        //msg1.setMessage("How r u doing???");
        msg1.setDate(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.add(msg1);
*/
        //위에꺼랑 같음

        adapter = new ChatAdapter(Chat_page.this, new ArrayList<ChatMessage>());
        messagesContainer.setAdapter(adapter);//messagesContainer는 리스트 뷰 이다.

        for(int i=0; i<chatHistory.size(); i++) {//List안에 있는 chatHistory의 갯수만큼 for문을 돌리고
            ChatMessage message = chatHistory.get(i);//Arraylist에 하나씩 위치를 받아서 meesage에 넣고
            displayMessage(message);//displayMessage()라는 메소드의 인자값으로 넣는다
        }
    }
    //network 연결 메소드
    public void startconnection(){
        chat_handler = new Handler();
        //여기에 핸들러 넣어야 될 수 도 있음
        class start_connection extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {}
            @Override
            protected String doInBackground(String... strings) {//(4)server ip랑 port 번호 9999넘겨줌
                try {
                    chatting_socket = new Socket(server_ip, 9999);//(5)접속하기위해서 넘겨줌, 접속에 성공한 것 까지 확인했고 (6)으로 넘어감
                    sender_name_setting ss = new sender_name_setting(chatting_socket,me);
                    chatting_sender_thread = new Thread(ss);
                    chatting_sender_thread.start();

                    receiver_setting rc = new receiver_setting(chatting_socket);
                    chatting_receive_thread = new Thread(rc);
                    chatting_receive_thread.start();

                    // chatting_sender_thread = new Thread(new client_sender(chat6_socket, client_name));//(6)client_sender를 객체화 한 값을. thread발동을 위해 인자값으로 넘겨줌
                    //chat6_sender.start();//(9)여기서 이제 start를 해주면 아래 client_sender의 run()이 발동한다.

                    //chat6_receiver = new Thread(new client_receiver(chat6_socket));
                    //q2`   chat6_receiver.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "nickname 넘기기 성공";
            }
            @Override
            protected void onPostExecute(String s) {
                Log.v("성공하면", "-" + s);
            }
        }
        start_connection task = new start_connection();//(1)class를 객체화 하고
        task.execute();//(2)시작
    }

    class sender_name_setting extends Thread{
        Socket socket_sender;
        String name_sender;

        DataOutputStream out_sender;

        sender_name_setting(Socket s,String name){
            this.socket_sender = s;//소켓
            this.name_sender = name;//이름

            try {//테그
                out_sender = new DataOutputStream(socket_sender.getOutputStream());
                out_sender.writeUTF(name_sender);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //테그
                /*
                try {
                    out_sender.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }

        @Override
        public void run() {//원래는 이름보내기 스레드인데 여기안에 메세지 보내주는 스레드도 같이 있음
            try {
                while(out_sender != null) {
                    // Log.v("out_sender안에 진입 상황","TES");//r계속들어와 있어서 그만해야됨
                    if (messageText!= null) {
                        Log.v("messageText가 빈것이 아니다?","TES");
                        out_sender.writeUTF(messageText);
                        Log.v("보낼 messageText","-"+messageText);
                        messageText=null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class receiver_setting extends Thread{
        Socket socket_receiver;
        DataInputStream input;

        public receiver_setting(Socket socket){
            try {
                this.socket_receiver = socket;
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String[] real_message;
        @Override
        public void run() {
            while (input != null) {
                try {
                    mesageReceive = input.readUTF();
                    Log.v("서버측에서 온 메세지 확인", "-" +  mesageReceive);
                    real_message = mesageReceive.split("\\|");
                    Log.v("(자른거)서버측에서 온 메세지 확인", "-" +  real_message[0]);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                chat_handler.post(new Runnable() {
                    @Override
                    public void run() {
                        /*
                        ChatMessage msg = new ChatMessage();//메세지 객체 만들고
                        msg.setId(999999);//위치를 set한다
                        msg.setMe(false);//나는 아니고 상대쪽
                        msg.setMessage(mesageReceive);//Message를 Hi로 setting
                        msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));//날짜 셋팅인것같다
                        chatHistory.add(msg);//chat을 관리하는 chatHistory에 위에걸 추가한다
                        displayMessage(msg);
                        */
                        if(real_message[0].equals(me)) {//쪽지 보낸사람이 나면 왼쪽
                            //내가 여자1호로 밝혀지면

                            ChatMessage msg = new ChatMessage();//메세지 객체 만들고
                            msg.setId(999999);//위치를 set한다
                            msg.setMe(false);//나는 아니고 상대쪽
                            msg.setMessage(real_message[1]);//Message를 Hi로 setting
                            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));//날짜 셋팅인것같다
                            chatHistory.add(msg);//chat을 관리하는 chatHistory에 위에걸 추가한다
                            //displayMessage(msg);//직업 adapter단에 알려주는..★★★★★무쟈게 중요한 메소드 이게 리스트뷰에 꽃히냐 안꽃히냐를 결정한다

                            String time = msg.getDate();
                            db_insert(me,real_message[1],time);//데이터 저장(point)
                            Log.v("여자로 나와?","?");
                        }else if(real_message[0].equals(you)){//다른사람이면 오른쪽으로 하자 그냥 else를 해도된다. you가 없어도된다는 뜻(아마도)
                            ChatMessage msg = new ChatMessage();//메세지 객체 만들고
                            msg.setId(999999);//위치를 set한다
                            msg.setMe(false);//나는 아니고 상대쪽
                            msg.setMessage(real_message[1]);//Message를 Hi로 setting
                            msg.setDate(DateFormat.getDateTimeInstance().format(new Date()));//날짜 셋팅인것같다
                            chatHistory.add(msg);//chat을 관리하는 chatHistory에 위에걸 추가한다
                            displayMessage(msg);//직업 adapter단에 알려주는..

                            other = real_message[0];//그 다른사람의 이름을 넣고
                            String time= msg.getDate();
                            db_insert(other,real_message[1],time);//데이터 저장(point)
                            Log.v("남자로 나와?","?");
                        }
                    }
                });

            }
        }
    }

    public void db_insert(String user,String msg,String time){
        db=dbhelper.getWritableDatabase();//db핼퍼로부터 쓰기 가능한 메소드를 호출
        ContentValues values = new ContentValues();
        //ContentValues는 자료를 데이터베이스에 입력하기 위해서 ContentValues 객체를 데이터베이스의 레코드와 동일하게 사용합니다. ContentValues 객체에 데이터베이스 테이블에 맞게 자료를 입력한 후, SQLiteDatabase 클래스의 insert()메소드를 사용하여 데이터베이스에 새로운 레코드를 추가합니다.

        values.put("user",user);
        values.put("message",msg);
        values.put("time",time);
        db.insert("chatting",null,values);//table name, null,data(null=디폴트)
    }

    public void db_select(){
        //db의 데이터를 읽어온다
        db = dbhelper.getReadableDatabase();//읽기전용 메소드 호출
        Cursor c = db.query("chatting",null,null,null,null,null,null);
        //Cursor에 대해 나도 커니의 안드로이드를 봤지만,마우스 커서처럼 레코드 하나,하나를 조회해주는 것이 커서 (마우스 커서라고 생각하면 편함)

        while(c.moveToNext()){//마우스 커서가 가르킬 다음 레코드값이 true일 경우 그러니까, true인 경우 계속 while문이 돌아감
            int _id = c.getInt(c.getColumnIndex("_id"));//php에서 autoincrease되는 거랑 똑같은 값이라고 보면 됨. 커서를 이용해서 끌고온다
            String user = c.getString(c.getColumnIndex("user"));
            String message = c.getString(c.getColumnIndex("message"));
            String time = c.getString(c.getColumnIndex("time"));

            if(user.equals(me)){
                ChatMessage msg = new ChatMessage();//메세지 객체 만들고
                msg.setId(999999);//위치를 set한다
                msg.setMe(true);//나는 아니고 상대쪽
                msg.setMessage(message);//Message를 Hi로 setting
                msg.setDate(time);
                displayMessage(msg);
                Log.v("내측 메세지 소환","!");
            }else{
                ChatMessage msg = new ChatMessage();//메세지 객체 만들고
                msg.setId(999999);//위치를 set한다
                msg.setMe(false);//나는 아니고 상대쪽
                msg.setMessage(message);//Message를 Hi로 setting
                msg.setDate(time);
                displayMessage(msg);
                Log.v("상대측 메세지 소환","!");
            }

            Log.v("로그에 찍힌 값 test","id"+_id+"  "+"user"+user+"  "+"message"+message);

        }
    }

    private void call_img(String json_for_img) {//step2.
        class call_img extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {}

            @Override
            protected String doInBackground(String... strings) {
                try {
                    String json_result = (String) strings[0];
                    String data = "json_final" + "=" + json_result;
                    Log.v("json결과","json결과"+data);
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_signup/user_img.php");//여기 수정
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

                try{
                    JSONObject j_o = new JSONObject(s);
                    left_imgr = j_o.getString("right_img");
                    right_imgr = j_o.getString("left_img");

                    Log.v("이미지 잘 왔나?","-"+left_imgr+right_imgr);


                    //왼쪽사진 시작
                    ImageView left_iv = (ImageView)findViewById(R.id.left_img);
                    Glide.with(Chat_page.this).load(server_url+left_imgr)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            //.thumbnail((float) 0.5)
                            .into(left_iv);
                    //왼쪽사진 끝

                    //오른쪽 사진 시작
                    ImageView right_iv = (ImageView)findViewById(R.id.right_img);
                    Glide.with(Chat_page.this).load(server_url+right_imgr)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .bitmapTransform(new CropCircleTransformation(mContext))
                            //.thumbnail((float) 0.5)
                            .into(right_iv);
                    //오른쪽 사진 끝


                } catch(JSONException e){e.printStackTrace();}
            }
        }
        call_img task = new call_img();
        task.execute(json_for_img);
    }
}//맨 윗단 클래스 끝

