package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.accounts.SignUpActivity;
import uk.ac.swan.digitaltrails.fragments.LaunchFragment;
import uk.ac.swan.digitaltrails.fragments.LogInFragment;
import uk.ac.swan.digitaltrails.fragments.RegisterFragment;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class LaunchActivity extends ActionBarActivity{
	
	private static final String TAG = "LaunchActivity";
	private AccountManager mAccountManager;
	private String mAuthToken = null;
	private Account mConnectedAccount;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launch);
		getSupportActionBar().hide();
		mAccountManager = AccountManager.get(getBaseContext().getApplicationContext());
		Log.d(TAG, "context: " + getBaseContext().getApplicationContext());
		LaunchFragment launchFragment = new LaunchFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_launcher, launchFragment).commit();
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
        getTokenForAccountCreateIfNeeded(AccountGeneral.ACCOUNT_TYPE, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
	}
	
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
