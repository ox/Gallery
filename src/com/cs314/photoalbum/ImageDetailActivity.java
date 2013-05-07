package com.cs314.photoalbum;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * An activity representing a single Image detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ImageListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ImageDetailFragment}.
 */
public class ImageDetailActivity extends FragmentActivity {
	public String locationTag;
	public ArrayList<String> peopleTags = new ArrayList<String>();
	public String path;
	public String selectedWord;
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_detail);
    Intent intent = getIntent();
	Bundle photos = intent.getExtras();
    path = photos.getString(ImageDetailFragment.ARG_ITEM_ID);
    selectedWord = photos.getString("selected");
    // Show the Up button in the action bar.
    // getActionBar().setDisplayHomeAsUpEnabled(true);

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
    //
    // http://developer.android.com/guide/components/fragments.html
    //
    if (savedInstanceState == null) {
      // Create the detail fragment and add it to the activity
      // using a fragment transaction.
      Bundle arguments = new Bundle();
      arguments.putString(ImageDetailFragment.ARG_ITEM_ID, getIntent()
          .getStringExtra(ImageDetailFragment.ARG_ITEM_ID));
      ImageDetailFragment fragment = new ImageDetailFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction()
          .add(R.id.image_detail_container, fragment).commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    	case R.id.addTag:
    		final Dialog dialog = new Dialog(this);
    		  dialog.setContentView(R.layout.add_tag);
    		  dialog.setTitle("Add Tag");
    		  Button dialogButton = (Button) dialog.findViewById(R.id.tagOKbutton);
    		  dialogButton.setOnClickListener(new OnClickListener() {
    			  @Override
    				public void onClick(View v) {
    				  EditText editText = (EditText) dialog.findViewById(R.id.location_tag);
    				  String locationInput = editText.getText().toString();
    				  if(locationInput!="")
    					  locationTag=locationInput;
    				  EditText editText2 = (EditText) dialog.findViewById(R.id.people_tag);
    				  String peopleInput = editText2.getText().toString();
    				  if (peopleInput!="")
    					  peopleTags.add(peopleInput);
    				  dialog.dismiss();
    				  Toast.makeText(getApplicationContext(),
    				             locationTag, Toast.LENGTH_SHORT).show();
    				}
    		  });
    		  dialog.show();
    		  return true;
    	case R.id.move_it:
    		final Dialog dialog2 = new Dialog(this);
    		  dialog2.setContentView(R.layout.edit_dialog);
    		  dialog2.setTitle("Move Photo");
    		  Button dialogButton2 = (Button) dialog2.findViewById(R.id.dialogButtonOK);
    		  dialogButton2.setOnClickListener(new OnClickListener() {
    			  @Override
    				public void onClick(View v) {
    				  EditText editText = (EditText) dialog2.findViewById(R.id.new_title);
    				  String albumDestination = editText.getText().toString();
    				 
    				  if(AlbumActivity.albums.get(albumDestination) != null) {
    					  ArrayList<String> temp=AlbumActivity.albums.get(albumDestination);
    					  temp.add(path); 
    					  AlbumActivity.albums.remove(albumDestination);
    					  AlbumActivity.albums.put(albumDestination, temp);
    					  dialog2.dismiss();
    					  ArrayList<String> temp2=AlbumActivity.albums.get(selectedWord);
    					  temp2.remove(path); 
    					  AlbumActivity.albums.remove(selectedWord);
    					  AlbumActivity.albums.put(selectedWord, temp2);
    					  

    				  }
    				  
    				}
    		  });
    		  dialog2.show();
    		  return true;
	    case android.R.id.home:
	    	// This ID represents the Home or Up button. In the case of this
	        // activity, the Up button is shown. Use NavUtils to allow users
	        // to navigate up one level in the application structure. For
	        // more details, see the Navigation pattern on Android Design:
	        //
	        // http://developer.android.com/design/patterns/navigation.html#up-vs-back
	    	//
	    	NavUtils.navigateUpTo(this, new Intent(this, ImageListActivity.class));
	        return true;
	    default:
	          return false;
    }
    //return super.onOptionsItemSelected(item);
  }
  
  public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.add_tag_inflater, menu);
	    return true;
  }
  
}
