package com.example.lovef.newcatchmeifyoucan;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
//https://github.com/suwa-yuki/GoogleMapMarkerSample/blob/master/GoogleMapMarkerSample/src/jp/classmethod/android/sample/googlemapmarker/MainActivity.java

/**
 * Created by lovef on 2016-11-16.
 */
public class Roommatching_page extends FragmentActivity implements OnMapReadyCallback {
    //아래 값들은 계속해서 공유되며, insert되기도하고 update되기도 한다
    //처음 접속했을때 서버에서 불러올 변수 모음 시작
    private String get_json;
    private String user_sex;//그사람의 성별
    private String user_num;//그사람의 번호
    private double user_room_lat;
    private double user_room_long;
    private Integer user_lovebean;//그사람의 사랑콩
    private Integer user_lovelevel;//그사람의 사랑지수
    private Integer user_room_radius;//반지름
    //private Integer user_radius_value = 600;
    //처음 접속했을때 서버에서 불러올 변수 모음 끝

    //서버에서 불러올 값이 없을경우 넣을 Json 값 시작
    private String first_insert_room_information;
    private String update_json_room_information_for_moving;
    private String update_json_room_information_for_lovebean;

    private String update_json_room_information_for_lovelevel;
    private String update_json_room_information_for_radius;
    //서버에서 불러올 값이 없을경우 넣을 Json 값 끝

    //위도 경도 저장을 위한 변수 시작
    private LatLng room_matching_page_start_location_latlng;
    private LatLng room_matching_page_user_location_latlng;
    //private LatLng test_room_matching_page_start_location_latlng;
    //위도 경도 저장을 위한 변수 끝

    //마커 시작
    private Marker my_marker;
    private MarkerOptions my_marker_option;

    private Marker othres_marker;
    private MarkerOptions others_marker_option;
    //아래는 test...
    private Marker girl_Marker;
    private MarkerOptions girl_marker;
    //마커 끝


    //드래그를 위한 변수 시작
    private Integer check_first_drag = 0;//맨처음 드래그 한것인지, 사랑콩을 써서 드래그한 것인지 알아보기 위한 변수
    //드래그를 위한 변수 끝

    //원을 위한 변수 시작
    private CircleOptions circleOptions;
    private Circle map_circle;
    //원을 위한 변수 끝

    //스니펫을 위한 변수 시작
    private ContactsContract.SearchSnippets roommatching_snippet;
    //스니펫을 위한 변수 끝

    //이미지를 위한 변수 선언 시작
    private String server_ip = "http://cmic.dothome.co.kr/user_signup/uploads/";
    private String img_name = "test_man2.png";
    private Context mContext;
    //이미지를 위한 변수 선언 끝

    //버튼 위한 변수 선언 시작
    private LinearLayout blue_buttons;
    private Button minihome_enter_button;
    private Button minihome_preference;
    //버튼 위한 변수 선언 끝

    //love status를 위한 변수선언 시작
    private LinearLayout room_matching_page_love_status_frame;
    private TextView room_matching_page_user_redius;
    private TextView room_matching_page_love_level;
    private TextView room_matching_page_love_bean;
    private Button room_matching_page_super_room;
    private Button room_matching_page_loyal_room;
    private Button room_matching_page_lovelove_moving;
    private TextView room_matching_page_textview_explain;
    //private ImageView room_matching_page_explain_bug;
    //love status를 위한 변수선언 끝

    //현재 객체들이 켜져있는지 꺼져있는지 확인하기 위한 변수들 시작
    private Boolean check_frame = false;//기본적으로 false
    private Boolean check_circle = false;//기본적으로 false
    private Boolean check_snippet = false;
    //현재 객체들이 켜져있는지 꺼져있는지 확인하기 위한 변수들 끝

    //맵의 현재 ROOM상황을 파악하기 위한 변수 시작(미니홈피와 Room을 생성했는지 안했는지)
    private Boolean room_tutorial_status;
    private Boolean room_confirm_status=false;//미니홈피+룸을 결정했는지 안했는지
    //맵의 현재 ROOM상황을 파악하기 위한 변수 끝

    //콩들을 서버에 반영하고 나서 스킬을 발동하기 위한 boolean 시작
    private Boolean ready_superoom;
    private Boolean loyal_superoom;
    private Boolean lovemovinghouse_superoom;

    GoogleMap googleMap;
    CameraUpdate zoom;
    Bitmap bt_1;

    //다른마커들을 넣기 위한 list 시작
    //List<Marker> markers;
    Map<String, String>markers;
    //다른 마커들을 넣기 위한 list 끝
    //mapFragment;

    private void Insert_json_to_variable() {
        if (get_json != null) {
            try {
                JSONObject j_o = new JSONObject(get_json);
                img_name = j_o.getString("img");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roommatching_page);
        //step0.저번 main화면으로부터 들어온 회원번호 getIntent-!테그
        user_num = getIntent().getStringExtra("user_num");
        user_sex = getIntent().getStringExtra("user_sex");
        get_json = getIntent().getStringExtra("login_json");
        Log.v("user_num이 넘어왔는지", "-" + user_num + user_sex);
        //step0-1.저번 main 화면으로부터 넘어온 이미지 name역시 담아둡니다
        Insert_json_to_variable();
        Log.v("img_name이 넘어왔는지", "-" +img_name);

        //step1.우선 그사람의 위도,경도를 조회한다(이건 절대적으로 서버에 저장해야됨)


        //GoogleMapOptions options = new GoogleMapOptions().liteMode(true);
        //Googlemap에 mapfragment를 담는다

        //love status 객체화 시작
        love_status_setting();
        //love status 객체화 끝
        SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.roommatching_page_map);
        mapFragment.getMapAsync(this);
    }

    private void map_camera_setting(){
        googleMap.getUiSettings().setZoomGesturesEnabled(true);//손가락 두개를 벌려서 지도의 zoom을 당기는 제스쳐
        googleMap.getUiSettings().setScrollGesturesEnabled(true);//스크롤 제스쳐 - 지도 움직이는거
        googleMap.getUiSettings().setTiltGesturesEnabled(true);//틸트 제스처 - 화면 눕히는 제스처
        googleMap.getUiSettings().setRotateGesturesEnabled(true);//회전이 가능하게끔 - 손가락 두개로 화면 돌리는 제스쳐
        googleMap.getUiSettings().setZoomControlsEnabled(true);//Zoom을 컨트롤 할 수 있게끔 setting, + - 버튼이 그것임
        googleMap.setInfoWindowAdapter(new CustomInfoAdapter());//스니펫 어뎁터 꽂기
        zoom = CameraUpdateFactory.zoomTo(14);//zoom level은 1~23자까지 있다.14레벨이 적절함...
        googleMap.animateCamera(zoom);//카메라 줌 떙기기 끝
    }
    @Override
    public void onMapReady(final GoogleMap google_Map) {
        //구글맵 기본 환경 setting 시작
        //default_map_setting(googleMap);

        googleMap = google_Map;//googlemap을 메소드 밖에서도 호출하게 해준 한 줄, 이걸로 인해서 카메라 setting도 밖에서 할 수 있게 되었음

        check_user_room_information(user_num);
        //사용자 정보 불러오는 곳...정보가 없으면 튜토리얼 시작

        if(map_circle!=null){
            map_circle.remove();
        }

        //마커 클릭 이벤트 처리
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
                    googleMap.animateCamera(center);//마커 클릭시 중앙으로 이동
                    //Toast.makeText(getApplicationContext(), marker.getTitle() + " 클릭했음", Toast.LENGTH_SHORT).show();

                    //원 생성 시작

                    //원 생성 끝

                    //똑닥이 버튼 전체 시작
                    //혹시나 이것을 볼 미래의 나에게 이야기 하지만 , 똑딱이 버튼은 메소드로 쓸 필요가 없다 어짜피 한번밖에 못쓰지 않나?
                    //그러니 똑딱이 버튼과, 켜진것을 확인하여 끄는 메소드를 햇갈리지 말기를 바란다...

                    //나중에 룸 생성/해제 살리고 싶으면 이걸 만지면 된다
                    //원 체크 여부 시작
                    /*
                    if (!check_circle) {//클릭을 했을때에 원이 false라면(원이 생성되어있지 않다면),원을 추가한다. 똑딱이 버튼처럼 사용할 것
                        check_circle = true;
                        //원테그
                        map_circle = googleMap.addCircle(circleOptions);
                        Toast.makeText(getApplicationContext(), "Room을 생성합니다", Toast.LENGTH_SHORT).show();
                        Log.v("원의 존재를 체크해보자", "미존재");
                    } else {//만약에 원이 있으면!
                        check_circle = false;
                        map_circle.remove();//원을 지운다
                        Toast.makeText(getApplicationContext(), "Room을 제거합니다", Toast.LENGTH_SHORT).show();
                        Log.v("원의 존재를 체크해보자", "존재");
                    }
                    */
                    //원 체크 여부 끝

                    //버튼들 체크여부 시작(주의해야 할 것은 이것은 마커를 클릭했을때 라는 것)
                    if( my_marker.equals(marker)) {
                        if (!check_frame) {//status frame이 꺼져있으면
                            room_matching_page_love_status_frame.setVisibility(View.VISIBLE);//보이게금
                            check_frame = true;//버튼 켜주고

                        } else {
                            room_matching_page_love_status_frame.setVisibility(View.INVISIBLE);
                            check_frame = false;
                        }
                    }else{//내 마커가 아니면
                        if(check_frame){
                            room_matching_page_love_status_frame.setVisibility(View.INVISIBLE);
                            check_frame = false;
                        }
                    }
                    //버튼들 체크여부 끝(주의해야 할 것은 이것은 마커를 클릭했을때 라는 것)

                    //스니펫 체크여부 시작
                    /*
                    if (!check_snippet) {//꺼져있으면
                        //googleMap.sewindow
                        marker.showInfoWindow();//키고
                    } else {//켜져있으면
                        marker.hideInfoWindow();//끄고
                    }
                    */

                    //스니펫 체크 여부 끝
                    return false;
                    //똑닥이 버튼 전체 끝
                }
            });
        //마커 클릭 이벤트처리 끝

        //테그
        //스니펫 클릭 이벤트 시작(스니펫이 없으면 아예 클릭할 수가 없을테니 그것에 대한 예외처리는 안해도 됨......
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(getApplicationContext(), marker.getSnippet() + " 클릭했음", Toast.LENGTH_SHORT).show();
                //return false;
                //Log.v("markerid","-"+marker.getId()+marker.getPosition()+marker.getTitle());
                //for(int i=0; i<markers.size();i++){
                    //Log.v("markersssid","-"+markers.get(i));
                    //if(marker.getId()==markers.get(marker.getId())){
                        Log.v("test1", "-" + marker.getTitle() + marker.getId()+marker.getSnippet());
                        Intent go_minihome = new Intent(Roommatching_page.this, Room_matching_page_minihome.class);

                        if(markers.get(marker.getId())==null){
                                go_minihome.putExtra("user_num",user_num);
                                go_minihome.putExtra("user_sex",user_sex);
                        }else{
                            //다른사람 미니홈피 들어갔을때
                                 go_minihome.putExtra("user_num",markers.get(marker.getId()));
                               // if(user_sex=="남자"){user_sex="여자";}else{user_sex="남자";}
                                go_minihome.putExtra("other_num",user_num);
                                go_minihome.putExtra("other_sex",user_sex);
                            //다른사람 미니홈피 들어갔을때 끝
                        }
                            Log.v("!!!!!","!!!!"+markers.get(marker.getId()));
                            //markers가 맵으로 되어있으니 , key값을 넣어주면 그에 상응하는 값을 뱉어준다
                            overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);

                            startActivity(go_minihome);


                  //  }else{
                        /*
                        Log.v("test2", "-" + marker.getTitle() + marker.getId()+marker.getSnippet());
                        Intent go_minihome = new Intent(Roommatching_page.this, Room_matching_page_minihome.class);
                        go_minihome.putExtra("user_num", user_num);
                        go_minihome.putExtra("user_sex", user_sex);
                        //번호나 여러가지를 넘겨줘야겠지
                        startActivity(go_minihome);
                        */
                      //  }
               // }
            }
        });
        //스니펫 이벤트 클릭 끝

        //맵 클릭 이벤트 시작
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {//맵을 클릭했을때
                //frame+circle+snippet 켜져있을때 끄는 메소드 시작
                turn_off_objects();
                //frame+circle+snippet 켜져있을때 끄는 메소드 끝
                //테그
                //othres_marker.remove();
            }
        });
        //맵 클릭 이벤트 끝

        //마커 드래그(미니홈피 드래그) 이벤트 시작
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {//드래그를 클릭하면 이게 호출된다
                //처음일때와 처음이 아닐때(사랑콩을 가지고 수정했을때)를 구별해야함

                //방의 확정이 지정되지 않았을때 드래그가 가능하게끔 시작
                if (room_confirm_status.equals(false)) {
                    if (check_first_drag.equals(0)) {//처음 미니홈피 설치할 때
                        turn_off_objects();
                        Toast.makeText(getApplicationContext(), "미니홈피 설치를 시작합니다", Toast.LENGTH_SHORT).show();
                        //frame+circle+snippet 켜져있을때 끄는 메소드 시작
                        if(map_circle!=null){
                            map_circle.remove();
                        }
                        //frame+circle+snippet 켜져있을때 끄는 메소드 끝
                    } else {//두번째로(혹은 그 이상으로) 미니홈피를 설치할 때
                        turn_off_objects();
                        Toast.makeText(getApplicationContext(), "미니홈피 이사를 시작합니다", Toast.LENGTH_SHORT).show();
                        //frame+circle+snippet 켜져있을때 끄는 메소드 시작
                        if(map_circle!=null){
                            map_circle.remove();
                        }
                        //frame+circle+snippet 켜져있을때 끄는 메소드 끝
                    }
                    marker.setAlpha(0.3f);
                } else {
                    Toast.makeText(getApplicationContext(), "미니홈피를 다시 이사하려면 사랑콩을 사용해야 합니다!", Toast.LENGTH_SHORT).show();
                }
                //방의 확정이 지정되지 않았을때 드래그가 가능하게끔 끝
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Toast.makeText(getApplicationContext(),"마커 이동중..", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //카메라 셋팅 , 중앙으로 시작
                CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
                googleMap.animateCamera(center);
                //카메라 셋팅, 중앙으로 끝

                //check_circle = false;//원 체크 해제
                //check_frame = false;//Linear layout 체크 해제

                //마커 드래그 된 이후 위도 경도 분리하는 법 알아보기 -> 굳이 알아볼 필요 없네....같이 저장해도 되잖아 멍충아!
                //->다시 아니지,,얻은 마커 값을 db에 저장하려면...
                room_matching_page_start_location_latlng = marker.getPosition();//위도 경도 get
                Log.v("새로바뀐 마커 position", "-" + room_matching_page_start_location_latlng);//여기서 새로 얻은 마커분리하고 setting 해야할 듯...
                //위,경도 분리해서 구하는 방법 1
                //user_room_lat = googleMap.getMyLocation().getLatitude();//헐 이런방법이...
                //user_room_long = googleMap.getMyLocation().getLongitude();//대박..신기방기..

                //위,경도 분리해서 구하는 방법 2
                user_room_lat = room_matching_page_start_location_latlng.latitude;
                user_room_long = room_matching_page_start_location_latlng.longitude;
                Log.v("새로바뀐 마커 위,경도", "-" + user_room_lat + "," + user_room_long);

                //0.우선, 위도 경도를 따로 저장해서 범위를 검색하는 sql을 하는지 알아보기....-ok
                //1.마커 분리부터 하자!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! - ok
                //2.마커 분리가 끝나고 위도,경도를 얻었으면 그 위도 경도를 update_json에서 json으로 감싸준 후에 서버로 보내야 한다~!!! -ok
                //3.서버에서는 update받아서 db에 넣으면 되는거고~! - ok
                //4,주의 할 것은.최초에는 레벨/사랑콩/위도/경도/원의 둘레 전부다 전송하지만, 이사 할때에는 위도/경도만 보낸다는거~ -ok

                //4.5 - 자신의 범위 안에 있는 이성들 marker로 찍기~ 이거 하기전에 5번이 재밌을거같으니 일단이거나 하자~
                //5.update가 끝나면 슈퍼룸,로얄룸,러브러브 무빙을 손보면 될 것 같다~
                //6.그다음 미니홈피 꾸미기해서, 레벨과 연계되게끔 해보자...(어휴 힘들다)

                //다이얼로그 띄우기 시작
                confirm_my_room("알림", "미니홈피의 지정을 확정하시겠습니까? \n\n\n *미니홈피의 위치를 수정하시기를 원하실 경우 상점에서 사랑콩을 구매하셔야 합니다");
                //다이얼로그 띄우기 끝
            }
        });
        //마커 드래그(미니홈피 드래그) 이벤트 끝

        //슈퍼룸 스킬 시작
        room_matching_page_super_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_lovebean < 2) {
                    Toast.makeText(getApplicationContext(), "러브러브콩의 개수가 부족합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    update_lovebean("superroom");
                    set_super_room();
                }

            }
        });
        //슈퍼룸 스킬 끝

        //로얄룸 스킬 시작
        room_matching_page_loyal_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로얄룸 뜯어 고치기 시작
                if (user_lovebean < 5) {
                    Toast.makeText(getApplicationContext(), "러브러브콩의 개수가 부족합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    update_lovebean("loyalroom");
                    set_loyal_room();
                }
                //로얄룸 뜯어 고치기 끝
            }
        });
        //로얄룸 스킬 끝

        //러브러브 이사 스킬 시작
        room_matching_page_lovelove_moving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //러브러브 무빙 뜯어 고치기 시작
                if (user_lovebean < 10) {
                    Toast.makeText(getApplicationContext(), "러브러브콩의 개수가 부족합니다!", Toast.LENGTH_SHORT).show();
                } else {
                    update_lovebean("lovemovinghouse");
                    set_moving_room();
                }
                //러브러브 무빙 뜯어 고치기 시작
            }
        });
        //러브러브 이사 스킬 끝

        //설명충 시작
        room_matching_page_textview_explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_explain_bug = new Intent(Roommatching_page.this, Room_matcihng_explain_bug_page.class);
                startActivity(go_explain_bug);
            }
        });
        //설명충 끝
        //map_circle.get

    }
    //현재의 사원번호와 lovebean의 갯수를 합쳐서 json으로 만드는 메소드 시작
    private void get_lovebean_to_json(){
        JSONObject j_o = new JSONObject();
        try {
            j_o.put("user_num_update_love_bean", user_num);
            j_o.put("user_bean_num_update_love_bean", user_lovebean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        update_json_room_information_for_lovebean = j_o.toString();
    }
    //현재의 사원번호와 lovebean의 갯수를 합쳐서 json으로 만드는 메소드 끝

    //합쳐진 json을 서버로 보내는 ansync tesk 시작
    private void update_lovebean_to_server(String bean_num) {
        class update_lovebean_to_server extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String user_bean_num = (String) params[0];
                    Log.v("doInbackground 진입", "-" + user_bean_num);
                    String data = "update_user_bean_num" + "=" + user_bean_num;
                    //step2.user의 room matching에 관한 정보를 조회 후 불러오기
                    URL my_server = new URL("http://cmic.dothome.co.kr/new_cmic/user_room_matching/update_user_love_bean.php");
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
                    return sb.toString();//넘어온 json을 조회한다
                } catch (Exception e) {
                    return new String("exeption :" + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("넘어온 user_update_bean", "-" + s);
                //위도,경도,사랑콩,사랑지수
                //테그 : 혹시나 ready_superoom을 여기서 체크안하고 바로 발동한게 문제가 될수도 있어서 테그 남김

                Toast.makeText(getApplicationContext(),"스킬이 발동되었습니다", Toast.LENGTH_SHORT).show();

                /*
                ready_superoom=true;//true면
                if (ready_superoom) {//set_super_room() 메소드 시작하는 걸로...

                }*/
                //돌아온 정보는 super room인지 loyal룸인지 모르니까 취소....
            }
        }
        update_lovebean_to_server task = new update_lovebean_to_server();
        task.execute(bean_num);
    }
    //합쳐진 json을 서버로 보내는 ansync tesk 끝

    //스킬 이름을 인자값으로 하여 lovebean을 감소시키는 메소드, 그리고 그 결과를 서버단에 전송함. 시작
    private void update_lovebean(String skillname) {
        if (skillname.equals("superroom")) {
            user_lovebean = user_lovebean - 1;//슈퍼룸이라고 해서 -1이 차감된 lovebean
            room_matching_page_love_bean.setText(user_lovebean.toString());//먼저 반영하고
            //lovebean update하는 메소드 제작
            get_lovebean_to_json();//최종적으로 여기서 얻는건 update_json_room_information_or_lovebean이라는 String변수
            update_lovebean_to_server(update_json_room_information_for_lovebean);//서버단에 올린다
        } else if (skillname.equals("loyalroom")) {
            user_lovebean = user_lovebean - 5;
            room_matching_page_love_bean.setText(user_lovebean.toString());//먼저 반영하고
            //lovebean update하는 메소드 제작
            get_lovebean_to_json();//최종적으로 여기서 얻는건 update_json_room_information_or_lovebean이라는 String변수
            update_lovebean_to_server(update_json_room_information_for_lovebean);//서버단에 올린다
        } else if (skillname.equals("lovemovinghouse")) {
            user_lovebean = user_lovebean - 10;
            room_matching_page_love_bean.setText(user_lovebean.toString());
            //lovebean update하는 메소드 제작
            get_lovebean_to_json();//최종적으로 여기서 얻는건 update_json_room_information_or_lovebean이라는 String변수
            update_lovebean_to_server(update_json_room_information_for_lovebean);//서버단에 올린다
        }
    }
    //스킬 이름을 인자값으로 하여 lovebean을 감소시키는 메소드, 그리고 그 결과를 서버단에 전송함. 끝

    //슈퍼룸 setting 시작
    private void set_super_room() {
        //지금 기본 radius에 *2를 할것..
        Integer value = (user_room_radius) * 2;
        //테그 : 원의 반경을 넓히는 부분과, 실제로 status부분에 넓어진 반경을 표시하는 부분을 thread를 써서 표시해주어야 한다....
        //스레드로 시간초 만들기
        map_circle.setRadius(value);//스레드로 이것도 조작...
        room_matching_page_user_redius.setText(value.toString());//스레드로 표기되어있는 숫자 조작...
    }
    //슈퍼룸 setting 끝

    //로얄룸 setting 시작
    private void set_loyal_room(){
        Integer value = (user_room_radius) * 3;
        map_circle.setRadius(value);
        room_matching_page_user_redius.setText(value.toString());
    }
    //로얄룸 setting 끝

    //러브러브 무빙 setting 시작
    private void set_moving_room(){
        my_marker.setDraggable(true);//마커를 setdraggle할 수 있게 만들고
        room_tutorial_status = false;//room tutorial의 상태를 false로 만든다
    }
    //러브러브 무빙 setting 끝

    //구글맵 기본 환경 끝

    private void changeSelectedMarker(Marker marker) {//이 메소드는 박상권의 삽질블로그를 보면 해답이 나옴
    }

    //오 이런식으로 setupmap도 좋은듯참고하기
            /*
            private void setUpMap() {

                // マーカーを貼る緯度・経度
                LatLng location = new LatLng(35.697261, 139.774728);

                // マーカーの設定
                MarkerOptions options = new MarkerOptions();
                options.position(location);
                options.title("クラスメソッド株式会社");
                options.snippet(location.toString());
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_logo);
                options.icon(icon);

                // マップにマーカーを追加
                mMarker = mMap.addMarker(options);

        //        mMap.setInfoWindowAdapter(new CustomInfoAdapter()); ★바로 이부분!중요한 부분!!
            }*/
    //URL을 비트맵파일로 바꾸어주는 메소드 시작
    public Bitmap bitmapFromurl(final String sUrl){
        final Bitmap[] bitmap = new Bitmap[1];

        Thread mThread = new Thread(){
            @Override
            public void run() {
                try{
                    URL url= new URL(sUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);
                }catch (IOException e){e.printStackTrace();}
            }
        };
        mThread.start();

        try {
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bt_1 = bitmap[0];
    }
    //URL을 비트맵 파일로 바꾸어 주는 메소드 끝

    //비트맵을 이미지 뷰에 적용해주는 메소드 시작
    public void intoImageView(ImageView iv,Bitmap bitmap){
        if(bitmap!=null){
            iv.setImageBitmap(bitmap);
        }else{

        }
    }
    //비트맵을 이미지 뷰에 적용해주느 메소드 끝


    //구글맵 snipet 커스텀 어뎁터 시작
    private class CustomInfoAdapter implements GoogleMap.InfoWindowAdapter {

        /**
         * Window の View.
         */
        private final View mWindow;

        /**
         * コンストラクタ.
         */
        public CustomInfoAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.roommatching_custom_info_page, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        /**
         * InfoWindow を表示する.
         *
         * @param marker {@link Marker}
         * @param view   {@link View}
         */
        private void render(Marker marker, View view) {
            // ここでどの Marker がタップされたか判別する
            if (marker.equals(my_marker)) {
                // 画像
                //ImageView badge = (ImageView) view.findViewById(R.id.rommatching_page_imageview_badge);
                //bitmapFromurl(server_ip+img_name);//url을 bitmap으로 바꾸어주는...img_name을 배열로 setting하면 될 듯 한데
                //intoImageView(badge,bt_1);
            }else{

            }

            TextView title = (TextView) view.findViewById(R.id.roommatching_page_textview_title);
            TextView snippet = (TextView) view.findViewById(R.id.roommatching_page_textview_snippet);
            title.setText(marker.getTitle());
            snippet.setText(marker.getSnippet());
        }
    }
    //구글맵 snipet 커스텀 어뎁터 끝

    //원만들기 시작
    //디자인5-6.원
    private void make_circle() {
      Log.v("make_circle진입","!");
        LatLng test_latlng = new LatLng(user_room_lat,user_room_long);
        if(user_sex.equals("남자")){
            Log.v("남자진입","!");
            circleOptions = new CircleOptions()
                    .strokeWidth(5)//선굵기
                    .strokeColor(Color.TRANSPARENT)//선의 색깔
                    .fillColor(Color.parseColor("#80ADD7FF"))//투명도 조절
                    .center(test_latlng)
                    .radius(user_room_radius);//둘레도....
            map_circle = googleMap.addCircle(circleOptions);
        }else{
            Log.v("여자진입","!");
            circleOptions = new CircleOptions()
                    .strokeWidth(5)//선굵기
                    .strokeColor(Color.TRANSPARENT)//선의 색깔
                    .fillColor(Color.parseColor("#80EABFE5"))//투명도 조절
                    .center(test_latlng)
                    .radius(user_room_radius);//둘레도....
            map_circle = googleMap.addCircle(circleOptions);
        }

       // }
        //.visible(false);
        //일단 기본적으로 안보이는걸로,.
    }
    //원만들기 끝

    //처음 화면 등장시, 회원의 room db에 접근하여 정보조회하는 메소드 시작
    private void check_user_room_information(String user_num_thread) {
        class roommatching_page_select_lt extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String user_num_select = (String) params[0];
                    Log.v("doInbackground 진입", "-" + user_num_select);
                    String data = "check_user_room_information" + "=" + user_num_select;
                    //step2.user의 room matching에 관한 정보를 조회 후 불러오기
                    URL my_server = new URL("http://cmic.dothome.co.kr/new_cmic/user_room_matching/check_user_room_information.php");
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
                    return sb.toString();//넘어온 json을 조회한다
                } catch (Exception e) {
                    return new String("exeption :" + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("넘어온 user_room_info", "-" + s);
                //위도,경도,사랑콩,사랑지수
                if(s.equals(null) || s.equals("") || s.equals(" ")){
                    //step3.만약에 그사람의 위도,경도가 없으면 튜토리얼 시작
                    //step4.'예','아니오'둘다 존재하는 메소드 시작
                    //왜 자꾸 위도랑 경도가 없는걸로 나오는...
                    //테스트 마커
                    Log.v("첫번째로 들어온거야?", "!");
                    //test_room_matching_page_start_location_latlng = new LatLng(37.553333,126.97);
                    roommatching_page_message("알림", "아직 미니홈피를 만들지 않으셨습니다.\n미니홈피를 생성하시겠습니까?\n");
                    //룸의 처음 시작위치가 null값이 아니라면, 자세히 말하면 db값에 온전히 처음 값들이 전부 들어갔다고 한다면...시작
                    // if(room_matching_page_start_location_latlng != null){
                    room_matching_page_start_location_latlng = new LatLng(37.553333,126.97);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(room_matching_page_start_location_latlng));//서울역으로 camera를 move시킬 것(이거 꼭 들어가야됨..이것때메 고생함 )
                    man_marker_setting(user_sex + user_num + "호 님의 미니홈피",room_matching_page_start_location_latlng);
                   // make_circle();
                    my_marker = googleMap.addMarker(my_marker_option);
                    my_marker.showInfoWindow();
                    map_camera_setting();
                    go_server_lat_lon(user_num);

                }else{//기존에 만들어놓은 마커가 있을때에는
                    try {
                        JSONObject j_o = new JSONObject(s);//결과를 받고
                        //user_latitude = 31;
                        // user_latitude = j_o.getDouble("user_latitude");//위도
                        //user_longitude = j_o.getDouble("user_longitude");//경도
                        user_lovelevel = j_o.getInt("user_love_level");//러브지수
                        user_lovebean = j_o.getInt("user_love_bean");//러브콩
                        user_room_radius = j_o.getInt("user_room_radius");//룸의 반경
                        user_room_lat = j_o.getDouble("user_room_lat");
                        user_room_long = j_o.getDouble("user_room_long");
                        Log.v("최종 위도,경도,레벨,콩", "-" + user_lovelevel +"/"+ user_lovebean +"/"+ user_room_radius +"/"+user_room_lat +"/"+ user_room_long);
                        //만약에 없으면다 null로 뜨겟지...
                        //if ((user_room_lat != 0) && (user_room_long != 0)) {//step2.그사람의 위도,경도가 있으면 그거 그대로 화면에 뿌려주면 되고.....
                        //그대로 화면에 뿌려주는 메소드
                        //  setting_minihome_status();//그대로 화면에 뿌려주는 메소드 발동~!

                        //테그
                        if ((user_room_lat != 0) && (user_room_long != 0)) {
                            //위도랑 경도가 있으면
                            Log.v("위도랑 경도 있는 곳 ","!");
                            room_matching_page_start_location_latlng = new LatLng(user_room_lat,user_room_long);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(room_matching_page_start_location_latlng));
                            Log.v("최종 위도,경도","-"+user_room_lat+user_room_long);
                            man_marker_setting(user_sex + user_num + "호 님의 미니홈피",room_matching_page_start_location_latlng);
                            my_marker = googleMap.addMarker(my_marker_option);
                            my_marker.setDraggable(false);//테그:고친곳
                            map_camera_setting();
                            make_circle();//성별에 따라서 다른 room이 생성됨
                            settext_room_status();
                            my_marker.showInfoWindow();
                            //create_others_marker(37.553336,126.96,"테스트 미니홈피","(클릭하면 미니홈피로 이동합니다)");
                            //othres_marker = googleMap.addMarker(others_marker_option);
                            //othres_marker.showInfoWindow();

                            //자신의 위도 경도를 기반으로 반경 내 다른 마커들 가져오기 시작
                            go_server_lat_lon(s);
                            //자신의 위도 경도를 기반으로 반경 내 다른 마커들 가져오기 끝
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        roommatching_page_select_lt task = new roommatching_page_select_lt();
        task.execute(user_num_thread);
    }

    private void go_server_lat_lon(String s) {
        class roommatching_page_select_lt extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String user_num_insert = (String) params[0];
                    Log.v("보낼json(search)", "-" + user_num_insert);
                    String data = "json_result" + "=" + user_num_insert;

                    URL my_server = new URL("http://cmic.dothome.co.kr/new_cmic/user_room_matching/search_lat_lon.php");
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
                    return sb.toString();//넘어온 json을 조회한다
                } catch (Exception e) {
                    return new String("exeption :" + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("반경이성검색", "-" + s);
                try {
                    create_markers(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        roommatching_page_select_lt task = new roommatching_page_select_lt();
        task.execute(s);
    }
    //처음 화면 등장시, 회원의 room db에 접근하여 정보조회하는 메소드 끝

    //json으로 조회된 반경 안의 사람들 json 분해 시작
    private void create_markers(String s) throws JSONException {
       JSONArray jsonArray = new JSONArray(s);
       // JSONObject j_o = new JSONObject(s);


        Log.v("json array","-"+jsonArray);
        Log.v("json array length","-"+jsonArray.length());
        Log.v("json s lenght","-"+s.length());

        //Log.v("j_o","-"+j_o);
        //Log.v("j_o lenght","-"+j_o.length());
       // Log.v("j_o i","-"+j_o.get);

        //markers = new ArrayList<Marker>();
        markers= new HashMap<String, String>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Log.v("jsonObj","-"+jsonArray.getJSONObject(i));
            //번호:jsonObj.getString("user_num")
            //
            //Marker marker = googleMap.addMarker(new MarkerOptions().title(jsonObj.getString("user_num")).position(new LatLng(jsonObj.getDouble("user_room_lat"),jsonObj.getDouble("user_room_long"))).icon(BitmapDescriptorFactory.fromResource(R.drawable.d_girl_minihome_icon)));
            String a;
            if(user_sex.equals("남자")){
                a="여자";
            }else{
                a="남자";
            }

            create_others_marker(jsonObj.getDouble("user_room_lat"),jsonObj.getDouble("user_room_long"),a+jsonObj.getString("user_num")+"님의 미니홈피","(룸을 클릭하면 미니홈피로 이동합니다)");
            Marker marker = googleMap.addMarker(others_marker_option);
            //테그
            markers.put(marker.getId(),jsonObj.getString("user_num"));
        }
        Log.v("마커즈","-"+markers);
    }
    //json으로 조회된 반경 안의 사람들 json 분해 끝

    //튜토리얼 다이어로그 시작
    //'예','아니오' 메소드 시작
    private void roommatching_page_message(String title, String contents) {//step5.미니홈피 생성과 그에 대한 설명
        AlertDialog.Builder builder = new AlertDialog.Builder(Roommatching_page.this);
        // 다이얼로그의 제목을 설정한다.
        TextView tt = new TextView(Roommatching_page.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그의 제목을 설정한다.
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Roommatching_page.this);
        tm.setText(contents);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        //tm.setBackgroundColor(Color.parseColor("#FC6265"));
        builder.setView(tm);
        // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
        //builder.setIcon(R.drawable.core_icon_image_1);
        //사용자가 취소 못하게 막는다
        builder.setCancelable(false);
        /////////////////////////////////////////////////////////////////////////
       // if(map_circle!=null){
         //   map_circle.remove();
        //}
        /////////////////////////////////////////////////////////////////////////

        // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//step6.예를 눌렀을때의 처리
                String title = "알림";
                String contents = "미니홈피가 지정됨과 동시에 ROOM이 지정됩니다.\n*ROOM이란? - 당신 주변의 이성을 보이게끔 도와주는 페시브 스킬입니다." +
                        "\n\n\n당신의 집 근처, 직장근처 어디든 좋습니다.\n이성을 만나고 싶은 장소에 미니홈피를 설치하세요.\n\n" +
                        "설치방법 : 하우스 모양의 아이콘을 꾸욱 눌러서 원하는 곳에 놓으세요!";
                roommatching_page_confirm_message(title, contents, "예");
            }
        });
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//step7.아니오를 눌렀을때의 예외처리
                String title = "알림";
                String contents = "미니홈피를 지정하지 않으면 \n 위치기반 서비스를 이용할 수 없습니다." +
                        "\n서비스를 이용하고 싶으시면 \n 다시 접속해주세요.";
                roommatching_page_confirm_message(title, contents, "아니오");
            }
        });
        // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.
        builder.show();
    }
    //'예','아니오' 메소드 끝

    //'예' 메소드 시작
    private void roommatching_page_confirm_message(String title, String contents, String yes_no) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Roommatching_page.this);
        // 다이얼로그의 제목을 설정한다.
        TextView tt = new TextView(Roommatching_page.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그의 제목을 설정한다.
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Roommatching_page.this);
        tm.setText(contents);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        //tm.setBackgroundColor(Color.parseColor("#FC6265"));
        builder.setView(tm);
        // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
        //builder.setIcon(R.drawable.core_icon_image_1);
        //사용자가 취소 못하게 막는다
        builder.setCancelable(false);
        if (yes_no.equals("예")) {//step8.예가 들어왔을때에는 미니홈피 setting 시작
            room_tutorial_status = true;
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //드래그를 시작하기 전에, 최초 값들을 서버로 넣어주기도 하고, 초기값들을 지정해보자 시작
                    first_get_json();


                    if(map_circle!=null){
                        map_circle.remove();
                    }
                    //테그
                    //make_circle();//성별에 따라서 다른 room이 생성됨

                    insert_user_room_information(first_insert_room_information);
                    //드래그를 시작하기 전에, 최초 값들을 서버로 넣어주기도 하고, 초기값들을 지정해보자 끝

                    Toast.makeText(getApplicationContext(), "미니홈피를 길게 눌러서 위치지정을 해보세요!", Toast.LENGTH_SHORT).show();
                    //여기서 이제 곧 드래그가 시작 될 것. 위로 올라가보자
                }
            });
        } else if (yes_no.equals("아니오")) {
            room_tutorial_status = false;
            my_marker.remove();//마커도 지우고...
            builder.setPositiveButton("예", null);//예 버튼 있고 끝...

        }
        // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.
        builder.show();
    }
    //'예' 메소드 끝
    //튜토리얼 다이어로그 끝

    //위도,경도 확정 다이얼로그 시작
    private void confirm_my_room(String title, String contents) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Roommatching_page.this);
        //시작
        TextView tt = new TextView(Roommatching_page.this);
        tt.setText(title);
        tt.setPadding(10,15,10,15);
        tt.setGravity(Gravity.CENTER);
        tt.setTextSize(20);
        tt.setTextColor(Color.parseColor("#FFFFFF"));
        tt.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그의 제목을 설정한다.
        builder.setCustomTitle(tt);

        TextView tm = new TextView(Roommatching_page.this);
        tm.setText(contents);
        tm.setPadding(10,60,10,60);
        tm.setGravity(Gravity.CENTER);
        tm.setTextColor(Color.parseColor("#FC6265"));
        //tm.setBackgroundColor(Color.parseColor("#FC6265"));
        // 다이얼로그에 출력될 메세지 내용을 적는다.
        builder.setView(tm);


        //끝

        // 다이얼로그에 출력될 아이콘의 리소스 ID 를 명시한다.
        //builder.setIcon(R.drawable.core_icon_image_1);
        //사용자가 취소 못하게 막는다
        builder.setCancelable(false);

        // 좌측(Positive Button) 에 출력될 버튼과 우측(Neutral Button) 에 출력될 버튼을 설정한다.
        builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //서버로 마커 위치 전송

                //마커색깔 흐릿한것 다시 true로
                my_marker.setAlpha(1f);
                //마커 움직이지 못하게 false
                my_marker.setDraggable(false);
                //다시 룸을 옮기지 못하게 하기 위한..room_confirm_status = true! 확정되었다
                room_confirm_status = true;

                //위도,경도 DB삽입 및 업데이트 시작
                //첫번째로 입력된 값이면 insert, 첫번째(최초생성시) 이동하는것이 아니면 콩을써서 이동한것이니 update를 해야겠지
                if (check_first_drag.equals(0)) {
                    Log.v("최초 수정 드래그,위경insert 시작", "!");
                    // first_get_json();//json 얻고
                    //insert_user_room_information(first_insert_room_information);//첫째로 서버에 값 삽입
                    //여기서 실수할 뻔 한게, 이사하고 난 drag값이니 이것도 엄연히 update문을 해줘야 한다. update메소드 시작할 것
                    //위도랑 경도만 update시켜주면 된다. 최초 drag값이니까...
                    //update 시작
                    update_lat_long_get_json_for_moving();
                    if(update_json_room_information_for_moving!=null){
                        update_user_room_information_for_moving(update_json_room_information_for_moving);
                    }
                  //  update_user_room_information_for_moving(update_json_room_information_for_moving);//서울역으로 되어있을 경도와 위도를 바꾸어 준다
                    //update 끝
                    check_first_drag = 1;//마커드래그가 끝나면! check_first_drag에 1을 대입해준다

                } else if (check_first_drag.equals(1)) {//step11.기존에 미니홈피 지정되지 않았을 경우를 했으니, 이제는 update했을때를 예외처리 해보면
                    Log.v("중복 수정 드래그,위경update 시작", "!");
                    update_lat_long_get_json_for_moving();//업데이트 시키기 위해 지금의 정보값을 가져옴
                    if(update_json_room_information_for_moving!=null){
                        update_user_room_information_for_moving(update_json_room_information_for_moving);//update시키는 메소드
                    }
                }
                //위도,경도 DB삽입 및 업데이트 끝
            }
        });
        builder.setPositiveButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                room_confirm_status = false;//확정안되었다
            }
        });
        // 빌더에 설정한 속성들을 이용하여 다이얼로그를 생성하고, 출력시킨다.
        builder.show();
    }
    //위도,경도 확정 다이얼로그 끝

    //첫번째로 서버로 보낼 정보 json으로 묶는 메소드 시작
    private void first_get_json() {
        //서버에 보낼 첫 설정 자료 json으로 묶기 시작
        JSONObject insert_room_info = new JSONObject();
        //우선 변수 선언부터 시작하기 user_num은 전화면에서 넘어온 값이 있으므로 설정 no
        user_lovelevel = 0;
        user_lovebean = 20;
        user_room_radius = 1200;
        user_room_lat = 37.56;
        user_room_long = 126.97;

        //각각의 room status에 setting 해놓기 및 마커 setting시작
        room_matching_page_user_redius.setText(user_room_radius.toString());//룸의반경
        room_matching_page_love_level.setText(user_lovelevel.toString());//사랑지수
        room_matching_page_love_bean.setText(user_lovebean.toString());//사랑콩까지
        room_matching_page_start_location_latlng = new LatLng(user_room_lat, user_room_long);//마커도 찍어주고
        //각각의 room status에 setting 해놓기 및 마커 setting 끝

        try {
            insert_room_info.put("json_user_num", user_num);
            insert_room_info.put("json_user_sex",user_sex);
            insert_room_info.put("json_user_love_level", user_lovelevel);
            insert_room_info.put("json_user_love_bean", user_lovebean);
            insert_room_info.put("json_user_room_radius", user_room_radius);//여기서 user_radius_value는 600이다
            insert_room_info.put("json_user_room_lat", user_room_lat);
            insert_room_info.put("json_user_room_long", user_room_long);
            //insert_room_info.put("json_user_location_latitude",user_latitude);
            //insert_room_info.put("json_user_location_longitude",user_longitude);//여기서 위도와 경도는 서울역이다.
        } catch (JSONException e) {
            e.printStackTrace();
        }
        first_insert_room_information = insert_room_info.toString();
        Log.v("첫번째 json 검사","-"+first_insert_room_information);
        //서버에 보낼 첫 설정 자료 json으로 묶기 끝
    }
    //첫번째로 서버로 보낼 정보 json으로 묶는 메소드 끝\

    //기존에 서버에 계정이 있는 유저의 값들을 settext해주는 메소드 시작
    //테그
    private void settext_room_status(){
        room_matching_page_user_redius.setText(user_room_radius.toString());//룸의반경
        room_matching_page_love_level.setText(user_lovelevel.toString());//사랑지수
        room_matching_page_love_bean.setText(user_lovebean.toString());//사랑콩까지
    }
    //기존에 서버에 계정이 있는 유저의 값들을 settext해주는 메소드 끝

    //room db에 정보 입력하는 메소드 시작
    private void insert_user_room_information(String user_num_thread) {//step9.서버측으로 처음 설정값들 보내기, 서버 db에 삽입하기
        class roommatching_page_select_lt extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String user_num_insert = (String) params[0];
                    Log.v("step9.insertdoInback진입", "-" + user_num_insert);
                    String data = "insert_user_room_information" + "=" + user_num_insert;

                    URL my_server = new URL("http://cmic.dothome.co.kr/new_cmic/user_room_matching/insert_user_room_information.php");
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
                    return sb.toString();//넘어온 json을 조회한다
                } catch (Exception e) {
                    return new String("exeption :" + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                /*
                if(map_circle!=null){
                    map_circle.remove();
                }
                */

                Log.v("server삽입후 넘어온 값", "-" + s);
                if (s.equals("0")) {//step10.실제로 0을 반환하면 삽입에 성공한 것이고, 1을 반환하면 실패하는 것으로 Toast메세지를 띄운다
                    Log.v("삽입성공", "!");
                    //삽입이 성공하였으면 아까 json처럼 보냈던 값을 그대로 setting해야겠지, 초기값이니 굳이 서버에서 다시 안불러와도 됨
                   // make_circle();
                   // Toast.makeText(getApplicationContext(), "미니홈피 설치가 완료되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("삽입실패", "!");
                    //Toast.makeText(getApplicationContext(), "미니홈피 설치에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            }
        }
        roommatching_page_select_lt task = new roommatching_page_select_lt();
        task.execute(user_num_thread);
    }


    //미니홈피 만드는 메소드 시작
    private void room_matching_page_make_minihomepage() {

    }
    //미니홈피 만드는 메소드 끝

    //처음 미니홈피 개설시 & 사랑콩 결재시 서버로 보낼 정보 json으로 묶는 메소드 시작
    private void update_lat_long_get_json_for_moving() {
        //현재 유저의 정보를 json으로 묶어서 update시킬 것....
        JSONObject update_room_info_for_moving = new JSONObject();
        try {
            //처음 지정하는 혹은, 이사하는 것이기 때문에 두개의 값만 있으면 충분하다
            update_room_info_for_moving.put("json_user_num", user_num);
            update_room_info_for_moving.put("json_user_room_lat", user_room_lat);
            update_room_info_for_moving.put("json_user_room_long", user_room_long);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        update_json_room_information_for_moving = update_room_info_for_moving.toString();

    }
    //사랑콩 결재시 서버로 보낼 정보 json으로 묶는 메소드 끝

    //사랑콩 결재시 회원의 room db에 접근하여 정보 update하는 메소드 시작
    private void update_user_room_information_for_moving(String user_num_thread) {//step9.서버측으로 처음 설정값들 보내기, 서버 db에 삽입하기
        class roommatching_page_select_lt extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                try {
                    String user_num_insert = (String) params[0];
                    Log.v("update.insertdoInback진입", "-" + user_num_insert);
                    String data = "update_user_room_information" + "=" + user_num_insert;

                    URL my_server = new URL("http://cmic.dothome.co.kr/new_cmic/user_room_matching/update_user_room_information_for_moving.php");
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
                    return sb.toString();//넘어온 json을 조회한다
                } catch (Exception e) {
                    return new String("exeption :" + e.getMessage());
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("server update후 넘어온 값", "-" + s);
                if (s.equals("0")) {
                    make_circle();
                    Log.v("업데이트 성공", "!");
                    Toast.makeText(getApplicationContext(), "미니홈피 이사를 완료하였습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Log.v("업데이트 실패", "!");
                    Toast.makeText(getApplicationContext(), "미니홈피 이사에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }
            }
        }
        roommatching_page_select_lt task = new roommatching_page_select_lt();
        task.execute(user_num_thread);
    }
    //사랑콩 결재시 회원의 room db에 접근하여 정보 update하는 메소드 끝

    //초기 lovestatus setting 객체화 method 시작
    private void love_status_setting() {
        room_matching_page_love_status_frame = (LinearLayout) findViewById(R.id.room_matching_page_linearlayout_frame);
        room_matching_page_user_redius = (TextView) findViewById(R.id.room_matching_page_textview_room_radius);
        room_matching_page_love_level = (TextView) findViewById(R.id.room_matching_page_textview_cupid_level);
        room_matching_page_love_bean = (TextView) findViewById(R.id.room_matching_page_textview_lovelovebean_count);
        room_matching_page_super_room = (Button) findViewById(R.id.room_matching_page_button_super_room);
        room_matching_page_loyal_room = (Button) findViewById(R.id.room_matching_page_button_loyal_room);
        room_matching_page_lovelove_moving = (Button) findViewById(R.id.room_matching_page_button_luvluvmoving_house);
        room_matching_page_textview_explain = (TextView) findViewById(R.id.room_matching_page_textview_explain);
    }
    //초기 lovestatus setting 객체화 method 끝

    //자기 자신의 marker setting 시작
    private void man_marker_setting(String title,LatLng ll) {
        //마커 생성
        my_marker_option = new MarkerOptions();//마커옵션의 마커 생성
        if(user_sex.equals("남자")){
            my_marker_option.position(ll)//아까만든 마커에 위도경도 setting
                    .title(title)//제목
                    .snippet("(클릭하면 미니홈피로 이동합니다)")//설명
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//아이콘 파란색으로 바꾸기
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.d_man_minihome_icon))
                    //디자인5-1.아이콘
                    .draggable(true);//마커의 드래그를 가능하게 설정
        }else{
            my_marker_option.position(ll)//아까만든 마커에 위도경도 setting
                    .title(title)//제목
                    .snippet("(클릭하면 미니홈피로 이동합니다)")//설명
                    //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//아이콘 파란색으로 바꾸기
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.d_girl_minihome_icon))
                    //디자인5-1.아이콘
                    .draggable(true);//마커의 드래그를 가능하게 설정
        }
        //.alpha(0.5f);//투명도
        // .flat(true);//마커의 평면
        //showInfoWindow()를하면 스니펫이 보이고, hideInfoWindow()를하면 스니펫이 사라진다
        //마커 생성 끝
        //+마커 커스텀마이징
    }
    //자기 자신의 marker setting 끝

    //다른 사람의 marker setting 시작
    public void create_others_marker(double lat,double longi,String title,String snippet){
        others_marker_option = new MarkerOptions();
        if(user_sex.equals("남자")){
                others_marker_option.position(new LatLng(lat,longi))
                        .title(title).snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.d_girl_minihome_icon))
                        .draggable(false);
            }else{
            others_marker_option.position(new LatLng(lat,longi))
                    .title(title).snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.d_man_minihome_icon))
                    .draggable(false);
        }
    }
    //다른 사람의 marker setting 끝

    //여자의 marker setting 시작
    private void girl_marker_setting(Marker mk, MarkerOptions mo, String title) {
        //마커 생성
        my_marker_option = new MarkerOptions();//마커옵션의 마커 생성
        my_marker_option.position(room_matching_page_start_location_latlng)//아까만든 마커에 위도경도 setting
                .title("남자 124호")//제목
                .snippet("클릭하면 미니홈피로 이동합니다")//설명
                //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//아이콘 파란색으로 바꾸기
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.man_flag2))
                .draggable(true);//마커의 드래그를 가능하게 설정
        //.alpha(0.5f);//투명도
        // .flat(true);//마커의 평면
        //showInfoWindow()를하면 스니펫이 보이고, hideInfoWindow()를하면 스니펫이 사라진다
        //마커 생성 끝
        //+마커 커스텀마이징

        //여자 마커 생성
                    /*
                    girl_marker = new MarkerOptions();//마커옵션의 마커 생성
                    girl_marker.position(test_room_matching_page_start_location_latlng)//아까만든 마커에 위도경도 setting
                            .title("여자 132호의 미니홈피")//제목
                            .snippet("진지하신 분만 와주세요")//설명
                            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//아이콘 파란색으로 바꾸기
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.women_marker))
                            .draggable(true);//마커의 드래그를 가능하게 설정
                    //.alpha(0.5f);//투명도
                    // .flat(true);//마커의 평면화
                    girl_Marker = googleMap.addMarker(girl_marker);
                    */
        //여자 마커 생성 끝
    }
    //여자의 marker setting 끝

    //frame+circle+snippet off 시작
    private void turn_off_objects() {
        //frame이 켜져있다면 시작
        if (check_frame != false) {
            room_matching_page_love_status_frame.setVisibility(View.INVISIBLE);
            check_frame = false;//false로 되돌려놔야 나중에 marker클릭했을때 감지한다
        }
        //frame이 켜져있다면 끝

        //원(room)이 켜져있다면 시작
        /*
        if (check_circle != false) {
            map_circle.remove();
            check_circle = false;//false로 되돌려놔야 나중에 marker클릭했을때 감지한다
        }
        */
        //원(room)이 켜져있다면 끝

        //스니펫이 켜져있다면 시작
        /*
        if (check_snippet != false) {
            my_marker.hideInfoWindow();//내 마커 가리기
            check_frame = false;
        }
        */
        //스니펫이 켜져있다면 끝
    }
    //frame+circle+snippet off 끝

    //만약의 기존에 미니홈피가 있는 사용자라고 하면 그 값들을 setting하기 시작
    private void setting_minihome_status() {
        CameraUpdate center = CameraUpdateFactory.newLatLng(my_marker.getPosition());

        //google맵 객체화해서 center하면 된다.하지만일단 google맵을 가져올수 없으니 이런식으로만 써보자

        my_marker.setAlpha(1f);
        my_marker.setDraggable(false);
        room_confirm_status = true;//room의 확정 status
        //원의 반경 - frame
        //사랑의 지수 - frame
        //사랑콩 - frame
        //위도,경도 - marker
        room_matching_page_user_redius.setText(user_room_radius.toString());//룸의반경
        room_matching_page_love_level.setText(user_lovelevel.toString());//사랑지수
        room_matching_page_love_bean.setText(user_lovebean.toString());//사랑콩까지
        room_matching_page_start_location_latlng = new LatLng(user_room_lat, user_room_long);//마커도 찍어주고

        man_marker_setting(user_sex + user_num + "호 님의 미니홈피",room_matching_page_start_location_latlng);//snippet의 제목,내용 마커의 포지션으로 이동시켜주는 메소드
    }
    //만약의 기존에 미니홈피가 있는 사용자라고 하면 그 값들을 setting하기 끝끝

    //정지할때, 자신의 큐피트 지수와 러브러브 콩의 개수를 서버로 보내는 메소드 시작

    @Override
    protected void onPause() {
        super.onPause();

    }
    //정지할때, 자신의 큐피트 지수와 러브러브 콩의 개수를 서버로 보내는 메소드 끝
}


