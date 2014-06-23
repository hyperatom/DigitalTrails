package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.components.Waypoint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Lewis Hancock
 *
 */
public class WaypointDataSource extends DataSource {

	/**
	 * 
	 */
	private static final String TAG = "WaypointDataSource";
	/**
	 * 
	 */
	private static final String[] ALL_COLUMNS = WhiteRockContract.Waypoint.PROJECTION_ALL;
	/**
	 * 
	 */
	private static final Uri URI = WhiteRockContract.Waypoint.CONTENT_URI;

	/**
	 * @param context
	 */
	public WaypointDataSource(Context context) {
		super(context);
	}

	/**
	 * @param wp
	 * @return
	 */
	public long addWaypoint(Waypoint wp) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], wp.getLatitude());
		values.put(ALL_COLUMNS[2], wp.getLongitude());
		values.put(ALL_COLUMNS[3], wp.isRequest());
		values.put(ALL_COLUMNS[4], wp.getVisitOrder());
		values.put(ALL_COLUMNS[5], wp.getWalkId());
		values.put(ALL_COLUMNS[6], wp.getUserId());
		values.put(ALL_COLUMNS[7], wp.getWaypointId());
		Log.d(TAG, "New Waypoint Values: " + values.toString());
		Uri newWp = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(newWp);	
	}
	
	public ContentValues getContentValues(Waypoint wp) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], wp.getLatitude());
		values.put(ALL_COLUMNS[2], wp.getLongitude());
		values.put(ALL_COLUMNS[3], wp.isRequest());
		values.put(ALL_COLUMNS[4], wp.getVisitOrder());
		values.put(ALL_COLUMNS[5], wp.getWalkId());
		values.put(ALL_COLUMNS[6], wp.getUserId());
		values.put(ALL_COLUMNS[7], wp.getWaypointId());
		return values;
	}
	
	/**
	 * @param id
	 * @param latitude
	 * @param longitude
	 * @param isRequest
	 * @param visitOrder
	 * @param walkId
	 * @param userId
	 * @return
	 */
	public int updateWaypoint(long id, Double latitude, Double longitude, Integer isRequest, Long visitOrder, Long walkId, Long userId) {
		ContentValues values = new ContentValues();
		if (latitude != null) {
			values.put(ALL_COLUMNS[1], latitude);
		}
		if (longitude != null) {
			values.put(ALL_COLUMNS[2], longitude);
		}
		if (isRequest != null) {
			values.put(ALL_COLUMNS[3], isRequest);
		}
		if (visitOrder != null) {
			values.put(ALL_COLUMNS[4], visitOrder);
		}
		if (walkId != null) {
			values.put(ALL_COLUMNS[5], walkId);
		}
		if (userId != null) {
			values.put(ALL_COLUMNS[6], userId);
		}
		return mContext.getContentResolver().update(URI, values, ALL_COLUMNS[0] + " == " + id, null);
	}
	
	public int updateWaypoint(Waypoint wp) {
		return mContext.getContentResolver().update(URI, this.getContentValues(wp), ALL_COLUMNS[0] + " == " + wp.getId(), null);
	}

	/**
	 * Delete waypoint from database
	 * @param id the id of the waypoint to delete
	 */
	public void deleteWaypoint(long id) {
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[0] + " = " + id, null);
	}

	/**
	 * Delete all waypoints on a walk
	 * @param walkId the walk to delete from
	 */
	public void deleteAllWaypointsInWalk(long walkId) {
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[5] + " = " + walkId, null);
	}

	/**
	 * Get all waypoints from a walk
	 * @param walkId the walk to get waypoints from
	 */
	/**
	 * @param walkId
	 * @return
	 */
	public Cursor getAllWaypointsInWalk(long walkId) {
		String 	select  = "((" + ALL_COLUMNS[5] + " == " + walkId + "))";
		return mContext.getContentResolver().query(URI, ALL_COLUMNS, select, null, ALL_COLUMNS[5] + " COLLATE LOCALIZED ASC");
	}

	/**
	 * Get all waypoints from walk, only returning items set to true, waypointId and walkId.
	 * @param walkId
	 * @param getLatitude
	 * @param getLongitude
	 * @param getIsRequest
	 * @param getVisitOrder
	 * @param getUserId
	 * @return 
	 */
	public Cursor getAllWaypointsInWalk(long walkId, boolean getLatitude, boolean getLongitude, boolean getIsRequest, boolean getVisitOrder, boolean getUserId) {
		String 	select  = "((" + ALL_COLUMNS[5] + " == " + walkId + "))";
		ArrayList<String> projection = new ArrayList<String>();
		projection.add(ALL_COLUMNS[0]);
		if (getLatitude) {
			projection.add(ALL_COLUMNS[1]);
		}
		if (getLongitude) {
			projection.add(ALL_COLUMNS[2]);
		}
		if (getIsRequest) {
			projection.add(ALL_COLUMNS[3]);
		}
		if (getVisitOrder) {
			projection.add(ALL_COLUMNS[4]);
		}
		projection.add(ALL_COLUMNS[5]);
		if (getUserId) {
			projection.add(ALL_COLUMNS[6]);
		}
		projection.trimToSize();
		String[] arr = new String[projection.size()];
		projection.toArray(arr);
		return mContext.getContentResolver().query(URI, arr, select, null, ALL_COLUMNS[5] + " COLLATE LOCALIZED ASC");
	}
	
	public ArrayList<Waypoint> cursorToWaypoints(Cursor cursor) {
		ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
		
		if (cursor != null && cursor.moveToFirst()) {
			cursor.moveToPrevious();
			while (cursor.moveToNext()) {
				Waypoint wp = new Waypoint();
				wp.setId(cursor.getLong(0));
				wp.setLatitude(cursor.getDouble(1));
				wp.setLongitude(cursor.getDouble(2));
				wp.setLatLng(new LatLng(wp.getLatitude(), wp.getLongitude()));
				wp.setIsRequest(cursor.getInt(3));
				wp.setVisitOrder(cursor.getInt(4));
				wp.setWalkId(cursor.getLong(5));
				wp.setUserId(cursor.getLong(6));
				wp.setWaypointId(cursor.getLong(7));
				waypoints.add(wp);
			}
		}
		return waypoints;
		
	}
	
}
