package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.R;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



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
		mAccountManager = AccountManager.get(getBaseContext());
	
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
				Intent singup = new Intent(getBaseContext(), SignUpActivity.class);
				signup.putExtras(getIntent().getExtras());
				startActivityForResult(signup, REQ_SIGNUP);
			}
		});
	}
	
	public void submit() {
	
	}
	
}
