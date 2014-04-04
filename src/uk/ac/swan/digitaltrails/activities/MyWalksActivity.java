package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.WalkListFragment;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


@SuppressLint("NewApi")
public class MyWalksActivity extends ActionBarActivity implements
	WalkListFragment.OnWalkSelectedListener {
	
	private static final String TAG = "MyWalksActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_walks_activity);
		// check if using small layout
		if (findViewById(R.id.fragment_container) != null) {

			// if restoring from prev state do nothing
			if (savedInstanceState != null) {
				return;
			}

			Log.d(TAG, "Container not ewas null");
			WalkListFragment walkListFragment = new WalkListFragment();

			walkListFragment.setArguments(getIntent().getExtras());

			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, walkListFragment).commit();
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
		
		if((MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.my_walk_details_fragment) != null){
			Log.d(TAG, "Valid detail fragment");
		}

		MyWalkDetailsFragment detailsFrag = (MyWalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.my_walk_details_fragment);

		if (detailsFrag != null) {
			// if available and we are in 2-pane view.
			detailsFrag.updateDetailsView(position);
		} else {
			// if in 1 pane view
			Log.d(TAG, "1 pane view onwalkSelected " + position);
			MyWalkDetailsFragment newDetailsFragment = new MyWalkDetailsFragment();
			Bundle args = new Bundle();
			args.putInt(MyWalkDetailsFragment.ARG_POSITION, position);
			newDetailsFragment.setArguments(args);

			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

			transaction.replace(R.id.fragment_container, newDetailsFragment);
			transaction.addToBackStack(null);

			transaction.commit();
		}
		
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
	
	public void createWalkButton(View view){
		Intent intent = new Intent(this, CreateWalkActivity.class);
		startActivity(intent);
	}
	
	public void buttonEditWalk(View view){
		Intent intent = new Intent(this, EditWalksActivity.class);
		startActivity(intent);
	}
	
	public void buttonDeleteWalk(View view){
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
