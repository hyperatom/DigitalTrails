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
 * CRUD operations for video data sources.
 */
public class VideoDataSource extends MediaDataSource {

	/**
	 * 
	 */
	private static final String TAG = "VideoDataSource";
	
	/**
	 * Constructor
	 * @param context
	 */
	protected VideoDataSource(Context context) {
		super(context);
		mTable = DbSchema.TABLE_WAYPOINT_VIDEO;
	}
	
	/**
	 * Create a video
	 * @param fileLocation
	 * @return the created video
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
	 * delete the chosen video
	 * @param video the video to delete
	 */
	public void deleteVideo(Video video) {
		long id = video.getId();
		Log.i(TAG, "Video deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * Get all videos
	 * @return List of all Videos in database
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
	 * Get all videos at a specified waypoint.
	 * @param wp The Waypoint to retrieve videos for.
	 * @return List of all videos
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
