package com.cs314.photoalbum;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AlbumActivity extends Activity {
  Hashtable<String, ArrayList<String>> albums = new Hashtable<String, ArrayList<String>>();

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

    final ArrayList<String> albumNames = new ArrayList<String>();
    for (String key : albums.keySet()) {
      albumNames.add(key);
    }

    final ListView listview = (ListView) findViewById(R.id.listview);
    final StableArrayAdapter adapter = new StableArrayAdapter(this,
        android.R.layout.simple_list_item_1, albumNames);
    listview.setAdapter(adapter);
    
    listview.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position,
          long id) {
        Toast.makeText(getApplicationContext(),
             albumNames.get(position), Toast.LENGTH_SHORT).show();
      }
    }); 
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.album, menu);
    return true;
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
