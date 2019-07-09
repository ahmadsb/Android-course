package com.example.ahmadsb.askjce.tab1Posts;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmadsb.askjce.R;
import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener  {
    TextView txtLocFrom;
    EditText edtxtPost;
    SQLiteDatabase db;
    DBHelper dbPost;
    Geocoder geocoder;
    List<Address> addresses;
    String userName;
    Button btn_addPost;
    public ArrayList<Post> listPost;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db2;
    DBHelper dbHelper;
    String address = null;
    String country =null;
    // variable init of condition the app. every 10 meter or 1 min run
    private final int INTERVAL = 60 * 1000;
    private final int FEASTEST_INTERVAL = 5 * 1000;
    private final int DISPLACEMENT = 10;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        userName=getIntent().getStringExtra("username");
        initCoponents();//  method returns init txtLocTo , edtxtPost , dbPost and listPost
        buildGoogleApiClient();// bulic the googleApiclient for get last  location
        // allow to permission


        btn_addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation!=null){
                    writeDataOfPost(v);
                    onBackPressed();
                }
                else{
                    Toast.makeText(getBaseContext(),"Don't forgget to send location",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private  void initCoponents(){
        txtLocFrom=(TextView)findViewById(R.id.txtView_from);
        edtxtPost=(EditText) findViewById(R.id.edtxt_post);
        dbPost=new DBHelper(this);
        dbHelper=new DBHelper(this);
        listPost = new ArrayList<Post>();
        btn_addPost=(Button)findViewById(R.id.btn_addPOST);


    }

    protected void writeDataOfPost(View v) {
        db = dbPost.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ASKJCE.LOCATIONFrom,txtLocFrom.getText().toString());
        values.put(Constants.ASKJCE.POST_TEXT,edtxtPost.getText().toString());
        values.put(Constants.ASKJCE.USER_NAME,userName.trim());
        byte[] photoUser=getPhotoFromDBUser();
        values.put(Constants.ASKJCE.KEY_IMG,photoUser);
        /*current time and date */
        String currentDate=getCurrentDate();
        String currentTime=getCurrentTime();
        values.put(Constants.ASKJCE.DATE,currentDate);
        values.put(Constants.ASKJCE.TIME,currentTime);

        long id;
        id = db.insert(Constants.ASKJCE.TABLE_NAME_POST, null, values);
        // check if the insert
        if (id == -1) {
            Toast.makeText(this, "not successful write data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " successful write data", Toast.LENGTH_SHORT).show();
            listPost.add(new Post(edtxtPost.getText().toString(),txtLocFrom.getText().toString(),currentDate,currentTime,userName,photoUser,id+""));
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(checkPlayServices()){
            startLocationUpdates();

        }

        getLastLocation();// get the last location and put the result in variables mLastLocaiton
        String address = null;
        String country =null;
        String knownName =null;
        if(mLastLocation != null){
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if(addresses.size()!=0) {
                    country = addresses.get(0).getCountryName();
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    txtLocFrom.setText(country + "/" + address);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

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
    protected void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        else{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if(mLastLocation == null){
            startLocationUpdates();

        }
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  this);

        }

    }

    @Override
    protected void onStart() {
        // Connect the client.
        super.onStart();
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
        mGoogleApiClient.connect();
        txtLocFrom.setText(country + "/" + address);



    }

    private  void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        System.out.println("asaaaa"+mLastLocation);
        getLastLocation();

        String knownName =null;
        if(mLastLocation != null){
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                if(addresses.size()!=0) {
                    country = addresses.get(0).getCountryName();
                    address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    txtLocFrom.setText(country + "/" + address);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String getCurrentDate(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);

        return formattedDate;
    }
    public String getCurrentTime(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c);

        return formattedDate;
    }
    protected void readData(){

        db2 = dbHelper.getReadableDatabase();
        Cursor res;

        res = db2.rawQuery("SELECT * FROM tableNameLogin", null);

        list.clear();
        if (!res.equals(null) && res.moveToFirst()) {
            while (!res.isAfterLast()) {
                String username = res.getString(res.getColumnIndex(Constants.ASKJCE.USER_NAME));
                String email = res.getString(res.getColumnIndex(Constants.ASKJCE.EMAIL));
                String password = res.getString(res.getColumnIndex(Constants.ASKJCE.PASSWORD));
                String phonenumber = res.getString(res.getColumnIndex(Constants.ASKJCE.PHONE_NUMBER));
                String bio = res.getString(res.getColumnIndex(Constants.ASKJCE.BIO));
                byte[] photo = res.getBlob(res.getColumnIndex(Constants.ASKJCE.KEY_IMG));

                list.add(new User(username, email, password,phonenumber,bio,photo));
                res.moveToNext();
            }

        }
    }

    public byte[] getPhotoFromDBUser(){
        byte[] retPhoto = null;
        readData();//init the list of uaser ' photo , ... '
        for (int i=0 ; i<list.size();i++){
            if(list.get(i).getUsername().equals(userName)){
                retPhoto=list.get(i).getPhoto();
            }
        }
        return retPhoto;
    }
}
