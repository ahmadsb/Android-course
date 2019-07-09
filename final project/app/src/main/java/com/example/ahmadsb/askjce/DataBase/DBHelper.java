package com.example.ahmadsb.askjce.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmadsb on 12/31/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =3000;
    public static final String DATABASE_NAME ="DBHelper.db";

    private static final String SQL_CREATE_TABLE="CREATE TABLE "+ Constants.ASKJCE.TABLE_NAME+" ( "+
            Constants.ASKJCE._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Constants.ASKJCE.USER_NAME +" TEXT NOT NULL UNIQUE, " +
            Constants.ASKJCE.EMAIL +" TEXT NOT NULL UNIQUE, "+
            Constants.ASKJCE.PHONE_NUMBER +" TEXT NOT NULL UNIQUE, "+
            Constants.ASKJCE.BIO +" TEXT, "+
            Constants.ASKJCE.KEY_IMG +" BLOB, "+
            Constants.ASKJCE.PASSWORD +" TEXT NOT NULL)";

    private static final String SQL_CREATE_TABLE_COMMENTS="CREATE TABLE "+ Constants.ASKJCE.TABLE_NAME_COMMENT+" ( "+
            Constants.ASKJCE._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Constants.ASKJCE.USER_NAME +" TEXT NOT NULL , " +
            Constants.ASKJCE.ID_POST +" TEXT NOT NULL , "+
            Constants.ASKJCE.MSG_COMMENT +" TEXT NOT NULL , "+
            Constants.ASKJCE.KEY_IMG +" Blob, "+
            Constants.ASKJCE.TIME +" TEXT, "+
            Constants.ASKJCE.DATE +" TEXT NOT NULL)";

    private static final String SQL_CREATE_TABLE_POST="CREATE TABLE "+ Constants.ASKJCE.TABLE_NAME_POST+" ( "+
            Constants.ASKJCE._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            Constants.ASKJCE.POST_TEXT +" TEXT NOT NULL, " +
            Constants.ASKJCE.USER_NAME +" TEXT NOT NULL, " +
            Constants.ASKJCE.LOCATIONFrom +" TEXT NOT NULL, "+
            Constants.ASKJCE.KEY_IMG +" BLOB, "+
            Constants.ASKJCE.DATE +" TEXT NOT NULL, "+
            Constants.ASKJCE.TIME +" TEXT NOT NULL)";
    private static final String SQL_CREATE_TABLE_LIKE="CREATE TABLE "+ Constants.ASKJCE.TABLE_NAME2+" ( "+
            Constants.ASKJCE.ID_POST +" TEXT NOT NULL , "+
            Constants.ASKJCE.USER_NAME +" TEXT NOT NULL)";


    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ Constants.ASKJCE.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_COMMENTS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_POST);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_LIKE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }



}

