package com.example.ahmadsb.home_ex2;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.TelephonyManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    CellSignalStrength cellSignalStrength;
    SQLiteDatabase db;
    recyclerAdapter adapter;
    DataBases dbHandler;
    Location mLastLocation;

    // variable init of condition the app. every 10 meter or 1 min run
    private final int INTERVAL = 60 * 1000;
    private final int FEASTEST_INTERVAL = 5 * 1000;
    private final int DISPLACEMENT = 10;
    //variable to consider the date
    String Date;
    // ArrayList is list to save the items in recyclerView
    public ArrayList<items> list = new ArrayList<>();
    Button btn_sort_ASU, btn_sort_date;
    // is a flag from type inter check={0,1,2}
    /*
    * check=0 init all data without condition
    * check=1 init all data with condition ' sorting by ASU '
    * check=2 init all data withcondition 'sorting by date '*/
    int check =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init the components of buttons sorting ASU or Date
        btn_sort_ASU = findViewById(R.id.btn_asu);
        btn_sort_date = findViewById(R.id.btn_date);
        RecyclerView myRecyclerView = findViewById(R.id.recyclerView);
        // allow to permission
        ActivityCompat.requestPermissions(this
                , new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                1);


        dbHandler = new DataBases(this);//handling database
        // read all data from database
       readData();
        // set adapter with data 'list' and send adapter to RecyclerView
        adapter = new recyclerAdapter(this, list);
        myRecyclerView.setAdapter(adapter);

        myRecyclerView.setLayoutManager(new LinearLayoutManager((this)));
        btn_sort_ASU.setOnClickListener(new View.OnClickListener() {//sort by acu button
            @Override
            public void onClick(View view) {
                check=2;
                readData();
            }
        });
        btn_sort_date.setOnClickListener(new View.OnClickListener() {//sort by date button
            @Override
            public void onClick(View view) {
                check=1;
              readData();
            }
        });

    }

    @Override
    public void onConnected(Bundle bundle) {
        // if there is connection get the location request with conditions 1o meters and 1 minute
        if(checkPlayServices()){
            startLocationUpdates();

        }

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
    // method check if the phone play with services or not
    protected boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        1000).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    protected void startLocationUpdates() {
        // create object locationRequest with the conditions 10 meter and 1 minute
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(INTERVAL)//1 minute
                .setFastestInterval(FEASTEST_INTERVAL)
                .setSmallestDisplacement(DISPLACEMENT);//10 meter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    // method create object of GoogleApiClient 'builder'
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void onStart() {//on start before calling oncreate get google api services for location
        super.onStart();
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        initSignal();

        //getting ready to write to database
        mLastLocation=location;
        getCurrentDate();
        writeData();
    }

    protected void initSignal() {

 /*telephony service
            * get the all cellinfoGSM and sace in list<CellInfo>
            *     and get the cell signal strength
            *     final get the number of Dbm (Integer)'*/
        // Call some material design APIs here

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //checks for all types of cell info lte gsm wcmda cmda
        List<CellInfo> allCell = telephonyManager.getAllCellInfo();
        for (final CellInfo info : allCell) {
            if (info instanceof CellInfoGsm) {
                cellSignalStrength = ((CellInfoGsm) info).getCellSignalStrength();
            } else if (info instanceof CellInfoCdma) {
                cellSignalStrength = ((CellInfoCdma) info).getCellSignalStrength();
            } else if (info instanceof CellInfoLte) {
                cellSignalStrength = ((CellInfoLte) info).getCellSignalStrength();
            } else if (info instanceof CellInfoWcdma) {
                cellSignalStrength = ((CellInfoWcdma) info).getCellSignalStrength();
            }
        }


    }

    protected void getCurrentDate() {
        /*
        * method to get the date
        * with pattern "yyyy-MM-dd  HH:mm:ss"*/
        Calendar date = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy \n HH:mm:ss");
        Date = df.format(date.getTime());

    }

    protected void writeData() {

        /*
        * require from data base to write
        * values that to want to write save in  variable
         * after that sent by function insert the name of table in DataBase
         * and the value that want to save */
        db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.Signal.LATI_TUDE, String.valueOf(mLastLocation.getLatitude()));
        values.put(Constants.Signal.LONGI_TUDE,String.valueOf(mLastLocation.getLongitude()));
        values.put(Constants.Signal.TIME_DATE, Date);
        values.put(Constants.Signal.VAL_ASU, String.valueOf(cellSignalStrength.getAsuLevel()));
        long id;
        id = db.insert(Constants.Signal.TABLE_NAME, null, values);
        // check if the insert
        if (id == -1) {
            Toast.makeText(this, "not successful write data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " successful write data", Toast.LENGTH_SHORT).show();
        }
        System.out.println("locationLLaste"+mLastLocation);
        list.add(new items(mLastLocation.getLongitude(), mLastLocation.getLatitude(), Date, cellSignalStrength.getAsuLevel(), id));
        //notify the adapter that we change something
        adapter.notifyDataSetChanged();
        // close the require from DataBase
        db.close();
    }

    protected void readData(){
        /*
        * read data from database by variable check
        * if the check equals zero so read all the data
        * if the check equals one so read data according date
        * else the check equals two so read data according value of ASU */

        db = dbHandler.getReadableDatabase();
        Cursor res;
        if(check==0) {
            res = db.rawQuery("SELECT * FROM tablename", null);
        }else if(check ==1){

            res = db.rawQuery("SELECT * FROM tablename ORDER BY timedate DESC", null);
        }
        else {
            res = db.rawQuery("SELECT * FROM tablename ORDER BY valasu ASC", null);
        }
        list.clear();
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                double Latitude = res.getDouble(res.getColumnIndex(Constants.Signal.LONGI_TUDE));
                double Longtitude = res.getDouble(res.getColumnIndex(Constants.Signal.LONGI_TUDE));
                int Asu = res.getInt(res.getColumnIndex(Constants.Signal.VAL_ASU));
                long id = res.getLong(res.getColumnIndex(Constants.Signal._ID));
                String date = res.getString(res.getColumnIndex(Constants.Signal.TIME_DATE));
                list.add(new items(Longtitude, Latitude, date, Asu, id));
                res.moveToNext();
            }
            if(check != 0)
            adapter.notifyDataSetChanged();

        }
    }

}