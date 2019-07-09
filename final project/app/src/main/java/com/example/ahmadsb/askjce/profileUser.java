package com.example.ahmadsb.askjce;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;

import java.util.ArrayList;

public class profileUser extends AppCompatActivity {
    de.hdodenhof.circleimageview.CircleImageView img;
    TextView tv_username,tv_numberPhone,tv_massage,tv_bio_profile;
    String username,numberphone,massage,bio;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db;
    DBHelper dbHelper;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        dbHelper=new DBHelper(this);
        initComp();
        setDataOfUser();
        upDateForActivity();


        tv_numberPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall= new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:"+tv_numberPhone.getHint()));
                startActivity(intentCall);
            }
        });
        tv_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", tv_numberPhone.getHint().toString(), null)));

            }
        });
    }
    public void initComp(){
        img=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.profile_post_img);
        tv_username=(TextView)findViewById(R.id.profile_post_name);
        tv_numberPhone=(TextView)findViewById(R.id.profile_post_call);
        tv_massage=(TextView)findViewById(R.id.profile_post_message);
        tv_bio_profile=(TextView)findViewById(R.id.txtBIO_post);
    }
    public void setDataOfUser(){
        username=getIntent().getStringExtra("username");
        readData();//init the list

        for (int i=0;i<list.size();i++){
            if(list.get(i).getUsername().equals(username))
            {
                numberphone=list.get(i).getPhonnumber();
                massage=list.get(i).getPhonnumber();
                bio=list.get(i).getBIO();
                img.setImageResource(0);
                Bitmap bm = BitmapFactory.decodeByteArray(list.get(i).getPhoto(), 0 ,list.get(i).getPhoto().length);
                img.setImageBitmap(bm);
            }
        }



    }
    public void upDateForActivity(){
        tv_username.setText(username);
        tv_numberPhone.setHint(numberphone);
        tv_massage.setHint(massage);
        tv_bio_profile.setText(bio);
    }

    protected void readData(){

        db = dbHelper.getReadableDatabase();
        Cursor res;

        res = db.rawQuery("SELECT * FROM tableNameLogin", null);

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


}
