package uk.ac.swan.digitaltrails.activities;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.WalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.WalkListFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

/**
 * Activity to allow a user to choose a walk, see its details and then load it.
 * @author Lewis Hancock
 *
 */
public class ChooseWalkActivity extends ActionBarActivity
			implements WalkListFragment.OnWalkSelectedListener {
	
	/**
	 * 	Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_walk_activity);

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
		Log.d("ChooseWalkActivity", String.valueOf(position));
		WalkListFragment listFrag = (WalkListFragment) getSupportFragmentManager().findFragmentById(R.id.walk_list_fragment);
		if (listFrag != null) {

		} else if (findViewById(R.id.fragment_container) != null) {
			listFrag = (WalkListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		}

		position = listFrag.getWalkIds().get(position);	
		
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
		WalkDetailsFragment detailsFrag = (WalkDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.walk_details_fragment);
		if(detailsFrag.getCurrentPosition() > 0){
			Intent intent = new Intent(this, MapActivity.class);
			intent.putExtra(MapActivity.ARG_EXPLORE, 1);
			intent.putExtra("walkId", detailsFrag.getCurrentPosition());	
			startActivity(intent);
		}
	}
	
	public void deleteWalkButtonClick(View view) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// Add the positive button
		builder.setPositiveButton(R.string.confirm_delete_walk, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				WalkDetailsFragment detailsFrag = (WalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.walk_details_fragment);
				if (detailsFrag == null) {
					detailsFrag = (WalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
				}
				int walkId = detailsFrag.getCurrentPosition();
				WalkDataSource wdSource = new WalkDataSource(getBaseContext());
				WaypointDataSource wpdSource = new WaypointDataSource(getBaseContext());
				
				DescriptionDataSource descdSource = new EnglishWalkDescriptionDataSource(getBaseContext());
				descdSource.deleteAllDescriptions(walkId);
				
				// get all waypoints in the walk
				Cursor cursor = wpdSource.getAllWaypointsInWalk(walkId, false, false, false, false, false);
				ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>();
				if (cursor != null && cursor.moveToFirst()) {
					waypointList = new ArrayList<Waypoint>();
					while (cursor.moveToNext()) {
						Waypoint wp = new Waypoint();
						wp.setId(cursor.getInt(0));
						wp.setWalkId(cursor.getInt(1));
						waypointList.add(wp);
					}
				}
				descdSource = new EnglishWaypointDescriptionDataSource(getBaseContext());
				for (Waypoint wp : waypointList) {
					descdSource.deleteAllDescriptions(wp.getId());
				}
				
				wpdSource.deleteAllWaypointsInWalk(walkId);
				
				wdSource.deleteWalk(walkId);
				waypointList.clear();
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
	
	
	public void onBackPressed(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}
}