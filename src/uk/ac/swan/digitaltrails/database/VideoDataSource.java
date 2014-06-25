package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
	 * All columns in table. id, fileLoc, waypointId, remoteId
	 */
	private static final String[] ALL_COLUMNS = WhiteRockContract.WaypointVideo.PROJECTION_ALL;
	
	/**
	 * The URI of the table.
	 */
	private static final Uri URI = WhiteRockContract.WaypointVideo.CONTENT_URI;
	
	/**
	 * Constructor
	 * @param context
	 */
	public VideoDataSource(Context context) {
		super(context);
	}

	@Override
	public long addMedia(Media m) {
		Video video = (Video) m;
		ContentValues values = getContentValues(video);
		Uri newAudio = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(newAudio);
	}

	@Override
	public ContentValues getContentValues(Media m) {
		Video video = (Video) m;
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], video.getFileLocation());
		values.put(ALL_COLUMNS[2], video.getWaypointId());
		values.put(ALL_COLUMNS[3], video.getRemoteId());
		return values;
	}

	@Override
	public int updateMedia(Media m) {
		Video video = (Video) m;
		return mContext.getContentResolver().update(URI, this.getContentValues(video), ALL_COLUMNS[0] + " == " + video.getId(), null);
	}

	@Override
	public void deleteMedia(Media m) {
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[0] + " == " + m.getId(), null);
	}

	@Override
	public void deleteMedia(long mediaId) {
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[0] + " == " + mediaId, null);		
	}
	
	@Override
	public void deleteAllMediaAtWaypoint(long waypointId) {
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[2] + " == " + waypointId, null);		
	}

	@Override
	public void deleteAllMediaAtWaypoint(Waypoint waypoint) {
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[2] + " == " + waypoint.getId(), null);		
	}
	
	@Override
	public void deleteAllMediaAtWalk(long walkId) {
	}

	@Override
	public Cursor getAllMediaAtWaypoint(long waypointId) {
		String select = "((" + ALL_COLUMNS[2] + " == " + waypointId + "))";
		return mContext.getContentResolver().query(URI, ALL_COLUMNS, select, null, ALL_COLUMNS[0] + " COLLATE LOCALIZED ASC");
	}
	
	@Override
	public Cursor getAllMediaAtWaypoint(Waypoint waypoint) {
		String select = "((" + ALL_COLUMNS[2] + " == " + waypoint.getId() + "))";
		return mContext.getContentResolver().query(URI, ALL_COLUMNS, select, null, ALL_COLUMNS[0] + " COLLATE LOCALIZED ASC");
	}

	@Override
	public ArrayList<Media> cursorToMedia(Cursor cursor) {
		ArrayList<Media> videoList = new ArrayList<Media>();
		
		if (cursor != null && cursor.moveToFirst()) {
			cursor.moveToPrevious();
			while (cursor.moveToNext()) {
				Video video = new Video();
				video.setId(cursor.getLong(0));
				video.setFileLocation(cursor.getString(1));
				video.setWaypointId(cursor.getLong(2));
				video.setRemoteId(cursor.getLong(3));
				videoList.add(video);
			}
		}
		return videoList;
	}

	@Override
	public void deleteAllMediaAtWalk(Walk walk) {
		// TODO Auto-generated method stub
		
	}
}