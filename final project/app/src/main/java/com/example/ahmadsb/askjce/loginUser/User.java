package com.example.ahmadsb.askjce.loginUser;

/**
 * Created by ahmadsb on 12/31/2017.
 */

public class User {

    private String username;
    private String email;
    private String password;
    private String phonnumber;
    private String BIO;
    private byte[] photo;


    //class for the database items
    public User(String username,String email,String password,String phonnumber,String BIO,byte [] photo)
    {
        this.username = username;
        this.photo = photo;
        this.email = email;
        this.password = password;
        this.phonnumber = phonnumber;
        this.BIO = BIO;
    }

    public void setBIO(String BIO) {
        this.BIO = BIO;
    }

    public String getBIO() {return BIO;}
    public byte[] getPhoto() {return photo;}
    public String getPhonnumber() {
        return phonnumber;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){return email;}
    public String getPassword(){return password;}
}