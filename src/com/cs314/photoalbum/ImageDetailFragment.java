package com.cs314.photoalbum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_image_detail, container,
        false);

    // Show the dummy content as text in a TextView.
//    if (mItem != null) {
//      ((TextView) rootView.findViewById(R.id.image_detail)).setText(mItem.content);
//    }

    return rootView;
  }
}
