package com.example.ahmadsb.ex4_home;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmadsb on 1/7/2018.
 */

public class ContactsFragment extends Fragment {

    List<person> contact_List;
    RecyclerView recyclerView;
    private final int REQUEST_PERMISSON_CODE=2;// random number not important
    // constructor to init the list of contacts
    public ContactsFragment() {

    }


    // init the inflate 'file.xml'
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.contacts_fragment, container, false);
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.recyclerView_id);
        //first step check the permission if exists if yes get all the contacts else request permission
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED){
           getContancts();
        }
        else {
            requestPermissions(
                    new String[]{Manifest.permission.READ_CONTACTS},
                    REQUEST_PERMISSON_CODE);
        }




    }

    // method check the permission
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 2: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getContancts();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void getContancts() {
        contact_List = new ArrayList<>();
        // init the cursor with all the contacts and get the name ,photo and number from lib 'ContactsContract.CommonDataKinds.Phone'
        Cursor mCursor =getActivity().managedQuery(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        int number = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int name = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int photoUri = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI);

        while(mCursor.moveToNext()) {
            // init the list of contacts with name ,number and photo
            String contact_number = mCursor.getString(number);
            String contact_name = mCursor.getString(name);
            String contact_photo = mCursor.getString(photoUri);
            contact_List.add(new person(contact_number,contact_name, contact_photo));
        }
        // update Adapter
        upDataAdapter();
    }
    private  void upDataAdapter(){
        listAdapter adapter = new listAdapter(getActivity(),contact_List,this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
    }
}
