package com.example.lovef.newcatchmeifyoucan;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.google.firebase.messaging.RemoteMessage;
/**
 * Created by lovef on 2016-12-02.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    //선택 - 2,3단계 시작
    String select_mode, select_depth, select_title, select_message, select_human, selected_human, select_left_human, select_right_human, select_left_human_img, select_right_human_img;
    //선택 - 2,3단계 끝

    String sex;
    Intent i;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //fcm으로 메시지를 받고 유저에게 알려준다 노티피케이션을 통해 이 함수를 재정의 하면, 수신된 메세지에 따라 작업을 수행하고 메세지 데이터를 가져올 수 있습니다
        Log.v("맨처음받은msg","-"+remoteMessage);
        //선택이면서, 2단계 혹은 3단계인 경우에는 이곳으로 변수가 들어온다 시작
        select_mode = remoteMessage.getData().get("mode");//선택인지, 결정인지
        select_depth = remoteMessage.getData().get("depth");//몇단계인지
        select_title = remoteMessage.getData().get("title");//제목은 뭔지
        select_message = remoteMessage.getData().get("message");//메세지는 뭘로 할지
        select_human = remoteMessage.getData().get("select_human");//선택한 사람
        selected_human = remoteMessage.getData().get("selected_human");//왼쪽/오른쪽 중 선택된 사람 - ★선택된 사람이 없지 바보야....내가 선택해야되는데...
        //아니지, 결과가 있을땐 결과를 받아야지!
        select_left_human = remoteMessage.getData().get("left_human");//왼쪽 사람
        select_right_human = remoteMessage.getData().get("right_human");//오른쪽 사람
        select_left_human_img = remoteMessage.getData().get("left_human_img");
        select_right_human_img = remoteMessage.getData().get("right_human_img");

        sex = remoteMessage.getData().get("sex");

        Log.v("MSG service(값 확인)", "-" + select_mode + select_depth + select_human + selected_human + select_left_human + select_right_human);
        Log.v("MSG service(이미지 확인)", "-" + select_left_human_img + select_right_human_img);
        //선택이면서, 2단계혹은 3단계인 경우에는 이곳으로 변수가 들어온다 끝

        if(select_mode.equals("쪽지")){
            i=new Intent(this,Chat_page.class);
            //받는쪽은 바꿔서......
            i.putExtra("chat_selected_num",select_human);//여자 165호
            i.putExtra("chat_select_num",selected_human);//남자...
            i.putExtra("chat_select_sex",sex);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)//notification을 누르면 자동으로 닫히도록 처리
                    .setContentTitle(select_title)//메세지 제목
                    .setContentText(select_message)//메세지 내용
                    .setSmallIcon(R.drawable.d_message)//메세지 그림
                    .setContentIntent(pendingIntent);//여기에 pending intent를 setting한다(notification을 누르면 처리되는 intent이다!!!)
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }


        if (select_mode.equals("선택")) {//2단계든 3단계든, 선택은 여기서 변수를 뽑아서 일단 class로 보내준다
            i = new Intent(this, Nationwidematching_page_my_choice.class);//corepage로 넘겨주면 알아서 리스트 뷰 뽑아오니까 php로 부터 정보만 받아서 뿌려주기 한번 해보자
            i.putExtra("matching_result_mode", select_mode);//선택이고
            i.putExtra("matching_result_depth", select_depth);//여기에 몇번째 단계인지 push_notification2.php로부터 받았지,그걸 위에서 또 변수에 넣어주었지
            i.putExtra("matching_result_s", select_human);//선택한 사람
            i.putExtra("matching_result_left", select_left_human);//왼쪽 사람
            i.putExtra("matching_result_right", select_right_human);//오른쪽 사람
            i.putExtra("matching_result_left_img", select_left_human_img);//왼쪽 사람 이미지
            i.putExtra("matching_result_right_img", select_right_human_img);//오른쪽 사람 이미지
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)//notification을 누르면 자동으로 닫히도록 처리
                    .setContentTitle(select_title)//메세지 제목
                    .setContentText(select_message)//메세지 내용
                    .setSmallIcon(R.drawable.nw_page_noti)//메세지 그림
                    .setContentIntent(pendingIntent);//여기에 pending intent를 setting한다(notification을 누르면 처리되는 intent이다!!!)
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

        } else if (select_mode.equals("결과")) {//1단계든,2단계든,3단계든 결정은 여기서 변수를 뽑아서 일단 class로 넘겨준다
            i = new Intent(this, Nationwidematching_page_others_choice.class);//corepage로 넘겨주면 알아서 리스트 뷰 뽑아오니까 php로 부터 정보만 받아서 뿌려주기 한번 해보자
            i.putExtra("matching_result_mode", select_mode);//결과이고
            i.putExtra("matching_result_depth", select_depth);//여기에 몇번째 단계인지 push_notification2.php로부터 받았지,그걸 위에서 또 변수에 넣어주었지
            i.putExtra("matching_result_s", select_human);//선택한 사람
            i.putExtra("matching_result_st", selected_human);//선택 당한 사람
            i.putExtra("matching_result_left", select_left_human);//왼쪽 사람
            i.putExtra("matching_result_right", select_right_human);//오른쪽 사람
            i.putExtra("matching_result_left_img", select_left_human_img);//왼쪽 사람 이미지
            i.putExtra("matching_result_right_img", select_right_human_img);//오른쪽 사람 이미지
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)//notification을 누르면 자동으로 닫히도록 처리
                    .setContentTitle(select_title)//메세지 제목
                    .setContentText(select_message)//메세지 내용
                    .setSmallIcon(R.drawable.nw_page_noti)//메세지 그림
                    .setContentIntent(pendingIntent);//여기에 pending intent를 setting한다(notification을 누르면 처리되는 intent이다!!!)
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
    }
}