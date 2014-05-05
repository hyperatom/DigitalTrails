package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;


public class Media {

	@SerializedName("id") protected long mId;
	@SerializedName("url") protected String mFileLocation;
	@SerializedName("thumbnail_url") protected String mThumbnailLocation;
	@SerializedName("caption") protected String mCaption;
	@SerializedName("waypoint_id") protected int mWaypointId;
	protected Waypoint mWaypoint;
	
	public long getId() {
		return mId;
	}
	
	public void setId(long id) {
		this.mId = id;
	}
	
	public String getFileLocation() {
		return mFileLocation;
	}
	
	public void setFileLocation(String location) {
		this.mFileLocation = location;
	}
	
	public Waypoint getWaypoint() {
		return mWaypoint;
	}
	
	public void setWaypoint(Waypoint waypoint) {
		mWaypoint = waypoint;
	}
	
	public Media() {
		setId(-1);
		setFileLocation("NULL");
	}
	
	public Media(int id, String fileLocation, Waypoint place) {
		setId(id);
		setFileLocation(fileLocation);
		setWaypoint(place);
	}
	
}
