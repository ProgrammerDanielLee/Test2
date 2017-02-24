package com.example.lovef.newcatchmeifyoucan;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by lovef on 2016-11-19.
 */
public class Room_matching_page_minihome_fragment extends Fragment {
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


/*
    public void setText1(String item){
        TextView view = (TextView)getView().findViewById(R.id.room_matching_page_minihome_textview_contents);
        view.setText(item);
    }*/

}
