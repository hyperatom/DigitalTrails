package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.R;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Authenticator activity. In charge of identifying a user.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

	public final static String ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public final static String AUTH_TYPE = "AUTH_TYPE";
	public final static String ACCOUNT_NAME = "ACCOUNT_NAME";
	public final static String IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
	
	public final static  String ERROR_MESSAGE = "ERR_MSG";
	
	public final static String USER_PASS = "USER_PASS";
	
	private final int REQ_SIGNUP = 1;
	
	private final String TAG = "AuthenticatorActivity";
	
	private AccountManager mAccountManager;
	private String mAuthTokenType;
	
	/**
	 * Called when activity created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		mAccountManager = AccountManager.get(getBaseContext().getApplicationContext());
		Log.d(TAG, "context: " + getBaseContext().getApplicationContext());
		String accountName = getIntent().getStringExtra(ACCOUNT_NAME);
		mAuthTokenType = getIntent().getStringExtra(AUTH_TYPE);
		
		if (mAuthTokenType == null) {
			mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
		}
		
		if (accountName != null) {
			((TextView)findViewById(R.id.accountName)).setText(accountName);
		}
		
		findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			submit();
			}
		});
		
		findViewById(R.id.signUp).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent signup = new Intent(getBaseContext().getApplicationContext(), SignUpActivity.class);
				signup.putExtras(getIntent().getExtras());
				startActivityForResult(signup, REQ_SIGNUP);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// returned signup, user created account.
		if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
			finishLogin(data);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public void submit() {
		
		final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
		final String userPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString();
		
		final String accountType = AccountGeneral.ACCOUNT_TYPE;

		new AsyncTask<String, Void, Intent>() {

			@Override
			protected Intent doInBackground(String... arg0) {
				String authToken = null;
				Bundle data = new Bundle();
				try {
					Log.d(TAG, "I die at line 101");
					authToken = AccountGeneral.sServerAuthenticate.userSignIn(userName, userPassword, mAuthTokenType);
					Log.d(TAG, "authToken: " + authToken);
					data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
					data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
					data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
					data.putString(USER_PASS, userPassword);
				} catch (Exception e) {
					data.putString(ERROR_MESSAGE, e.getMessage());
					Log.e(TAG, "Error: " + e.getMessage());
				}
				
				final Intent result = new Intent();
				result.putExtras(data);
				return result;
			}
			
			@Override
			protected void onPostExecute(Intent intent) {
				if (intent.hasExtra(ERROR_MESSAGE)) {
					Toast.makeText(getBaseContext(), intent.getStringExtra(ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
				} else {
					finishLogin(intent);
				}
			}	
		}.execute();
		
	}
	
	private void finishLogin(Intent intent) {
		String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
		String accountPassword = intent.getStringExtra(USER_PASS);
		final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
		Log.d(TAG, "Alive before if: line 132");
		if (getIntent().getBooleanExtra(IS_ADDING_NEW_ACCOUNT, false)) {
			Log.d(TAG, "NOT Adding new account");
			String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
			String authTokenType = mAuthTokenType;
			Log.d(TAG, "authTokenType: " + authTokenType);
			Log.d(TAG, "Account: " + account.toString());
			// create local account, set auth token.
			mAccountManager.addAccountExplicitly(account, accountPassword, intent.getBundleExtra(AccountManager.KEY_USERDATA));
			mAccountManager.setAuthToken(account, authTokenType, authToken);
			Log.d(TAG, "Alive at end of if: line 142");
		} else {
			mAccountManager.setPassword(account, accountPassword);;
		}
		
		TextView ta = (TextView) findViewById(R.id.signUp);
		ta.setText(intent.getStringExtra(AccountManager.KEY_AUTHTOKEN));
		
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
		
	}
	
}
