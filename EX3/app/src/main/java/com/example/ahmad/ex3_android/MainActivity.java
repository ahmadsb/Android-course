package com.example.ahmad.ex3_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText str;
    Intent i = getIntent();
    String action = i.getAction();
    String type = i.getType();
    String tempText;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            str=(EditText) findViewById( R.id.editText1);
            Intent i =new Intent(this,Main2Activity.class);
            setContentView(R.layout.activity_main);
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                     tempText = i.getStringExtra(Intent.EXTRA_TEXT);
                    str.setText(tempText);
                }
            }

    }




    public void OnSubmit(View view) {
        i.putExtra("enter_input",str.getText().toString());
        startActivity(i);
    }
}
