package uk.ac.swan.digitaltrails.datasources;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class VideoDataSource extends MediaDataSource {

	private static final String TAG = "VideoDataSource";
	
	protected VideoDataSource(Context context) {
		super(context);
		mTable = DatabaseHandler.VIDEOS_TABLE;
	}
	
	/**
	 * 
	 * @param fileLocation
	 * @return
	 */
	public Video createVideo(String fileLocation) {
		ContentValues values = new ContentValues();
		values.put("FileLocation", fileLocation);
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
	public void deleteVideo(Video video) {
		long id = video.getId();
		Log.i(TAG, "Video deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
	 */
	public List<Video> getAllVideo() {
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
	private Video cursorToVideo(Cursor cursor) {
		Video video = new Video();
		video.setId(cursor.getLong(0));
		video.setFileLocation(cursor.getString(1));
		return video;
	}
	
}
