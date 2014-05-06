package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Audio;
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
	 * Constructor
	 * @param context The context to use
	 */
	protected AudioDataSource(Context context) {
		super(context);
		mTable = DbSchema.TABLE_WAYPOINT_AUDIO;
	}

	/**
	 * @param fileLocation location of this file
	 * @return Audio component which is created
	 */
	public Audio createAudio(String fileLocation) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], fileLocation);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = "
				+ insertId, null, null, null, null);
		Audio newAudio = cursorToAudio(cursor);
		cursor.close();
		return newAudio;
	}

	/**
	 * Deletes an audio file from the database.
	 * @param audio The audio file to delete
	 */
	public void deleteAudio(Audio audio) {
		long id = audio.getId();
		Log.i(TAG, "Audio deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * Get all audio files in the database
	 * @return A List of all audio files
	 */
	public List<Audio> getAllAudio() {
		ArrayList<Audio> audioList = new ArrayList<Audio>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, null, null,
				null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Audio audio = cursorToAudio(cursor);
			audioList.add(audio);
			cursor.moveToNext();
		}
		
		cursor.close();
		return audioList;
	}
	
	/**
	 * Get all audio objects at a certain waypoint
	 * @param wp Th waypoint
	 * @return List of Audio objects
	 */
	public List<Audio> getAllAudioAtWaypoint(Waypoint wp) {
		ArrayList<Audio> audioList = new ArrayList<Audio>();
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS,  "waypoint_id" + " = " + wp.getId(),  null, null, null, null);
	    cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Audio audio = cursorToAudio(cursor);
			audioList.add(audio);
			cursor.moveToNext();
		}
		cursor.close();
		return audioList;
	}

	/**
	 * Create Audio from the cursor.
	 * 
	 * @param cursor
	 * @return New Audio.
	 */
	private Audio cursorToAudio(Cursor cursor) {
		Audio audio = new Audio();
		audio.setId(cursor.getLong(0));
		audio.setFileLocation(cursor.getString(1));
		return audio;
	}

}
