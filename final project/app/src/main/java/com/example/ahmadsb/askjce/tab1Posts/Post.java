package com.example.ahmadsb.askjce.tab1Posts;

/**
 * Created by ahmadsb on 1/4/2018.
 */


public class Post {
    private final String id;
    private  byte[] photo;
    private String postText,fromLocation,Date,Time,username;

    //class for the database items
    public Post(String postText,String fromLocation,String Date,String Time,String username,byte [] photo,String id)
    {
        this.username=username;
        this.id=id;
        this.photo=photo;
        this.postText=postText;
        this.fromLocation=fromLocation;
        this.Date=Date;
        this.Time=Time;

    }

    public String getUsername() {
        return username;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPostText() {return postText;}
    public String getId() {return id;}
    public byte[] getPhoto() {return photo;}
    public String getFromLocation() {
        return fromLocation;
    }
    public String getDate() {
        return Date;
    }
    public String getTime() {
        return Time;
    }
}