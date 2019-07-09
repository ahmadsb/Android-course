package com.example.ahmadsb.askjce.loginUser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.HomeActivity;
import com.example.ahmadsb.askjce.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //    CallbackManager callbackManager;
//    LoginButton mLoginButton;
//    String id;
//    URL profile_pic;
    Button btn_login,btn_signup;
    LinearLayout layout_visibility;
    EditText name_login,password_login;
    TextView massage_login;
    CheckBox checkbox;
    boolean flag=true,check=false;// check of 'list not found User Login'
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        dbHelper=new DBHelper(this);

        massage_login=(TextView)findViewById(R.id.massage_login);
        checkbox=(CheckBox)findViewById(R.id.checkbox);
        massage_login.setText("");

        name_login=(EditText)findViewById(R.id.name_login);
        password_login=(EditText)findViewById(R.id.password_login);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        name_login.setText(sharedPref.getString("SaveUserName",""));
        password_login.setText(sharedPref.getString("SavePassword",""));

        btn_login=(Button)findViewById(R.id.btn_login);
        layout_visibility=(LinearLayout)findViewById(R.id.layout_visibility);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag){
                    massage_login.setText("");
                    layout_visibility.setVisibility(View.VISIBLE);
                    flag =false;
                }
                else{
                    if(checkbox.isChecked()){
                        checkBoxShared();// save details of user
                    }
                    checkLogin();
                }
            }
        });



        //button of register
        btn_signup=(Button)findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
            }
        });


//        mLoginButton =(LoginButton) findViewById(R.id.login_button);
//        mLoginButton.setReadPermissions("email" , "public_profile");
//        callbackManager=CallbackManager.Factory.create();
//        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                String accessToken = loginResult.getAccessToken().getToken();
//                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//
////                        Bundle bFacebookData = getFacebookData(object);
//                        String firstname = null,lastname=null,email=null,profilepic=null;
//
//                        try {
//                            id = object.getString("id");
//                            firstname = object.getString("first_name");
//                            lastname = object.getString("last_name");
//                            email = object.getString("email");
//
//                            profile_pic = new URL("https://grahp.facebook.com/"+ id +"/picture?width&height=100");
//                            profilepic = object.getString("profile_pic");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        }
//
//                        Intent i = new Intent(MainActivity.this,SignupActivity.class);
//                        i.putExtra("firstname",firstname);
//                        i.putExtra("lastname",lastname);
//                        i.putExtra("email",email);
//                        i.putExtra("Image",profilepic);
//                        startActivity(i);
//                        finish();
//                    }
//                });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id, first_name, last_name, email");
//                graphRequest.setParameters(parameters);
//                //Log.e(" About to Graph Call", " ");
//                graphRequest.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

    }
    //Log in
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        mLoginButton.setVisibility(View.GONE);
//    }


    protected void checkLogin() {
        readData();// init the list of data SQL



            checkInputLogin();// check the name login and password login == > '2'
            if (checkInputLogin() == 2) {
                for (int i = 0; i < list.size(); i++) {
                    // if the User name and password is correct so goto home page
                    if (list.get(i).getUsername().equals(name_login.getText().toString().trim())
                            && list.get(i).getPassword().equals(password_login.getText().toString().trim())) {
                        // goto page home with user and email of User
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.putExtra("username", list.get(i).getUsername());
                        intent.putExtra("email", list.get(i).getEmail());
                        intent.putExtra("phonenumber", list.get(i).getPhonnumber());
                        intent.putExtra("bio", list.get(i).getBIO());
//                        intent.putExtra("photo", list.get(i).getPhoto());
                        System.out.println("information of uaser" + list.get(i).getPhoto());
                        startActivity(intent);
                        check = true;
                    }
                    // if the uaser or password not founded send error massage
                    if (i == list.size() - 1 && check == false) {
                        name_login.setText("");
                        password_login.setText("");
                        massage_login.setText("Your username or password are incorrect!");
                    }
                }
            }
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
    protected int checkInputLogin(){

        int counter=0;
        if(name_login.getText().toString().trim().length() == 0)
        {
            name_login.setError("is required");
        }else{counter++;}
        if(password_login.getText().toString().trim().length() == 0)
        {
            password_login.setError("is required");
        }else{counter++;}
        return counter;
    }
    protected void checkBoxShared(){
        editor.putString("SaveUserName", name_login.getText().toString());
        editor.putString("SavePassword", password_login.getText().toString());
        editor.apply();
    }
}
