package com.example.ahmadsb.home_ex2;

/**
 * Created by ahmadsb on 12/7/2017.
 */

public class items {
    private double longi;
    private  double lati;
    private int asu;
    private String date;
    private long id;

    //class for the database items
    public items(double longi,double lati,String date,int asu,long id)
    {
        this.longi = longi;
        this.lati = lati;
        this.asu = asu;
        this.id = id;
        this.date = date;
    }
    public double getLongtitude(){
        return longi;
    }
    public double getLatitude(){
        return lati;
    }
    public int getAsu(){
        return asu;
    }
    public long getId() {return id;}
    public String getDate() {return date;}
}