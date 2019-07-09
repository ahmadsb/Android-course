package com.example.ahmad.ex3_android;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class Main2Activity extends AppCompatActivity {
    TextView text;
    Intent i;
    String str;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        i=getIntent();
        str=i.getStringExtra("enter_input");

        text=(TextView)findViewById(R.id.textView3);
        text.setText(str);

        tts = new TextToSpeech(this,new TextToSpeech.OnInitListener(){
            public void onInit(int status ) {

                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
                if(status == TextToSpeech.SUCCESS){
                  tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                      @Override
                      public void onStart(String utteranceId) {

                      }

                      @Override
                      public void onDone(String utteranceId) {
                          tts.stop();
                          tts.shutdown();
                      }

                      @Override
                      public void onError(String utteranceId) {

                      }
                  });
                }

            }
        });

    }

    public void OnSpeak(View view) {
        tts.speak(text.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);

    }
}
