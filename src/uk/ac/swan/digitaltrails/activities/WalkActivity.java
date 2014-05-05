package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.fragments.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * @author Lewis Hancock
 * 
 */
public class WalkActivity extends ActionBarActivity {

	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walk_view);
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	/* @SuppressLint("NewApi")
	@Override
	 public void onSearchItemSelected(String link) {
		    WalkDetailFragment fragment = (WalkDetailFragment) getFragmentManager()
		        .findFragmentById(R.id.walkViewDetailFragment);
		    if (fragment != null && fragment.isInLayout()) {
		      fragment.setText(link);
		    } else {
		      Intent intent = new Intent(getApplicationContext(),
		          WalkDetailActivity.class);
		      intent.putExtra(WalkDetailActivity.EXTRA_URL, link);
		      startActivity(intent);

		    }
	 }*/
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
	

	/**
	 * @param view
	 */
	public void waypointButton1(View view){
	//	Intent intent = new Intent(this, ViewWaypointActivity.class);
		//startActivity(intent);
	}
	
	/**
	 * @param view
	 */
	public void walkOptionsButton(View view){
        Intent intent = new Intent(this, MyWalksActivity.class);
        startActivity(intent);
    }

	/**
	 * @param menu
	 */
	public void accountButton(MenuItem menu){
        Intent intent = new Intent(this, EditAccountActivity.class);
        startActivity(intent);
    }
	
	/**
	 * @param menu
	 */
	public void logOutButton(MenuItem menu){
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
	
	/**
	 * @param menu
	 */
	public void settingsButton(MenuItem menu){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
