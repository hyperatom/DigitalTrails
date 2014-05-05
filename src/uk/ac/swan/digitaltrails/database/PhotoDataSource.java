package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Waypoint;

/**
 * @author Lewis Hancock
 *
 */
public class PhotoDataSource extends MediaDataSource {

	/**
	 * 
	 */
	private static final String TAG = "PhotoDataSource";
	
	/**
	 * @param context
	 */
	protected PhotoDataSource(Context context) {
		super(context);
		mTable = DbSchema.TABLE_WAYPOINT_IMAGE;
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
	public Photo createPhoto(String fileLocation) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], fileLocation);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = "
				+ insertId, null, null, null, null);
		Photo newPhoto = cursorToPhoto(cursor);
		cursor.close();
		return newPhoto;
	}

	/**
	 * 
	 * @param photo
	 */
	/**
	 * @param photo
	 */
	public void deletePhoto(Photo photo) {
		long id = photo.getId();
		Log.i(TAG, "Photo deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
	 */
	/**
	 * @return
	 */
	public List<Photo> getAllPhoto() {
		ArrayList<Photo> photoList = new ArrayList<Photo>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo photo = cursorToPhoto(cursor);
			photoList.add(photo);
			cursor.moveToNext();
		}
		
		cursor.close();
		return photoList;
	}
	
	/**
	 * 
	 * @return
	 */
	/**
	 * @param wp
	 * @return
	 */
	public List<Photo> getAllPhotosAtWaypoint(Waypoint wp) {
		ArrayList<Photo> photoList = new ArrayList<Photo>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS,  "Waypoint_id" + " = " + wp.getId(),  null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Photo photo = cursorToPhoto(cursor);
			photoList.add(photo);
			cursor.moveToNext();
		}
		
		cursor.close();
		return photoList;
	}
	


	/**
	 * Create Photo from the cursor.
	 * 
	 * @param cursor
	 * @return New Photo.
	 */
	/**
	 * @param cursor
	 * @return
	 */
	private Photo cursorToPhoto(Cursor cursor) {
		Photo photo = new Photo();
		photo.setId(cursor.getLong(0));
		photo.setFileLocation(cursor.getString(1));
		return photo;
	}
	
}
