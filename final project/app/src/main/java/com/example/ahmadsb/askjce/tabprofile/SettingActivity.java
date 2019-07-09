package com.example.ahmadsb.askjce.tabprofile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ahmadsb.askjce.R;
import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    EditText ed_setting_username,etxt_setting_call;
    Button btn_setteing;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db;
    DBHelper dbHelper;

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ed_setting_username=(EditText)findViewById(R.id.ed_setting_username);
        etxt_setting_call=(EditText)findViewById(R.id.etxt_setting_call);
        username=getIntent().getStringExtra("username");
        dbHelper = new DBHelper(this);
        btn_setteing=(Button)findViewById(R.id.btn_setteing);
        btn_setteing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readData();
                if(checkUserNameIfInDB()){
                    ed_setting_username.setError("the username not available");
                    ed_setting_username.setText("");
                }
                else{
                    setDetailes();// set the user name and phone number
                    onBackPressed();

                }


            }
        });


    }
    protected void readData(){

        db = dbHelper.getReadableDatabase();
        Cursor res;

        res = db.rawQuery("SELECT * FROM tableNameLogin", null);
        list.clear();
        if (res.moveToFirst()) {
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
    protected void setDetailes(){


        for (int i=0;i<list.size();i++){
            //if same email of user so set edTxtBio
            if(list.get(i).getUsername().equals(username)){

                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Constants.ASKJCE.USER_NAME,ed_setting_username.getText().toString());
                db.update(Constants.ASKJCE.TABLE_NAME, values,Constants.ASKJCE.USER_NAME+"=?"  , new String[]{username});

            }
        }
        ed_setting_username.setText(username);


    }
    protected boolean checkUserNameIfInDB(){
        for (int i=0;i<list.size();i++){
            if(list.get(i).getUsername().equals(ed_setting_username.getText().toString())){
                return true;
            }
        }
        return false;
    }
}
