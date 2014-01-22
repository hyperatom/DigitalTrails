package uk.ac.swan.digitaltrails;

public class Video {
	
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
	
	public Video() {
		setID(0);
		setLocation("");
	}

	public Video(int id, String location) {
		setID(id);
		setLocation(location);
	}
	
}
