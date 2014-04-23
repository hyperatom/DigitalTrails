package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.fragments.CreateWalkFragment;
import uk.ac.swan.digitaltrails.fragments.MyWalkDetailsFragment;
import android.annotation.SuppressLint;
import android.app.*;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

@SuppressLint("NewApi")
public class HomeActivity extends ActionBarActivity{

	private static final String TAG = "MyWalksActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	public void createWalkButtonOnClick(View view){
		// Needs to connect to the Fragment somehow
	}
	public void searchButton(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
	
	public void walkOptionButton(View view){
        Intent intent = new Intent(this, MyWalksActivity.class);
        startActivity(intent);
    }
	
	public void viewWalkButton(View view){
        Intent intent = new Intent(this, ChooseWalkActivity.class);
        startActivity(intent);
	}
	
	public void exploreButton(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("explore", 0);
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
	
	public void onBackPressed(){
		
	}
	
}

