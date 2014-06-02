package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.sync.TableObserver;
import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

/**
 * @author Lewis Hancock
 * Activity which lets user's navigate the application.
 */
@SuppressLint("NewApi")
public class HomeActivity extends ActionBarActivity{

	/**
	 * static tag for the class.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "MyWalksActivity";
	/**
	 * The account connected to the application.
	 */
	private Account mConnectedAccount;
	
    public static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
            SECONDS_PER_MINUTE *
            MILLISECONDS_PER_SECOND;

	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_view);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		Intent intent = getIntent();
		mConnectedAccount = (Account) intent.getParcelableExtra("account");
		TableObserver observer = new TableObserver(null);
		getContentResolver().registerContentObserver(WhiteRockContract.CONTENT_URI, true, observer);
		//ContentResolver.addPeriodicSync(mConnectedAccount, WhiteRockContract.AUTHORITY, null, SYNC_INTERVAL);
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

	/**
	 * Button onClick to create a walk.
	 * @param view The view of the button
	 */
	public void createWalkButtonOnClick(View view){
		//TODO: Needs to connect to the Fragment somehow
	}
	/**
	 * Button onClick for searching
	 * @param view The View of the button
	 */
	public void searchButton(View view){
        Intent intent = new Intent(this, SearchActivity.class);
        
        startActivity(intent);
    }
	
	/**
	 * OnClick method for walk options button
	 * @param view The View of the button
	 */
	public void walkOptionButton(View view){
        Intent intent = new Intent(this, MyWalksActivity.class);
        intent.putExtra("account", getIntent().getParcelableExtra("account"));
        startActivity(intent);
    }
	
	/**
	 * OnClick method for viewing a walk.
	 * @param view The View of the button
	 */
	public void viewWalkButton(View view){
        Intent intent = new Intent(this, ChooseWalkActivity.class);
        startActivity(intent);
	}
	
	/**
	 * OnClick button to explore the map
	 * @param view The View of the button
	 */
	public void exploreButton(View view){
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("explore", 0);
        startActivity(intent);
	}
	
	/**
	 * Button onClick to view account details.
	 * @param menu The item of the menu
	 */
	public void accountButton(MenuItem menu){
        Intent intent = new Intent(this, EditAccountActivity.class);
        startActivity(intent);
    }
	
	/**
	 * Button onClick to log out.
	 * @param menu The item of the menu
	 */
	public void logOutButton(MenuItem menu){
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
    }
	
	/**
	 * Button onClick for settings.
	 * @param menu The item of the menu
	 */
	public void settingsButton(MenuItem menu){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	public void onBackPressed(){
		
	}
	
}

