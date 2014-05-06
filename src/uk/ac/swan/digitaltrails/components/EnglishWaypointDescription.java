package uk.ac.swan.digitaltrails.components;


/**
 * @author Lewis Hancock
 *
 */
public class EnglishWaypointDescription extends WaypointDescription {

	/**
	 * constructor
	 * @param id id
	 * @param title title
	 * @param shortDesc short description
	 * @param longDesc long description
	 */
	public EnglishWaypointDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	/**
	 * default constructor
	 */
	public EnglishWaypointDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
