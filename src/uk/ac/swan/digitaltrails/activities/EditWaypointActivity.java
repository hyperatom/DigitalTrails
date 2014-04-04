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
import android.view.WindowManager;

@SuppressLint("NewApi")
public class EditWaypointActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_waypoint_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.getWindow().setSoftInputMode
		(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void saveWaypointButton(View view){
		
	}
	
	public void viewMediaButton(final View view){
		Log.d("DELETE MEDIA", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setPositiveButton(R.string.confirm_delete_media, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   deleteMediaButton(view);
		           }
		       });
		// Add the negative button
		builder.setNegativeButton(R.string.back_media, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.waypoint_media_title)
			.setMessage(R.string.waypoint_media_message);
		//Build and create dialog
		builder.create();
		builder.show();
	}
	
	public void deleteMediaButton(View view){
		Log.d("DELETE MEDIA", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setPositiveButton(R.string.confirm_delete_media_final, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // Refresh gallery with media removed
		           }
		       });
		// Add the negative button
		builder.setNegativeButton(R.string.cancel_delete_media, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.delete_media_title)
			.setMessage(R.string.delete_media_message);
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
