package com.example.ahmadsb.home_ex2;

import android.provider.BaseColumns;

/**
 * Created by ahmadsb on 12/7/2017.
 */

public final class Constants {
    private Constants(){
        throw new AssertionError("cant create Cnostants class");
    }
    public static abstract class Signal implements BaseColumns{
        public static final String TABLE_NAME ="tablename";
        public static final String LATI_TUDE ="latitude";
        public static final String LONGI_TUDE ="longitude";
        public static final String TIME_DATE ="timedate";
        public static final String VAL_ASU ="valasu";
    }
}
