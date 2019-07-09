package com.example.ahmadsb.askjce.tabprofile;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmadsb.askjce.R;
import com.example.ahmadsb.askjce.DataBase.Constants;
import com.example.ahmadsb.askjce.DataBase.DBHelper;
import com.example.ahmadsb.askjce.loginUser.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;



public class Tab3 extends Fragment {
    final static int REQUEST_CODE_GALLERY=1000;

    TextView profile_setting,profile_name,txtBIO,profile_call,profile_message;
    TextView edtxtBIO;
    ImageView profile_image;
    String phoneNumber,emailOfUser;
    public ArrayList<User> list = new ArrayList<>();
    SQLiteDatabase db;
    DBHelper dbHelper;
    public Tab3() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_tab3, container, false);
        profile_name=(TextView) rootView.findViewById(R.id.profile_name);
        txtBIO=(TextView)rootView.findViewById(R.id.txtBIO);
        dbHelper=new DBHelper(getActivity());

        profile_image=(ImageView)rootView.findViewById(R.id.profile_image);
        profile_image.setBackgroundResource(R.drawable.image_user);



        if(getArguments()!=null){
            profile_name.setText(getArguments().getString("username"));
            phoneNumber=getArguments().getString("phonenumber");
            emailOfUser=getArguments().getString("email");
            txtBIO.setText(getArguments().getString("bio"));
        }

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
                intentGallery.setType("image/*");
                startActivityForResult(intentGallery,REQUEST_CODE_GALLERY);

            }
        });

        profile_setting=(TextView) rootView.findViewById(R.id.profile_setting);
        profile_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SettingActivity.class);
                intent.putExtra("username",getArguments().getString("username"));
                startActivity(intent);
            }
        });

        edtxtBIO=(TextView)rootView.findViewById(R.id.edtxtBIO);
        edtxtBIO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getContext(),BIOActivity.class);
                intent.putExtra("email",emailOfUser);
                intent.putExtra("username",profile_name.getText().toString());
                intent.putExtra("phonenumber",phoneNumber);

                startActivity(intent);
            }
        });


        profile_call=(TextView)rootView.findViewById(R.id.profile_call);
        profile_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_call.setHint(phoneNumber);
                Intent intentCall= new Intent(Intent.ACTION_DIAL);
                intentCall.setData(Uri.parse("tel:"+profile_call.getHint()));
                startActivity(intentCall);
            }
        });
        profile_message=(TextView)rootView.findViewById(R.id.profile_message);
        profile_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse(emailOfUser));
//                startActivity(emailIntent);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null)));

            }
        });
        return rootView;
    }



    // photo from gallery with connection request code '1000' and with ACK 'resultCode'
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  REQUEST_CODE_GALLERY  && resultCode == RESULT_OK){
            Uri uri=data.getData();
//            profile_image.setImageURI(uri);
//            profile_image.setBackgroundResource(0);

            try {
                InputStream inputStream= getActivity().getContentResolver().openInputStream(uri);
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
                insertImage(decodeStream);
                profile_image.setImageBitmap(decodeStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        readData();// return date in list
        profile_image.setBackgroundResource(R.drawable.image_user);
        setTXT();
    }
    protected void readData(){

        db = dbHelper.getReadableDatabase();
        Cursor res;

        res = db.rawQuery("SELECT * FROM tableNameLogin", null);
        list.clear();
        if (res.moveToFirst()) {
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
    protected void setTXT(){
        readData();

        for (int i=0;i<list.size();i++){
            //if same email of user so set edTxtBio
            if(list.get(i).getEmail().equals(emailOfUser)){
                txtBIO.setText(list.get(i).getBIO().toString());
                profile_name.setText(list.get(i).getUsername());
                profile_call.setHint(phoneNumber);
                Bitmap bm = BitmapFactory.decodeByteArray(list.get(i).getPhoto(), 0 ,list.get(i).getPhoto().length);
                if(list.get(i).getPhoto().length==0){
                    profile_image.setBackgroundResource(R.drawable.image_user);


                }else {

                    String userName =(getActivity().getIntent().getStringExtra("username"));
                    for ( int j=0;j<list.size();j++) {

                        if(list.get(j).getUsername().equals(userName));
                        {
                            db = dbHelper.getWritableDatabase();
                            ContentValues values = new ContentValues();
                            values.put(Constants.ASKJCE.KEY_IMG, list.get(j).getPhoto());
                            db.update(Constants.ASKJCE.TABLE_NAME_POST, values, Constants.ASKJCE.USER_NAME + "=?", new String[]{userName});
                        }
                    }
                    if(bm!=null){
                        profile_image.setBackgroundResource(0);
                        profile_image.setImageBitmap(bm);
                    }

                }

            }
        }

    }

    public void insertImage( Bitmap bitmap) throws SQLiteException {
        byte[] image=getBitmapAsByteArray(bitmap);
        readData();

        db = dbHelper.getWritableDatabase();

        for (int i=0;i<list.size();i++){
            //if same email of user so set edTxtBio
            if(list.get(i).getUsername().equals(getArguments().getString("username"))){

                db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Constants.ASKJCE.KEY_IMG, image);
                long id;
                id =     db.update(Constants.ASKJCE.TABLE_NAME, values,Constants.ASKJCE._ID  , null);

                // check if the insert
                if (id == -1) {
                    Toast.makeText(getActivity(), "not successful write data of image", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), " successful write data of image", Toast.LENGTH_SHORT).show();
                }
                db.close();
            }
        }



    }
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
