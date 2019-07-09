package com.example.ahmadsb.home_ex2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    ArrayList<items> mlist;
    Context context;
    SQLiteDatabase db;
    DataBases dbHandler;
    recyclerAdapter(Context context,ArrayList<items> list){
        this.context = context;
        mlist = list;
        dbHandler =new  DataBases(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row,parent,false);
        return new MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final items item = mlist.get(position);
        holder.asu.setText(item.getAsu()+"");
        holder.longtitude.setText(item.getLongtitude() + "");
        holder.latitude.setText(item.getLatitude()+ "");
        holder.date.setText(item.getDate());
        if(item.getAsu()>=21)
        {
            holder.layout.setBackgroundColor(Color.GREEN);
        }
        else if (item.getAsu()>=11 && item.getAsu()<=20)
        {
            holder.layout.setBackgroundColor(Color.YELLOW);
        }
        else{
            holder.layout.setBackgroundColor(Color.RED);
        }
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHandler.getWritableDatabase();
                db.delete(Constants.Signal.TABLE_NAME,Constants.Signal._ID +" = "+item.getId(),null);
                mlist.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mlist.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView asu,longtitude,latitude,date;
        LinearLayout layout;
        Button btnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textView3);
            asu = itemView.findViewById(R.id.textView4);
            longtitude = itemView.findViewById(R.id.textView);
            latitude = itemView.findViewById(R.id.textView2);
            layout = itemView.findViewById(R.id.layoutView);
            btnDelete=itemView.findViewById(R.id.btn_icon_delete);

        }
    }
}