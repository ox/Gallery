package com.cs314.photoalbum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A fragment representing a single Image detail screen. This fragment is either
 * contained in a {@link ImageListActivity} in two-pane mode (on tablets) or a
 * {@link ImageDetailActivity} on handsets.
 */
public class ImageDetailFragment extends Fragment {
  /**
   * The fragment argument representing the item ID that this fragment
   * represents.
   */
  public static final String ARG_ITEM_ID = "item_id";

  private String imagePath = null;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public ImageDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_ITEM_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      imagePath = getArguments().getString(ARG_ITEM_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_image_detail, container,
        false);

    // Show the dummy content as text in a TextView.
    if (imagePath != null) {
      Bitmap image = decodeFile(new File(imagePath));
      ((ImageView) rootView.findViewById(R.id.image_detail)).setImageBitmap(image);
    }


    return rootView;
  }
  
  private Bitmap decodeFile(File f){
    Bitmap b = null;
    try {
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = new FileInputStream(f);
        BitmapFactory.decodeStream(fis, null, o);
        fis.close();

        int scale = 1;
        int IMAGE_MAX_SIZE = 1680;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int)Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / 
               (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
    } catch (IOException e) { }
    return b;
}
}
