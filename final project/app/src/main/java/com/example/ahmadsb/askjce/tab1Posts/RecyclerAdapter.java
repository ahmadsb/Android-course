package com.example.ahmadsb.askjce.tab1Posts;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmadsb.askjce.R;
import com.example.ahmadsb.askjce.commentsActivity;
import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.profileUser;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

/**
 * Created by ahmadsb on 1/4/2018.
 */


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    ArrayList<Post> mlist;
    Context context;
    SQLiteDatabase db;
    DBHelper database;
    RecyclerAdapter(Context context, ArrayList<Post> list){
        this.context = context;
        mlist = list;
        database= new DBHelper(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.row,parent,false);
        return new MyViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Post post = mlist.get(position);
        // show the photo of user in the post
        Bitmap bm = BitmapFactory.decodeByteArray(post.getPhoto(), 0 ,post.getPhoto().length);
        holder.image_profile.setBackgroundResource(0);
        holder.image_profile.setImageBitmap(bm);


        holder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context,profileUser.class);
                newIntent.putExtra("username",post.getUsername());
                context.startActivity(newIntent);

            }
        });


        holder.post_UserName.setText(post.getUsername());
        holder.post_Text.setText(post.getPostText());

        holder.fromLocation.setText(post.getFromLocation());
        holder.fromLocation.setTextColor(Color.BLACK);

        holder.date.setText(post.getDate());
        holder.time.setText(post.getTime());
        // of button like
        if( checkLike((((Activity)context).getIntent().getStringExtra("username")),post.getId())){
            holder.likebtn.setLiked(true);


        }
        holder.Like.setText(checkNumberOfLikes(post.getId())+"");

        holder.likebtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                writeDataOfLike(post);


            holder.Like.setText((Integer.parseInt(holder.Like.getText().toString())+1)+"");


            }

            @Override
            public void unLiked(LikeButton likeButton) {
            dislike((((Activity)context).getIntent().getStringExtra("username")),post.getId());
                holder.Like.setText((Integer.parseInt(holder.Like.getText().toString())-1)+"");

            }
        });

        holder.Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,commentsActivity.class);
                i.putExtra("username",((Activity)context).getIntent().getStringExtra("username"));
                i.putExtra("postid",post.getId());
                ((Activity)context).startActivity(i);
            }
        });




    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView fromLocation,date,time,Like,Comment,post_UserName,post_Text;
        LikeButton likebtn;
        de.hdodenhof.circleimageview.CircleImageView image_profile;
        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            post_UserName = itemView.findViewById(R.id.post_UserName);
            post_Text = itemView.findViewById(R.id.tv_post);
            fromLocation = itemView.findViewById(R.id.tv_fromlocation);
            date = itemView.findViewById(R.id.tv_date);
            time = itemView.findViewById(R.id.tv_Time);
            Like = itemView.findViewById(R.id.tv_Like);
            Comment = itemView.findViewById(R.id.tv_comment);
            image_profile= itemView.findViewById(R.id.profile_image);
            likebtn = itemView.findViewById(R.id.star_button);
        }
    }
    public void writeDataOfLike(Post post){

        db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ASKJCE.USER_NAME,(((Activity)context).getIntent().getStringExtra("username")));
        values.put(Constants.ASKJCE.ID_POST, post.getId());

        long id;
        id = db.insert(Constants.ASKJCE.TABLE_NAME2, null, values);
        // check if the insert
        if (id == -1) {

            Toast.makeText(context, "not successful Like", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(context, " successful Like", Toast.LENGTH_SHORT).show();
        }
    }
    public int checkNumberOfLikes(String id){
        SQLiteDatabase db = database.getReadableDatabase();
        String selection =  Constants.ASKJCE.ID_POST + " =?";
        String[] selectionArgs = {id};
        Cursor cursor = db.query(Constants.ASKJCE.TABLE_NAME2,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        Log.d("cursor",cursor.getCount()+"");
        return cursor.getCount();
    }
    public boolean checkLike(String username,String id ){
        String [] columns = {Constants.ASKJCE.USER_NAME,Constants.ASKJCE.ID_POST};
        SQLiteDatabase db = database.getReadableDatabase();
        String selection = Constants.ASKJCE.USER_NAME + " = ?" + " AND " + Constants.ASKJCE.ID_POST + " =?";
        String[] selectionArgs = {username,id};

        Cursor cursor = db.query(Constants.ASKJCE.TABLE_NAME2,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;

    }
    public void dislike(String username,String id ){
        db=database.getWritableDatabase();
        db.delete(Constants.ASKJCE.TABLE_NAME2,Constants.ASKJCE.USER_NAME +" = '"+username+"' AND " +Constants.ASKJCE.ID_POST + " = '"+id+"'",null);
    }
}