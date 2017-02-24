package com.example.lovef.newcatchmeifyoucan;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

/**
 * Created by lovef on 2016-11-19.
 */
public class Room_matching_page_minihome_fragment_contents extends ListFragment {

    Fragment profile_fragment,photo_fragment,guestbook_fragment;
    //세가지 메소드를 호출하고 나머지 한가지는 정의한다
    //세가지 : onCreate,onActivityCreated,onListItemClick
    FragmentTransaction fragment_trans;
    android.app.FragmentManager fragment_manager;
    Room_matching_page_minihome_profile frag_pro = new Room_matching_page_minihome_profile();

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
        String item = (String) getListAdapter().getItem(position);//클릭한 위치의 메뉴이름 땡겨오기
        //Room_matching_page_minihome_fragment frag = (Room_matching_page_minihome_fragment) getFragmentManager().findFragmentById(R.id.room_matching_page_minihome_contents);

        getContents(item);
/*
        if(frag != null && frag.isInLayout()){
            frag.getFragmentManager();
        }
 */
    }

    private String getContents(String item) {
        fragment_manager = getFragmentManager();

        if(item.toLowerCase().contains("프로필")){
            Log.v("프로필 들어왔나(1)","?");
            //profile_fragment =
            //Intent go_proIntent = new Intent(Room_matching_page_minihome_fragment_contents.this,Room_matching_page_minihome_profile.class);
            //Fragment frag_pro = new Room_matching_page_minihome_profile();
            //return new Room_matching_page_minihome_profile();
            /*
            fragment_trans = fragment_manager.beginTransaction();
            frag_pro = (Room_matching_page_minihome_profile)fragment_manager.findFragmentById(R.id.room_matching_page_minihome_contents);
            fragment_trans.replace(R.id.room_matching_page_minihome_contents,frag_pro);
            fragment_trans.commit();
            */
            return "프로필 test";
        }

        if(item.toLowerCase().contains("사진첩")){
            return " 사진첩 test";
        }
        if(item.toLowerCase().contains("방명록")){
            return " 방명록 test";
        }
        return null;
    }
}


