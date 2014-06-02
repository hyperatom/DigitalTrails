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
import android.net.Uri;
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
			
			Log.i(TAG, "Get Remote Walks");
			List<Walk> remoteWalks = serverAccessor.getWalks();
			
			Log.i(TAG, "Update Local Walk Data");
			updateLocalWalkData((ArrayList<Walk>) remoteWalks, syncResult);
			
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
		final ContentResolver contentResolver = getContext().getContentResolver();
		
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		
		// hashtable of remote walks.
		HashMap<Long, Walk> remoteMap = new HashMap<Long, Walk>();
		for (Walk walk : remoteWalks) {
			remoteMap.put(walk.getWalkId(), walk);
		}
		
		WalkDataSource walkDataSource = new WalkDataSource(this.getContext());
		DescriptionDataSource walkDescDataSource = new EnglishWalkDescriptionDataSource(this.getContext());
		DescriptionDataSource wpDescDataSource = new EnglishWaypointDescriptionDataSource(this.getContext());
		WaypointDataSource wpDataSource = new WaypointDataSource(this.getContext());
		
		ArrayList<Walk> localWalks = (ArrayList<Walk>) walkDataSource.getAllWalk();
		
		assert localWalks != null;
		
		Log.i(TAG, "Found: " + localWalks.size() + " local walks. Computing merge");
		
		for (Walk walk : localWalks) {
			if (walk.getWalkId() == -1) {
				// we need to sort this out.
				return;
			}
			Walk tmp = remoteMap.get(walk.getWalkId());
			if (tmp != null) {				
				// We know the walk exists, so we remove it from the map.
				remoteMap.remove(tmp.getWalkId());
				
				updateLocalWaypointData(walk, tmp);
				
				// Check to see if we need to update it.
				Uri existingUri = WhiteRockContract.Walk.CONTENT_URI.buildUpon().appendPath(Long.toString(tmp.getId())).build();
	
				if ((tmp.getDifficultyRating() != -1 && tmp.getDifficultyRating() != walk.getDifficultyRating()) ||
						tmp.getDownloadCount() != -1 && tmp.getDownloadCount() != walk.getDownloadCount() ||
						tmp.getEnglishDescriptions() != null && !tmp.getEnglishDescriptions().equals(walk.getEnglishDescriptions()) ||
						tmp.getWelshDescriptions() != null && !tmp.getWelshDescriptions().equals(walk.getWelshDescriptions()) ||
						tmp.getDistance() != -1 && tmp.getDistance() != walk.getDistance()) {
					
					Log.i(TAG, "Scheduling Update: " + existingUri);
					//batch.add(ContentProviderOperation.newUpdate(existingUri))
				} else {
					Log.i(TAG, "No Action Required: " + existingUri);
				}
			} else {
				// Entry does not exist. Remove it from db.
				walkDescDataSource.deleteAllDescriptions(walk.getId());
				wpDataSource.deleteAllWaypointsInWalk(walk.getId());
				for (Waypoint wp : walk.getWaypoints()) {
					wpDescDataSource.deleteAllDescriptions(wp.getId());
				}
				walkDataSource.deleteWalk(walk.getId());
			}
		}
		// adding new items
		for (Walk walk : remoteMap.values()) {
			Log.i(TAG, "Inserting remote data");
			walkDataSource.addWalk(walk);
		}		
		Log.i(TAG, "Applying Batch Update");
		mContentResolver.applyBatch(WhiteRockContract.AUTHORITY, batch);
		mContentResolver.notifyChange(WhiteRockContract.Walk.CONTENT_URI, null, false);
	}

	
	private void updateLocalWaypointData(Walk walk, Walk tmpWalk) {
		HashMap<Long, Waypoint> wpMap = new HashMap<Long, Waypoint>();
		// Check Waypoints
		for (Waypoint wp : tmpWalk.getWaypoints()) {
			wpMap.put(wp.getId(), wp);
		}
		for (Waypoint wp : walk.getWaypoints()) {
			Waypoint tmpWp = wpMap.get(wp.getId());
			if (tmpWp != null) {
				// Check to see if it requires updating.
				if ((tmpWp.getEnglishDescription() != null && !tmpWp.getEnglishDescription().equals(wp.getEnglishDescription())) ||
					(tmpWp.isRequest() != wp.isRequest()) ||
					(tmpWp.getWelshDescription() != null && !tmpWp.getWelshDescription().equals(wp.getWelshDescription())) || 
					tmpWp.getLatitude() != wp.getLatitude() || 
					tmpWp.getLongitude() != wp.getLongitude() ||
					tmpWp.getVisitOrder() != wp.getVisitOrder()) {
					
					// Update as necessary.				
				}
			}
		}
	}
	
}
