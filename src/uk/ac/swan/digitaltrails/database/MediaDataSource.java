package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author Lewis Hancock
 * See DataSource
 */
public abstract class MediaDataSource extends DataSource {

	protected String[] ALL_COLUMNS;
	
	/**
	 * @param context
	 */
	protected MediaDataSource(Context context) {
		super(context);
	}

	/**
	 * add Media object to local database
	 * @param m the media
	 * @return the id given to media in database.
	 */
	public abstract long addMedia(Media m);
	
	/**
	 * Get the ContentValues of the Media object
	 * @param m The Media object
	 * @return The ContentValues from m
	 */
	public abstract ContentValues getContentValues(Media m);
	
	/**
	 * Update the contents of the Media object in local database
	 * @param m the Media to update.
	 * @return The number of updates.
	 */
	public abstract int updateMedia(Media m);
	
	/**
	 * Delete Media from database
	 * @param m the Media to delete.
	 */
	public abstract void deleteMedia(Media m);
	
	/**
	 * Delete Media from database
	 * @param mediaId the id to use for deletion.
	 */
	public abstract void deleteMedia(long mediaId);
	
	/**
	 * Delete all Media (of correct type - see child classes) at this Waypoint from local DB.
	 * @param waypointId The id of the Waypoint
	 */
	public abstract void deleteAllMediaAtWaypoint(long waypointId);
	
	/**
	 * Delete all Media (of correct type - see child classes) at this Waypoint from local DB.
	 * @param waypoint the Waypoint to delete Media from
	 */
	public abstract void deleteAllMediaAtWaypoint(Waypoint waypoint);
	
	/**
	 * Delete all Media (of correct type - see child classes) at this Walk from local Db.
	 * @param walkId
	 */
	public abstract void deleteAllMediaAtWalk(long walkId);

	/**
	 * Delete all Media (of correct type - see child classes) at this Walk from local Db.
	 * @param walk
	 */
	public abstract void deleteAllMediaAtWalk(Walk walk);

	/**
	 * Get all Media objects (of correct type - see child classes) from local DB found at this Waypoint
	 * @param waypointId the ID of the waypoint
	 * @return the Cursor containing all of the Media information.
	 */
	public abstract Cursor getAllMediaAtWaypoint(long waypointId);
	
	/**
	 * Get all Media objects (of correct type - see child classes) from local DB found at this Waypoint
	 * @param waypoint The Waypoint from which to get Media.
	 * @return The Cursor object containing Media information.
	 */
	public abstract Cursor getAllMediaAtWaypoint(Waypoint waypoint);
	
	/**
	 * Convert a Cursor Object into an ArrayList of Media Objects.
	 * @param cursor The Cursor object containing Media information.
	 * @return ArrayList of Media files.
	 */
	public abstract ArrayList<Media> cursorToMedia(Cursor cursor); 
	
}
