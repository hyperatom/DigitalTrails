package uk.ac.swan.digitaltrails.datasources;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;

public class AudioDataSource extends MediaDataSource {

	private static final String TAG = "AudioDataSource";

	protected AudioDataSource(Context context) {
		super(context);
		mTable = DatabaseHandler.AUDIO_TABLE;
	}

	/**
	 * 
	 * @param fileLocation
	 * @return
	 */
	public Audio createAudio(String fileLocation) {
		ContentValues values = new ContentValues();
		values.put("FileLocation", fileLocation);
		long insertId = mWhiteRockDB.insert(mTable, null, values);
		Cursor cursor = mWhiteRockDB.query(mTable, ALL_COLUMNS, "id" + " = "
				+ insertId, null, null, null, null);
		Audio newAudio = cursorToAudio(cursor);
		cursor.close();
		return newAudio;
	}

	/**
	 * 
	 * @param audio
	 */
	public void deleteAudio(Audio audio) {
		long id = audio.getId();
		Log.i(TAG, "Audio deleted with id: " + id);
		mWhiteRockDB.delete(mTable, "id" + " = " + id, null);
	}

	/**
	 * 
	 * @return
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
