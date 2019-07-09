package com.example.ahmadsb.ex3_home;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    TextView quoteTextView,tv_BFF_id;
    Button getQuoteButton,btn_send_quote;
    String phoneNumber;

    ArrayList<friend> callHistory = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // textView to set the name of my best friend
        tv_BFF_id = findViewById(R.id.tv_BFF_id);
        //init component of textView and button of get Quote
        quoteTextView = findViewById(R.id.tv_quote_id);
        getQuoteButton = findViewById(R.id.btn_getQuote);
        getQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the URL is the site will take quotes from him
                String myUrl = "https://talaikis.com/api/quotes/random/";
                // Of course, since reading a network is a "blocking action" so using the thread
                // thread --> MyAsynTask
                MyAsyncTask task = new MyAsyncTask();
                task.execute(myUrl);
            }
        });


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED){Contancts();}
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, 2);
            }
        }
        /*
        * send SMS to my best friend if click on btn_send_quote*/
        btn_send_quote=(Button)findViewById(R.id.btn_send_quote);
        btn_send_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSmsToBFF();
            }
        });


    }
    /*
    * methods to contancts*/
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Contancts();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void Contancts() {
        Cursor mCursor = managedQuery(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " LIMIT 500");
        int number = mCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int name = mCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);
        while(mCursor.moveToNext()) {
            String callNumber = mCursor.getString(number);
            String callType = mCursor.getString(type);
            String callName =mCursor.getString(name);
            switch(Integer.parseInt(callType))
            {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType="outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "missed";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    callType = "blocked";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    callType = "rejected";
            }
            callHistory.add(new friend(callNumber,callName,callType));
            setBFF();
        }
    }
    private void setBFF(){
        int counter=1;
        int index=0;
        int maxCount=1;
        ArrayList<Integer> array = new ArrayList<Integer>();
        /*
        * Move on the array to count how much the name appears
        * foreach put the index in variable
        * after check the max number of names*/
        for (int i=0;i<callHistory.size()-1; i++){
            counter=1;

            for (int j=i;j<callHistory.size(); j++){
                if(callHistory.get(i).getnumberphone().toString().equals(callHistory.get(j).getnumberphone().toString())){
                    counter++;
                }
            }
            if(counter>maxCount)
            {
                maxCount = counter;
                index =i;
            }
        }
        if(callHistory.get(index).getNamefriend() != null){
            tv_BFF_id.setText(callHistory.get(index).getNamefriend());
        }
        else {
            tv_BFF_id.setText(callHistory.get(index).getnumberphone());
        }
        phoneNumber=callHistory.get(index).getnumberphone();

    }
    // my thread to read data from link
    public class MyAsyncTask extends AsyncTask<String,Void,String> {
        ProgressDialog dialog;
        final int REQUEST_TIMEOUT=20000;
        @Override
        /* display the user loading widget
        while reading the network
        */
        protected void onPreExecute(){
            dialog = ProgressDialog.show(MainActivity.this,"","Loading. Please wait...!!",true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            String result = "";
            String line;
            try{
                /**
                 *myUrl is my link of website of quotes
                 * connection to link through HttpURLConnection
                 * init the connection with method 'GET' ,
                 * read time and time of connection are 20 sec
                 * 'REQUEST_TIMEOUT' ==  20,000  is Integer variable
                 */

                URL myUrl = new URL(stringUrl);
                HttpURLConnection conn =(HttpURLConnection) myUrl.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(REQUEST_TIMEOUT);
                conn.setConnectTimeout(REQUEST_TIMEOUT);
                conn.connect();

                // create object bufferReader to data from link
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
                result = (new JSONObject(result)).getString("quote");
            }
            catch(IOException e){
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        /*final dismiss the dialog and
        update the textView of quoteTextView on activity
        * */
        @Override
        protected  void onPostExecute(String Result){
            dialog.dismiss();
            quoteTextView.setText(Result);
        }
    }
    // method to send sms
    private void sendSmsToBFF(){
        /*
        * send SMS to phone number of my friend by intent*/
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
        intent.putExtra("sms_body", quoteTextView.getText().toString());
        startActivity(intent);
    }

}

