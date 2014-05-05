package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class WaypointDataSource extends DataSource {

	private static final String TAG = "WaypointDataSource";
	private static final String[] ALL_COLUMNS = WhiteRockContract.Waypoint.PROJECTION_ALL;
	private static final Uri URI = WhiteRockContract.Waypoint.CONTENT_URI;
	
	public WaypointDataSource(Context context) {
		super(context);
	}

	public long addWaypoint(double latitude, double longitude, int isRequest, long visitOrder, long walkId, long userId) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], latitude);
		values.put(ALL_COLUMNS[2], longitude);
		values.put(ALL_COLUMNS[3], isRequest);
		values.put(ALL_COLUMNS[4], visitOrder);
		values.put(ALL_COLUMNS[5], walkId);
		values.put(ALL_COLUMNS[6], userId);
		Uri newWp = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(newWp);
	}
}
