package com.example.ahmadsb.ex1_home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    String currentDateTimeString ;
    TextView tv;
    SharedPreferences shrd;
    SharedPreferences.Editor editor;
    String score;
// textView is the TextView view that should display it
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //init the variable of textView that including the data with score
        tv=(TextView)findViewById(R.id.textView3);
        //init the intent because to get the number correct answer by getStringExtra
        Intent i =getIntent();
        String score=i.getStringExtra("score");
        // create file1 to save the data
        shrd = this.getSharedPreferences("file1", this.MODE_PRIVATE);
        editor= shrd.edit();
        //init the format of date by pattern
        SimpleDateFormat patternDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        //get the date of phone
        String formattedTime = patternDate.format(new Date());
        /* change the textView that including data ,
        *   show the date from install the app until now
        *   by save get the past dates by sherd that save it in the file1 by value="save" */
        tv.setText(shrd.getString("save","")+formattedTime+" | "+score+"\n");
        //save the value of textView that show the how mauch play and when with th number of score
        editor.putString("save", tv.getText().toString());
        editor.apply();


    }
}
