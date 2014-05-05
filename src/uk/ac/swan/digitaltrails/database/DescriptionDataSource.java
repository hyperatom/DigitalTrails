package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public abstract class DescriptionDataSource extends DataSource {

	private static final String TAG = "DescriptionDataSource";
	protected Uri uri;
	protected String[] allColumns;
	
	public DescriptionDataSource(Context context) {
		super(context);
	}

	public long addDescription(String title, String shortDescr, String longDescr, long foreignId ) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], title);
		values.put(allColumns[2], shortDescr);
		values.put(allColumns[3], longDescr);
		values.put(allColumns[4], foreignId);
		Uri newDescr = mContext.getContentResolver().insert(uri, values);
		return ContentUris.parseId(newDescr);
	}

	public void deleteDescription(long id) {
		Log.d(TAG, "Attempting to delete Description " + id);
		mContext.getContentResolver().delete(uri, "id" + " == " + id, null);
		Log.d(TAG, "Deleted Description with ID: " + id);
	}
	
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
		mContext.getContentResolver().update(uri, values, "id" + " == " + id, null);
		Log.d(TAG, "Updated Description " + id);
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
