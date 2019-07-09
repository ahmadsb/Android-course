package com.example.ahmadsb.askjce;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;


import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.MainActivity;

import com.example.ahmadsb.askjce.tab1Posts.Post;
import com.example.ahmadsb.askjce.tab1Posts.Tab1;
import com.example.ahmadsb.askjce.tabprofile.Tab3;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity

{
    private BottomNavigationView bottomNavigationView;
    private Button btn_logout;


    SQLiteDatabase db;
    public ArrayList<Post> list = new ArrayList<>();
    DBHelper dbPostHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Tab1 fragment1 =new Tab1();

        Bundle bundle1 = new Bundle();
        String username1 =getIntent().getStringExtra("username");
        bundle1.putString("username",username1);
        fragment1.setArguments(bundle1);
        switchFragment(fragment1,"fragmentHome");

//        System.out.println("information of uaser"+username+":"+email+":"+phonenumber);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_nativigation_id);
        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected( MenuItem item) {
                selectItemIdBottomNavigation(item);
            }
        });
        btn_logout=(Button)findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Just launch a new intent and clear the activities in the stack.
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        dbPostHandler=new DBHelper(this);

    }
    protected void selectItemIdBottomNavigation(MenuItem item){
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.menu_home:
                Tab1 fragment1 =new Tab1();

                Bundle bundle1 = new Bundle();
                String username1 =getIntent().getStringExtra("username");
                bundle1.putString("username",username1);
                fragment1.setArguments(bundle1);
                switchFragment(fragment1,"fragmentHome");
                break;
            case R.id.menu_notification:
                switchFragment(new Tab2(),"fragmentNotification");
                break;
            case R.id.menu_profile:
                Tab3 fragment3 =new Tab3();
                String username =getIntent().getStringExtra("username");
                String email =getIntent().getStringExtra("email");
                String phonenumber =getIntent().getStringExtra("phonenumber");
                String bio =getIntent().getStringExtra("bio");

                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putString("email",email);
                bundle.putString("phonenumber",phonenumber);
                bundle.putString("bio",bio);
                fragment3.setArguments(bundle);
                switchFragment(fragment3,"fragmentrofile");
                break;
            default:
                return ;

        }
    }

    protected  void switchFragment(android.support.v4.app.Fragment fragment,String nameFragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fram,fragment,nameFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return;
    }

}
