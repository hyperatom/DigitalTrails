package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishWalkDescription;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author Lewis Hancock
 * See DescriptionDataSource
 */
public class EnglishWaypointDescriptionDataSource extends DescriptionDataSource {

	private static String TAG = "EnglishWaypointDescriptionDataSource";
	
	/**
	 * @param context
	 */
	public EnglishWaypointDescriptionDataSource(Context context) {
		super(context);
		uri = WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI;
		allColumns = WhiteRockContract.EnglishWaypointDescriptions.PROJECTION_ALL;
	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.database.DescriptionDataSource#getAllDescriptions()
	 */
	@Override
	public List<Description> getAllDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Description getDescriptonFromWaypoint(Waypoint wp) {
		EnglishWaypointDescription d = new EnglishWaypointDescription();
		String 	select  = "((" + allColumns[4] + " == " + wp.getId() + "))";
		d = (EnglishWaypointDescription) cursorToDescription(mContext.getContentResolver().query(uri, allColumns, select, null, allColumns[0] + " COLLATE LOCALIZED ASC"));
		return d;

	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.database.DescriptionDataSource#cursorToDescription(android.database.Cursor)
	 */
	@Override
	protected Description cursorToDescription(Cursor cursor) {
		EnglishWaypointDescription desc = new EnglishWaypointDescription();
		if (cursor != null & cursor.moveToFirst()) {
			desc.setId(cursor.getLong(0));
			desc.setTitle(cursor.getString(1));
			desc.setShortDescription(cursor.getString(2));
			desc.setLongDescription(cursor.getString(3));
			desc.setForeignId(cursor.getLong(4));
			desc.setDescriptionId(cursor.getLong(5));
		}
		return desc;
	}

	@Override
	public long addDescription(Description d) {
		EnglishWaypointDescription descr = (EnglishWaypointDescription) d;
		ContentValues values = new ContentValues();
		values.put(allColumns[1], descr.getTitle());
		values.put(allColumns[2], descr.getShortDescription());
		values.put(allColumns[3], descr.getLongDescription());
		values.put(allColumns[4], descr.getForeignId());
		values.put(allColumns[5], descr.getDescriptionId());
		Uri newDescr = mContext.getContentResolver().insert(uri, values);
		Log.d(TAG, "New description id = " + newDescr);
		return ContentUris.parseId(newDescr);
	}

	@Override
	public ContentValues getContentValues(Description d) {
		EnglishWaypointDescription descr = (EnglishWaypointDescription) d;
		ContentValues values = new ContentValues();
		values.put(allColumns[1], descr.getTitle());
		values.put(allColumns[2], descr.getShortDescription());
		values.put(allColumns[3], descr.getLongDescription());
		values.put(allColumns[4], descr.getForeignId());
		values.put(allColumns[5], descr.getDescriptionId());
		return values;
	}

	@Override
	public void updateDescription(Description d) {
		ContentValues values = this.getContentValues(d);
		int numUpdates = mContext.getContentResolver().update(uri, values, allColumns[0] + " == " + d.getId(), null);
		if (numUpdates == 0) {
			Log.e(TAG, "Failed to update EnglishWalkDescription " + d.getId());
		} else {
			Log.i(TAG, "Updated EnglishWalkDescription " + d.getId());
		}
	}

}
