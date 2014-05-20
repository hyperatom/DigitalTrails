package uk.ac.swan.digitaltrails.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

/**
 * 
 * @author Lewis Hancock
 * WhiteRockSyncAdapter syncs between server and the local database.
 */
public class WhiteRockSyncAdapter extends AbstractThreadedSyncAdapter {
	
	
	private static final String TAG = "WhiteRockSyncAdapter";
	/** Blank final - may only be initialised once and then not changed */
	private final AccountManager ACCOUNT_MANAGER;
	private final ContentResolver mContentResolver;
	
	/**
	 * Constructor
	 * @param context context to use
	 * @param autoInitialise whether to auto initialise
	 */
	public WhiteRockSyncAdapter(Context context, boolean autoInitialise) {
		super(context, autoInitialise);
		ACCOUNT_MANAGER = AccountManager.get(context);
		mContentResolver = context.getContentResolver();
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
			
			Log.d(TAG, "Get Remote Walks");
			List<Walk> remoteWalks = serverAccessor.getWalks();
			
			Log.d(TAG, "Get Local Walks");
			List<Walk> localWalks = new ArrayList<Walk>();
			Cursor curWalks = provider.query(WhiteRockContract.CONTENT_URI, null, null, null, null);
			WalkDataSource wd = new WalkDataSource(this.getContext());
			if (curWalks != null) {
				while (curWalks.moveToNext()) {
					localWalks.add(wd.cursorToWalk(curWalks));
				}
				curWalks.close();
			}
			
			ArrayList<Walk> toRemote = new ArrayList<Walk>();
			for (Walk walk : localWalks) {
				if (!remoteWalks.contains(walk)) {
					toRemote.add(walk);
				}
			}
			
			ArrayList<Walk> toLocal = new ArrayList<Walk>();
			for (Walk walk : remoteWalks) {
				if (!localWalks.contains(walk)) {
					toLocal.add(walk);
				}
			}
			
			if (toRemote.size() == 0) {
				Log.d(TAG, "No local changes to be pushed");
			} else {
				Log.d(TAG, "Local to Remote");
				for (Walk walk : toRemote) {
					Log.d(TAG, "Adding walk: " + walk.getId());
					serverAccessor.addWalk(walk);
				}
			}
			
			if (toLocal.size() == 0) {
				Log.d(TAG, "No server changes to pull");
			} else {
				Log.d(TAG, "Updating local db");
				int i = 0;
				ContentValues walkValues[] = new ContentValues[toLocal.size()];
				ArrayList<ContentValues> waypointValues = new ArrayList<ContentValues>();
				ArrayList<ContentValues> wpDescriptionValues = new ArrayList<ContentValues>();
				ArrayList<ContentValues> walkDescriptionValues = new ArrayList<ContentValues>();
				WaypointDataSource wpDataSource = new WaypointDataSource(this.getContext());
				DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this.getContext());
			
				for (Walk walk : toLocal) {
					walkValues[i++] = wd.getContentValues(walk);
					walkDescriptionValues.add(descrDataSource.getContentValues(walk.getEnglishDescriptions()));
					
					ArrayList<Waypoint> wps = walk.getWaypoints();
					descrDataSource = new EnglishWaypointDescriptionDataSource(this.getContext());
					for (Waypoint wp : wps) {
						waypointValues.add(wpDataSource.getContentValues(wp));
						wpDescriptionValues.add(descrDataSource.getContentValues(wp.getEnglishDescription()));						
					}
				}

				provider.bulkInsert(WhiteRockContract.Walk.CONTENT_URI, walkValues);	
				provider.bulkInsert(WhiteRockContract.Waypoint.CONTENT_URI, (ContentValues[]) waypointValues.toArray());
				provider.bulkInsert(WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI, (ContentValues[]) walkDescriptionValues.toArray());
				provider.bulkInsert(WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI, (ContentValues[]) wpDescriptionValues.toArray());
			}
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void updateLocalWalkData(final ArrayList<Walk> remoteWalks, final SyncResult syncResult) throws IOException, RemoteException, OperationApplicationException {
//		final ContentResolver contentResolver = getContext().getContentResolver();
//		
//		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
//		
//		// hashtable of remote walks.
//		HashMap<String, Walk> remoteMap = new HashMap<String, Walk>();
//		for (Walk walk : remoteWalks) {
//			remoteMap.put(walk.getEnglishDescriptions(), value)
//		}
	}

}
