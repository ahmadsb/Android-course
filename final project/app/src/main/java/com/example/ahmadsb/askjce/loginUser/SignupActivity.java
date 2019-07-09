package com.example.ahmadsb.askjce.loginUser;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.R;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {
    EditText etxt_userName,etxt_email,etxt_password,etxt_confirmPassword,etxt_phonenumber;
    SQLiteDatabase db;
    DBHelper database;
    public ArrayList<User> list = new ArrayList<>();
    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etxt_userName=(EditText)findViewById(R.id.etxt_userName);
        etxt_email=(EditText)findViewById(R.id.etxt_email);
        etxt_phonenumber=(EditText)findViewById(R.id.etxt_phonenumber);
        etxt_password=(EditText)findViewById(R.id.etxt_password);
        etxt_confirmPassword=(EditText)findViewById(R.id.etxt_confirm_password);
        btn_register=(Button)findViewById(R.id.btn_register);

        database=new DBHelper(this);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                requireEdetText();
                if(requireEdetText()==5) {
                    if(etxt_password.getText().toString().trim().equals(etxt_confirmPassword.getText().toString().trim())){
                        Register(v);
                        onBackPressed();
                    }
                    else {
                        Toast.makeText(SignupActivity.this, "Password does not match the confirm password.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }
    protected void Register(View v) {
        db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ASKJCE.USER_NAME,etxt_userName.getText().toString().trim());
        values.put(Constants.ASKJCE.EMAIL,etxt_email.getText().toString().trim());
        values.put(Constants.ASKJCE.PASSWORD,etxt_password.getText().toString().trim());
        values.put(Constants.ASKJCE.PHONE_NUMBER,etxt_phonenumber.getText().toString().trim());
        values.put(Constants.ASKJCE.KEY_IMG,"");
        values.put(Constants.ASKJCE.BIO,"");

        long id;
        id = db.insert(Constants.ASKJCE.TABLE_NAME, null, values);
        // check if the insert
        if (id == -1) {

            Toast.makeText(this, "not successful write data", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, " successful write data", Toast.LENGTH_SHORT).show();
        }
        list.add(new User(etxt_userName.getText().toString().trim(),etxt_email.getText().toString().trim(),
                            etxt_password.getText().toString().trim(),String.valueOf(etxt_phonenumber.getText().toString().trim()),
                                "",null));
        db.close();
    }

    protected int requireEdetText(){
        int counter=0;
        if(etxt_userName.getText().toString().trim().length() ==0){
            etxt_userName.setError(etxt_userName.getHint().toString()+" is required!");
        }
        else{ counter++;}
        if(etxt_email.getText().toString().trim().length() ==0){
            etxt_email.setError(etxt_email.getHint().toString().trim()+" is required!");
        }else{ counter++;}

        if(etxt_phonenumber.getText().toString().trim().length() == 0){
            etxt_phonenumber.setError(etxt_phonenumber.getHint().toString().trim()+" is required!");
        }else{ counter++;}

        if(etxt_password.getText().toString().trim().length() ==0){
            etxt_password.setError(etxt_password.getHint().toString().trim()+" is required!");
        }else{ counter++;}
        if(etxt_confirmPassword.getText().toString().trim().length() ==0){
            etxt_confirmPassword.setError(etxt_confirmPassword.getHint().toString().trim()+" is required!");
        }else{ counter++;}


        return counter;
    }
}
