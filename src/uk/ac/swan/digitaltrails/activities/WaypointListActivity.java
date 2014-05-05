package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

/**
 * @author Lewis Hancock
 *
 */
@SuppressLint("NewApi")
public class WaypointListActivity extends ActionBarActivity{
		
		/* (non-Javadoc)
		 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
		 */
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.waypoint_list);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			this.getWindow().setSoftInputMode
			(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		}
		
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
		public void editWaypointButton1(View view){
			//Intent intent = new Intent(this, EditWaypointActivity.class);
			//startActivity(intent);
		}
		
		/**
		 * @param view
		 */
		public void deleteWaypointButton(View view){
			
			Log.d("DELETE WAYPOINT", "BUTTON PRESSED");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			// Add the positive button
			builder.setPositiveButton(R.string.confirm_delete_waypoint, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   // Deletes waypoint
			           }
			       });
			// Add the negative button
			builder.setNegativeButton(R.string.cancel_delete_waypoint, new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               // User cancelled the dialog
			           }
			       });  
			//Set the title
			builder.setTitle(R.string.delete_waypoint_title)
				.setMessage(R.string.delete_waypoint_message);
			//Build and create dialog
			builder.create();
			builder.show();	
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
