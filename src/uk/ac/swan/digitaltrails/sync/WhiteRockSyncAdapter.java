package uk.ac.swan.digitaltrails.sync;

import java.io.IOException;

import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * 
 * @author Lewis Hancock
 * WhiteRockSyncAdapter syncs between server and the local database.
 */
/**
 * @author Lewis Hancock
 *
 */
public class WhiteRockSyncAdapter extends AbstractThreadedSyncAdapter {
	
	/** Blank final - may only be initialised once and then not changed */
	/**
	 * 
	 */
	private final AccountManager ACCOUNT_MANAGER;
	
	/**
	 * @param context
	 * @param autoInitialise
	 */
	public WhiteRockSyncAdapter(Context context, boolean autoInitialise) {
		super(context, autoInitialise);
		ACCOUNT_MANAGER = AccountManager.get(context);
	}

	/* (non-Javadoc)
	 * @see android.content.AbstractThreadedSyncAdapter#onPerformSync(android.accounts.Account, android.os.Bundle, java.lang.String, android.content.ContentProviderClient, android.content.SyncResult)
	 */
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {

		StringBuilder sb = new StringBuilder();
		if (extras != null) {
			for (String key : extras.keySet()) {
				sb.append(key + "[" + extras.getByte(key) + "] ");
			}
		}
		
		try {
			String authToken = ACCOUNT_MANAGER.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
			String userObjectId = ACCOUNT_MANAGER.getUserData(account,  AccountGeneral.ACCOUNT_NAME);
			
			uk.ac.swan.digitaltrails.components.Account acc = new uk.ac.swan.digitaltrails.components.Account(account.name, authToken);
			WhiteRockServerAccessor serverAccessor = new WhiteRockServerAccessor(acc);
			
			
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
