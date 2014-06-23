package uk.ac.swan.digitaltrails.sync;

import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.utils.WhiteRockApp;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

public class TableObserver extends ContentObserver {

	private Account mAccount;
	
	@SuppressLint("NewApi")
	@Override
	public void onChange(boolean selfChange) {
		onChange(selfChange, null);
	}
	
	
	@Override
	public void onChange(boolean selfChange, Uri changeUri) {
		ContentResolver.requestSync(mAccount, WhiteRockContract.AUTHORITY, new Bundle());
	}
	
	public TableObserver(Handler handler) {
		super(handler);
		Account[] acct = AccountManager.get(WhiteRockApp.getContext()).getAccounts();
		if (acct.length > 0) {
			mAccount = acct[0];
		} else {
			mAccount = null;
		}
	}

}
