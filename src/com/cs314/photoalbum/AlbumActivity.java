package com.cs314.photoalbum;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

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


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album);

    ArrayList<String> listOfAllImages = new ArrayList<String>();
    String absolutePathOfImage = null;
    Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    String[] projection = { MediaColumns.DATA,
        MediaColumns.DISPLAY_NAME};

    Cursor cursor = this.managedQuery(uri, projection, null, null, null);
    ArrayList<String> albumFolders = new ArrayList<String>();
    
    int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index);
      absolutePathOfImage = absolutePathOfImage.replaceAll("^(.*)\\/(\\w*\\/.*?\\.(?:jpg|png))$", "$2");
      String[] folderFilePair = absolutePathOfImage.split("/");
      
      if (!albumFolders.contains(folderFilePair[0]) && !folderFilePair[0].isEmpty()) {
        albumFolders.add(folderFilePair[0]);
        System.err.println("sourced from: " + absolutePathOfImage);
      }
      
      listOfAllImages.add(absolutePathOfImage);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.album, menu);
    return true;
  }

}
