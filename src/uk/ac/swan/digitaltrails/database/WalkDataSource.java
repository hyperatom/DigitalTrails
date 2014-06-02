package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.utils.Duration;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


/**
 * @author Lewis Hancock
 *
 */
public class WalkDataSource extends DataSource {

	/**
	 * 
	 */
	private static final String TAG = "WalkDataSource";

	/**
	 * 
	 */
	private static final String[] ALL_COLUMNS = WhiteRockContract.Walk.PROJECTION_ALL;

	/**
	 * 
	 */
	private static final Uri URI = WhiteRockContract.Walk.CONTENT_URI;

	/**
	 * @param context
	 */
	public WalkDataSource(Context context) {
		super(context);
	}

	/**
	 * @param duration
	 * @param distance
	 * @param downloadCount
	 * @param difficultyRating
	 * @param ownerId
	 * @return
	 */
	public long addWalk(int duration, double distance, int downloadCount, int difficultyRating, long ownerId) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], duration);
		values.put(ALL_COLUMNS[2], distance);
		values.put(ALL_COLUMNS[3], downloadCount);
		values.put(ALL_COLUMNS[4], difficultyRating);
		values.put(ALL_COLUMNS[5], ownerId);
		//values.put(ALL_COLUMNS[6], walkId)
		Uri addedWalk = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(addedWalk);
	}
	
	/**
	 * @param walk
	 * @return
	 */
	public long addWalk(Walk walk) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], walk.getDuration());
		values.put(ALL_COLUMNS[2], walk.getDistance());
		values.put(ALL_COLUMNS[3], walk.getDownloadCount());
		values.put(ALL_COLUMNS[4], walk.getDifficultyRating());
		values.put(ALL_COLUMNS[5], walk.getOwner());
		values.put(ALL_COLUMNS[6], walk.getWalkId());
		Uri addedWalk = mContext.getContentResolver().insert(URI, values);
		Log.d(TAG, "Walk added at pos: " + ContentUris.parseId(addedWalk));
		return ContentUris.parseId(addedWalk);
	}
	
	/**
	 * Return ContentValues for the walk.
	 * @param walk the walk to deal with.
	 * @return The ContentValues for the walk.
	 */
	public ContentValues getContentValues(Walk walk) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], walk.getDuration());
		values.put(ALL_COLUMNS[2], walk.getDistance());
		values.put(ALL_COLUMNS[3], walk.getDownloadCount());
		values.put(ALL_COLUMNS[4], walk.getDifficultyRating());
		values.put(ALL_COLUMNS[5], walk.getOwner());
		values.put(ALL_COLUMNS[6], walk.getWalkId());
		return values;
	}

	/** 
	 * Delete walk from database
	 * @param id the id of the walk to delete
	 */
	/**
	 * @param id
	 */
	public void deleteWalk(long id) {
		Log.i(TAG, "Walk deleted with id: " + id);
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[0] + " = " + id, null);
	}

	/**
	 * @param id
	 * @param duration
	 * @param distance
	 * @param downloadCount
	 * @param difficultyRating
	 * @return
	 */
	public int updateWalk(long id, Integer duration, Double distance, Integer downloadCount, Integer difficultyRating) {
		ContentValues values = new ContentValues();
		if (duration != null) {
		values.put(ALL_COLUMNS[1], duration.intValue());
		}
		if (distance != null) {	
			values.put(ALL_COLUMNS[2], distance.doubleValue());
		}
		if (downloadCount != null) {
			values.put(ALL_COLUMNS[3], downloadCount.doubleValue());
		}
		if (difficultyRating != null) {
			values.put(ALL_COLUMNS[4], difficultyRating.intValue());
		}
		return mContext.getContentResolver().update(URI, values, ALL_COLUMNS[0] + " == " + id, null);
	}
	
	/**
	 * Look up all walks in the database and add them to a list.
	 * @return
	 */
	public List<Walk> getAllWalk() {
		ArrayList<Walk> walkList = new ArrayList<Walk>();
		Cursor cursor = mContext.getContentResolver().query(URI, ALL_COLUMNS, null, null, null);
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
	 * @param cursor
	 * @return New Walk.
	 */
	public Walk cursorToWalk(Cursor cursor) {
		Walk walk = new Walk();
		walk.setId(cursor.getLong(0));
		walk.setDuration(cursor.getInt(1));
		walk.setDistance(cursor.getDouble(2));
		walk.setDownloadCount(cursor.getLong(3));
		walk.setDifficultyRating(cursor.getInt(4));
		return walk;
	}
	
}
