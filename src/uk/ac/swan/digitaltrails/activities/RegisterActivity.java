package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class RegisterActivity extends ActionBarActivity{

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		this.getWindow().setSoftInputMode
			(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
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
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void cancelRButton(View view){
        onBackPressed();
    }
	
	public void registerButton(View view){
		
		Log.d("REGISTER", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the neutral button
		builder.setNeutralButton(R.string.valid_register, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
		        	   startActivity(intent);
		           }
		       });
		
		//Set the title
		builder.setTitle(R.string.register_title)
			.setMessage(R.string.register_message);
		//Build and create dialog
		builder.create();
		builder.show();	
    }	
    
}
