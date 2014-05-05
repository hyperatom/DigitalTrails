package uk.ac.swan.digitaltrails.components;


/**
 * @author Lewis Hancock
 * Component to contain Photo items
 */
public class Photo extends Media  {
	
	/**
	 * Default constructor
	 */
	public Photo() {
		super();
	}

	/**
	 * Constructor
	 * @param id
	 * @param location
	 * @param place
	 */
	public Photo(int id, String location, Waypoint place) {
		super(id, location, place);
	}
	
	
	
}
