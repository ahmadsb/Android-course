package com.example.ahmadsb.ex1_home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // variable to component on main activity (4 components)
    Chronometer ch;
    TextView tv;
    EditText ed;
    TextView tv2;
    // temps variables
    String num1="0";
    int temp;

    ConstraintLayout mConstraintLayout ;
    //init the chronometer component to 60:00 60,000 millisecound
    long initTime=SystemClock.elapsedRealtime()+60000;//init chronometer 60:00

//====Random number from 0 to 15=== //
    final int min = 0;
    final int max = 15;
    int random = new Random().nextInt((max - min) + 1) + min;
    protected void onResume() {

        super.onResume();
        long initTime=SystemClock.elapsedRealtime()+60000;//init chronometer 60:00
        tv2.setText("0");
        mConstraintLayout.setBackgroundColor(Color.WHITE);
        num1="0";
        random = new Random().nextInt((max - min) + 1) + min;
        ed.setText("");// init the value of editText of formula //{input result of formula}
        // init the component textView of formula
        // left number init to zero + operator plus {+} + Random number
        tv.setText(num1 + "+" + String.valueOf(random)+"=");
        tv2.setText("0");//init the score Zero
        // get the value of score and put the value in variable temp
        temp = Integer.valueOf(tv2.getText().toString());

        ch.setBase(initTime);// init the chronometer ot 60:00
        ch.start();// to start the chronometer


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init all the components on mainActivity (4 components)
        ch = (Chronometer) findViewById(R.id.chronometer2);
        tv = (TextView) findViewById(R.id.textView);// of formula
        ed = (EditText) findViewById(R.id.editText);
        tv2 = (TextView) findViewById(R.id.textView2);
        // call the id of ConstraintLayout that layout of mainActivity
        mConstraintLayout =(ConstraintLayout)findViewById(R.id.constraintLayout);

        ed.setText("");// init the value of editText of formula //{input result of formula}
        // init the component textView of formula
        // left number init to zero + operator plus {+} + Random number
        tv.setText(num1 + "+" + String.valueOf(random)+"=");
        tv2.setText("0");//init the score Zero
        // get the value of score and put the value in variable temp
        temp = Integer.valueOf(tv2.getText().toString());

        ch.setBase(initTime);// init the chronometer ot 60:00
        ch.start();// to start the chronometer

       ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
           @Override
           public void onChronometerTick(Chronometer chronometer) {
               // check the value of chronometer if is 30:00 or 10:00 or 00:00
               if((chronometer.getBase()- SystemClock.elapsedRealtime()  )<=0)
               {
                   //if timer of chronometer is 00:00 to dump main2Activity and take the number score to another Activity
                   Intent i = new Intent(getBaseContext(),Main2Activity.class);
                   i.putExtra("score",tv2.getText().toString());
                   startActivity(i);
               }
               if((chronometer.getBase()- SystemClock.elapsedRealtime())<=30000)
               {
                   //if the timer of chronometer iis 30:00 change the color background of layout to yellow
                   mConstraintLayout.setBackgroundColor(Color.YELLOW);
               }
               if((chronometer.getBase()- SystemClock.elapsedRealtime())<=10000)
               {
                   //if the timer of chronometer iis 10:00 change the color background of layout to red
                   mConstraintLayout.setBackgroundColor(Color.RED);
               }

           }
       });

ed.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //if there is input check
        if (ed.getText().toString().equals("") ) {
            //if the input of editText is null , don't do anything
        }
        else{
            //if the input of editText is not null, check input if is correct
        int temp1 = Integer.valueOf(ed.getText().toString());
        int temp2 = Integer.valueOf(num1) + random;
        if(temp1==temp2) {
            //if correct input
            tv.setText(num1 + "+" + String.valueOf(random)+"=");
            temp += 1;//score plus one
            tv2.setText(String.valueOf(temp));// and show the answer in activity
            num1 = ed.getText().toString();//take the answer put it in the left number of formula
            random = new Random().nextInt((max - min) + 1) + min;// right number is random integer
            tv.setText(num1 + "+" + String.valueOf(random)+"=");// to build the formula from left number + right number =
        }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});
//if click the text view of score
tv2.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        // go to the link of video
        ch.isTheFinalCountDown();
    }
});

    }
}
