package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.fragments.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class WalkActivity extends ActionBarActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walk_view);
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
	

	public void waypointButton1(View view){
	//	Intent intent = new Intent(this, ViewWaypointActivity.class);
		//startActivity(intent);
	}
	
	public void walkOptionsButton(View view){
        Intent intent = new Intent(this, MyWalksActivity.class);
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
}
