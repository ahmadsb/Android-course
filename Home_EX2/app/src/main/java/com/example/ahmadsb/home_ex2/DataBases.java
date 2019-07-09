package com.example.ahmadsb.home_ex2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBases extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DataBases.db";

    public static final String SQL_CREATE_ENTERIS =
            "CREATE TABLE "+Constants.Signal.TABLE_NAME+" ( "+
                    Constants.Signal._ID+" INTEGER PRIMARY KEY, "+
                    Constants.Signal.LATI_TUDE +" TEXT, "+
                    Constants.Signal.LONGI_TUDE +" TEXT, "+
                    Constants.Signal.TIME_DATE +" TEXT, "+
                    Constants.Signal.VAL_ASU +" TEXT)";
    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+Constants.Signal.TABLE_NAME;


    public DataBases(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTERIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
