package uk.ac.swan.digitaltrails.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;

public class PhotoDataSource extends MediaDataSource {

	private static final String TAG = "PhotoDataSource";
	
	protected PhotoDataSource(Context context) {
		super(context);
		mTable = DatabaseHandler.IMAGES_TABLE;
	}
	
	/**
	 * 
	 * @param fileLocation
	 * @return
	 */
	public Photo createPhoto(String fileLocation) {
		ContentValues values = new ContentValues();
		values.put("FileLocation", fileLocation);
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
	public void deletePhoto(Photo photo) {
		long id = photo.getId();
		Log.i(TAG, "Photo deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
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
	private Photo cursorToPhoto(Cursor cursor) {
		Photo photo = new Photo();
		photo.setId(cursor.getLong(0));
		photo.setFileLocation(cursor.getString(1));
		return photo;
	}
	
}
