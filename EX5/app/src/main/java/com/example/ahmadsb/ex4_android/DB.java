package com.example.ahmadsb.ex4_android;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

import java.util.ArrayList;


/**
 * Created by ahmadsb on 11/12/2017.
 */

  public class DB extends SQLiteOpenHelper {

      public static final int DATABASE_VERSION =1000;
      public static final String DATABASE_NAME ="DB.db";


      private static final String SQL_CREATE_TABLE="CREATE TABLE "+Constants.Course.TABLE_NAME+" ( "+
              Constants.Course._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
              Constants.Course.COURSE_NAME +" TEXT, " +
              Constants.Course.COURSE_ID +" INTEGER NOT NULL UNIQUE, "+
              Constants.Course.COURSE_GRADE +" INTEGER)";
      private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+Constants.Course.COURSE_NAME;

        public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
     public Boolean insert_data(String name,int ID,int grade){
         SQLiteDatabase db;

         db =this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put(Constants.Course.COURSE_NAME, name);
         values.put(Constants.Course.COURSE_ID, ID);
         values.put(Constants.Course.COURSE_GRADE,grade);
         long result;
         result =db.insert(Constants.Course.TABLE_NAME,null,values);
            if(result == -1)
                return false ;
            else
                return true;
     }



}
