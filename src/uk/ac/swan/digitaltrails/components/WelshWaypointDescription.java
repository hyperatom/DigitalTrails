package uk.ac.swan.digitaltrails.components;


/**
 * @author Thomas Milner
 * Component to contain a welsh waypoint description item
 */
public class WelshWaypointDescription extends WaypointDescription {

	/**
	 * Constructor
	 * @param id
	 * @param title
	 * @param shortDesc
	 * @param longDesc
	 */
	public WelshWaypointDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	/**
	 * Default constructor.
	 */
	public WelshWaypointDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
