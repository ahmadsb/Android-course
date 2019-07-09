package com.example.ahmadsb.askjce;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by ahmadsb on 3/19/2018.
 */

public class RecyclerAdapterComments extends RecyclerView.Adapter<RecyclerAdapterComments.MyViewHolder> {
    private Context context;
    ArrayList<Comment> mlist;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db;
    DBHelper dbHelper;


    public RecyclerAdapterComments(Context context, ArrayList<Comment> list) {
        this.context = context;
        mlist = list;
        dbHelper=new DBHelper(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.rowcomment,parent,false);
        return new RecyclerAdapterComments.MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Comment commentPostion=mlist.get(position);
        holder.username.setText(commentPostion.getUsername());
        holder.date.setText(commentPostion.getDateComment());
        holder.time.setText(commentPostion.getTimeComment());
        holder.comment.setText(commentPostion.getComment());
//        Log.d("image",commentPostion.getPhoto().length + "");

        Bitmap bm = BitmapFactory.decodeByteArray(getPhotoFromDB(commentPostion.getUsername()), 0 ,getPhotoFromDB(commentPostion.getUsername()).length);
        holder.img_Comment.setImageResource(0);
        holder.img_Comment.setImageBitmap(bm);
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username,date,time,comment;
        de.hdodenhof.circleimageview.CircleImageView img_Comment;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            img_Comment=itemView.findViewById(R.id.img_Comment);
            username=itemView.findViewById(R.id.username_comment);
            date=itemView.findViewById(R.id.comment_date);
            time=itemView.findViewById(R.id.time_comment);
            comment=itemView.findViewById(R.id.comment_text);

        }
    }
    protected void readData(){

        db = dbHelper.getReadableDatabase();
        Cursor res;

        res = db.rawQuery("SELECT * FROM tableNameLogin", null);

        list.clear();
        if (!res.equals(null) && res.moveToFirst()) {
            while (!res.isAfterLast()) {
                String username = res.getString(res.getColumnIndex(Constants.ASKJCE.USER_NAME));
                String email = res.getString(res.getColumnIndex(Constants.ASKJCE.EMAIL));
                String password = res.getString(res.getColumnIndex(Constants.ASKJCE.PASSWORD));
                String phonenumber = res.getString(res.getColumnIndex(Constants.ASKJCE.PHONE_NUMBER));
                String bio = res.getString(res.getColumnIndex(Constants.ASKJCE.BIO));
                byte[] photo = res.getBlob(res.getColumnIndex(Constants.ASKJCE.KEY_IMG));

                list.add(new User(username, email, password,phonenumber,bio,photo));
                res.moveToNext();
            }

        }
    }
    protected  byte[] getPhotoFromDB(String username){
        readData();//init the liste of DB
        for (int i=0;i<list.size();i++){
            if(list.get(i).getUsername().equals(username)){
                return list.get(i).getPhoto();
            }
        }
        return null;
    }
}
