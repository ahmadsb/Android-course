package com.example.ahmadsb.askjce.tab1Posts;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmadsb.askjce.R;
import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;

import java.util.ArrayList;


public class Tab1 extends Fragment {
    FloatingActionButton btnAddPost;
    public Tab1() {
        // Required empty public constructor
    }


    SQLiteDatabase db;
    public ArrayList<Post> list = new ArrayList<>();
    DBHelper dbPostHandler;
    RecyclerView ResView;
    String userName;

    public ArrayList<User> listOFUser = new ArrayList<>();
    SQLiteDatabase dbUser;
    DBHelper dbHelper;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tab1, container, false);
        ActivityCompat.requestPermissions(getActivity()
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                1);

        ResView  = view.findViewById(R.id.myRecyclerView);
        dbPostHandler = new DBHelper(getActivity());//handling database
        dbHelper = new DBHelper(getActivity());//handling database
        userName=getArguments().getString("username");
        readData();




        // click on button floating of adding post do thid : intent to acticity add post
        btnAddPost=(FloatingActionButton)view.findViewById(R.id.onAddPost);
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AddPostActivity.class);
                i.putExtra("username",userName);
                startActivity(i);

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        readData();
    }

    protected void readData(){

        db = dbPostHandler.getReadableDatabase();
        Cursor res;
        res = db.rawQuery("SELECT * FROM tableNamePost", null);
        list.clear();
        if (res.moveToFirst() && !res.equals(null)) {
            while (!res.isAfterLast()) {

                String username = res.getString(res.getColumnIndex(Constants.ASKJCE.USER_NAME));
                String postText = res.getString(res.getColumnIndex(Constants.ASKJCE.POST_TEXT));
                String fromLocation = res.getString(res.getColumnIndex(Constants.ASKJCE.LOCATIONFrom));
                String date = res.getString(res.getColumnIndex(Constants.ASKJCE.DATE));
                String Time = res.getString(res.getColumnIndex(Constants.ASKJCE.TIME));
                String id = res.getString(res.getColumnIndex(Constants.ASKJCE._ID));
                byte[] photoPost = res.getBlob(res.getColumnIndex(Constants.ASKJCE.KEY_IMG));

                list.add(new Post(postText, fromLocation,date,Time,username,photoPost,id));
                res.moveToNext();
            }

            upDateRecyclerAdapter();

        }
    }

    protected void upDateRecyclerAdapter(){
        RecyclerAdapter adapter = new RecyclerAdapter(getActivity(),list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        ResView.setAdapter(adapter);
        ResView.setLayoutManager(mLayoutManager);
    }


}
