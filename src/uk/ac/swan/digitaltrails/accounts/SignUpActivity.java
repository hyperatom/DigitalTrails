package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends Activity {

	private String TAG = "SignUpActivity";
	private String mAccountType;
	
	
	@Override
	protected void onCreate(Bundle savedInstnaceState) {
		super.onCreate(savedInstnaceState);
		
		mAccountType = getIntent().getStringExtra(AccountGeneral.ACCOUNT_TYPE);
		Log.d(TAG, "mAccountType: " + mAccountType);
		setContentView(R.layout.act_register);
		
		findViewById(R.id.alreadyMember).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "clicked submit");
				createAccount();
			}
		});
	}
	
	private void createAccount() {
		new AsyncTask<String, Void, Intent>() {
			
			String firstName = ((TextView) findViewById(R.id.firstName)).getText().toString().trim();
			String lastName = ((TextView) findViewById(R.id.lastName)).getText().toString().trim();
			String accountName = ((TextView) findViewById(R.id.accountName)).getText().toString().trim();
			String accountPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString().trim();
			
			@Override
			protected Intent doInBackground(String... arg0) {
				Log.d(TAG, "Started authenticating");
				
				String authtoken = null;
				Bundle data = new Bundle();
				try {
					Account acc = AccountGeneral.sServerAuthenticate.userSignUp(firstName, lastName, accountName, accountPassword, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
					if (acc != null) {
						if(acc.email == "Email Already Exists"){
							throw new Exception("Email Already Exists");
						}
						Log.d(TAG, "acc is not null");
						authtoken = acc.authToken;
					} else {
						Log.e(TAG, "acc is null wtf!");
					}
					Log.d(TAG, "authtoken is: " + authtoken);
					data.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
					data.putString(AccountManager.KEY_ACCOUNT_TYPE, mAccountType);;
					data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
					data.putString(AuthenticatorActivity.USER_PASS, accountPassword);
				} catch (Exception e) {
					data.putString(AuthenticatorActivity.ERROR_MESSAGE, e.getMessage());
				}
				
				final Intent result = new Intent();
				result.putExtras(data);
				return result;
			}
			
			
			@Override
			protected void onPostExecute(Intent intent) {
				if (intent.hasExtra(AuthenticatorActivity.ERROR_MESSAGE)) {
					Log.d(TAG, "Error in register");
					Toast.makeText(getBaseContext(), intent.getStringExtra(AuthenticatorActivity.ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
				} else {
					Log.d(TAG, "Result ok");
                    setResult(RESULT_OK, intent);
                    finish();
            
				}
			}
			
		}.execute(); 
	}
	
	@Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}
	
}
