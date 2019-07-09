package com.example.ahmadsb.ex4_home;

/**
 * Created by ahmadsb on 1/7/2018.
 */


class person {
    private String mName;
    private String mNumber;
    private String mPhotoUri;
    person(String mName, String mNumber, String mPhotoUri) {
        this.mName = mName;
        this.mNumber = mNumber;
        this.mPhotoUri = mPhotoUri;
    }

    public String getNumber() {
        return mNumber;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUri() {
        return mPhotoUri;
    }
}
