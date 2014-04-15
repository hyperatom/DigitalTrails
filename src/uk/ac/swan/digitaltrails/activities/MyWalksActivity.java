package uk.ac.swan.digitaltrails.activities;

import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.fragments.AddWaypointFragment;
import uk.ac.swan.digitaltrails.fragments.CreateWalkFragment;
import uk.ac.swan.digitaltrails.fragments.EditWalkFragment;
import uk.ac.swan.digitaltrails.fragments.EditWaypointMapFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkListFragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
		Log.d(TAG, "createAddWaypointButton Pressed");

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
		walkSource.updateWalk(editFrag.getCurrentPosition(), 0, 0.0, 0, 0);
		EditText title = (EditText) findViewById(R.id.title);
		EditText descr = (EditText) findViewById(R.id.long_descr);
		DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this);
		String shortDescr = descr.getText().subSequence(0, descr.getText().length()/2).toString();
		descrDataSource.updateDescription(editFrag.getDescriptionId(), title.getText().toString(), shortDescr, descr.getText().toString());
		descrDataSource = new EnglishWaypointDescriptionDataSource(this);
		if (mWaypointList.size() > 0) {
			descrDataSource = new EnglishWaypointDescriptionDataSource(this);
			WaypointDataSource wpDataSource = new WaypointDataSource(this);
			for (Waypoint wp : mWaypointList) {
				//TODO: Figure out the best strategy for waypoints (update first, then delete, then add new ones I think...).
				//long waypointId = wpDataSource.addWaypoint(wp.getLatitude(), wp.getLongitude(), 0, wp.getId(), walkId, 0);

				//TODO: Use real values
				//descrDataSource.addDescription(wp.getTitle(), wp.getDescriptions().get(0).getShortDescription(), wp.getDescriptions().get(0).getLongDescription(), waypointId);
				//descrDataSource.addDescription(wp.getTitle(), "ShortDescr", "LongDescr", waypointId);
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
		//TODO: Figure out why thin pane is now not disappearing (chris?).
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
		// Walk Table
		WalkDataSource walkDataSource = new WalkDataSource(this);
		long walkId = walkDataSource.addWalk(0, 0, 0, 0); //TODO: Use real values for this
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

				//TODO: Use real values
				//descrDataSource.addDescription(wp.getTitle(), wp.getDescriptions().get(0).getShortDescription(), wp.getDescriptions().get(0).getLongDescription(), waypointId);
				descrDataSource.addDescription(wp.getTitle(), "ShortDescr", "LongDescr", waypointId);
			}
			mWaypointList.clear();
		}
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
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Add the positive button
		builder.setPositiveButton(R.string.confirm_delete_walk, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
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
