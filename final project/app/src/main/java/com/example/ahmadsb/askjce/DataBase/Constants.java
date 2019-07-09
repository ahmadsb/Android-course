package com.example.ahmadsb.askjce.DataBase;

import android.provider.BaseColumns;

/**
 * Created by ahmadsb on 12/31/2017.
 */

public class Constants {
    private Constants(){
        throw new AssertionError("Can't create constants class");
    }

    public static abstract  class ASKJCE implements BaseColumns{
        public static final String TABLE_NAME = "tableNameLogin";
        public static final String TABLE_NAME_POST = "tableNamePost";
        public static final String TABLE_NAME_COMMENT = "tableNameComment";
        public static final String TABLE_NAME2 = "tableNameLike";
        // tableNameLogin ==> username , email ,password ,phone number ,key img default and bio
        public static final String EMAIL  = "emailLogin";
        public static final String PASSWORD = "passwordLogin";
        public static final String PHONE_NUMBER = "phonenumber";
        public static final String BIO = "bio";
        // tableNamePost ==> key img , username ,id post , fromlocation ,time , date and text post
        public static final String ID_POST = "idPost";
        public static final String POST_TEXT = "postText";
        public static final String LOCATIONFrom = "locationFrom";
        // tableNameComment
        public static final String MSG_COMMENT = "msgComment";
        //tableNameLike postID,username
        // common for three tables
        public static final String KEY_IMG = "image";
        public static final String DATE  = "datePost";
        public static final String TIME = "timePost";

        public static final String USER_NAME = "userName";


//This application is used to allow jce students to help each other by allowing user to ask for a ride and other students can check who needs a ride and give them a rioe

    }
}
