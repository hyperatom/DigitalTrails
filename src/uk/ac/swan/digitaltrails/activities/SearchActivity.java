package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.fragments.SearchDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.SearchListFragment;
import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * Activity to allow users to search the remote database for walks, then download them.
 * @author Lewis H
 *
 */
public class SearchActivity extends ActionBarActivity implements
SearchListFragment.OnWalkSelectedListener  {

	public static final String TAG = "SearchActivity";
	private Account mConnectedAccount;
	private Walk mWalk;
	
	public Account getConnectedAccount() {
		return mConnectedAccount;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		if (findViewById(R.id.fragment_container) != null) {

			if (savedInstanceState != null) {
				return;
			}
			
			SearchListFragment searchListFragment = new SearchListFragment();
			searchListFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchListFragment).commit();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onWalkSelected(Walk walk) {
		Log.d(TAG, "Trying to choose a walk");
		SearchDetailsFragment detailsFrag = (SearchDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.search_details_fragment);
		mWalk = walk;
		if (detailsFrag != null) {
			Log.d(TAG, "In 2 pane view");
			// if available and we are in 2-pane view.
			detailsFrag.updateDetailsView(walk);
		} else {
			Log.d(TAG, "In 1 pane view");
			// if in 1 pane view
			SearchDetailsFragment newDetailsFragment = new SearchDetailsFragment();
			Bundle args = new Bundle();
			args.putParcelable(SearchDetailsFragment.ARG_POSITION, walk);
			newDetailsFragment.setArguments(args);
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			
			transaction.replace(R.id.fragment_container, newDetailsFragment);
			transaction.addToBackStack(null);
			
			transaction.commit();
		}
	}

	public void returnButton(View view){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
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

	public void downloadWalkButtonOnClick(View v) {
		Log.d(TAG, "Download button pressed");
		WalkDataSource walkDataSource = new WalkDataSource(this);
		DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this);
		WaypointDataSource wpDataSource = new WaypointDataSource(this);
		walkDataSource.addWalk(mWalk);
		descrDataSource.addDescription(mWalk.getEnglishDescriptions());
		descrDataSource = new EnglishWaypointDescriptionDataSource(this);
		for (Waypoint wp : mWalk.getWaypoints()) {
			wpDataSource.addWaypoint(wp);
			descrDataSource.addDescription(wp.getEnglishDescription());
		}
	}


}
