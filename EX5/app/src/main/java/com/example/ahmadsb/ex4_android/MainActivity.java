package com.example.ahmadsb.ex4_android;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText CourseName;
    EditText CourseID;
    EditText CourseGrade;
    Button submit;
    SQLiteDatabase db;
    DB Clas_DB;
    String strName;
    int intID,intGrade;
    ListView myListView =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Clas_DB=new DB(this);
        CourseName=(EditText) findViewById(R.id.editText);
        CourseID=(EditText) findViewById(R.id.editText2);
        CourseGrade=(EditText) findViewById(R.id.editText3);

        submit=(Button) findViewById(R.id.button);

        SQLiteDatabase db = Clas_DB.getReadableDatabase();

        Cursor res = db.rawQuery("select * from tableName", null);
        if(res.moveToFirst()) {
            //====== new code of Recele View =======//
            myListView=findViewById(R.id.listView);
            MyCursorAdapter myAdapter=new MyCursorAdapter(getBaseContext(),res);
            myListView.setAdapter(myAdapter);

        }



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //==== add the value ===//
                strName=CourseName.getText().toString();
                intID=Integer.valueOf(CourseID.getText().toString());
                intGrade=Integer.valueOf(CourseGrade.getText().toString());
                Boolean result;
                result= Clas_DB.insert_data(strName,intID,intGrade);
               if(result==true)
               {
                   Toast.makeText(MainActivity.this, "successful ", Toast.LENGTH_SHORT).show();
                   CourseName.setText("");
                   CourseID.setText("");
                   CourseGrade.setText("");
               }
               else {
                   Toast.makeText(MainActivity.this, "not successful ", Toast.LENGTH_SHORT).show();
               }
                //=== find the max grade ===//
                SQLiteDatabase db = Clas_DB.getReadableDatabase();

                Cursor res = db.rawQuery("select * from tableName", null);
                if(res.moveToFirst()) {
                    //====== new code of Recele View =======//
                    myListView=findViewById(R.id.listView);
                    MyCursorAdapter myAdapter=new MyCursorAdapter(getBaseContext(),res);
                    myListView.setAdapter(myAdapter);

                }
                db.close();




            }
        });




    }
}
