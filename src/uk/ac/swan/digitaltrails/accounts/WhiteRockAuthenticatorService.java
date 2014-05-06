package uk.ac.swan.digitaltrails.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Lewis Hancock
 * Service to allow user's to authenticate with the WhiteRock server
 */
public class WhiteRockAuthenticatorService extends Service {

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		AccountAuthenticator authenticator = new AccountAuthenticator(this);
		return authenticator.getIBinder();
	}



}
