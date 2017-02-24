package com.example.lovef.newcatchmeifyoucan;

import android.app.Activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lovef on 2016-11-19.
 */
public class Room_matching_page_minihome_profile extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v("프로필 들어왔나(2)","?");
        View view = inflater.inflate(R.layout.room_matching_page_minihome_profile,container,false);
        return view;
    }
}
