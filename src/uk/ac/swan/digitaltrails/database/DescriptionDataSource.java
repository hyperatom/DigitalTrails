package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.WalkDescription;
import uk.ac.swan.digitaltrails.components.WaypointDescription;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


/**
 * @author Lewis Hancock
 * Abstract base class for all DescriptionDataSources to manage Descriptions in the database.
 */
public abstract class DescriptionDataSource extends DataSource {

	/**
	 * Constant TAG to identify the class
	 */
	private static final String TAG = "DescriptionDataSource";
	/**
	 * The Uri of the table to access.
	 */
	protected Uri uri;
	/**
	 * All columns in the table.
	 */
	protected String[] allColumns;
	
	/**
	 * Constructor
	 * @param context The context to use
	 */
	public DescriptionDataSource(Context context) {
		super(context);
	}

	/**
	 * Adds the description to the database
	 * @param title Title of description
	 * @param shortDescr Short description
	 * @param longDescr Long Description
	 * @param foreignId foreign id that the description belongs to
	 * @return The id of the newly added entry.
	 */
	public long addDescription(String title, String shortDescr, String longDescr, long foreignId ) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], title);
		values.put(allColumns[2], shortDescr);
		values.put(allColumns[3], longDescr);
		values.put(allColumns[4], foreignId);
		Uri newDescr = mContext.getContentResolver().insert(uri, values);
		Log.d(TAG, "New description id = " + newDescr);
		return ContentUris.parseId(newDescr);
	}

	/**
	 * Add a Description
	 * @param d the description to add
	 * @return the id of the description
	 */
	public long addDescription(Description d) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], d.getTitle());
		values.put(allColumns[2], d.getShortDescription());
		values.put(allColumns[3], d.getLongDescription());
		Uri newDescr = mContext.getContentResolver().insert(uri, values);
		Log.d(TAG, "New description id = " + newDescr);
		return ContentUris.parseId(newDescr);
	}
	
	/**
	 * Add a WalkDescription
	 * @param d the walk description to add
	 * @return The ID of the newly added WalkDescription
	 */
	public long addDescription(WalkDescription d) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], d.getTitle());
		values.put(allColumns[2], d.getShortDescription());
		values.put(allColumns[3], d.getLongDescription());
		values.put(allColumns[4], d.getForeignId());
		Log.d(TAG, "foreign id: " + d.getForeignId());
		Uri newDescr = mContext.getContentResolver().insert(uri, values);
		Log.d(TAG, "New description id = " + newDescr);
		return ContentUris.parseId(newDescr);
	}
	
	/**
	 * Add a WaypointDescription
	 * @param d the WaypointDescripiton to add
	 * @return the id of the newly added WaypointDescription
	 */
	public long addDescription(WaypointDescription d) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], d.getTitle());
		values.put(allColumns[2], d.getShortDescription());
		values.put(allColumns[3], d.getLongDescription());
		values.put(allColumns[4], d.getForeignId());
		Log.d(TAG, "foreign id: " + d.getForeignId());
		Uri newDescr = mContext.getContentResolver().insert(uri, values);
		Log.d(TAG, "New description id = " + newDescr);
		return ContentUris.parseId(newDescr);
	}
	
	public ContentValues getContentValues(WalkDescription d) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], d.getTitle());
		values.put(allColumns[2], d.getShortDescription());
		values.put(allColumns[3], d.getLongDescription());
		values.put(allColumns[4], d.getForeignId());
		return values;
	}
	
	public ContentValues getContentValues(WaypointDescription d) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], d.getTitle());
		values.put(allColumns[2], d.getShortDescription());
		values.put(allColumns[3], d.getLongDescription());
		values.put(allColumns[4], d.getForeignId());
		return values;
	}

	
	/**
	 * Delete desired description
	 * @param id id of description
	 */
	public void deleteDescription(long id) {
		Log.d(TAG, "Attempting to delete Description " + id);
		mContext.getContentResolver().delete(uri, allColumns[0] + " == " + id, null);
		Log.d(TAG, "Deleted Description with ID: " + id);
	}
	
	/**
	 * Delete all descriptions with the same parentId
	 * @param parentId the parent which will have descriptions deleted
	 */
	public void deleteAllDescriptions(long parentId) {
		// allColumns[4] = walk_id / waypoint_id
		mContext.getContentResolver().delete(uri, allColumns[4] + " == " + parentId, null);
	}
	
	/**
	 * Update the values of a description in the database
	 * @param id
	 * @param title
	 * @param shortDescr
	 * @param longDescr
	 */
	public void updateDescription(long id, String title, String shortDescr, String longDescr) {
		ContentValues values = new ContentValues();
		Log.d(TAG, "Attempting to update Description " + id);
		if (title != null) {
			values.put(allColumns[1], title);
		}
		if (shortDescr != null) {
			values.put(allColumns[2], shortDescr);
		}
		if (longDescr != null) {
			values.put(allColumns[3], longDescr);
		}
		Log.d(TAG, "Where: " + allColumns[0] + " == " + id);
		int numUpdates = mContext.getContentResolver().update(uri, values, allColumns[0] + " == " + id, null);
		Log.d(TAG, uri + " " + values.toString() + " " + allColumns[0] + " == " + id);
		if (numUpdates == 0) {
			Log.e(TAG, "Failed to update Description " + id);
		} else {
			Log.d(TAG, "Updated Description " + id + "updated: " + numUpdates);
		}
	}
	
	/**
	 * Return all descriptions of this type.
	 * @return All descriptions of this type.
	 */
	public abstract List<Description> getAllDescriptions();

	/**
	 * Create Description from the cursor.
	 * 
	 * @param cursor
	 * @return New Description
	 */
	protected abstract Description cursorToDescription(Cursor cursor);




}
