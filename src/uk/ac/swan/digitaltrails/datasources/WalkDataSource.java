package uk.ac.swan.digitaltrails.datasources;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;
import uk.ac.swan.digitaltrails.utils.Duration;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


public class WalkDataSource extends SingletonDataSource {

	private static final String TAG = "WalkDataSource";
	private static final String[] ALL_COLUMNS = { "id", "duration_minutes",
			"distance_miles", "download_count", "difficulty_rating" };

	protected WalkDataSource(Context context) {
		super(context);
		mTable = DatabaseHandler.WALKS_TABLE;
	}


	//TODO: Validation etc
	/**
	 * Create a walk and add to db.
	 * @param duration
	 * @param distance
	 * @param downloadCount
	 * @param difficultyRating
	 * @return
	 */
	public Walk createWalk(int duration,
			double distance, int downloadCount, int difficultyRating) {
		// Somewhat awkward validation.
		if (duration < 0) {
			duration = 0;
		}
		if (distance < 0) {
			distance = 0;
		}
		if (downloadCount < 0) {
			downloadCount = 0;
		}
		if (difficultyRating < 0) {
			difficultyRating = 0;
		}
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
		walk.setDuration(new Duration(cursor.getInt(1)));
		walk.setDistance(cursor.getDouble(2));
		walk.setDownloadCount(cursor.getLong(3));
		walk.setDifficultyRating(cursor.getInt(4));
		return walk;
	}

	

}
