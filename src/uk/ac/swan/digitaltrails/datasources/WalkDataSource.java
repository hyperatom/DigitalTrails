package uk.ac.swan.digitaltrails.datasources;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitailtrails.utils.Duration;
import uk.ac.swan.digitaltrails.components.Walk;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;


public class WalkDataSource extends SingletonDataSource {

	private static final String TAG = "WalkDataSource";
	private static final String[] ALL_COLUMNS = { "id", "DurationMinutes",
			"DistanceMiles", "DownloadCount", "DifficultyRating" };

	protected WalkDataSource() {
		super();
		mTable = mDbHandler.WALKS_TABLE;
	}

	/**
	 * 
	 * @param fileLocation
	 * @return
	 */
	//TODO: Validation etc
	public Walk createWalk(int duration,
			double distance, int downloadCount, int difficultyRating) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], duration);
		values.put(ALL_COLUMNS[2], distance);
		values.put(ALL_COLUMNS[3], downloadCount);
		values.put(ALL_COLUMNS[4], difficultyRating);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = "
				+ insertId, null, null, null, null);
		Walk newWalk = cursorToWalk(cursor);
		cursor.close();
		return newWalk;
	}

	/**
	 * 
	 * @param walk
	 */
	public void deleteWalk(Walk walk) {
		long id = walk.getId();
		Log.i(TAG, "Walk deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
	 */
	public List<Walk> getAllWalk() {
		ArrayList<Walk> walkList = new ArrayList<Walk>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, null, null,
				null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Walk walk = cursorToWalk(cursor);
			walkList.add(walk);
			cursor.moveToNext();
		}
		cursor.close();
		return walkList;
	}

	/**
	 * Create Walk from the cursor.
	 * 
	 * @param cursor
	 * @return New Walk.
	 */
	private Walk cursorToWalk(Cursor cursor) {
		Walk walk = new Walk();
		walk.setId(cursor.getLong(0));
		walk.setDuration(new Duration(cursor.getDouble(1)));
		walk.setLongitude(cursor.getDouble(2));
		walk.setIsRequest(cursor.getInt(3));
		walk.setVisitOrder(cursor.getInt(4));
		return walk;
	}
	

}
