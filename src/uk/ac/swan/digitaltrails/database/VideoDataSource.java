package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

/**
 * @author Lewis Hancock
 *
 */
public class VideoDataSource extends MediaDataSource {

	/**
	 * 
	 */
	private static final String TAG = "VideoDataSource";
	
	/**
	 * @param context
	 */
	protected VideoDataSource(Context context) {
		super(context);
		mTable = DbSchema.TABLE_WAYPOINT_VIDEO;
	}
	
	/**
	 * 
	 * @param fileLocation
	 * @return
	 */
	/**
	 * @param fileLocation
	 * @return
	 */
	public Video createVideo(String fileLocation) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], fileLocation);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = "
				+ insertId, null, null, null, null);
		Video newVideo = cursorToVideo(cursor);
		cursor.close();
		return newVideo;
	}

	/**
	 * 
	 * @param video
	 */
	/**
	 * @param video
	 */
	public void deleteVideo(Video video) {
		long id = video.getId();
		Log.i(TAG, "Video deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
	 */
	/**
	 * @return
	 */
	public List<Video> getAllVideos() {
		ArrayList<Video> videoList = new ArrayList<Video>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Video video = cursorToVideo(cursor);
			videoList.add(video);
			cursor.moveToNext();
		}
		
		cursor.close();
		return videoList;
	}
	
	/**
	 * 
	 * @return
	 */
	/**
	 * @param wp
	 * @return
	 */
	public List<Video> getAllVideosAtWaypoint(Waypoint wp) {
		ArrayList<Video> videoList = new ArrayList<Video>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS,  "Waypoint_id" + " = " + wp.getId(),  null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Video video = cursorToVideo(cursor);
			videoList.add(video);
			cursor.moveToNext();
		}
		
		cursor.close();
		return videoList;
	}


	/**
	 * Create Video from the cursor.
	 * 
	 * @param cursor
	 * @return New Video.
	 */
	/**
	 * @param cursor
	 * @return
	 */
	private Video cursorToVideo(Cursor cursor) {
		Video video = new Video();
		video.setId(cursor.getLong(0));
		video.setFileLocation(cursor.getString(1));
		return video;
	}
	
}
