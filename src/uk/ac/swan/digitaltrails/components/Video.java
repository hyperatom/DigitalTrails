package uk.ac.swan.digitaltrails.components;

/**
 * @author Lewis Hancock
 * Component to contain video item
 */
public class Video extends Media {
	
	/**
	 * Default constructor
	 */
	public Video() {
		super();
	}

	/**
	 * Constructor
	 * @param id
	 * @param location
	 * @param place
	 */
	public Video(int id, String location, Waypoint place) {
		super(id, location, place);
	}
	
}
