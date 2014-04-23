package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.fragments.AddWaypointFragment;
import uk.ac.swan.digitaltrails.fragments.EditWalkFragment;
import uk.ac.swan.digitaltrails.fragments.LaunchFragment;
import uk.ac.swan.digitaltrails.fragments.LogInFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkListFragment;
import uk.ac.swan.digitaltrails.fragments.RegisterFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

public class LaunchActivity extends ActionBarActivity{
	
	private static final String TAG = "MyWalksActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		getSupportActionBar().hide();
		LaunchFragment launchFragment = new LaunchFragment();

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_launcher, launchFragment).commit();
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	public void skipButton(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
	
	//LogInFragment functions
	
	public void logInButton(View view){
		Log.d(TAG, "logInButton Pressed");
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		LogInFragment logInFrag = new LogInFragment();
	
		
			Log.d(TAG, "1 pane, replace fragment_launcher");
			transaction.replace(R.id.fragment_launcher, logInFrag);
		
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	public void cancelButton(View view){
        onBackPressed();
    }

	public void signInButton(View view){
		
		Log.d("SIGN IN", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
		        	   startActivity(intent);
		           }
		       });
		// Add the negative button
		builder.setNegativeButton(R.string.retry, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User cancelled the dialog
		           }
		       });  
		//Set the title
		builder.setTitle(R.string.log_in_error)
			.setMessage(R.string.log_in_message);
		//Build and create dialog
		builder.create();
		builder.show();	
    }
	
	// Register functions
	
	public void registerButtonOnClick(View view){
		Log.d(TAG, "registerButton Pressed");
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		RegisterFragment registerFrag = new RegisterFragment();
	
		
			Log.d(TAG, "1 pane, replace fragment_launcher");
			transaction.replace(R.id.fragment_launcher, registerFrag);
		
		transaction.addToBackStack(null);
		transaction.commit();
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
