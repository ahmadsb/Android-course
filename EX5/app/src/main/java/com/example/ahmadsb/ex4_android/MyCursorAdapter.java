package com.example.ahmadsb.ex4_android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ahmadsb on 11/19/2017.
 */

public class MyCursorAdapter extends CursorAdapter {
    LayoutInflater inflater;

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        inflater=LayoutInflater.from(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.row,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_name=(TextView)view.findViewById(R.id.textView);
        TextView tv_id=(TextView)view.findViewById(R.id.textView2);
        TextView tv_grade=(TextView)view.findViewById(R.id.textView3);
        LinearLayout layoutView=(LinearLayout)view.findViewById(R.id.layoutView);

        tv_name.setText(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_NAME)));
        tv_id.setText(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_ID)));
        tv_grade.setText(cursor.getString(cursor.getColumnIndex(Constants.Course.COURSE_GRADE)));
        if(Integer.valueOf(tv_grade.getText().toString())>=90)
        {
            layoutView.setBackgroundColor(Color.GREEN);
        }
       else if(Integer.valueOf(tv_grade.getText().toString())<55)
        {
            layoutView.setBackgroundColor(Color.RED);

        }

    }
}
