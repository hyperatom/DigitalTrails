package uk.ac.swan.digitaltrails.components;

/**
 * @author Lewis Hancock
 * Audio component 
 */
public class Audio extends Media {

	/**
	 * Default Constructor
	 */
	public Audio() {
		super();
	}

	/**
	 * Constructor
	 * @param id id of the audio
	 * @param location file location of the audio
	 * @param waypoint the waypoint it belongs to
	 */
	public Audio(int id, String location, Waypoint waypoint) {
		super(id, location, waypoint);
	}
	
	
}
