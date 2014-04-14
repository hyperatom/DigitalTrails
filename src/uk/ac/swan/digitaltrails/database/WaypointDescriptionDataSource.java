package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishDescription;
import uk.ac.swan.digitaltrails.components.WelshDescription;
import uk.ac.swan.digitaltrails.components.Description.Languages;
import uk.ac.swan.digitaltrails.components.Waypoint;

public class WaypointDescriptionDataSource extends DescriptionDataSource {


	private static final String TAG = "WaypointDescriptionDataSource";
	private final String[] ALL_COLUMNS = { "id", "title", "short_description",
			"long_description", "waypoint_id" };
	private Languages mLanguage;

	protected WaypointDescriptionDataSource(Context context, Languages language) {
		super(context);
		mLanguage = language;
		switch (language) {
		case ENGLISH:	mTable = DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR;
		break;
		case WELSH: 	mTable = DbSchema.TABLE_WELSH_WAYPOINT_DESCR;
		break;
		}
	}

	/**
	 * 
	 * @param Title
	 * @param shortDescription
	 * @param longDescription
	 * @param waypointID
	 * @return
	 */
	public Description createDescription(String Title, String shortDescription, String longDescription, long waypointID) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], Title);
		values.put(ALL_COLUMNS[2], shortDescription);
		values.put(ALL_COLUMNS[3], longDescription);
		values.put(ALL_COLUMNS[4], waypointID);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = " + insertId, null, null, null, null);
		Description newDescription = cursorToDescription(cursor);
		return newDescription;
	}

	/**
	 * 
	 * @param description
	 */
	public void deleteDescription(Description description) {
		long id = description.getId();
		Log.i(TAG, "Description deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
	 */
	public List<Description> getAllDescriptions() {
		ArrayList<Description> descriptionList = new ArrayList<Description>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Description description = cursorToDescription(cursor);
			descriptionList.add(description);
			cursor.moveToNext();
		}
		cursor.close();
		return descriptionList;
	}

	/**
	 * 
	 * @return
	 */
	public List<Description> getAllDescriptionsAtWaypoint(Waypoint wp) {
		ArrayList<Description> descriptionList = new ArrayList<Description>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS,  "waypoint_id" + " = " + wp.getId(),  null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Description description = cursorToDescription(cursor);
			descriptionList.add(description);
			cursor.moveToNext();
		}
		cursor.close();
		return descriptionList;
	}

	/**
	 * 
	 * @param cursor
	 * @return
	 */

	/**
	 * 
	 * @param cursor
	 * @return
	 */
	private Description cursorToDescription(Cursor cursor) {
		// Assume English.
		Description description = new EnglishDescription();
		if (mLanguage == Description.Languages.WELSH){
			description = new WelshDescription();
		}
		description.setId(cursor.getLong(0));
		description.setTitle(cursor.getString(1));
		description.setShortDescription(cursor.getString(2));
		description.setLongDescription(cursor.getString(3));
		description.setForeignId(cursor.getLong(4));
		return description;
	}

}
