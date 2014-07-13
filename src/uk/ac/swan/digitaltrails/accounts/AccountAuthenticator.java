package uk.ac.swan.digitaltrails.accounts;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Class to handle a WhiteRock account. Talks to the account manager to perform
 * all available actions. 
 * @author Lewis Hancock
 *
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

	/**
	 * Identifier tag for the class
	 */
	private static final String TAG = "AccountAuthenticator";
	/**
	 * Var to store context since Android is stupid.
	 */
	private final Context mContext;
	
	/**
	 * The context being given to the app.yyyy
	 * @param context
	 */
	public AccountAuthenticator(Context context) {
		super(context);
		this.mContext = context.getApplicationContext();
		Log.d(TAG, "context: " + mContext);
	}

	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#addAccount(android.accounts.AccountAuthenticatorResponse, java.lang.String, java.lang.String, java.lang.String[], android.os.Bundle)
	 */
	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
			String authTokenType, String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		
		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
		intent.putExtra(AuthenticatorActivity.ACCOUNT_TYPE,  accountType);
		intent.putExtra(AuthenticatorActivity.AUTH_TYPE, authTokenType);
		intent.putExtra(AuthenticatorActivity.IS_ADDING_NEW_ACCOUNT, true);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#getAuthToken(android.accounts.AccountAuthenticatorResponse, android.accounts.Account, java.lang.String, android.os.Bundle)
	 */
	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		
		// if incorrect authToken request
		if (!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY) && !authTokenType.equals(
			AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
			return result;
		}
		
		// Get Username and Password from Account Manager. Get authToken from server.
		final AccountManager accountMgr = AccountManager.get(mContext);
		
		String authToken = accountMgr.peekAuthToken(account, authTokenType);
		//Log.d(TAG, "peekAuthToken returned - " + authToken);
		
		// try to auth user.
		if (TextUtils.isEmpty(authToken)) {
			final String password = accountMgr.getPassword(account);
			if (password != null) {
				try {
					//Log.d(TAG, "re-auth with existing password");
					authToken = AccountGeneral.sServerAuthenticate.userSignIn(account.name, password, authTokenType);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		// if we have authToken, return it!
		if (!TextUtils.isEmpty(authToken)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
			result.putString(AccountManager.KEY_ACCOUNT_TYPE,  account.type);
			result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
			return result;
		}
		
		// can't access user password, get credentials again.
		final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		intent.putExtra(AuthenticatorActivity.ACCOUNT_TYPE, account.type);
		intent.putExtra(AuthenticatorActivity.AUTH_TYPE, authTokenType);
		intent.putExtra(AuthenticatorActivity.ACCOUNT_NAME, account.name);
		
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#getAuthTokenLabel(java.lang.String)
	 */
	@Override
	public String getAuthTokenLabel(String authTokenType) {
		if (AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType)) {
			return AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
		} else if (AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType)) {
			return AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
		} else {
			return authTokenType + " (Label)";
		}
	}
	
	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#hasFeatures(android.accounts.AccountAuthenticatorResponse, android.accounts.Account, java.lang.String[])
	 */
	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		final Bundle result = new Bundle();
		//result.putBoolean(KEY_BOOLEAN_RESULT, false);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#confirmCredentials(android.accounts.AccountAuthenticatorResponse, android.accounts.Account, android.os.Bundle)
	 */
	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#editProperties(android.accounts.AccountAuthenticatorResponse, java.lang.String)
	 */
	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		return null;
	}

	/* (non-Javadoc)
	 * @see android.accounts.AbstractAccountAuthenticator#updateCredentials(android.accounts.AccountAuthenticatorResponse, android.accounts.Account, java.lang.String, android.os.Bundle)
	 */
	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}

}
