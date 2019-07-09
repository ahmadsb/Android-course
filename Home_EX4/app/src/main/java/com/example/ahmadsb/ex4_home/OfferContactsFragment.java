package com.example.ahmadsb.ex4_home;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfferContactsFragment extends Fragment {
    TextView nameText;
    TextView numberText;
    ImageView photoUriImage;
    final  String DEFAULT_NAME="not found name",DEFAULT_NUMBER="not found number";
    public OfferContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get the saving variables in the fragment and put on object bundle
        Bundle bundle = getArguments();

        View view =inflater.inflate(R.layout.offer_contacts_fragment, container, false);
        nameText = view.findViewById(R.id.contact_Name);
        numberText = view.findViewById(R.id.contact_Number);
        photoUriImage = view.findViewById(R.id.contact_Image);
        /* get the photo and put the bitmap*/
        Bitmap bitmap_photo = null;
        try {
            bitmap_photo  = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.parse(bundle.getString("photoUri","")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        nameText.setText(bundle.getString("name",DEFAULT_NAME));
        numberText.setText(bundle.getString("number",DEFAULT_NUMBER));
        if(bitmap_photo != null)
        {
            Drawable drawable = new BitmapDrawable(getResources(), bitmap_photo);

            photoUriImage.setImageDrawable(drawable);
        }
        // Inflate the layout for this fragment
        return  view;
    }

}
