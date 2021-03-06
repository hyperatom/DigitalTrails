package uk.ac.swan.digitaltrails.activities;

import java.io.IOException;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.sync.TableObserver;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		Intent intent = getIntent();
		mConnectedAccount = (Account) intent.getParcelableExtra("account");
		TableObserver observer = new TableObserver(null);
		getContentResolver().registerContentObserver(WhiteRockContract.CONTENT_URI, true, observer);
		//ContentResolver.addPeriodicSync(mConnectedAccount, WhiteRockContract.AUTHORITY, null, SYNC_INTERVAL);
	
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	
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
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	public void onBackPressed(){
		
	}
	
	public void onSync(MenuItem v){
		// Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mConnectedAccount, WhiteRockContract.AUTHORITY, settingsBundle);
	}
	
	//TODO: Move logOut and onSync calls to a new class.
	// Ensure you can't log out when not logged in as it crashes the program.
	// Also, figure out a better way to log out (like, y'know, check it's ACTUALLY THE USERS ACCOUNT).
	public void logOutButton(MenuItem v){
		AccountManager am = AccountManager.get(this);
		Account a = am.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];
		String authToken;
		try {
			authToken = am.peekAuthToken(a, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
			am.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, authToken);
			am.removeAccount(a, null, null);
			Intent i = new Intent(this, SplashActivity.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

