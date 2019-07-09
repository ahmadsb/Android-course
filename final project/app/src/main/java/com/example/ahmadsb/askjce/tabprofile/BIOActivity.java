package com.example.ahmadsb.askjce.tabprofile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ahmadsb.askjce.R;
import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.MainActivity;
import com.example.ahmadsb.askjce.loginUser.User;

import java.util.ArrayList;

public class BIOActivity extends MainActivity  {
    EditText edTxtBIO;
    TextView textView_BIO_id;
    Button add_button;
    String emailOfUser;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);
        edTxtBIO=(EditText)findViewById(R.id.edTxtBIO);
        textView_BIO_id=(TextView)findViewById(R.id.textView_BIO_id);
        emailOfUser=getIntent().getStringExtra("email");


        dbHelper=new DBHelper(this);

        add_button=(Button)findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTXTBIO();

              onBackPressed();

            }
        });
        edTxtBIO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView_BIO_id.setText(s.length()+"/120");
            }

            @Override
            public void afterTextChanged(Editable s) {

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
    protected void setTXTBIO(){
        readData();

        for (int i=0;i<list.size();i++){
            //if same email of user so set edTxtBio
            if(list.get(i).getEmail().equals(emailOfUser)){
                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Constants.ASKJCE.BIO,edTxtBIO.getText().toString());
                db.update(Constants.ASKJCE.TABLE_NAME, values,Constants.ASKJCE.EMAIL + "=?" ,new String[] {emailOfUser} );
                list.get(i).setBIO(edTxtBIO.getText().toString());
            }
        }

    }


}
