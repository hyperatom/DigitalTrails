package uk.ac.swan.digitaltrails.datasources;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class WaypointDataSource extends SingletonDataSource {

	private static final String TAG = "WaypointDataSource";
	private static final String[] ALL_COLUMNS = { "id", "Latitude",
			"Longitude", "IsRequest", "VisitOrder" };

	protected WaypointDataSource(Context context) {
		super(context);
		mTable = DatabaseHandler.WAYPOINT_TABLE;
	}

	/**
	 * 
	 * @param fileLocation
	 * @return
	 */
	//TODO: Validation etc
	public Waypoint createWaypoint(double longitude,
			double latitude, int isRequest, int visitOrder) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], latitude);
		values.put(ALL_COLUMNS[2], longitude);
		values.put(ALL_COLUMNS[3], isRequest);
		values.put(ALL_COLUMNS[4], visitOrder);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = "
				+ insertId, null, null, null, null);
		Waypoint newWaypoint = cursorToWaypoint(cursor);
		cursor.close();
		//TODO: get video, audio etc
		return newWaypoint;
	}
	
	/**
	 * Add waypoint to database
	 * @param wp waypoint to add.
	 */
	public void addWaypointToDb(Waypoint wp) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[0], wp.getId());
		values.put(ALL_COLUMNS[1], wp.getLatitude());
		values.put(ALL_COLUMNS[2], wp.getLongitude());
		values.put(ALL_COLUMNS[3], wp.isRequest());
		values.put(ALL_COLUMNS[4], wp.getVisitOrder());
	}
	
	/**
	 * 
	 * @param waypoint
	 */
	public void deleteWaypoint(Waypoint waypoint) {
		long id = waypoint.getId();
		Log.i(TAG, "Waypoint deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
	 */
	public List<Waypoint> getAllWaypoints() {
		ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Waypoint waypoint = cursorToWaypoint(cursor);
			waypointList.add(waypoint);
			cursor.moveToNext();
		}
		cursor.close();
		return waypointList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Waypoint> getAllWaypointsOnWalk(Walk walk) {
		ArrayList<Waypoint> waypointList = new ArrayList<Waypoint>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS,  "Walk_id" + " = " + walk.getId(),  null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Waypoint waypoint = cursorToWaypoint(cursor);
			waypointList.add(waypoint);
			cursor.moveToNext();
		}
		cursor.close();
		return waypointList;
	}


	/**
	 * Create Waypoint from the cursor.
	 * 
	 * @param cursor
	 * @return New Waypoint.
	 */
	private Waypoint cursorToWaypoint(Cursor cursor) {
		Waypoint waypoint = new Waypoint();
		waypoint.setId(cursor.getLong(0));
		waypoint.setLatitude(cursor.getDouble(1));
		waypoint.setLongitude(cursor.getDouble(2));
		waypoint.setIsRequest(cursor.getInt(3));
		waypoint.setVisitOrder(cursor.getInt(4));
		return waypoint;
	}

}
