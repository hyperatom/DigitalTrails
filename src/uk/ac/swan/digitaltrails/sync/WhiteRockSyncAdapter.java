package uk.ac.swan.digitaltrails.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
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

		Log.d(TAG, "Syncing!");
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
			Log.d(TAG, "Number of remote walks "+remoteWalks.size());
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
		
		Log.d(TAG, "Beginning updates");
		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		
		// hashtable of remote walks.
		HashMap<Long, Walk> remoteMap = new HashMap<Long, Walk>();
		for (Walk walk : remoteWalks) {
			remoteMap.put(walk.getWalkId(), walk);
			Log.d(TAG, "Remote Walk Ids: " + walk.getWalkId());
		}
		
		WalkDataSource walkDataSource = new WalkDataSource(this.getContext());
		DescriptionDataSource walkDescDataSource = new EnglishWalkDescriptionDataSource(this.getContext());
		DescriptionDataSource wpDescDataSource = new EnglishWaypointDescriptionDataSource(this.getContext());
		WaypointDataSource wpDataSource = new WaypointDataSource(this.getContext());
		
		ArrayList<Walk> localWalks = (ArrayList<Walk>) getLocalWalks();
		assert localWalks != null;
		
		Log.i(TAG, "Found: " + localWalks.size() + " local walks. Computing merge");
		
		for (Walk walk : localWalks) {
			if (walk.getWalkId() == -1) {
				Log.d(TAG, "Walk has invalid walkId");
				// we need to sort this out.
				break;
			}
			// tmp is coming out as null for some reason.
			Walk tmp = remoteMap.get(walk.getWalkId());
			Log.d(TAG, "Walk id: " + walk.getWalkId());
			if (tmp == null) {
				Log.d(TAG, "tmp not found");
			}
			if (tmp != null) {		
				Log.d(TAG, "tmp walkId: " + tmp.getWalkId());

				// We know the walk exists, so we remove it from the map.
				remoteMap.remove(tmp.getWalkId());
								
				// Check to see if we need to update it.
				Uri existingUri = WhiteRockContract.Walk.CONTENT_URI.buildUpon().appendPath(Long.toString(walk.getId())).build();
				
				if ((tmp.getDifficultyRating() != -1 && tmp.getDifficultyRating() != walk.getDifficultyRating()) ||
						tmp.getDownloadCount() != -1 && tmp.getDownloadCount() != walk.getDownloadCount() ||
						tmp.getDistance() != -1 && tmp.getDistance() != walk.getDistance() ||
						tmp.getEnglishDescriptions().getTitle() != null && !tmp.getEnglishDescriptions().getTitle().equals(walk.getEnglishDescriptions().getTitle()) ||
						tmp.getEnglishDescriptions().getLongDescription() != null  && !tmp.getEnglishDescriptions().getLongDescription().equals(walk.getEnglishDescriptions().getLongDescription())) {
		
					
					Log.i(TAG, "Scheduling Update: " + existingUri);
					ContentValues values = walkDataSource.getContentValues(tmp);
					values.remove("_id");
					batch.add(ContentProviderOperation.newUpdate(existingUri)
							.withValues(values)
							.withYieldAllowed(true)
							.build());
					
					Uri descrUri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI.buildUpon().appendPath(Long.toString(walk.getEnglishDescriptions().getId())).build();
					values.clear();
					values = walkDescDataSource.getContentValues(tmp.getEnglishDescriptions());
					values.remove("_id");
					values.remove("walk_id");
					batch.add(ContentProviderOperation.newUpdate(descrUri)
							.withValues(values)
							.withYieldAllowed(true)
							.build());
					
//					descrUri = WhiteRockContract.WelshWalkDescriptions.CONTENT_URI.buildUpon().appendPath(Long.toString(walk.getWelshDescriptions().getId())).build();
//					values.clear();
//					values = walkDescDataSource.getContentValues(tmp.getWelshDescriptions());
//					values.remove("_id");
//					values.remove("walk_id");
//					batch.add(ContentProviderOperation.newUpdate(descrUri)
//							.withValues(values)
//							.withYieldAllowed(true)
//							.build());

				} else {
					Log.i(TAG, "No Action Required: " + existingUri);
				}
				
				//TODO: Fix updating waypoint data.
				//updateLocalWaypointData(walk, tmp, batch);

			} // could delete here, but strategy is to NOT delete from devices.
		}
//		// adding new items
//		for (Walk walk : remoteMap.values()) {
//			Log.i(TAG, "Storing walk for batch insert");
//			Uri uri = WhiteRockContract.Walk.CONTENT_URI;
//			ContentValues values = walkDataSource.getContentValues(walk);
//			Log.d(TAG, "Values in contentvalues: " + values.size());
//			batch.add(ContentProviderOperation.newInsert(uri)
//					.withValues(walkDataSource.getContentValues(walk))
//					.withYieldAllowed(true)
//					.build());
//			
//			Log.i(TAG, "Adding waypoint for batch insert");
//			for (Waypoint wp : walk.getWaypoints()) {
//				Uri wpUri = WhiteRockContract.Waypoint.CONTENT_URI;
//				batch.add(ContentProviderOperation.newInsert(wpUri)
//						.withValues(wpDataSource.getContentValues(wp))
//						.withYieldAllowed(true)
//						.build());
//			}
			//walkDataSource.addWalk(walk);
//		}		
		Log.i(TAG, "Applying Batch Update");
		contentResolver.applyBatch(WhiteRockContract.AUTHORITY, batch);
		contentResolver.notifyChange(WhiteRockContract.Walk.CONTENT_URI, null, false);
	}
	
	private void updateLocalWaypointData(Walk walk, Walk tmpWalk, ArrayList<ContentProviderOperation> batch) {
		Log.d(TAG, "Updating waypoints");
		HashMap<Long, Waypoint> wpMap = new HashMap<Long, Waypoint>();
		WaypointDataSource wpDataSource = new WaypointDataSource(this.getContext());
		// Check Waypoints
		for (Waypoint wp : tmpWalk.getWaypoints()) {
			wpMap.put(wp.getId(), wp);
		}
		Log.d(TAG, "This walk has: " + wpMap.size() + " waypoints");
		for (Waypoint wp : walk.getWaypoints()) {
			Waypoint tmpWp = wpMap.get(wp.getId());
			if (tmpWp != null) {
				wpMap.remove(tmpWp.getId());
				Uri existingUri = WhiteRockContract.Waypoint.CONTENT_URI.buildUpon().appendPath(Long.toString(wp.getId())).build();
				// Check to see if it requires updating.
				if ((tmpWp.getEnglishDescription() != null && !tmpWp.getEnglishDescription().getTitle().equals(wp.getEnglishDescription().getTitle())) ||
					(tmpWp.isRequest() != wp.isRequest()) ||
					(tmpWp.getWelshDescription().getTitle() != null && !tmpWp.getWelshDescription().getTitle().equals(wp.getWelshDescription().getTitle())) || 
					tmpWp.getLatitude() != wp.getLatitude() || 
					tmpWp.getLongitude() != wp.getLongitude() ||
					tmpWp.getVisitOrder() != wp.getVisitOrder()) {
					
					ContentValues values = wpDataSource.getContentValues(tmpWp);
					values.remove("_id");
					values.remove("waypoint_id");
					// Update as necessary.		
					batch.add(ContentProviderOperation.newUpdate(existingUri)
							.withValues(values)
							.build());
				}
			}
		}
		for (Waypoint wp : wpMap.values()) {
			Log.d(TAG, "Storing Waypoint for batch insert");
			Uri uri = WhiteRockContract.Waypoint.CONTENT_URI;
			
			ContentValues values = wpDataSource.getContentValues(wp);
			values.remove("_id");
			values.remove("walk_id");
			values.put("walk_id", walk.getId());
			batch.add(ContentProviderOperation.newInsert(uri)
					.withValues(wpDataSource.getContentValues(wp))
					.build());
		}
	}
	
	private List<Walk> getLocalWalks() {
		WalkDataSource walkDataSource = new WalkDataSource(this.getContext());
		ArrayList<Walk> localWalks = (ArrayList<Walk>) walkDataSource.getAllWalks();
		EnglishWalkDescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(this.getContext());
		EnglishWaypointDescriptionDataSource wpDescrDataSource = new EnglishWaypointDescriptionDataSource(this.getContext());
		
		WaypointDataSource wpDataSource = new WaypointDataSource(this.getContext());
		for (Walk walk : localWalks) {
			walk.setWaypoints(wpDataSource.cursorToWaypoints(wpDataSource.getAllWaypointsInWalk(walk.getId())));
			walk.setEnglishDescriptions(descrDataSource.getDescriptionForWalk(walk));
			for (Waypoint wp : walk.getWaypoints()) {
				wp.setEnglishDescription((EnglishWaypointDescription) (wpDescrDataSource.getDescriptonFromWaypoint(wp)));
			}
		}
		
		return localWalks;
	}
	
}
