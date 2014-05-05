package uk.ac.swan.digitaltrails.activities;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.fragments.AddWaypointMapFragment;
import uk.ac.swan.digitaltrails.fragments.CreateWalkFragment;
import uk.ac.swan.digitaltrails.fragments.EditWalkFragment;
import uk.ac.swan.digitaltrails.fragments.EditWaypointMapFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkListFragment;
import uk.ac.swan.digitaltrails.fragments.WalkListFragment;
import uk.ac.swan.digitaltrails.utils.GlobalFlags;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
MyWalkListFragment.OnWalkSelectedListener, AddWaypointMapFragment.OnMapClosedListener {

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

			Log.d(TAG, "Container was not null");
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
			Bundle args = new Bundle();
			args.putInt(MyWalkDetailsFragment.ARG_POSITION, -1);
			detailsFrag.setArguments(args);
			transaction.add(R.id.fragment_container_large, detailsFrag);
			transaction.addToBackStack(null);
			transaction.commit();
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
		WalkListFragment listFrag = (WalkListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_thin);
		if (listFrag != null) {

		} else if (findViewById(R.id.fragment_container) != null) {
			listFrag = (WalkListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		}

		position = listFrag.getWalkIds().get(position);	
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

	/**
	 * Load the EditWalkFragment after clicking on "Edit"
	 * @param view
	 */
	public void editWalkButtonOnClick(View view){
		Log.d(TAG, "editWalkButton Pressed");

		EditWalkFragment editWalkFrag = new EditWalkFragment();
		Fragment thinFrag = (Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_thin);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		MyWalkDetailsFragment detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);
		Bundle args = new Bundle();

		// if 2 panes
		if (thinFrag != null) {
			Log.d(TAG, "2 panes - replace and remove");
			transaction.remove(thinFrag);
			args.putInt(EditWalkFragment.ARG_POSITION, detailsFrag.getCurrentPosition());
			editWalkFrag.setArguments(args);
			transaction.replace(R.id.fragment_container_large, editWalkFrag);
		} else {
			Log.d(TAG, "1 pane, replace fragment_container");
			detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
			args.putInt(EditWalkFragment.ARG_POSITION, detailsFrag.getCurrentPosition());
			editWalkFrag.setArguments(args);
			transaction.replace(R.id.fragment_container, editWalkFrag);
		}

		transaction.addToBackStack(null);
		transaction.commit();

	}

	// EditWalkFragment functions

	public void editAddWaypointButtonOnClick(View view){
		Log.d(TAG, "editAddWaypointButton Pressed");

		Bundle args = new Bundle();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		EditWaypointMapFragment waypointFrag = new EditWaypointMapFragment();
		if (findViewById(R.id.fragment_container_large) != null) {
			Log.d(TAG, "2 panes");
			EditWalkFragment editFrag = (EditWalkFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);
			args.putInt(EditWaypointMapFragment.ARG_POSITION, editFrag.getCurrentPosition());
			waypointFrag.setArguments(args);
			transaction.replace(R.id.fragment_container_large, waypointFrag);
		} else {
			Log.d(TAG, "1 pane");
			EditWalkFragment editFrag = (EditWalkFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
			args.putInt(EditWaypointMapFragment.ARG_POSITION, editFrag.getCurrentPosition());
			waypointFrag.setArguments(args);
			transaction.replace(R.id.fragment_container, waypointFrag);
		}
		transaction.addToBackStack(null);
		transaction.commit();
	}

	public void editEditWaypointButtonOnClick(View view) {
	}

	public void editCancelButtonOnClick(View view){
		onBackPressed();
	}

	public void editSaveButtonOnClick(View view){
		// update stuff here
		WalkDataSource walkSource = new WalkDataSource(this);
		EditWalkFragment editFrag = (EditWalkFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);
		if (editFrag == null) {
			editFrag = (EditWalkFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		}
		Log.d(TAG+":editSaveButtOn", "walk: " + editFrag.getCurrentPosition() + " descr " + editFrag.getDescriptionId());
		long walkId = editFrag.getCurrentPosition();
		walkSource.updateWalk(walkId, 0, 0.0, 0, 0);
		EditText title = (EditText) findViewById(R.id.title);
		EditText descr = (EditText) findViewById(R.id.long_descr);
		DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this);
		String longDescr = descr.getText().toString();
		String shortDescr = descr.getText().subSequence(0, descr.getText().length()/2).toString();
		descrDataSource.updateDescription(editFrag.getDescriptionId(), title.getText().toString(), shortDescr, descr.getText().toString());
		
		WaypointDataSource wpDataSource = new WaypointDataSource(this);

		List<Waypoint> oldWaypoints = new ArrayList<Waypoint>();
		// get old waypoints in the walk
		Cursor cursor = wpDataSource.getAllWaypointsInWalk(walkId);
		if (cursor != null && cursor.moveToFirst()) {
			cursor.moveToPrevious();
			Log.d(TAG, "Old Waypoints");
			while (cursor.moveToNext()) {
				Waypoint wp = new Waypoint();
				wp.setId(cursor.getLong(0));
				wp.setLatitude(cursor.getDouble(1));
				wp.setLongitude(cursor.getDouble(2));
				wp.setIsRequest(cursor.getInt(3));
				wp.setVisitOrder(cursor.getInt(4));
				wp.setWalkId(cursor.getInt(5));
				oldWaypoints.add(wp);
				Log.d(TAG, String.valueOf(wp.getId()));
				
			}
		}
		
		descrDataSource = new EnglishWaypointDescriptionDataSource(this);
		// update if we find old in new
		for (Waypoint wp : oldWaypoints) {
			for (int i = 0; i < mWaypointList.size(); i++) {
				if (wp.getId() == mWaypointList.get(i).getId())
				{
					Waypoint newWp = mWaypointList.get(i);
					wpDataSource.updateWaypoint(wp.getId(), newWp.getLatitude(), newWp.getLongitude(), null, (long) newWp.getVisitOrder(), null, null);
					if (newWp.getEnglishDescription().getId() == -1) {
						Log.d(TAG, "Adding new descr");
						descrDataSource.addDescription(newWp.getEnglishDescription().getTitle().toString(), newWp.getEnglishDescription().getShortDescription(), newWp.getEnglishDescription().getLongDescription(), wp.getId());
					} else {
						Log.d(TAG, "Updating descr");
						descrDataSource.updateDescription(newWp.getEnglishDescription().getId(), newWp.getEnglishDescription().getTitle().toString(), newWp.getEnglishDescription().getShortDescription(), newWp.getEnglishDescription().getLongDescription());
					}
					mWaypointList.remove(i);
					continue;
				} else {
					// delete if we can't find it
					descrDataSource.deleteAllDescriptions(wp.getId());
					wpDataSource.deleteWaypoint(wp.getId());
				}
			}
		}
		if (mWaypointList.size() > 0) {
			for (Waypoint wp : mWaypointList) {
				// add what is left over
				long waypointId = wpDataSource.addWaypoint(wp.getLatitude(), wp.getLongitude(), 0, wp.getId(), walkId, 0);
				descrDataSource.addDescription(wp.getEnglishDescription().getTitle(), wp.getEnglishDescription().getShortDescription(), wp.getEnglishDescription().getLongDescription(), waypointId);
			}
			mWaypointList.clear();
		}
		onBackPressed();
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
			transaction.remove(thinFrag);
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
		if (mWaypointList == null || mWaypointList.size() <= 0) {
			onBackPressed();
			return;
		}
		// Walk Table
		WalkDataSource walkDataSource = new WalkDataSource(this);
		SharedPreferences settings = this.getSharedPreferences(GlobalFlags.PREF_NAME, 0);
		long userId = settings.getInt("userId", -1);
		long walkId = walkDataSource.addWalk(0, 0, 0, 0, userId); //TODO: Use real values for this
		Log.d(TAG, "walkId value = " + walkId);
		// EnglishWalkDescriptions table
		EditText titleView = (EditText) findViewById(R.id.titledit);
		EditText descr = (EditText) findViewById(R.id.walkDescriptionEdit);
		DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this);
		descrDataSource.addDescription(titleView.getText().toString(), descr.getText().toString().substring(0, descr.getText().length()/2), descr.getText().toString(), walkId);

		WaypointDataSource wpDataSource = new WaypointDataSource(this);
		// Waypoints
		if (mWaypointList.size() > 0) {
			descrDataSource = new EnglishWaypointDescriptionDataSource(this);
			for (Waypoint wp : mWaypointList) {
				long waypointId = wpDataSource.addWaypoint(wp.getLatitude(), wp.getLongitude(), 0, wp.getId(), walkId, 0);
				descrDataSource.addDescription(wp.getEnglishDescription().getTitle(), wp.getEnglishDescription().getShortDescription(), wp.getEnglishDescription().getLongDescription(), waypointId);
			}
		}
			mWaypointList.clear();
		onBackPressed();
	}

	/**
	 * Called when CreateWalkFragment add waypoint button is pressed. Swap in AddWaypointFragment.
	 * @param view
	 */
	public void createAddWaypointButtonOnClick(View view) {
		Log.d(TAG, "createAddWaypointButton Pressed");

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		AddWaypointMapFragment waypointFrag = new AddWaypointMapFragment();

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

	public void deleteWalkButtonOnClick(View view){
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Add the positive button
		builder.setPositiveButton(R.string.confirm_delete_walk, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				MyWalkDetailsFragment detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_large);
				if (detailsFrag == null) {
					detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
				}
				int walkId = detailsFrag.getCurrentPosition();
				WalkDataSource wdSource = new WalkDataSource(getBaseContext());
				WaypointDataSource wpdSource = new WaypointDataSource(getBaseContext());
				
				DescriptionDataSource descdSource = new EnglishWalkDescriptionDataSource(getBaseContext());
				descdSource.deleteAllDescriptions(walkId);
				
				// get all waypoints in the walk
				Cursor cursor = wpdSource.getAllWaypointsInWalk(walkId, false, false, false, false, false);
				if (cursor != null && cursor.moveToFirst()) {
					mWaypointList = new ArrayList<Waypoint>();
					while (cursor.moveToNext()) {
						Waypoint wp = new Waypoint();
						wp.setId(cursor.getInt(0));
						wp.setWalkId(cursor.getInt(1));
						mWaypointList.add(wp);
					}
				}
				descdSource = new EnglishWaypointDescriptionDataSource(getBaseContext());
				for (Waypoint wp : mWaypointList) {
					descdSource.deleteAllDescriptions(wp.getId());
				}
				
				wpdSource.deleteAllWaypointsInWalk(walkId);
				
				wdSource.deleteWalk(walkId);
				mWaypointList.clear();
				onBackPressed();
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
