package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.accounts.SignUpActivity;
import uk.ac.swan.digitaltrails.fragments.LaunchFragment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author Lewis Hancock
 * Activity to allow a user to login, register or continue as a guest.
 */
public class LaunchActivity extends ActionBarActivity{
	
	/**
	 * Static tag for class
	 */
	private static final String TAG = "LaunchActivity";
	/**
	 * AccountManager to find the current account
	 */
	private AccountManager mAccountManager;
	/**
	 * 
	 */
	private String mAuthToken = null;
	/**
	 * The account logged in.
	 */
	private Account mConnectedAccount;
	

	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		getSupportActionBar().hide();
		mAccountManager = AccountManager.get(getBaseContext().getApplicationContext());
		Log.d(TAG, "context: " + getBaseContext().getApplicationContext());
		LaunchFragment launchFragment = new LaunchFragment();
		try{
			Account[] acc = mAccountManager.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
			if(acc.length != 0){
				Intent in = new Intent(this, HomeActivity.class);
				in.putExtra("account", acc[0]);
				startActivity(in);
			}
		}catch(Exception e){
			//Ignore it. Carry on. 
		}
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_launcher, launchFragment).commit();
	
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}
	
	
	/**
	 * OnClick method for skip button
	 * @param view The view for the button
	 */
	public void skipButton(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	/**
	 * onClick method to cancel.
	 * @param view The view for the button
	 */
	public void cancelButton(View view){
        onBackPressed();
    }
	
	// Register functions
	
	/**
	 * OnClick method to register / log in.
	 * @param view The view for the button
	 */
	public void registerButtonOnClick(View view){
        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
	}
	
	/**
	 * Get the token for the account, creating the authtoken if necessary.
	 * @param accountType The type of account
	 * @param authTokenType The type of auth token
	 */
	private void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {
		final Activity current = this;
		final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {

					@Override
					public void run(AccountManagerFuture<Bundle> future) {
						Bundle bundle = null;
						try {
							bundle = future.getResult();
							mAuthToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
							if (mAuthToken != null) {
								String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
								Log.d(TAG, "Account Type: " + AccountGeneral.ACCOUNT_TYPE);
								Log.d(TAG, "Connecting Account...");
								mConnectedAccount = new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
								Log.d(TAG, "Connected Account!");
								Toast.makeText(getBaseContext(), "Succesfully logged in!", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(current, HomeActivity.class);
								intent.putExtra("account", mConnectedAccount);
								startActivity(intent);
							} else {
								Toast.makeText(getBaseContext(), "Failed to log in!", Toast.LENGTH_SHORT).show();	
							}
						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
						}
					}
		
		}, null);
	}
	
	/**
	 * onClick to cancel registering
	 * @param view The view for the button
	 */
	public void cancelRButton(View view){
        onBackPressed();
    }
	
}
