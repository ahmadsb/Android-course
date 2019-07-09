package com.example.ahmadsb.lab_ex9;

public class oneCall {
    private String numberphone;
    private String DateCall;
    private String Duration;
    private String type;

    public oneCall(String numberphone, String DateCall, String duration, String type) {
        this.numberphone = numberphone;
        this.DateCall = DateCall;
        this.Duration = duration;
        this.type = type;
    }

    public String getnumberphone() {
        return numberphone;
    }

    public String getDateCall() {
        return DateCall;
    }

    public String getDuration() {
        return Duration;
    }

    public String getType() {
        return type;
    }
}
