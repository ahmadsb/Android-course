package com.example.ahmadsb.ex4_home;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ahmadsb on 1/7/2018.
 */


public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder> {
    List<person> mContactList;
    Context context;
    private LayoutInflater mInflater;
    private Fragment fragment;
    //constructor for init the variables fields of class
    public listAdapter(Context context, List<person> contactList, Fragment fragment)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mContactList = contactList;
        this.fragment = fragment;

    }
    // oncreateViewHolder to init the inflate of class 'file.XML'
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row, parent, false);

        return new ViewHolder(view);
    }

    // foreach item init the variables of line from name ,number and click on layout 'that offer details
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // init the number and name
        holder.nameContact.setText(mContactList.get(position).getName());
        holder.numberContact.setText(mContactList.get(position).getNumber());
        // get the number , name and photo if founded to intent to second fragment ' of offer  details'
        final String name= mContactList.get(position).getName();
        final String number=mContactList.get(position).getNumber();
        final String photoUri = mContactList.get(position).getPhotoUri();

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = fragment.getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                OfferContactsFragment fragment_Second = new OfferContactsFragment();
                /*
                * put the name ,number and photo in object bundle
                * and set the arguments of fragment second for
                * send the details to second fragment*/
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("number",number);
                bundle.putString("photoUri",photoUri);
                fragment_Second.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame_id, fragment_Second);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    /*methods returns the size of  list of contacts */
    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameContact;
        TextView numberContact;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            // init the components
            nameContact = itemView.findViewById(R.id.name_id);
            layout = itemView.findViewById(R.id.row_item);
            numberContact=itemView.findViewById(R.id.number_id);
        }
    }
}
