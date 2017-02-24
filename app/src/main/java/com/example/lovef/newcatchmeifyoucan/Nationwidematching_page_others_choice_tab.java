package com.example.lovef.newcatchmeifyoucan;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by lovef on 2016-12-07.
 */
public class Nationwidematching_page_others_choice_tab extends TabActivity{
    Context mContext;

    //tabhost 관련 메뉴 시작
    private Intent tab;
    private TabHost.TabSpec choice_page_tap;
    private TabHost tabhost;
    //tabhost 관련 메뉴 끝

    //선택에 관련된 변수 받기 시작
    private String select_mode,select_depth,select_human,selected_human,select_left_human,select_right_human,select_left_human_img,select_right_human_img;
    //선택에 관련된 변수 받기 끝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        select_mode = getIntent().getStringExtra("matching_result_mode");//선택인지 결과인지의 여부, 당근 선택이지만...
        select_depth = getIntent().getStringExtra("matching_result_depth");
        select_human = getIntent().getStringExtra("matching_result_s");
        selected_human = getIntent().getStringExtra("matching_result_st");
        select_left_human = getIntent().getStringExtra("matching_result_left");
        select_right_human = getIntent().getStringExtra("matching_result_right");
        select_left_human_img = getIntent().getStringExtra("matching_result_left_img");
        select_right_human_img = getIntent().getStringExtra("matching_result_right_img");
        setContentView(R.layout.nationwidematching_page_others_choice_tab);

        tabhost = getTabHost();
        //새로운 결과에 보내는 tab 시작
        tab = new Intent().setClass(Nationwidematching_page_others_choice_tab.this,Nationwidematching_page_others_choice.class);
        tab.putExtra("matching_result_mode",select_mode);
        tab.putExtra("matching_result_depth",select_depth);
        tab.putExtra("matching_result_s",select_human);
        tab.putExtra("matching_result_st",selected_human);
        tab.putExtra("matching_result_left",select_left_human );
        tab.putExtra("matching_result_right",select_right_human);
        tab.putExtra("matching_result_left_img",select_left_human_img);
        tab.putExtra("matching_result_right_img",select_right_human_img);
        choice_page_tap = tabhost.newTabSpec("Nationwide_choice").setIndicator("새로운 결과").setContent(tab);
        tabhost.addTab(choice_page_tap);
        //새로운 결과에 보내는 tab 끝

        //지난 결과에 보내는 메소드 시작(아마 여기서 직접 변수를 넘겨주는 일은 없을듯, 여기서는 단순히 tab만 추가)
        tab = new Intent().setClass(Nationwidematching_page_others_choice_tab.this,Nationwidematching_page_others_choice_before.class);
        choice_page_tap = tabhost.newTabSpec("Nationwide_choice").setIndicator("지난 결과").setContent(tab);
        tabhost.addTab(choice_page_tap);
        //지난 결과에 보내는 메소드 끝
    }
}
