package com.example.lovef.newcatchmeifyoucan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lovef on 2016-12-11.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    // MySQLiteOpenHelper는 SQLiteOpenHelper를 상속받는데, 이 SQLiteOpenHelper에 대해서 이야기 하자면\
    //db자체를 생성하고 업그레이드하는 기능 ㅗㄸ는..뭐 열려먼 SQLiteOpenHelper 객체를 사용한다.

    //생성메소드, oncreate와 onupgrade가 필수로 쓰임
    //생성메소드 : 상위 클래스의 생성메소드 호출, Activity등의 context인스턴스와 database의 이름, 커서팩토리(보통 null로 지정)등을 지정하고, database 스키마 버전을 알려주는 숫자를 넘겨준다
    //oncreate method : SQliteDatabase를 넘겨받고, 테이블을 새엇ㅇ하고 초기 데이터를 추가하기에 적당한 위치이다.
    //onUpgrade method : SQliteDatabase 인스턴스를 넘겨받으며, 현재 스키마 버전과 최신 스키마 버전 번호도 받는다.

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table chatting("+
                "_id integer primary key autoincrement,"+
                "user text,"+
                "message text," +
                "time text);";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldver, int newver) {
        //이 메소드의 하는일이 뭐냐..? 솔직히 제대로 모르겠다...
        String sql = "drop table if exists chatting";
        sqLiteDatabase.execSQL(sql);
    }

}
