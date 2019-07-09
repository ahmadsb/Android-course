package com.example.ahmadsb.lab_ex6;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class MainActivity extends Activity implements ConnectionCallbacks,
        OnConnectionFailedListener ,LocationListener  {

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private Button btnShowLocation;
    private EditText mLatitude,mLongitude;
    private TextView dist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnShowLocation = (Button) findViewById(R.id.btn_start);
        mLatitude = (EditText) findViewById(R.id.ed2);
        mLongitude = (EditText) findViewById(R.id.ed1);
        dist =(TextView)findViewById(R.id.textView);

        if( mLatitude.getText().toString().trim().equals("")){
            mLatitude.setError( "latitude is required!" );
        }
        if( mLongitude.getText().toString().trim().equals("")){
            mLongitude.setError( "longitude is required!" );
        }

        if (checkPlayServices())
        {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                displayDistance();
                sendSMS();

            }
        });
    }

private void sendSMS(){
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED ) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    1);
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    1);
        }

    } else {
System.out.println("send 12123");
        //do thing
        try{

            SmsManager smsManager =  SmsManager.getDefault();
            smsManager.sendTextMessage("0544442311",null,dist.getText().toString(),null,null);
            Toast.makeText(this,"send succssful"+dist.getText().toString(),Toast.LENGTH_SHORT).show();


        } catch (Exception e){
            Toast.makeText(this,"not send",Toast.LENGTH_SHORT).show();
        }

    }

}

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
switch (requestCode){

    case 1:{
        if(grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS )
                    ==  PackageManager.PERMISSION_GRANTED ){
    Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this,"No permission granted",Toast.LENGTH_SHORT).show();

        }
        return ;
    }
}
    }


    private void displayDistance() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
             mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        }

        if(mLastLocation == null){
            startLocationUpdates();
        }

        if (mLastLocation != null) {

            distance();
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,   this);

        }

    }

    private  void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
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

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null) {mGoogleApiClient.connect();}
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {}

    @Override
    public void onConnected(Bundle arg0) {displayDistance();}

    @Override
    public void onConnectionSuspended(int arg0) {mGoogleApiClient.connect();}

    protected void startLocationUpdates() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(60 * 1000)//1 minute
                .setFastestInterval(5 * 1000)
                .setSmallestDisplacement(10);//10 meter

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);

    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        distance();
    }

    private void distance(){
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();
        double y1y2=Math.pow(latitude- Double.parseDouble(mLatitude.getText().toString()),2);
        double x1x2=Math.pow(longitude- Double.parseDouble(mLongitude.getText().toString()),2);
        double resDis=Math.sqrt(x1x2+y1y2);
        dist.setText(String.valueOf(resDis));
    }




}