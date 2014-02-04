package uk.ac.swan.digitaltrails.components;

public class Media {

	protected long mId;
	protected String mFileLocation;
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
