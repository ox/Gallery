package com.cs314.photoalbum;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An activity representing a list of Images. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ImageDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ImageListFragment} and the item details (if present) is a
 * {@link ImageDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ImageListFragment.Callbacks} interface to listen for item selections.
 */
public class ImageListActivity extends FragmentActivity implements
		ImageListFragment.Callbacks {

	public static ArrayList<String> photoPaths;
	public static String selectedWord;
	public static Bitmap bitmap;
	public static ArrayAdapter<String> adapter;
	public static final int REQUEST_CODE = 1;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_list);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.image_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ImageListFragment) getSupportFragmentManager().findFragmentById(
					R.id.image_list)).setActivateOnItemClick(true);
		}

		Intent intent = getIntent();
		Bundle photos = intent.getExtras();
		photoPaths = photos.getStringArrayList("photos");
		selectedWord = photos.getString("selected");
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, photoPaths);

		((ImageListFragment) getSupportFragmentManager().findFragmentById(
				R.id.image_list)).setListAdapter(adapter);
		//registerForContextMenu((ListView) findViewById(R.layout.activity_image_list));

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo, menu);
		return true;
	}

	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		selectedWord = ((TextView) info.targetView).getText().toString();
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.photoremove, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.err.println("item: " + item.toString());
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.addPhoto:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			startActivityForResult(intent, REQUEST_CODE);
			return true;
		default:
			return false;
		}
		// return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link ImageListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = getPath(selectedImageUri);
				ArrayList<String> temp = AlbumActivity.albums.get(selectedWord);
				temp.add(selectedImagePath);
				AlbumActivity.albums.remove(selectedWord);
				AlbumActivity.albums.put(selectedWord, temp);
				photoPaths.add(selectedImagePath);
				adapter.notifyDataSetChanged();
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public void onItemSelected(long id) {
		System.err.println("selected id: " + id);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(ImageDetailFragment.ARG_ITEM_ID,
					photoPaths.get((int) id));
			ImageDetailFragment fragment = new ImageDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.image_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, ImageDetailActivity.class);
			 Bundle extras = new Bundle();
		     extras.putString(ImageDetailFragment.ARG_ITEM_ID,
						photoPaths.get((int) id));
		     extras.putString("selected",selectedWord);
		     detailIntent.putExtras(extras);
			startActivity(detailIntent);
		}
	}
}
