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
		if (extras != null && extras.keySet().size() != 0) {
			for (String key : extras.keySet()) {
				sb.append(key + "[" + extras.getByte(key) + "] ");
			}
		}
		
		try {
			String authToken = ACCOUNT_MANAGER.blockingGetAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS, true);
			String userObjectId = ACCOUNT_MANAGER.getUserData(account,  AccountGeneral.ACCOUNT_NAME);
			
			uk.ac.swan.digitaltrails.components.Account acc = new uk.ac.swan.digitaltrails.components.Account(account.name, authToken);
			WhiteRockServerAccessor serverAccessor = new WhiteRockServerAccessor(acc);
			
			Log.i(TAG, "Update Local Walk Data");
			updateLocalData(serverAccessor, syncResult);
			
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
	
	/**
	 * Update the walks
	 * @param remoteMap
	 * @param localWalks
	 * @param batch
	 * @param contentResolver
	 * @throws IOException
	 * @throws RemoteException
	 * @throws OperationApplicationException
	 */
	private void updateLocalWalks(HashMap<Long,Walk> remoteMap, final ArrayList<Walk> localWalks, ArrayList<ContentProviderOperation> batch, ContentResolver contentResolver) throws IOException, RemoteException, OperationApplicationException {		
		for (Walk walk : localWalks) {
			if (walk.getWalkId() == -1) {
				Log.d(TAG, "Walk has invalid walkId");
				// we need to sort this out.
				break;
			}
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
					
					updateWalk(walk, tmp, batch);
				} else {
					Log.i(TAG, "No Action Required: " + existingUri);
				}

				updateLocalWaypointData(walk, tmp, batch, contentResolver);

			} // could delete here, but strategy is to NOT delete from devices.
		}
		// adding new items
		for (Walk walk : remoteMap.values()) {
			//addWalk(walk, batch);
		}		
	}
	
	// TODO: Refactor this messy code.
	private void updateLocalWaypointData(Walk walk, Walk tmpWalk, ArrayList<ContentProviderOperation> batch, ContentResolver contentResolver) {
		Log.d(TAG, "Updating waypoints");
		HashMap<Long, Waypoint> wpMap = new HashMap<Long, Waypoint>();
		WaypointDataSource wpDataSource = new WaypointDataSource(this.getContext());
		ArrayList<Waypoint> remoteWaypoints = tmpWalk.getWaypoints();
		Log.d(TAG, "Number of remote waypoints: " + remoteWaypoints.size());
		
		// Add remote waypoints to the map.
		for (Waypoint remoteWp : remoteWaypoints) {
			Log.d(TAG, "Remote Waypoint Id: " + remoteWp.getWaypointId());
			wpMap.put(remoteWp.getWaypointId(), remoteWp);
		}
		
		ArrayList<Waypoint> localWaypoints = walk.getWaypoints();
		
		if (localWaypoints != null && localWaypoints.size() != 0) {
			// Update existing waypoints.
			for (Waypoint wp : localWaypoints) {
				Log.d(TAG, "Local wp mWaypointId: " + wp.getWaypointId());
				Waypoint tmpWp = wpMap.get(wp.getWaypointId());
				if (tmpWp != null) {
					Log.d(TAG, "Start Updating");
					wpMap.remove(tmpWp.getWaypointId());					
					updateWaypoint(wp, tmpWp, batch);
				} else {
					Log.d(TAG, "Temp waypoint = null");
				}
			}
		}
		// Adding new waypoints.
		for (Waypoint wp : wpMap.values()) {
			Log.d(TAG, "Adding waypoint");
			//addWaypoint(wp, walk, batch);
		}
	}
	private void addWaypoint(Waypoint wp, Walk walk, ArrayList<ContentProviderOperation> batch) {
		Log.d(TAG, "Storing Waypoint for batch insert");
		Uri uri = WhiteRockContract.Waypoint.CONTENT_URI;
		WaypointDataSource wpDataSource = new WaypointDataSource(getContext());
		ContentValues values = wpDataSource.getContentValues(wp);
		values.remove("_id");
		values.remove("waypoint_id");
		values.remove("walk_id");
		values.put("walk_id", walk.getId());
		batch.add(ContentProviderOperation.newInsert(uri)
				.withValues(wpDataSource.getContentValues(wp))
				.withYieldAllowed(true)
				.build());
		
		EnglishWaypointDescriptionDataSource wpDescDataSource = new EnglishWaypointDescriptionDataSource(this.getContext());
		Uri descrUri = WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI.buildUpon().appendPath(Long.toString(wp.getEnglishDescription().getId())).build();
		values.clear();
		values = wpDescDataSource.getContentValues(wp.getEnglishDescription());
		values.remove("_id");
		values.remove("waypoint_id");
		values.put("waypoint_id", wp.getId());
		batch.add(ContentProviderOperation.newUpdate(descrUri)
				.withValues(values)
				.withYieldAllowed(true)
				.build());	
	}
	
	private void updateWaypoint(Waypoint wp, Waypoint tmpWp, ArrayList<ContentProviderOperation> batch) {
		WaypointDataSource wpDataSource = new WaypointDataSource(getContext());
		Uri existingUri = WhiteRockContract.Waypoint.CONTENT_URI.buildUpon().appendPath(Long.toString(wp.getId())).build();
		Log.d(TAG, "Waypoint ids: tmp: " + tmpWp.getId() + " " + tmpWp.getWaypointId() + " wp: " + wp.getId() + " " + wp.getWaypointId());
		ContentValues values = wpDataSource.getContentValues(tmpWp);
		values.remove("_id");	
		values.remove("waypoint_id");
		values.remove("walk_id");
		values.put("waypoint_id", wp.getWaypointId());
		values.put("walk_id", wp.getWalkId());
		// Update as necessary.		
		batch.add(ContentProviderOperation.newUpdate(existingUri)
				.withValues(values)
				.withYieldAllowed(true)
				.build());
		
		EnglishWaypointDescriptionDataSource wpDescDataSource = new EnglishWaypointDescriptionDataSource(this.getContext());
		
		EnglishWaypointDescription descr = wp.getEnglishDescription();
		Log.d(TAG, "local descr: " + descr.getId() + " " + descr.getForeignId() + " " + descr.getLongDescription() + " " + descr.getShortDescription());
		
		existingUri = WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI.buildUpon().appendPath(Long.toString(descr.getId())).build();
		values.clear();
		values = wpDescDataSource.getContentValues(tmpWp.getEnglishDescription());
		values.remove("_id");	
		values.remove("waypoint_id");
		values.remove("description_id");
		values.put("waypoint_id", descr.getForeignId());
		values.put("description_id", descr.getDescriptionId());
		batch.add(ContentProviderOperation.newUpdate(existingUri)
				.withValues(values)
				.withYieldAllowed(true)
				.build());
	}
	
	private void updateWalk(Walk walk, Walk tmp, ArrayList<ContentProviderOperation> batch) {
		WalkDataSource walkDataSource = new WalkDataSource(this.getContext());
		EnglishWalkDescriptionDataSource walkDescrDataSource = new EnglishWalkDescriptionDataSource(this.getContext());
		Uri existingUri = WhiteRockContract.Walk.CONTENT_URI.buildUpon().appendPath(Long.toString(walk.getId())).build();
		Log.i(TAG, "Scheduling Update: " + existingUri);
		ContentValues values = walkDataSource.getContentValues(tmp);
		values.remove("_id");
		batch.add(ContentProviderOperation.newUpdate(existingUri)
				.withValues(values)
				.withYieldAllowed(true)
				.build());
		
		Uri descrUri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI.buildUpon().appendPath(Long.toString(walk.getEnglishDescriptions().getId())).build();
		values.clear();
		values = walkDescrDataSource.getContentValues(tmp.getEnglishDescriptions());
		values.remove("_id");
		values.remove("walk_id");
		batch.add(ContentProviderOperation.newUpdate(descrUri)
				.withValues(values)
				.withYieldAllowed(true)
				.build());
	}
	
	private void addWalk() {
		//TODO: Add walk in batch
	}
	
	private void updateLocalMedia() {
		
	}
	
	private void updateLocalWaypointDescriptions() {
		
	}
	
	private void updateLocalData(WhiteRockServerAccessor serverAccessor, final SyncResult syncResult) throws IOException, RemoteException, OperationApplicationException {
		final ContentResolver contentResolver = getContext().getContentResolver();

		ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
		
		// Get remote data.
		List<Walk> remoteWalks;
		try {
			remoteWalks = serverAccessor.getWalks();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG, "Unable to get remote walks, sync failed");
			return;
		}
		
		// HashMap of walks.
		
		HashMap<Long, Walk> remoteWalksMap = new HashMap<Long, Walk>();
		for (Walk walk : remoteWalks) {
			remoteWalksMap.put(walk.getWalkId(), walk);
		}
		
		// Get local walks.
		ArrayList<Walk> localWalks = (ArrayList<Walk>) getLocalWalks();
		
		if (localWalks == null || localWalks.size() == 0) {
			return;
		}
		
		updateLocalWalks(remoteWalksMap, localWalks, batch, contentResolver);
		contentResolver.applyBatch(WhiteRockContract.AUTHORITY, batch);
		contentResolver.notifyChange(WhiteRockContract.CONTENT_URI, null, false);

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
