package com.example.ahmadsb.alb_ex7;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ahmadsb.alb_ex7.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    TextView jokeTextView;
    Button getJokeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String result;
        jokeTextView = findViewById(R.id.jokeText);
        getJokeButton = findViewById(R.id.getjokeButton);
        getJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myUrl = "http://api.icndb.com/jokes/random/";

                MyAsyncTask task = new MyAsyncTask();
                task.execute(myUrl);
            }
        });

    }
    public class MyAsyncTask extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute(){
            dialog = ProgressDialog.show(MainActivity.this,"","Loading. Please wait...!!",true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            String result = "";
            String line;
            try{
                // the link ' url '
                URL myUrl = new URL(stringUrl);
                // init the connection with url
                HttpURLConnection conn =(HttpURLConnection) myUrl.openConnection();
                //  request method 'GET' ,read time  20 sec and time of connection is 20 sec
                conn.setRequestMethod("GET");
                conn.setReadTimeout(20000);
                conn.setConnectTimeout(20000);
                conn.connect();// connection

              // create object 'bufferReader' to data
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // create object array stringBuilder
                StringBuilder array = new StringBuilder();
                //to pass on the all lines
                while((line = reader.readLine()) != null){
                    array.append(line);
                }
                //Close Buffered reader
                reader.close();

                //Set our result equal to our array ' string Builder'
                result = array.toString();
                result = (new JSONObject(result)).getString("value");
                result = (new JSONObject(result)).getString("joke");

            }
            catch(IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected  void onPostExecute(String Result){
            dialog.dismiss();
            jokeTextView.setText(Result);
        }
    }
}