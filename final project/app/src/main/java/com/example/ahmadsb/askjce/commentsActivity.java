package com.example.ahmadsb.askjce;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;
import com.example.ahmadsb.askjce.tab1Posts.RecyclerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class commentsActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtxt_comment;
    Button btn_sendComments;
    SQLiteDatabase db;
    String idpost=null;
    LinearLayout idLayoutComment;
    DBHelper database;
    public ArrayList<Comment> list = new ArrayList<Comment>();
    RecyclerView comments_recycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        idLayoutComment=findViewById(R.id.idLayoutComment);
        comments_recycler=(RecyclerView)findViewById(R.id.comments_recycler);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        database=new DBHelper(this);

        edtxt_comment=(EditText)findViewById(R.id.edtxt_comment);
        btn_sendComments=(Button) findViewById(R.id.btn_sendComments);
        btn_sendComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // img,username ,date,time and comment (5)
                String comment=edtxt_comment.getText().toString();
                String timeComment=getTimeComment();
                String dateComment=getDateComment();
//                (((Activity)context).getIntent().getStringExtra("username"))
                String username=getIntent().getStringExtra("username");
                byte[] photo=getIntent().getByteArrayExtra("photo");

                writeDateOfComment(username,idpost,comment,timeComment,dateComment,photo);
                upDateRecyclerAdapter();

                edtxt_comment.setText("");
            }
        });
        idpost=getIntent().getStringExtra("postid");
        Log.d("ttt",idpost);
        readDataOFComments();
        upDateRecyclerAdapter();
    }
    public String getTimeComment(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c);

        return formattedDate;
    }
    public String getDateComment(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        return formattedDate;

    }
    public void writeDateOfComment(String username,String idPost,String comment,String timeComment,String dateComment,byte[]photo){
        db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.ASKJCE.USER_NAME,username);
        values.put(Constants.ASKJCE.ID_POST,idPost);
        values.put(Constants.ASKJCE.MSG_COMMENT,comment);
        values.put(Constants.ASKJCE.KEY_IMG,photo);
        values.put(Constants.ASKJCE.TIME,timeComment);
        values.put(Constants.ASKJCE.DATE,dateComment);


        long id;
        id = db.insert(Constants.ASKJCE.TABLE_NAME_COMMENT, null, values);
        // check if the insert
        if (id == -1) {

            Toast.makeText(this, "not successful write data of comment", Toast.LENGTH_SHORT).show();
        } else {

            Snackbar.make(idLayoutComment, " successful write data of comment", Snackbar.LENGTH_SHORT).show();
        }
        list.add(new Comment(username,idPost,comment,timeComment,dateComment,photo));
        db.close();
    }
    protected void upDateRecyclerAdapter(){
        RecyclerAdapterComments adapter = new RecyclerAdapterComments(commentsActivity.this,list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        comments_recycler.setAdapter(adapter);
        comments_recycler.setLayoutManager(mLayoutManager);
    }

    public void readDataOFComments(){
        db = database.getReadableDatabase();
        Cursor res;
        String [] selectionArgs = {idpost};
        res = db.query("tableNameComment",null,Constants.ASKJCE.ID_POST + "= ?",selectionArgs,null,null,null);

        list.clear();
        if (!res.equals(null) && res.moveToFirst()) {
            while (!res.isAfterLast()) {
                String username = res.getString(res.getColumnIndex(Constants.ASKJCE.USER_NAME));
                String idPost = res.getString(res.getColumnIndex(Constants.ASKJCE.ID_POST));
                String comment = res.getString(res.getColumnIndex(Constants.ASKJCE.MSG_COMMENT));
                String timeComment = res.getString(res.getColumnIndex(Constants.ASKJCE.TIME));
                String dateComment = res.getString(res.getColumnIndex(Constants.ASKJCE.DATE));
                byte[] photo = res.getBlob(res.getColumnIndex(Constants.ASKJCE.KEY_IMG));

                list.add(new Comment(username,idPost,comment,timeComment,dateComment,photo));
                res.moveToNext();
            }

        }

    }
}
