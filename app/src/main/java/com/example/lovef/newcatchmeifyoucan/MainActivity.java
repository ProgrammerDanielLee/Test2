package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    //이메일 변수 선언 시작
    private EditText activity_main_edittext_email;//이메일 에딧텍스트
    private TextView activity_main_textview_emailfail;
    private String activity_main_edittext_email_value;
    //이메일 변수 선언 끝

    //비밀번호 변수 선언 시작
    private EditText activity_main_edittext_password;
    private TextView activity_main_textview_passwordfail;
    private String activity_main_edittext_password_value;
    private Pattern pattern;//비밀번호 관련 정규표현식
    private Matcher matcher;//matcher..?이 뭐지...
    //비밀번호 변수 선언 끝

    //회원가입 변수 선언 시작
    private TextView activity_main_button_join;//회원가입 버튼
    private TextView activity_main_button_signup;//로그인 버튼
    //회원가입 변수 선언 끝

    //facebook 관련 변수 시작
    private CallbackManager facebook_callbackmanager;
    private LoginButton mainactivity_facebook_login_button;//facebook의 fhrmdls qjxms
    private ImageButton mainactivity_facebook_login_button_image;//custommizing한 facebook 버튼
    private String facebook_emailadress_result;
    private String facebook_name_result;
    private String facebook_gender_result;
    //facebok 관련 변수 끝

    //google 관련 변수 시작
    private static final int RC_SIGN_IN =  1;//onresultactivity넘겨줄 RC_SIGN_IN
    private GoogleSignInOptions googlesignoptions;
    GoogleApiClient googleapiclient;
    private SignInButton mainactivity_google_login_button;//facebook에는 LoginButton 클래스가있듯이 여기는 SignInButton이 있다
    private ImageButton mainactivity_google_login_button_image;//custommizing한 google 버튼
    private String google_emailadress_result;
    private String google_name_result;
    private String google_gender_result;
    //로그아웃
    //탈퇴
    //google 관련 변수 끝

    //twitter 관련 변수 시작
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "awNWsu4SVXeALclpPrxfImmQf";
    private static final String TWITTER_SECRET = "uQo1IQtK5mJdI7uhiPbKFmHHLki2jDiGsPJeHdKjpYpANBnL4D";
    private TwitterLoginButton mainactivity_twitter_login_button;
    private ImageButton mainactivity_twitter_login_button_image;
    private TwitterAuthClient mainactivity_twitter_authclient;
    private String twitter_emailadress_result;
    private String twitter_name_result;//트위터는 현재 이 값만 있음
    private String twitter_gender_result;
    //twitter 관련 변수 끝

    //instagram 관련 변수 시작
    public static final String CLIENT_ID = "2c344d590fdd40749a8acda9a5ae7aff";
    public static final String CLIENT_SECRET = "91bcafdba33f4077b3f2f947a301b0d3 ";
    public static final String CALLBACK_URL = "http://cmic.dothome.co.kr/instagram.php";
    //instagram 관련 변수 끝

    //Naver 관련 변수 시작
    private String OAUTH_CLIENT_ID = "T5OLhpW20csj3ZygCbh3";//Client id
    private String OAUTH_CLIENT_SECRET = "TZgTi5fBdV";//secret id
    private String OAUTH_CLIENT_NAME = "NewCatchMeIfYouCan";//name id
    private OAuthLogin mOAuthLoginModule;//미친 네이버개발자새끼들
    private OAuthLoginButton mainactivity_naver_login_button;
    private OAuthLoginHandler mOAuthLoginHandler;//핸들러 전역변수 선언
    private Context mContext=this;
    private String naver_token;
    private String naver_header;
    private String naver_emailadress_result;
    private String naver_name_result;
    private String naver_gender_result;
    //Naver 관련 변수 끝

    //보통 로그인 관련 변수 시작
    //최초에 로그인시, 보내는 변수 시작
        private String main_email_for_login;
        private String main_password_for_login;
        String main_get_token;
        private String json_for_login;
    //최초에 로그인시, 보내는 변수 끝

    //로그인 성공 후 돌아오는 변수 싲가
        private String main_user_num_from_server;
        private String main_user_sex_from_server;
        private String main_user_local_from_server;
    //로그인 성공 후 돌아오는 변수 끝
    //보통 로그인 관련 변수 끝

    //shared preperence 시작
    private SharedPreferences whole_sp;
    private SharedPreferences.Editor whole_sp_editor;
    private com.facebook.login.LoginResult facebook_login_result;
    private FacebookCallback facebook_callback;
    //저장된 pref값에 대한 선언 시작
    String mainactivity_edittext_email_situation;
    String mainactivity_edittext_password_situation;
    //저장된 pref값에 대한 선언 끝
    //shared preperence 끝

    //뒤로가기 눌렀을때의 버튼 핸들러 관련 선언 시작
    private BackPressCloseHandler backPressCloseHandler;
    //뒤로가기 눌렀을때의 버튼 핸들러 관련 선언 끝
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebooksdk초기화하기 시작(반드시 setContentView(R.layout.activity_main); 전에 가져와야 함)
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        //facebooksdk초기화하기 끝(반드시 setContentView(R.layout.activity_main); 전에 가져와야 함)

        //fcm 토큰값 발행 시작
        FirebaseInstanceId.getInstance().getToken();//요것도
        //fcm 토큰값 발행 끝

        //트위터 초기화 하기 시작(반드시 setContentView(R.layout.activity_main); 전에 가져와야 함)
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //트위터 초기화 하기 끝(반드시 setContentView(R.layout.activity_main); 전에 가져와야 함)

        //네이버 초기화 하기 시작(반드시 setContentView(R.layout.activity_main); 전에 가져와야 함)
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                MainActivity.this
                , OAUTH_CLIENT_ID
                , OAUTH_CLIENT_SECRET
                , OAUTH_CLIENT_NAME
        );
        //네이버 초기화 하기 끝(반드시 setContentView(R.layout.activity_main); 전에 가져와야 함)
        setContentView(R.layout.activity_main);
        TextView main_title = (TextView)findViewById(R.id.activity_main_button_main);
        LinearLayout ln = (LinearLayout)findViewById(R.id.activity_main_loginframe);
        activity_main_edittext_email = (EditText) findViewById(R.id.activity_main_edittext_email);//메인에 있는 email
        activity_main_edittext_password = (EditText) findViewById(R.id.activity_main_edittext_password);


        Animation main_1 = AnimationUtils.loadAnimation(this,R.anim.fade_in_ver2);
        //Animation main_2 = AnimationUtils.loadAnimation(this,R.anim.fade_out);

        main_title.startAnimation(main_1);
        ln.startAnimation(main_1);


        //기존의 id,랑 페스워드 로그인 창 상태 저장 시작
            whole_sp = getSharedPreferences("EmailAndPassword", Activity.MODE_PRIVATE);
            mainactivity_edittext_email_situation = whole_sp.getString("save_edittext_email", "");//뒤에 ""는 원래 그런거니까 신경쓰지 않아도 된다
            mainactivity_edittext_password_situation = whole_sp.getString("save_edittext_password", "");//얘도 마찬가지고...
            Log.v("저장된 pref값", "-" + mainactivity_edittext_email_situation + mainactivity_edittext_password_situation);

                //그리고 만약에 이메일과 비밀번호 값이 있다고 하면 edittext에 setting도 해준다 시작
                if((mainactivity_edittext_email_situation!=null)&&(mainactivity_edittext_password_situation!=null)){
                    Log.v("이메일비밀번호 값 체크","-"+mainactivity_edittext_email_situation+mainactivity_edittext_password_situation);
                    activity_main_edittext_email.setText(mainactivity_edittext_email_situation);
                    activity_main_edittext_password.setText(mainactivity_edittext_password_situation);
                }
                //그리고 만약에 이메일과 비밀번호 값이 있다고 하면 edittext에 setting도 해준다 끝
        //기존의 id,랑 페스워드 로그인 창 상태 저장 끝

        //facebook 로그인 시작
        mainactivity_facebook_login_button = (LoginButton) findViewById(R.id.activity_main_facebook_login_button);
        //커스텀 이미지가 대신 발동 시작
        mainactivity_facebook_login_button_image = (ImageButton) findViewById(R.id.custom_fb_button);
        mainactivity_facebook_login_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainactivity_facebook_login_button.performClick();
            }
        });
        //커스텀 이미지 발동 끝

        //facebook 어떤정보를 가져올 건지 구현 부분 시작
        facebook_callbackmanager = CallbackManager.Factory.create();
        mainactivity_facebook_login_button.setReadPermissions(Arrays.asList("public_profile", "email"));//권한 등록

        mainactivity_facebook_login_button.registerCallback(facebook_callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {//성공
                        Log.v("facebook login결과", "-" + loginResult);
                        GraphRequest graphrequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    facebook_emailadress_result = object.getString("email");
                                    facebook_name_result = object.getString("name");
                                    facebook_gender_result = object.getString("gender");
                                    Log.v("facebook value 검사", "-" + facebook_emailadress_result + facebook_name_result + facebook_gender_result);

                                if(facebook_emailadress_result!=null && facebook_name_result!=null && facebook_gender_result!=null ){
                                   Intent go_join_page = new Intent(MainActivity.this,Join_page.class);
                                    go_join_page.putExtra("facebook_email",facebook_emailadress_result);
                                    go_join_page.putExtra("facebook_name",facebook_name_result);
                                    go_join_page.putExtra("facebook_gender",facebook_gender_result);
                                    startActivity(go_join_page);
                                    overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
                                }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender");
                        graphrequest.setParameters(parameters);//graphrequest에게 파라미터를 셋팅
                        graphrequest.executeAsync();//graphrequest 실행!
                    }

                    @Override
                    public void onCancel() {//실패
                        Toast.makeText(MainActivity.this, "facebook 로그인이 취소되었습니다", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {//에러
                        Log.v("facebook login error", "-" + error);
                        Toast.makeText(MainActivity.this, "facebook 로그인 에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
        //facebook 어떤정보를 가져올 건지 구현 부분 끝
        //facebook 로그인 끝

        //google 로그인 시작
        googlesignoptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        googleapiclient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googlesignoptions)
                .addApi(Plus.API)
                .build();

        mainactivity_google_login_button = (SignInButton) findViewById(R.id.activity_main_google_login_button);//평범한 로그인 버튼
        mainactivity_google_login_button_image = (ImageButton) findViewById(R.id.custom_google_button);//커스텀 이미지 버튼

        mainactivity_google_login_button.setSize(SignInButton.SIZE_WIDE);
        mainactivity_google_login_button.setScopes(googlesignoptions.getScopeArray());
/*
        mainactivity_google_login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
*/
        mainactivity_google_login_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("이미지 버튼 속은 들어오나?", "?");
                // mainactivity_google_login_button.performClick();
                signIn();//바로 sign image
            }
        });
        //google 로그인 끝

        //트위터 로그인 시작
        mainactivity_twitter_login_button = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mainactivity_twitter_login_button_image = (ImageButton) findViewById(R.id.custom_twitter_button);
        mainactivity_twitter_login_button_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainactivity_twitter_login_button.performClick();
            }
        });
        mainactivity_twitter_login_button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                // String msg = "@" + session.getUserName() + " 이가 트위터에 로그인 되었습니다! (#" + session.getUserId() + ")";
                twitter_name_result = session.getUserName();//이름은 이렇게 바로..
                if(twitter_name_result!=null){
                    Intent go_join = new Intent(MainActivity.this,Join_page.class);
                    go_join.putExtra("twitter_name",twitter_name_result);
                    startActivity(go_join);
                    overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
                }
                //이메일 얻기
                /*
                mainactivity_twitter_authclient = new TwitterAuthClient();
                mainactivity_twitter_authclient.requestEmail(session, new Callback<String>() {
                    @Override
                    public void success(Result<String> result) {
                        Log.v("트위터 이메일 결과","-"+result);
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
                */
                //이메일 얻기 끝

                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
        //트위터 로그인 끝

        //네이버(naver)로그인 시작
        mainactivity_naver_login_button = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mainactivity_naver_login_button.setBgResourceId(R.drawable.activity_main_naver_login_button);
        //네이버(naver)로그인 핸들러 시작
        mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {
                if (success) {
                    naver_token = mOAuthLoginModule.getAccessToken(mContext);
                    naver_header = "Bearer " + naver_token;
                    Log.v("토큰이랑 헤더", "-" + naver_token + naver_header);

                    naver_user_pro();//이 메소드부터 무언가가 꼬여버림. 나중에 볼 것....유저 정보가 불러와 지지가 않음...(evernote 참고)

                    //String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                    //long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                    //String tokenType = mOAuthLoginModule.getTokenType(mContext);

                    //mOauthAT.setText(accessToken);
                    // mOauthRT.setText(refreshToken);
                    //mOauthExpires.setText(String.valueOf(expiresAt));
                    //mOauthTokenType.setText(tokenType);
                    //mOAuthState.setText(mOAuthLoginModule.getState(mContext).toString());
                    //  Log.v("at,rT","-"+accessToken+refreshToken+expiresAt+tokenType);

                } else {
                    String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                    String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                    Toast.makeText(mContext, "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                }
            }

            ;
        };
        //네이버(naver)로그인 핸들러 시작
        mainactivity_naver_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainactivity_naver_login_button.setOAuthLoginHandler(mOAuthLoginHandler);
                mOAuthLoginModule.startOauthLoginActivity((Activity) mContext, mOAuthLoginHandler);
            }
        });

        //네이버(naver)로그인 핸들러 끝

        //네이버(naver)로그인 끝

        //이메일 정규표현식 검사 시작
        //이메일 값 뽑기 시작
        activity_main_textview_emailfail = (TextView) findViewById(R.id.activity_main_under_edittext_email);//email 실패했을때 표시하는 textview
        activity_main_edittext_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //리스너를 확인해봐!!(this)하는거!<-this랑은 전혀 상관이 없으니 상관말 것, 그보다 data삭제해보던지 어플을 다시 설치해볼 것
                Log.v("email실시간 체크", "-" + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {// 바뀌고 나서 감지 그값을 edittable로 반환

                if (s.toString().length() > 60 || !email_validate(s.toString())) {
                    Log.v("이메일 실패", "-" + s.toString());
                    //60자보다 크고, 유효성 검사에 걸리면
                    activity_main_textview_emailfail.setVisibility(View.VISIBLE);
                }

                if (s.toString().length() < 60) {//60자 이하이고
                    if (email_validate(s.toString())) {
                        activity_main_textview_emailfail.setVisibility(View.GONE);
                        activity_main_edittext_email_value = s.toString();
                        Log.v("이메일 값 최종값 전달", "-" + activity_main_edittext_email_value);
                    }
                }

            }
        });
        //이메일 값 뽑기 끝
        //이메일 정규표현식 검사 끝

        //비밀번호 값 뽑기 시작
        activity_main_textview_passwordfail = (TextView) findViewById(R.id.activity_main_under_edittext_password);
        activity_main_edittext_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 12 || !password_validate(s.toString())) {//서버에 한글이 들어가면 안되니까
                    activity_main_textview_passwordfail.setVisibility(View.VISIBLE);
                }

                if (s.toString().length() < 13) {
                    if (password_validate(s.toString())) {
                        activity_main_textview_passwordfail.setVisibility(View.GONE);
                        activity_main_edittext_password_value = s.toString();//첫번째 비밀번호값을 넣고
                        Log.v("pass_validate", "-" + activity_main_edittext_password_value);
                    }
                }
            }
        });
        //비밀번호 값 뽑기 끝

        //회원가입 버튼을 눌렀을때 시작
        activity_main_button_join = (TextView) findViewById(R.id.activity_main_button_join);
        activity_main_button_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_join_page = new Intent(MainActivity.this, Join_page.class);
                startActivity(go_join_page);//회원가입 페이지로 이동
                overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
            }
        });
        //회원가입 버튼 눌렀을때 끝

        //로그인 버튼 눌렀을때 시작
        activity_main_button_signup = (TextView)findViewById(R.id.activity_main_button_signup);
        activity_main_button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //쉐어드에서 토큰 값 뽑아오기 시작
                SharedPreferences sf = getSharedPreferences("token", 0);
                main_get_token = sf.getString("save_token","");//서비스 단에서 저장한 토큰을 지금 로그인 하는 순간 꺼내서 get_token에 저장한다
                Log.v("쉐어드에서 뽑은 토큰값","-"+main_get_token);
                //쉐어드에서 토큰 값 뽑아오기 끝

                //email,passwword,token 뽑은 값들 json으로 묶기 시작
                JSONObject user_info = new JSONObject();//Json object
                try {
                    user_info.put("login_email",activity_main_edittext_email_value);//이메일 입력된 값 json으로 넣기
                    user_info.put("login_password",activity_main_edittext_password_value);//비밀번호 입력된 값 json으로 넣기
                    user_info.put("login_token",main_get_token);//얻은 토큰값 보내기
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                json_for_login = user_info.toString();
                //email,passwrod,token 뽑은 값들 json으로 묶기 끝

                //서버로 email이랑 password보내기 시작
                inserttodata_for_login(json_for_login);
                //서버로 email이랑 passworㅇ보내기 끝

            }
        });
        //로그인 버튼 눌렀을때 끝

        //메인페이지 버튼 눌렀을때 시작
        TextView main_button = (TextView) findViewById(R.id.activity_main_button_main);
        main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_main = new Intent(MainActivity.this, Main_page.class);
                startActivity(go_main);
            }
        });
        //메인페이지 버튼 눌렀을때 끝

        //facebook hashkey뽑기 시작
        /*
            try {
                PackageInfo info = getPackageManager().getPackageInfo("com.example.lovef.newcatchmeifyoucan", PackageManager.GET_SIGNATURES);
                for (android.content.pm.Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("해쉬키 : ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }
        */
        //facebook hashkey뽑기 끝
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void inserttodata_for_login(String json_for_login) {
        class insertdata extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {}
            @Override
            protected String doInBackground(String... strings) {
                try{
                    String json_result_for_send2 = (String)strings[0];
                    String data = "json_final"+"="+json_result_for_send2;
                    Log.v("inserttodata_for_login","-"+data);
                    URL my_server = new URL("http://cmic.dothome.co.kr/user_login/user_login.php");//여기 수정
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
                Log.v("json_recieve","json_recieve"+s);

                try{
                    JSONObject j_o = new JSONObject(s);
                    Log.v("서버에서 넘어온 json 넘어오긴했니","(Main)"+j_o);
                    main_user_num_from_server = j_o.getString("user_num");//서버로 부터 받은 사람 번호
                    main_user_local_from_server = j_o.getString("user_local");//서버로부터 받은 지역
                    main_user_sex_from_server = j_o.getString("user_sex");//서버로 부터 받은 성별
                    //위 3개를 받았다
                }catch(JSONException e){
                    e.printStackTrace();
                }



                if(main_user_num_from_server != null && main_user_local_from_server != null && main_user_sex_from_server != null && main_get_token != null) {//값이 제대로 넘어왔다면
                    //다음페이지로 넘겨주는건 number,local,sex,token 이 4가지 이다

                    //테그 : 나중에 이곳을 Main_page가 아니라 Join_page(추가정보입력)를 거쳐서 Main_page로 만들어 주면 된다.
                    Intent go_main_page = new Intent(MainActivity.this, Main_page.class);
                    go_main_page.putExtra("login_num",main_user_num_from_server);//위에서 json을 던져서 회원정보랑 local정보를 가져옴
                    go_main_page.putExtra("login_local",main_user_local_from_server);//위에서 json을 던져서 회원정보랑 local정보를 가져옴
                    go_main_page.putExtra("login_sex",main_user_sex_from_server);
                    //Log.v("go_catch_core_sex","sex"+user_sex_in_mainact);
                    go_main_page.putExtra("set_token_mainact",main_get_token);//token은 서비스단에서 저장시킨 token
                    Log.v("엑티비티 넘어가기 직전","-"+main_user_num_from_server+main_user_local_from_server+main_user_sex_from_server+main_get_token);
                    startActivity(go_main_page);//다음 페이지로 넘어가기
                    //에니메이션 시작
                    overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
                    //에니멩시ㅕㄴ 끝
                }else{//만약 값들이 제대로 넘어오지 않았다면, 서버에서 온 echo를 토스트 메세지로 띄워준다
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }

        }
        insertdata task = new insertdata();
        task.execute(json_for_login);
    }

    //naver test 시작
    public void naver_user_pro() {
        class user_pro extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                    try {
                        String apiURL = "https://openapi.naver.com/v1/nid/me";
                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("Authorization", naver_header);
                        int responseCode = con.getResponseCode();
                        BufferedReader br;
                        Log.v("api호출로 들어왔는지", "?" + apiURL);

                        if (responseCode == 200) { // 정상 호출
                            Log.v("정상호출로 들어왔는지", "?");
                            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        } else {  // 에러 발생
                            Log.v("에러로 들어왔는지", "?");
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }

                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                            Log.v("로그인후 결과", "-" + response.toString());
                            break;
                        }
                        return response.toString();
                       // br.close();
                    } catch (Exception e) {
                        return e.getMessage();
                    }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //여기서 이제 json뿌리면 되지
                try {
                    JSONObject j_o = new JSONObject(s);
                    String response = j_o.getString("response");
                     Log.v("response","-"+response);
                        JSONObject response_result = new JSONObject(response);
                        naver_emailadress_result = response_result.getString("email");
                        naver_name_result = response_result.getString("nickname");
                        naver_gender_result = response_result.getString("gender");
                            if(naver_gender_result.equals("M")){
                                naver_gender_result = "남자";
                            }else{
                                naver_gender_result = "여자";
                            }
                                Log.v("네이버이메일이름성별","-"+naver_emailadress_result+naver_name_result+naver_gender_result);

                            //네이버테그
                            if( naver_emailadress_result!=null && naver_name_result!=null &&  naver_gender_result!=null){
                                Intent go_join_page = new Intent(MainActivity.this,Join_page.class);
                                go_join_page.putExtra("naver_email",naver_emailadress_result);
                                go_join_page.putExtra("naver_name",naver_name_result);
                                go_join_page.putExtra("naver_gender",naver_gender_result);
                                startActivity(go_join_page);
                                overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
                            }





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        user_pro task = new user_pro();
        task.execute();
    }
    //naver test 끝

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebook에서 돌아온 값 시작
        facebook_callbackmanager.onActivityResult(requestCode, resultCode, data);//facebook의 callbackmanager에 setting해주기, 당근 결과값을 받아야겠지 ..ㅠㅠ
        //facebook에서 돌아온 값 끝

        //google에서 돌아온 값 시작
        if (requestCode == RC_SIGN_IN) {
            Log.v("RC_SIGN_IN에 들어왔나 테스트","!");
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handlesignresult(result);//여기가 핵심이다. 여기서 부터 다시 보면 됨
        }
        //google에서 돌아온 값 끝

        //twitter에서 들어온 값 시작
        mainactivity_twitter_login_button.onActivityResult(requestCode,resultCode,data);
        //twitter에서 들어온 값 끝

        //instagram에서 들어온 값 시작

        //instagram에서 들어온 값 끝
    }

    //google login 결과값 조작 시작
    public void handlesignresult(GoogleSignInResult result) {//핵심
        Log.d("TAG", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {//값을 가져오는데에 성공했다면

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount google_acount_result = result.getSignInAccount();//구글계정 결과값을 담고

            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //텍스트뷰 만들어서 확인할 것
            String email = google_acount_result.getEmail();//이메일 얻고
            String name = google_acount_result.getDisplayName();//이름 얻고

            Log.v("normal_google"," - "+email+"\n"+name);//로그 찍고

            Person person = Plus.PeopleApi.getCurrentPerson(googleapiclient);//사람 객체에 api를 넣고, 사람 객체를 통해 각종 객체에 접근함
            //String plus_name = person.getDisplayName();
            Integer plus_gender = person.getGender();
            //String plus_about_me = person.getAboutMe();
            //String plus_birthday = person.getBirthday();

            String google_sex;//성별을
            //google plus의 경우 남자면 0 여자면 1을 뱉어내기때문에 이런식으로 custum해줘야 한다.
            if(plus_gender==0){google_sex="남자";}else{google_sex="여자";}
            google_emailadress_result = email;
            google_name_result = name;
            google_gender_result = google_sex;

            Log.v("normal_and_plus","-"+google_emailadress_result+"\n"+google_name_result+"\n"+google_gender_result);
            if(google_emailadress_result !=null && google_name_result !=null && google_gender_result !=null){
                //구글테그
                Intent go_join_page = new Intent(MainActivity.this,Join_page.class);
                go_join_page.putExtra("google_email",google_emailadress_result);
                go_join_page.putExtra("google_name",google_name_result);
                go_join_page.putExtra("google_gender",google_gender_result);
                startActivity(go_join_page);
                overridePendingTransition(R.anim.up_transition1,R.anim.up_transition2);
            }
           // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
           // updateUI(false);
        }
    }
    //google login 결과값 조작 끝


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        whole_sp = getSharedPreferences("EmailAndPassword", Activity.MODE_PRIVATE);//UI의 상태를 저장합니다
        whole_sp_editor = whole_sp.edit();//에디터 소환

        EditText shared_email = (EditText)findViewById(R.id.activity_main_edittext_email);
        EditText shared_password = (EditText)findViewById(R.id.activity_main_edittext_password);

        whole_sp_editor.putString("save_edittext_email",shared_email.getText().toString());//에디터에 이메일을 기록한다
        whole_sp_editor.putString("save_edittext_password",shared_password.getText().toString());

        whole_sp_editor.commit();//현재의 아이디와 패스워드를 저장~
    }

    @Override
    protected void onStop() {//화면이 꺼지면 저장해주고
        super.onStop();
        whole_sp = getSharedPreferences("EmailAndPassword", Activity.MODE_PRIVATE);//UI의 상태를 저장합니다
        whole_sp_editor = whole_sp.edit();//에디터 소환

        EditText shared_email = (EditText)findViewById(R.id.activity_main_edittext_email);
        EditText shared_password = (EditText)findViewById(R.id.activity_main_edittext_password);

        whole_sp_editor.putString("save_edittext_email",shared_email.getText().toString());//에디터에 이메일을 기록한다
        whole_sp_editor.putString("save_edittext_password",shared_password.getText().toString());

        whole_sp_editor.commit();//현재의 아이디와 패스워드를 저장~
    }

    /*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_google_login_button:
                signIn();
                break;
        }
    }
    */
    private void signIn(){
        Log.v("google singin에는 들어왔나","?");
        Intent signintent = Auth.GoogleSignInApi.getSignInIntent(googleapiclient);
        startActivityForResult(signintent, RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class BackPressCloseHandler {

        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        public void showGuide() {
            toast = Toast.makeText(activity,
                    "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
