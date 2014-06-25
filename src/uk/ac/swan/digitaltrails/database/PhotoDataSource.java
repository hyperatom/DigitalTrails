package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;

/**
 * @author Lewis Hancock
 * Allows for CRUD operations for PhotoDataSource
 */
public class PhotoDataSource extends MediaDataSource {

	/**
	 * 
	 */
	private static final String TAG = "PhotoDataSource";
	
	/**
	 * All columns in table. id, fileLoc, waypointId, remoteId
	 */
	private static final String[] ALL_COLUMNS = WhiteRockContract.WaypointImage.PROJECTION_ALL;
	
	/**
	 * The URI of the table.
	 */
	private static final Uri URI = WhiteRockContract.WaypointImage.CONTENT_URI;

	
	/**
	 * Constructor
	 * @param context
	 */
	public PhotoDataSource(Context context) {
		super(context);
	}
	
	@Override
	public long addMedia(Media m) {
		Photo photo = (Photo) m;
		ContentValues values = getContentValues(photo);
		Uri newAudio = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(newAudio);
	}

	@Override
	public ContentValues getContentValues(Media m) {
		Photo photo = (Photo) m;
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], photo.getFileLocation());
		values.put(ALL_COLUMNS[2], photo.getWaypointId());
		values.put(ALL_COLUMNS[3], photo.getRemoteId());
		return values;
	}

	@Override
	public int updateMedia(Media m) {
		Photo photo = (Photo) m;
		return mContext.getContentResolver().update(URI, this.getContentValues(photo), ALL_COLUMNS[0] + " == " + photo.getId(), null);
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
		ArrayList<Media> photoList = new ArrayList<Media>();
		
		if (cursor != null && cursor.moveToFirst()) {
			cursor.moveToPrevious();
			while (cursor.moveToNext()) {
				Photo photo = new Photo();
				photo.setId(cursor.getLong(0));
				photo.setFileLocation(cursor.getString(1));
				photo.setWaypointId(cursor.getLong(2));
				photo.setRemoteId(cursor.getLong(3));
				photoList.add(photo);
			}
		}
		return photoList;
	}

	@Override
	public void deleteAllMediaAtWalk(Walk walk) {
		// TODO Auto-generated method stub
		
	}
}
