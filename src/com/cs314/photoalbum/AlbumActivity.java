package com.cs314.photoalbum;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.MediaColumns;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
    
    for (String key : albums.keySet()) {
      System.err.println("album: " + key);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.album, menu);
    return true;
  }

}
