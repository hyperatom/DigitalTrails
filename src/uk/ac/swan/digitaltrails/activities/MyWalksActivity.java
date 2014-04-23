package uk.ac.swan.digitaltrails.activities;

import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.fragments.AddWaypointFragment;
import uk.ac.swan.digitaltrails.fragments.CreateWalkFragment;
import uk.ac.swan.digitaltrails.fragments.EditWalkFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkListFragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

@SuppressLint("NewApi")
public class MyWalksActivity extends ActionBarActivity implements
	MyWalkListFragment.OnWalkSelectedListener, AddWaypointFragment.OnMapClosedListener {
	
	private static final String TAG = "MyWalksActivity";
	private List<Waypoint> mWaypointList;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_walks);
		// check if using small layout
		if (findViewById(R.id.fragment_container) != null) {

			// if restoring from prev state do nothing
			if (savedInstanceState != null) {
				return;
			}

			Log.d(TAG, "Container not ewas null");
			MyWalkListFragment walkListFragment = new MyWalkListFragment();

			walkListFragment.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, walkListFragment).commit();
		} else {
			// if 2 pane
			MyWalkDetailsFragment detailsFrag = new MyWalkDetailsFragment();
			MyWalkListFragment walkListFragment = new MyWalkListFragment();
			walkListFragment.setArguments(getIntent().getExtras());
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(R.id.fragment_container_thin, walkListFragment);
			transaction.add(R.id.fragment_container_large, detailsFrag);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onWalkSelected(int position) {
		
		MyWalkDetailsFragment detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);

		if (detailsFrag != null) {
			// if available and we are in 2-pane view.
			detailsFrag.updateDetailsView(position);
		} else if (findViewById(R.id.fragment_container) != null){
			// if in 1 pane view
			MyWalkDetailsFragment newDetailsFragment = new MyWalkDetailsFragment();
			Bundle args = new Bundle();
			args.putInt(MyWalkDetailsFragment.ARG_POSITION, position);
			newDetailsFragment.setArguments(args);

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			transaction.replace(R.id.fragment_container, newDetailsFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		} else {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			detailsFrag = new MyWalkDetailsFragment();
			transaction.replace(R.id.fragment_container_large, detailsFrag);
			transaction.addToBackStack(null);
			transaction.commit();
			detailsFrag.updateDetailsView(position);
		}
		
	}
	
	@Override
	public void onMapClosed(List<Waypoint> waypointList) {
		Log.d(TAG, "onMapClosed call");
		mWaypointList = waypointList;
	}
	
	@Override
	public void onBackPressed() {
		// Do different things depending on our chosen fragment.
		MyWalkListFragment walkFrag = (MyWalkListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_thin);
		if (walkFrag.isVisible()) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
		}
		super.onBackPressed();
	}
	
	public void accountButton(MenuItem menu){
        Intent intent = new Intent(this, EditAccountActivity.class);
        startActivity(intent);
    }
	
	public void logOutButton(MenuItem menu){
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
	
	public void settingsButton(MenuItem menu){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

	public void editWalkButtonOnClick(View view){
		Log.d(TAG, "editWalkButton Pressed");

		MyWalkDetailsFragment detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		EditWalkFragment editWalkFrag = new EditWalkFragment();
		Fragment thinFrag = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_thin);

		// if 2 panes
		if (detailsFrag != null && thinFrag != null) {
			Log.d(TAG, "2 panes - replace and remove");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.hide(thinFrag);
			transaction.replace(R.id.fragment_container_large, editWalkFrag);
			
		} else {
			Log.d(TAG, "1 pane, replace fragment_container");
			transaction.replace(R.id.fragment_container, editWalkFrag);
		}
		
		transaction.addToBackStack(null);
		transaction.commit();

	}
	
	// EditWalkFragment functions
	
	public void addWaypointButton(View view){
		//Intent intent = new Intent(this, AddWaypointActivity.class);
		//startActivity(intent);
	}

	public void cancelEditButton(View view){
		onBackPressed();
	}
	
	public void saveWalkButton(View view){
		// Waiting on implementation
	}
	
	/**
	 * What we do when createWalkButton in the ListFragment is pressed. Swaps out current fragments and creates a CreateWalkFragment.
	 * @param view
	 */
	
	public void listCreateWalkButtonOnClick(View view){
		Log.d(TAG, "CreateWalkButtonOnClick Pressed");
		MyWalkDetailsFragment detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		CreateWalkFragment createFrag = new CreateWalkFragment();
		Fragment thinFrag = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_thin);

		// if 2 panes
		if (detailsFrag != null && thinFrag != null) {
			Log.d(TAG, "2 panes - replace and remove");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.hide(thinFrag);
			transaction.replace(R.id.fragment_container_large, createFrag);
			
		} else {
			Log.d(TAG, "1 pane, replace fragment_container");
			transaction.replace(R.id.fragment_container, createFrag);
		}
		
		transaction.addToBackStack(null);
		transaction.commit();

	}
	
	/**
	 * Called when CreateWalkFragment create button is pressed. Create the walk in the database.
	 * @param view
	 */
	public void createCreateButtonOnClick(View view) {
		// Add values, insert into DB, clear values for the next table. repeat.
		ContentValues values = new ContentValues();
		// Walk Table
		values.put(WhiteRockContract.Walk.DURATION_MINUTES, 0);
		values.put(WhiteRockContract.Walk.DISTNACE_MILES, 0);
		values.put(WhiteRockContract.Walk.DIFFICULTY_RATING, 0);
		values.put(WhiteRockContract.Walk.DOWNLOAD_COUNT, 0);
		getContentResolver().insert(WhiteRockContract.Walk.CONTENT_URI, values);
		values.clear();
		
		// get ID of the inserted walk (it'll be the final ID)
		String[] projection = { WhiteRockContract.Walk.ID };
		Cursor cursor = getContentResolver().query(WhiteRockContract.Walk.CONTENT_URI, projection, WhiteRockContract.Walk.ID + " >= 0", null, WhiteRockContract.Walk.ID + " COLLATE LOCALIZED ASC");
		cursor.moveToLast();
		int walkId = cursor.getInt(0);
		Log.d(TAG, "walkId value = " + walkId);
		// EnglishWalkDescriptions table
		EditText titleView = (EditText) findViewById(R.id.walkNameEdit);
		EditText descr = (EditText) findViewById(R.id.walkDescriptionEdit);
		values.put(WhiteRockContract.EnglishWalkDescriptions.TITLE, titleView.getText().toString());
		values.put(WhiteRockContract.EnglishWalkDescriptions.SHORT_DESCR, descr.getText().toString().substring(0, descr.getText().length()/2));
		values.put(WhiteRockContract.EnglishWalkDescriptions.LONG_DESCR, descr.getText().toString());
		values.put(WhiteRockContract.EnglishWalkDescriptions.WALK_ID, walkId);
		getContentResolver().insert(WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI, values);
		
		values.clear();
		// Waypoints
		for (Waypoint wp : mWaypointList) {
			values.put(WhiteRockContract.Waypoint.IS_REQUEST, 0);
			values.put(WhiteRockContract.Waypoint.LATITUDE, wp.getLatitude());
			values.put(WhiteRockContract.Waypoint.LONGITUDE, wp.getLongitude());
			values.put(WhiteRockContract.Waypoint.VISIT_ORDER, wp.getId());
			values.put(WhiteRockContract.Waypoint.WALK_ID, walkId);
			values.put(WhiteRockContract.Waypoint.USER_ID, 0); // TODO: Use real user_Id value
			getContentResolver().insert(WhiteRockContract.Waypoint.CONTENT_URI, values);
			values.clear();		

			// get ID of the inserted waypoint (it'll be the final ID)
			projection[0] = WhiteRockContract.Waypoint.ID;
			cursor = getContentResolver().query(WhiteRockContract.Waypoint.CONTENT_URI, projection, WhiteRockContract.Waypoint.ID + " >= 0", null, WhiteRockContract.Waypoint.ID + " COLLATE LOCALIZED ASC");
			cursor.moveToLast();
			int waypointId = cursor.getInt(0);
			
			values.put(WhiteRockContract.EnglishWaypointDescriptions.TITLE, wp.getTitle());
			// TODO: replace dummy values with real ones
			values.put(WhiteRockContract.EnglishWaypointDescriptions.LONG_DESCR, "Dummy Long Descr");
			values.put(WhiteRockContract.EnglishWaypointDescriptions.SHORT_DESCR, "Dummy Short Descr");
			values.put(WhiteRockContract.EnglishWaypointDescriptions.WAYPOINT_ID, waypointId);
			getContentResolver().insert(WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI, values);
			values.clear();
		}
		
		mWaypointList.clear();
		cursor.close();
		onBackPressed();
		
	}
	
	/**
	 * Called when CreateWalkFragment add waypoint button is pressed. Swap in AddWaypointFragment.
	 * @param view
	 */
	public void createAddWaypointButtonOnClick(View view) {
		Log.d(TAG, "createAddWaypointButton Pressed");

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		AddWaypointFragment waypointFrag = new AddWaypointFragment();
	
		if (findViewById(R.id.fragment_container_large) != null) {
			Log.d(TAG, "2 panes - replace and remove");
			transaction.replace(R.id.fragment_container_large, waypointFrag);
		} else {
			Log.d(TAG, "1 pane, replace fragment_container");
			transaction.replace(R.id.fragment_container, waypointFrag);
		}
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	// TODO: Implement deleting etc.
	public void deleteWalkButtonOnClick(View view){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setPositiveButton(R.string.confirm_delete_walk, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // Refresh page with the walk removed
		           }
		       });
		// Add the negative button
		builder.setNegativeButton(R.string.cancel_delete_walk, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.delete_walk_title)
			.setMessage(R.string.delete_walk_message);
		//Build and create dialog
		builder.create();
		builder.show();
	}


}
