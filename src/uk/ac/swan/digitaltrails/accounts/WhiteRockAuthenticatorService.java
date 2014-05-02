package uk.ac.swan.digitaltrails.accounts;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * @author Lewis H
 *
 */
public class WhiteRockAuthenticatorService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		AccountAuthenticator authenticator = new AccountAuthenticator(this);
		return authenticator.getIBinder();
	}



}
