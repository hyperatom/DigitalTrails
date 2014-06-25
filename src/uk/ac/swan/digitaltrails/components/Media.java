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
	protected long mId;
	
	/**
	 * the remote id.
	 */
	@SerializedName("id") protected long mRemoteId;
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
	@SerializedName("waypoint_id") protected long mWaypointId;
	
	
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
	
	public long getRemoteId() {
		return mRemoteId;
	}
	
	public void setRemoteId(long id) {
		mRemoteId = id;
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
	 * Get Waypoint ID
	 * @return ID of waypoint the media belongs to.
	 */
	public long getWaypointId() {
		return mWaypointId;
	}
	
	/**
	 * Set waypoint id
	 * @param id the id to set it to.
	 */
	public void setWaypointId(long id) {
		mWaypointId = id;
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
		setWaypointId(place.getId());
	}
	
}
