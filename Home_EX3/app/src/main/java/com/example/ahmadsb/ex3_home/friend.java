package com.example.ahmadsb.ex3_home;

/**
 * Created by ahmadsb on 1/5/2018.
 */

/*class include the number , phone number and type of call my friend*/
public class friend {
    private String numberphone;
    private String namefriend;
    private String type;

    public friend(String numberphone, String namefriend,String type) {
        this.numberphone = numberphone;
        this.namefriend = namefriend;
        this.type = type;
    }

    public String getnumberphone() {
        return numberphone;
    }

    public String getNamefriend() {
        return namefriend;
    }

    public String getType() {
        return type;
    }
}