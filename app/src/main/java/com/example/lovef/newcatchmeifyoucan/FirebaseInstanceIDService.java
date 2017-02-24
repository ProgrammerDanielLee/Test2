package com.example.lovef.newcatchmeifyoucan;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by lovef on 2016-12-02.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    SharedPreferences sp;
    SharedPreferences.Editor speditor;

    //firebaseInstanceIdservice를 확장하면, 토큰의 값에 엑세스 할 수 있습니다. 매니페스트에  서비스 추가 후 아래오 ㅏ같이
    //onTokenrefresh의 컨텍스트(함수)에서 .gettoken()을 호출하고, 로그에 기록해본다
    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();//토큰값 get!
        Log.d("refreshed","refreshed token : "+ token);
        //register token을 database에 저장해야한다
        registerToken(token);//토큰을 등록하는 함수 제작
    }

    private void registerToken(String token){
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token)
                .build();

        SharedPreferences pref = getSharedPreferences("token", 0);//UI의 상태를 저장합니다
        SharedPreferences.Editor editor = pref.edit();//에디터 소환

        //String save_token;
        editor.putString("save_token",token);
        Log.v("service단에서 저장할때",token);

        editor.commit();


        Request request= new Request.Builder()
                .post(body)
                .url("")
                .build();
        /*
        Request request= new Request.Builder()
                .url("http://cmic.dothome.co.kr/user_login/user_login.php")
                .post(body)
                .build();
        원래는 위에꺼였음/확인결과 정상적으로 잘~작동함!
        * */

        /*
        Log.v("토큰보내기","레지스터토큰 : "+ body);
        try {
            client.newCall(request).execute();
        }catch(IOException e){
            e.printStackTrace();
        }
        */
    }
}
