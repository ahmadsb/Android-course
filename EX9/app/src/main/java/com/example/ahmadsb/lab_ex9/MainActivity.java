package com.example.ahmadsb.lab_ex9;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class MainActivity extends AppCompatActivity {
    List<oneCall> callHistory = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view_id);
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                == PackageManager.PERMISSION_GRANTED){
            Contancts();
        }
        else if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CALL_LOG)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CALL_LOG},
                        2);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }



    }
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
        Cursor mCursor = managedQuery(CallLog.Calls.CONTENT_URI,null,null,null,CallLog.Calls.DATE + " DESC LIMIT 30");
        int number = mCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int date = mCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = mCursor.getColumnIndex(CallLog.Calls.DURATION);
        int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);
        while(mCursor.moveToNext()) {
            String callNumber = mCursor.getString(number);
            String callDuration = mCursor.getString(duration);
            String callType = mCursor.getString(type);
            String callDate = mCursor.getString(date);
            Date d = new Date(parseLong(callDate));

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
            callHistory.add(new oneCall(callNumber, d.toString(), callDuration, callType));
        }
        upDateRecyclerView(new ListAdapter(this,callHistory));
    }

    private void upDateRecyclerView(ListAdapter history){
        LinearLayoutManager historyManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(history);
        recyclerView.setLayoutManager(historyManager);
    }
}
