package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Testing activity for content provider.
 * @author Lewis Hancock
 *
 */
public class MainActivity extends ActionBarActivity
			implements WalkListFragment.OnWalkSelectedListener {


	private boolean debug = false;
	
	//TODO: Use Loader here, not in fragments & pass data down? Currently using 2 loaders and being a lazy fucker.
	// Does mean doing joins, however.
	/**
	 * 	Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cp_test);

		if (debug) {
			ContentValues values = new ContentValues();
			
			values.put("title", "Test Walk 1");
			values.put("short_description", "The First Test Walk");
			values.put("long_description", "The long description of this walk...");
			values.put("walk_id", "1");
			getContentResolver().insert(WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI, values);
		
			values = new ContentValues();
			values.put("title", "Test Walk 2");
			values.put("short_description", "The Second Test Walk");
			values.put("long_description", "The long description of this walk...");
			values.put("walk_id", "2");
			getContentResolver().insert(WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI, values);

			values = new ContentValues();

			values.put("latitude", 51.63581500);
			values.put("longitude", -3.93372000);
			values.put("is_request", 0);
			values.put("visit_order", 1);
			values.put("walk_id", 1);
			values.put("user_id", 1);
			getContentResolver().insert(WhiteRockContract.Waypoint.CONTENT_URI, values);
			
			values = new ContentValues();
			values.put("latitude", 51.63511300);
			values.put("longitude", -3.93349000);
			values.put("is_request", 0);
			values.put("visit_order", 2);
			values.put("walk_id", 1);
			values.put("user_id", 1);
			getContentResolver().insert(WhiteRockContract.Waypoint.CONTENT_URI, values);
			
			values = new ContentValues();
			values.put("latitude", 51.63451800);
			values.put("longitude", -3.93291200);
			values.put("is_request", 0);
			values.put("visit_order", 1);
			values.put("walk_id", 2);
			values.put("user_id", 2);
			getContentResolver().insert(WhiteRockContract.Waypoint.CONTENT_URI, values);
			
			values = new ContentValues();
			values.put("latitude", 51.64581500);
			values.put("longitude", -3.94291200);
			values.put("is_request", 0);
			values.put("visit_order", 2);
			values.put("walk_id", 2);
			values.put("user_id", 2);
			getContentResolver().insert(WhiteRockContract.Waypoint.CONTENT_URI, values);
			
			values = new ContentValues();
			values.put("title", "Waypoint 1 Walk 1");
			values.put("short_description", "wp1short - FROM THE JOIN!");
			values.put("long_description", "wp1long - FROM THE JOIN!");
			values.put("waypoint_id", 1);
			getContentResolver().insert(WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI, values);
			
			values = new ContentValues();
			values.put("title", "Waypoint 2 Walk 1");
			values.put("short_description", "wp2short - FROM THE JOIN!");
			values.put("long_description", "wp2long - FROM THE JOIN!");
			values.put("waypoint_id", 2);
			getContentResolver().insert(WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI, values);

			
			values = new ContentValues();
			values.put("title", "Waypoint 1 Walk 2");
			values.put("short_description", "wp1short - FROM THE JOIN!");
			values.put("long_description", "wp1long - FROM THE JOIN!");
			values.put("waypoint_id", 3);
			getContentResolver().insert(WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI, values);

			
			values = new ContentValues();
			values.put("title", "Waypoint 2 Walk 2");
			values.put("short_description", "wp2short - FROM THE JOIN!");
			values.put("long_description", "wp2long - FROM THE JOIN!");
			values.put("waypoint_id", 4);
			getContentResolver().insert(WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI, values);


		}
		
		// check if using small layout
		if (findViewById(R.id.fragment_container) != null) {
			
			// if restoring from prev state do nothing
			if (savedInstanceState != null) {
				return;
			}
			
			WalkListFragment walkListFragment = new WalkListFragment();
			
			walkListFragment.setArguments(getIntent().getExtras());
			
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, walkListFragment).commit();
		}
	}

	/**
	 * Display selected walk data.
	 */
	@Override
	public void onWalkSelected(int position) {
		WalkDetailsFragment detailsFrag = (WalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.walk_details_fragment);
		
		if (detailsFrag != null) {
			// if available and we are in 2-pane view.
			detailsFrag.updateDetailsView(position);
		} else {
			// if in 1 pane view
			WalkDetailsFragment newDetailsFragment = new WalkDetailsFragment();
			Bundle args = new Bundle();
			args.putInt(WalkDetailsFragment.ARG_POSITION, position);
			newDetailsFragment.setArguments(args);
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			
			transaction.replace(R.id.fragment_container, newDetailsFragment);
			transaction.addToBackStack(null);
			
			transaction.commit();
		}
	}
	
	public void loadWalkButtonClick(View view) {
		Intent intent = new Intent(this, MapActivity.class);
		Bundle bundle = new Bundle();
		WalkDetailsFragment detailsFrag = (WalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.walk_details_fragment);
		intent.putExtra("walkId", detailsFrag.getCurrentPosition());
		startActivity(intent);
	}
}