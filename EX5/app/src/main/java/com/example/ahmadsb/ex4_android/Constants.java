package com.example.ahmadsb.ex4_android;

import android.provider.BaseColumns;

/**
 * Created by ahmadsb on 11/12/2017.
 */

public final class Constants {
    private Constants(){
        throw new AssertionError("Can't create constants class");
    }

    public static abstract  class Course implements BaseColumns{
        public static final String TABLE_NAME = "tableName";
        public static final String COURSE_NAME = "courseName";
        public static final String COURSE_ID  = "courseId";
        public static final String COURSE_GRADE = "courseGrade";
    }

}
