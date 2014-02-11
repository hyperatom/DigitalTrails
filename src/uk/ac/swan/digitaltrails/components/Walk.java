package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.utils.Duration;

public class Walk {
	private String mTitle;
	private long mId;
	private ArrayList<Description> mDescriptions;
	private Duration mDuration;
	private double mDistance;
	/** Total distance to walk in CHOSEN FORMAT */
	private ArrayList<Waypoint> mWaypoints;
	private long mOwnerId;
	private long mDownloadCount;
	private int mDifficultyRating;

	/**
	 * Setter for id
	 * 
	 * @param id
	 *            to be set
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Getter for id
	 * 
	 * @return the id
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Setter for title
	 * 
	 * @param title
	 *            to be set
	 */
	public void setTitle(String title) {
		this.mTitle = title;
	}

	/**
	 * Getter for title
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return mTitle;
	}

	public ArrayList<Description> getDescriptions() {
		return mDescriptions;
	}

	public void setDescriptions(ArrayList<Description> descriptions) {
		mDescriptions = descriptions;
	}

	/**
	 * Setter for duration
	 * 
	 * @param duration
	 *            to be set
	 */
	public void setDuration(Duration duration) {
		this.mDuration = duration;
	}

	/**
	 * Getter for duration
	 * 
	 * @return duration
	 */
	public Duration getDuration() {
		return mDuration;
	}

	/**
	 * Setter for distance
	 * 
	 * @param distance
	 *            to be set
	 */
	public void setDistance(double distance) {
		this.mDistance = distance;
	}

	/**
	 * Getter for distance
	 * 
	 * @return distance
	 */
	public double getDistance() {
		return mDistance;
	}

	/**
	 * Setter for places
	 * 
	 * @param waypoints
	 *            to be set
	 */
	public void setWaypoints(ArrayList<Waypoint> waypoints) {
		this.mWaypoints = waypoints;
	}

	/**
	 * Getter for places
	 * 
	 * @return places
	 */
	public ArrayList<Waypoint> getWaypoints() {
		return mWaypoints;
	}

	public long getOwner() {
		return mOwnerId;
	}

	public void setOwnerId(long ownerId) {
		mOwnerId = ownerId;
	}

	public long getDownloadCount() {
		return mDownloadCount;
	}

	public void setDownloadCount(long downloadCount) {
		if (downloadCount >= 0) {
			mDownloadCount = downloadCount;
		} else {
			mDownloadCount = 0;
		}
	}

	public int getDifficultyRating() {
		return mDifficultyRating;
	}

	public void setDifficultyRating(int difficultyRating) {
		if (difficultyRating > 0 && difficultyRating < 6) {
			mDifficultyRating = difficultyRating;
		} else {
			difficultyRating = 1;
		}
	}

	/**
	 * Default Constructor
	 */
	public Walk() {
		setId(-1);
		setTitle("New Walk");
		setDuration(new Duration(0, 0));
		setDistance(-1);
		setOwnerId(-1);
		setDifficultyRating(0);
		setDownloadCount(0);
	}

	/**
	 * Constructor method.
	 * 
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param longDescription
	 * @param duration
	 * @param distance
	 */
	public Walk(int id, String title, String shortDescription,
			String longDescription, Duration duration, double distance,
			long ownerId, long downloadCount, int difficulty) {
		setId(id);
		setTitle(title);
		setDuration(duration);
		setDistance(distance);
		setOwnerId(ownerId);
		setDownloadCount(downloadCount);
		setDifficultyRating(difficulty);
	}

}
