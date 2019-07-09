package com.example.ahmad.first_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar getBar;
    TextView getText;
    Button ButInit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init all the components
        getBar=(SeekBar)findViewById(R.id.seekBar1);
        getText=(TextView)findViewById(R.id.textView);
        ButInit=(Button)findViewById(R.id.button);
        changingSeekBar();
    }

    public void changingSeekBar(){
        getBar. setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                getText.setTextSize(20 + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void buInit(View view) {
        getBar.setProgress(0);// convert the size of seekbar to 0
    }
}
