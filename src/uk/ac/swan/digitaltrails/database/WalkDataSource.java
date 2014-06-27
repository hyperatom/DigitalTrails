package uk.ac.swan.digitaltrails.database;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.EnglishWalkDescription;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.components.WelshWalkDescription;
import uk.ac.swan.digitaltrails.components.WelshWaypointDescription;
import uk.ac.swan.digitaltrails.utils.Duration;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

//TODO : Write convenience methods to get all data at once (walk, waypoint, descr etc).
/**
 * @author Lewis Hancock
 *
 */
public class WalkDataSource extends DataSource {

	/**
	 * 
	 */
	private static final String TAG = "WalkDataSource";

	/**
	 * 
	 */
	private static final String[] ALL_COLUMNS = WhiteRockContract.Walk.PROJECTION_ALL;

	/**
	 * 
	 */
	private static final Uri URI = WhiteRockContract.Walk.CONTENT_URI;

	/**
	 * @param context
	 */
	public WalkDataSource(Context context) {
		super(context);
	}

	/**
	 * @param duration
	 * @param distance
	 * @param downloadCount
	 * @param difficultyRating
	 * @param ownerId
	 * @return
	 */
	public long addWalk(int duration, double distance, int downloadCount, int difficultyRating, long ownerId) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], duration);
		values.put(ALL_COLUMNS[2], distance);
		values.put(ALL_COLUMNS[3], downloadCount);
		values.put(ALL_COLUMNS[4], difficultyRating);
		values.put(ALL_COLUMNS[5], ownerId);
		//values.put(ALL_COLUMNS[6], walkId)
		Uri addedWalk = mContext.getContentResolver().insert(URI, values);
		return ContentUris.parseId(addedWalk);
	}

	/**
	 * @param walk
	 * @return
	 */
	public long addWalk(Walk walk) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], walk.getDuration());
		values.put(ALL_COLUMNS[2], walk.getDistance());
		values.put(ALL_COLUMNS[3], walk.getDownloadCount());
		values.put(ALL_COLUMNS[4], walk.getDifficultyRating());
		values.put(ALL_COLUMNS[5], walk.getOwner());
		values.put(ALL_COLUMNS[6], walk.getWalkId());
		Log.d(TAG, "walkId = " + walk.getWalkId());
		Uri addedWalk = mContext.getContentResolver().insert(URI, values);
		Log.d(TAG, "Walk added at pos: " + ContentUris.parseId(addedWalk));
		return ContentUris.parseId(addedWalk);
	}

	/**
	 * Return ContentValues for the walk.
	 * @param walk the walk to deal with.
	 * @return The ContentValues for the walk.
	 */
	public ContentValues getContentValues(Walk walk) {
		ContentValues values = new ContentValues();
		values.put(ALL_COLUMNS[1], walk.getDuration());
		values.put(ALL_COLUMNS[2], walk.getDistance());
		values.put(ALL_COLUMNS[3], walk.getDownloadCount());
		values.put(ALL_COLUMNS[4], walk.getDifficultyRating());
		values.put(ALL_COLUMNS[5], walk.getOwner());
		values.put(ALL_COLUMNS[6], walk.getWalkId());
		return values;
	}

	/** 
	 * Delete walk from database
	 * @param id the id of the walk to delete
	 */
	public void deleteWalk(long id) {
		Log.i(TAG, "Walk deleted with id: " + id);
		mContext.getContentResolver().delete(URI, ALL_COLUMNS[0] + " = " + id, null);
	}

	/**
	 * @param id
	 * @param duration
	 * @param distance
	 * @param downloadCount
	 * @param difficultyRating
	 * @return
	 */
	public int updateWalk(long id, Integer duration, Double distance, Integer downloadCount, Integer difficultyRating) {
		ContentValues values = new ContentValues();
		if (duration != null) {
			values.put(ALL_COLUMNS[1], duration.intValue());
		}
		if (distance != null) {	
			values.put(ALL_COLUMNS[2], distance.doubleValue());
		}
		if (downloadCount != null) {
			values.put(ALL_COLUMNS[3], downloadCount.doubleValue());
		}
		if (difficultyRating != null) {
			values.put(ALL_COLUMNS[4], difficultyRating.intValue());
		}
		return mContext.getContentResolver().update(URI, values, ALL_COLUMNS[0] + " == " + id, null);
	}

	public int updateWalk(Walk walk) {
		return mContext.getContentResolver().update(URI, this.getContentValues(walk), ALL_COLUMNS[0] + " == " + walk.getId(), null);
	}

	/**
	 * Look up all walks in the database and add them to a list.
	 * @return
	 */
	public List<Walk> getAllWalks() {
		ArrayList<Walk> walkList = new ArrayList<Walk>();
		Cursor cursor = mContext.getContentResolver().query(URI, ALL_COLUMNS, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Walk walk = cursorToWalk(cursor);
			walkList.add(walk);
			cursor.moveToNext();
		}
		cursor.close();
		return walkList;
	}

	/**
	 * Create Walk from the cursor.
	 * @param cursor
	 * @return New Walk.
	 */
	public Walk cursorToWalk(Cursor cursor) {
		Walk walk = new Walk();
		walk.setId(cursor.getLong(0));
		walk.setDuration(cursor.getInt(1));
		walk.setDistance(cursor.getDouble(2));
		walk.setDownloadCount(cursor.getLong(3));
		walk.setDifficultyRating(cursor.getInt(4));
		walk.setOwnerId(cursor.getLong(5));
		walk.setWalkId(cursor.getLong(6));
		return walk;
	}

	/**
	 * Convenience method to add everything at once.
	 * @param walk
	 */
	public void addWalkAndComponents(Walk walk) {
		// TODO: Sort welsh.
		long walkId = this.addWalk(walk);
		Log.d(TAG, "WalkId is: " + walkId);
		DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(mContext);
		walk.getEnglishDescriptions().setForeignId(walkId);
		//walk.getWelshDescriptions().setForeignId(walkId);
		descrDataSource.addDescription(walk.getEnglishDescriptions());
		//descrDataSource = new WelshWalkDescriptionDataSource(mContext);

		WaypointDataSource wpDataSource = new WaypointDataSource(mContext);
		for (Waypoint wp : walk.getWaypoints()) {
			wp.setWalkId(walkId);
			long wpId = wpDataSource.addWaypoint(wp);
			// Descriptions
			wp.getEnglishDescription().setForeignId(wpId);
			descrDataSource = new EnglishWaypointDescriptionDataSource(mContext);
			descrDataSource.addDescription(wp.getEnglishDescription());
			if (wp.getWelshDescription() != null) {
				wp.getWelshDescription().setForeignId(wpId);
			}
			//descrDataSource = new WelshWaypointDescriptionDataSource(mContext);
			//descrDataSource.addDescription(wp.getWelshDescription());

			// Media
			MediaDataSource mediaDataSource = new AudioDataSource(mContext);

			if (wp.getAudioFiles() != null) {
				for (Audio audio : wp.getAudioFiles()) {
					audio.setWaypointId(wpId);
					mediaDataSource.addMedia(audio);
				}
			}
			if (wp.getPhotos() != null) {
				mediaDataSource = new PhotoDataSource(mContext);
				for (Photo photo : wp.getPhotos()) {
					photo.setWaypointId(wpId);
					mediaDataSource.addMedia(photo);
				}
			}

			// TODO: Uncomment when we can download videos.

//			if (wp.getVideos() != null) {
//				mediaDataSource = new VideoDataSource(mContext);
//				for (Video video : wp.getVideos()) {
//					video.setWaypointId(wpId);
//					mediaDataSource.addMedia(video);
//				}
//			}
		}
	}

	public Walk LoadWalkAndComponentsFromCursor(Cursor data) {

		if (data != null && data.moveToFirst()) {
			data.moveToPrevious();
			Walk walk = new Walk();
			Waypoint wp = new Waypoint();
			long audioId = -1;
			long videoId = -1;
			long imageId = -1;

			while (data.moveToNext()) {
				if (data.isFirst()) {
					walk.setId(data.getLong(0));
					walk.setDuration(data.getInt(1));
					walk.setDistance(data.getDouble(2));
					walk.setDownloadCount(data.getLong(3));
					walk.setDifficultyRating(data.getInt(4));
					walk.setOwnerId(data.getInt(5));
					walk.setWalkId(data.getInt(6));
					EnglishWalkDescription englishDescr = new EnglishWalkDescription();
					englishDescr.setId(data.getLong(7));
					englishDescr.setTitle(data.getString(8));
					englishDescr.setShortDescription(data.getString(9));
					englishDescr.setLongDescription(data.getString(10));
					englishDescr.setForeignId(data.getLong(11));
					walk.setEnglishDescriptions(englishDescr);
					WelshWalkDescription welshDescr = new WelshWalkDescription();
					welshDescr.setId(data.getLong(12));
					welshDescr.setTitle(data.getString(13));
					welshDescr.setShortDescription(data.getString(14));
					welshDescr.setLongDescription(data.getString(15));
					welshDescr.setForeignId(data.getLong(16));
					walk.setWelshDescriptions(welshDescr);
					walk.setWaypoints(new ArrayList<Waypoint>());
				}
				if (data.getLong(17) == wp.getId()) {
					// get remaining media -- CARE OF DUPLICATES.
					if (!data.isNull(37) && audioId != data.getLong(37)) {
						Audio audio = new Audio();
						audio.setId(data.getLong(37));
						audio.setFileLocation(data.getString(38));
						audio.setWaypointId(data.getLong(39));
						audio.setRemoteId(data.getLong(40));
						wp.getAudioFiles().add(audio);
					}
					if (!data.isNull(41) && imageId != data.getLong(41)) {
						Photo image = new Photo();
						image.setId(data.getLong(41));
						image.setFileLocation(data.getString(42));
						image.setWaypointId(data.getLong(43));
						image.setRemoteId(data.getLong(44));
						wp.getPhotos().add(image);
					}
					if (!data.isNull(45) && videoId != data.getLong(45)) {
						Video video = new Video();
						video.setId(data.getLong(45));
						video.setFileLocation(data.getString(46));
						video.setWaypointId(data.getLong(47));
						video.setRemoteId(data.getLong(48));
						wp.getVideos().add(video);
					}

				} else {
					Log.d(TAG, "Creating new waypoint");
					// new waypoint
					if (!data.isFirst()) { 
						Log.d(TAG, "Adding Waypoint to walk: " + wp.getEnglishDescription().getTitle());
						walk.getWaypoints().add(wp);
					}
					wp = new Waypoint();
					wp.setId(data.getLong(17));
					wp.setLatitude(data.getDouble(18));
					wp.setLongitude(data.getDouble(19));
					wp.setIsRequest(data.getInt(20));
					wp.setVisitOrder(data.getInt(21));
					wp.setUserId(data.getLong(22));
					wp.setWalkId(data.getLong(23));
					wp.setWaypointId(data.getLong(24));
					EnglishWaypointDescription engWpDescr = new EnglishWaypointDescription();
					engWpDescr.setId(data.getLong(25));
					engWpDescr.setTitle(data.getString(26));
					engWpDescr.setShortDescription(data.getString(27));
					engWpDescr.setLongDescription(data.getString(28));
					engWpDescr.setForeignId(data.getLong(29));
					engWpDescr.setDescriptionId(data.getLong(30));
					wp.setEnglishDescription(engWpDescr);
					WelshWaypointDescription welWpDescr = new WelshWaypointDescription();
					welWpDescr.setId(data.getLong(31));
					welWpDescr.setTitle(data.getString(32));
					welWpDescr.setShortDescription(data.getString(33));
					welWpDescr.setLongDescription(data.getString(34));
					welWpDescr.setForeignId(data.getLong(35));
					welWpDescr.setDescriptionId(data.getLong(36));
					wp.setWelshDescription(welWpDescr);
					wp.setAudioFiles(new ArrayList<Audio>());
					wp.setVideos(new ArrayList<Video>());
					wp.setPhotos(new ArrayList<Photo>());
					if (!data.isNull(37)) {
						Audio audio = new Audio();
						audio.setId(data.getLong(37));
						audio.setFileLocation(data.getString(38));
						audio.setWaypointId(data.getLong(39));
						audio.setRemoteId(data.getLong(40));
						wp.getAudioFiles().add(audio);
						audioId = audio.getId();
					}
					if (!data.isNull(41)) {
						Photo image = new Photo();
						image.setId(data.getLong(41));
						image.setFileLocation(data.getString(42));
						image.setWaypointId(data.getLong(43));
						image.setRemoteId(data.getLong(44));
						wp.getPhotos().add(image);
						imageId = image.getId();
					}
					if (!data.isNull(45)) {
						Video video = new Video();
						video.setId(data.getLong(45));
						video.setFileLocation(data.getString(46));
						video.setWaypointId(data.getLong(47));
						video.setRemoteId(data.getLong(48));
						wp.getVideos().add(video);
						videoId = video.getId();
					}

				}
			}
			// add the last waypoint
			walk.getWaypoints().add(wp);
			data.close();
			return walk;
		}
		data.close();
		return null;
	}

}
