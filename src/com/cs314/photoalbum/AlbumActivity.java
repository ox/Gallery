package com.cs314.photoalbum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumActivity extends Activity {
  Context context = this;
  public Hashtable<String, ArrayList<String>> albums = new Hashtable<String, ArrayList<String>>();
  public ArrayList<String> albumNames = new ArrayList<String>();
  public ListView listview;
  public ArrayAdapter<String> adapter;
  public String selectedWord;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album);

    String absolutePathOfImage = null, albumFilePath = null;
    Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME};

    Cursor cursor = this.managedQuery(uri, projection, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index);
      albumFilePath = absolutePathOfImage.replaceAll("^(.*)\\/(\\w*\\/.*?\\.(?:jpg|png))$", "$2");
      String[] folderFilePair = albumFilePath.split("/");

      if (!folderFilePair[0].isEmpty()) {
        ArrayList<String> album = albums.get(folderFilePair[0]);
        if (album == null) {
          album = new ArrayList<String>();
        }

        album.add(absolutePathOfImage);
        albums.put(folderFilePair[0], album);
      }
    }

    for (String key : albums.keySet()) {
      albumNames.add(key);
    }
    
    listview = (ListView) findViewById(R.id.listview);
    adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, albumNames);
    listview.setAdapter(adapter);
    registerForContextMenu(listview);
    
    listview.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        Toast.makeText(getApplicationContext(),
             albumNames.get(position), Toast.LENGTH_SHORT).show();
        
        ArrayList<String> photos = albums.get(albumNames.get(position));
        
        Intent albumDetailIntent = new Intent(context, ImageListActivity.class);
        albumDetailIntent.putExtra("photos", photos);
        startActivity(albumDetailIntent);
      }
    }); 
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.album, menu);
    return true;
  }
  public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.addAlbum:
	    	albumDialog();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
 
  public void albumDialog() {
	  final Dialog dialog = new Dialog(this);
	  dialog.setContentView(R.layout.album_dialog);
	  dialog.setTitle("Create New Album");
	  Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
	  dialogButton.setOnClickListener(new OnClickListener() {
		  @Override
			public void onClick(View v) {
			  EditText editText = (EditText) dialog.findViewById(R.id.new_album);
			  String newAlbumName = editText.getText().toString();
			  albumNames.add(newAlbumName);
			  ArrayList<String> t1 = new ArrayList<String>();
			  albums.put(newAlbumName, t1);
			  adapter.notifyDataSetChanged();
			  dialog.dismiss();
			}
	  });
	  dialog.show();
  }
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      // TODO Auto-generated method stub
	  super.onCreateContextMenu(menu, v, menuInfo);  
	  AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) menuInfo;
	  selectedWord = ((TextView) info.targetView).getText().toString();
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.edit, menu);
  }
  
  public boolean onContextItemSelected(MenuItem item) {  
	  switch (item.getItemId()) {
      case R.id.option1: //edit
    	  editDialog(); 
          return true;
      case R.id.option2: //delete	
    	  albumNames.remove(selectedWord);
    	  adapter.notifyDataSetChanged();
          return true;
      default:
          return false;
	  }
  }  
  public void editDialog() {
	  final Dialog dialog = new Dialog(this);
	  dialog.setContentView(R.layout.edit_dialog);
	  dialog.setTitle("Edit Item Title");
	  Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
	  dialogButton.setOnClickListener(new OnClickListener() {
		  @Override
			public void onClick(View v) {
			  EditText editText = (EditText) dialog.findViewById(R.id.new_title);
			  String newTitle = editText.getText().toString();
			  ArrayList<String>temp = albums.get(selectedWord);
			  albums.remove(selectedWord);
			  albums.put(newTitle, temp);
			  albumNames.remove(selectedWord);
			  albumNames.add(newTitle);
			  adapter.notifyDataSetChanged();
			  dialog.dismiss();
			}
	  });
	  dialog.show();
  }  
  private class StableArrayAdapter extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }

    public long getItemId(int position) {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    public boolean hasStableIds() {
      return true;
    }

  }

}
