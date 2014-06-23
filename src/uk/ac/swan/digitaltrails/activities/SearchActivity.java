package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.fragments.SearchDetailsFragment;
import uk.ac.swan.digitaltrails.fragments.SearchListFragment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Lewis Hancock
 * Activity to allow users to search the remote database for walks, then download them.
 */
public class SearchActivity extends ActionBarActivity implements
SearchListFragment.OnWalkSelectedListener   {

	/**
	 * Static constant for class tag
	 */
	public static final String TAG = "SearchActivity";
	/**
	 * The connected account
	 */
	private Account mConnectedAccount;
	/**
	 * The walk to download
	 */
	private Walk mWalk;
	
	/**
	 * @return The connected account
	 */
	public Account getConnectedAccount() {
		return mConnectedAccount;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
		
		if (findViewById(R.id.fragment_container) != null) {

			if (savedInstanceState != null) {
				return;
			}
			
			SearchListFragment searchListFragment = new SearchListFragment();
			searchListFragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchListFragment).commit();
		} 
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		EditText search = (EditText) findViewById(R.id.searchQuery);
		
		search.addTextChangedListener(new TextWatcher() {
		     
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        search(cs.toString());
		    }
		     
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {
		        // TODO Auto-generated method stub
		         
		    }
		     
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub                          
		    }
		});
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

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.SearchListFragment.OnWalkSelectedListener#onWalkSelected(uk.ac.swan.digitaltrails.components.Walk)
	 */
	@Override
	public void onWalkSelected(Walk walk) {
		Log.d(TAG, "Trying to choose a walk");
		SearchDetailsFragment detailsFrag = (SearchDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.search_details_fragment);
		mWalk = walk;
		if (detailsFrag != null) {
			Log.d(TAG, "In 2 pane view");
			// if available and we are in 2-pane view.
			detailsFrag.updateDetailsView(walk);
		} else {
			Log.d(TAG, "In 1 pane view");
			// if in 1 pane view
			SearchDetailsFragment newDetailsFragment = new SearchDetailsFragment();
			Bundle args = new Bundle();
			args.putParcelable(SearchDetailsFragment.ARG_POSITION, walk);
			newDetailsFragment.setArguments(args);
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			
			transaction.replace(R.id.fragment_container, newDetailsFragment);
			transaction.addToBackStack(null);
			
			transaction.commit();
		}
	}

	/**
	 * onClick for return button
	 * @param view
	 */
	public void returnButton(View view){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	/**
	 * onClick method for account button
	 * @param menu
	 */
	public void accountButton(MenuItem menu){
		Intent intent = new Intent(this, EditAccountActivity.class);
		startActivity(intent);
	}

	/**
	 * onClick method for logout menu button
	 * @param menu
	 */
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
        AccountManager am = AccountManager.get(this);
		Account a = am.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];
        ContentResolver.requestSync(a, WhiteRockContract.AUTHORITY, settingsBundle);
	}
	

	/**
	 * onClick for settings menu butotn
	 * @param menu
	 */
	public void settingsButton(MenuItem menu){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	/**
	 * onClick method for downloadWalkButton
	 * @param v
	 */
	public void downloadWalkButtonOnClick(View v) {
		Log.d(TAG, "Download button pressed");
		WalkDataSource walkDataSource = new WalkDataSource(this);
		DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this);
		WaypointDataSource wpDataSource = new WaypointDataSource(this);
		Log.d(TAG, "walkId: " + mWalk.getWalkId());
		long walkId = walkDataSource.addWalk(mWalk);
		
		mWalk.getEnglishDescriptions().setForeignId(walkId);
		descrDataSource.addDescription(mWalk.getEnglishDescriptions());
		descrDataSource = new EnglishWaypointDescriptionDataSource(this);
		for (Waypoint wp : mWalk.getWaypoints()) {
			wp.setWalkId(walkId);
			long wpId = wpDataSource.addWaypoint(wp);
			Log.d(TAG, "Waypoint remoteId: " + wp.getWaypointId());
			wp.getEnglishDescription().setForeignId(wpId);
			EnglishWaypointDescription descr = wp.getEnglishDescription();
			descrDataSource.addDescription(descr);
			Log.d(TAG, "descr ids: " + descr.getId() +  " " + descr.getDescriptionId());
		}
		Toast toast = Toast.makeText(getBaseContext(), "Walk Downloaded Successfully", Toast.LENGTH_SHORT);
		toast.show();
		onBackPressed();
	}

	public void search(String query) {
		Log.d(TAG, "Search Sumbitted");
		Bundle bundle = new Bundle();
		bundle.putString("query", query);
		SearchListFragment list = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.search_list_fragment);
		if (list == null) {
			list = (SearchListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		}
		list.search(bundle);
	}


}
