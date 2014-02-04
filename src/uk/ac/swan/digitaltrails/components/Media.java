package uk.ac.swan.digitaltrails.components;

public class Media {

	protected long mId;
	protected String mFileLocation;
	protected Place mPlace;
	
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
	
	public Place getPlace() {
		return mPlace;
	}
	
	public void setPlace(Place place) {
		mPlace = place;
	}
	
	public Media() {
		setId(-1);
		setFileLocation("NULL");
	}
	
	public Media(int id, String fileLocation, Place place) {
		setId(id);
		setFileLocation(fileLocation);
		setPlace(place);
	}
	
}
