package uk.ac.swan.digitaltrails.activities;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

/**
 * @author Chris Lewis
 * Activity which allows user to edit an account.
 */
public class EditAccountActivity extends ActionBarActivity{
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_account_view);
		this.getWindow().setSoftInputMode
			(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
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
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
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
	 * Cancel editing on the Account.
	 * @param view View of the button
	 */
	public void cancelEditAccountButton(View view){
        onBackPressed();
    }
	
	/**
	 * Save the changes to the Account.
	 * @param view View of the button.
	 */
	public void saveButton(View view){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
	
	/**
	 * Confirms changes to account.
	 * @param view The view of the button.
	 */
	public void accountChanges(View view){
		Log.d("ACCOUNT CHANGES", "BUTTON PRESSED");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Add the positive button
		builder.setNeutralButton(R.string.valid_changes, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // Refresh gallery with media removed
		           }
		       });
		  
		//Set the title
		builder.setTitle(R.string.account_title)
			.setMessage(R.string.account_message);
		//Build and create dialog
		builder.create();
		builder.show();
	}
	
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
