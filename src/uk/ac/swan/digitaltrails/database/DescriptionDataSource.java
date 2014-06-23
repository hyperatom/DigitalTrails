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

	public abstract long addDescription(Description d);
	
	public abstract ContentValues getContentValues(Description d);
	
	public abstract void updateDescription(Description d);
	
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
