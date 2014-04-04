package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

@SuppressLint("NewApi")
public class ExploreActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.explore_view);
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
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void exploreOptionsButton(View view){
		
		Log.d("SIGN IN", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setPositiveButton(R.string.create_new_walk,
				new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent intent = new Intent(getApplicationContext(),CreateWalkActivity.class);
		        	   startActivity(intent);
		           }
		       });
		// Add the neutral button
				builder.setNeutralButton(R.string.add_to_existing, 
						new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				               // User cancelled the dialog
				           }
				       });
		// Add the negative button
		builder.setNegativeButton(R.string.cancel, 
				new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // Back to current location
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.explore_title);
		//Build and create dialog
		builder.create();
		builder.show();	
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