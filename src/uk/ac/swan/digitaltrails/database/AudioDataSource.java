package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
/**
 * @author Lewis Hancock
 * Data Access Object for the Audio component.
 */
public class AudioDataSource extends MediaDataSource {

	/**
	 * static constant tag for the class.
	 */
	private static final String TAG = "AudioDataSource";
	
	/**
	 * All columns in table. id, fileLoc, waypointId, remoteId
	 */
	private static final String[] ALL_COLUMNS = WhiteRockContract.WaypointAudio.PROJECTION_ALL;
	
	/**
	 * The URI of the table.
	 */
	private static final Uri URI = WhiteRockContract.WaypointAudio.CONTENT_URI;
	
	/**
	 * Constructor
	 * @param context The context to use
	 */
	public AudioDataSource(Context context) {
		super(context);
	}

	@Override
	public long addMedia(Media m) {
		Audio audio = (Audio) m;
		ContentValues values = getContentValues(audio);
		Uri newAudio = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(newAudio);
	}

	@Override
	public ContentValues getContentValues(Media m) {
		Audio audio = (Audio) m;
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], audio.getFileLocation());
		values.put(ALL_COLUMNS[2], audio.getWaypointId());
		values.put(ALL_COLUMNS[3], audio.getRemoteId());
		return values;
	}

	@Override
	public int updateMedia(Media m) {
		Audio audio = (Audio) m;
		return mContext.getContentResolver().update(URI, this.getContentValues(audio), ALL_COLUMNS[0] + " == " + audio.getId(), null);
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
		ArrayList<Media> audioList = new ArrayList<Media>();
		
		if (cursor != null && cursor.moveToFirst()) {
			cursor.moveToPrevious();
			while (cursor.moveToNext()) {
				Audio audio = new Audio();
				audio.setId(cursor.getLong(0));
				audio.setFileLocation(cursor.getString(1));
				audio.setWaypointId(cursor.getLong(2));
				audio.setRemoteId(cursor.getLong(3));
				audioList.add(audio);
			}
		}
		return audioList;
	}

	@Override
	public void deleteAllMediaAtWalk(Walk walk) {
		// TODO Auto-generated method stub
		
	}



}
