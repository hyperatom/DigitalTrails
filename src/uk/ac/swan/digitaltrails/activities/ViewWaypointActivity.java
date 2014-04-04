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
public class ViewWaypointActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_waypoint_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void saveRepositionButton(final View view){
		
		Log.d("SAVE REPOSITION", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   repositionMade(view);
		           }
		       });
		// Add the negative button
		builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.reposition_title)
			.setMessage(R.string.reposition_message);
		//Build and create dialog
		builder.create();
		builder.show();	
    }	
	
	public void repositionMade(View view){
		
		Log.d("REPOSITION MADE", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the neutral button
		builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   //Update made
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.reposition_made_title)
			.setMessage(R.string.reposition_made_message);
		//Build and create dialog
		builder.create();
		builder.show();	
    }	
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void returnWalkViewButton(View view){
        onBackPressed();
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
