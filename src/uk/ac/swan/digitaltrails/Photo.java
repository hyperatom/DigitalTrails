package uk.ac.swan.digitaltrails;

public class Photo {

	private int id;
	private String location;
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Photo() {
		setID(0);
		setLocation("");
	}

	public Photo(int id, String location) {
		setID(id);
		setLocation(location);
	}
	
}
