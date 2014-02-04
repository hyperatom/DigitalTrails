package uk.ac.swan.digitaltrails;

public class Media {

	private int id;
	private String fileLocation;
	private Place geolocation;
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public void setFileLocation(String location) {
		this.fileLocation = location;
	}
	
	public Media() {
		setID(-1);
		setFileLocation("NULL");
	}
	
	public Media(int id, String fileLocation) {
		setID(id);
		setFileLocation(fileLocation);
	}
	
}
