package uk.ac.swan.digitaltrails.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 
 * @author Lewis Hancock
 * Service handles account sync. Returns IBinder of WhiteRockSyncAdapter.
 */
/**
 * @author Lewis Hancock
 *
 */
public class WhiteRockSyncService extends Service {

	/**
	 * 
	 */
	private static final Object sSyncAdapterLock = new Object();
	/**
	 * 
	 */
	private static WhiteRockSyncAdapter sSyncAdapter = null;
	
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		synchronized (sSyncAdapterLock) {
			if (sSyncAdapter == null) {
				sSyncAdapter = new WhiteRockSyncAdapter(getApplicationContext(), true);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
