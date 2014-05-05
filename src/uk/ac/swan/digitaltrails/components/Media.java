package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;


/**
 * @author Lewis Hancock
 * Component which contains media item.
 */
public class Media {

	/**
	 * the id
	 */
	@SerializedName("id") protected long mId;
	/**
	 * the file location
	 */
	@SerializedName("url") protected String mFileLocation;
	/**
	 * the location of the thumbnail
	 */
	@SerializedName("thumbnail_url") protected String mThumbnailLocation;
	/**
	 * the caption of the media
	 */
	@SerializedName("caption") protected String mCaption;
	/**
	 *  the id of the waypoint the media is part of
	 */
	@SerializedName("waypoint_id") protected int mWaypointId;
	/**
	 * 
	 */
	protected Waypoint mWaypoint;
	
	/**
	 * @return
	 */
	public long getId() {
		return mId;
	}
	
	/**
	 * @param id
	 */
	public void setId(long id) {
		this.mId = id;
	}
	
	/**
	 * @return
	 */
	public String getFileLocation() {
		return mFileLocation;
	}
	
	/**
	 * @param location
	 */
	public void setFileLocation(String location) {
		this.mFileLocation = location;
	}
	
	/**
	 * @return
	 */
	public Waypoint getWaypoint() {
		return mWaypoint;
	}
	
	/**
	 * @param waypoint
	 */
	public void setWaypoint(Waypoint waypoint) {
		mWaypoint = waypoint;
	}
	
	/**
	 * Default constructor
	 */
	public Media() {
		setId(-1);
		setFileLocation("NULL");
	}
	
	/**
	 * @param id in
	 * @param fileLocation file location
	 * @param place waypoint media belongs to
	 */
	public Media(int id, String fileLocation, Waypoint place) {
		setId(id);
		setFileLocation(fileLocation);
		setWaypoint(place);
	}
	
}
