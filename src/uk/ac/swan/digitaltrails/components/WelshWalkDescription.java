package uk.ac.swan.digitaltrails.components;


/**
 * @author Thomas Milner
 * Component to contain a welsh walk description
 */
public class WelshWalkDescription extends WalkDescription {

	/**
	 * Constructor
	 * @param id
	 * @param title
	 * @param shortDesc
	 * @param longDesc
	 */
	public WelshWalkDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	/**
	 * Default Constructor
	 */
	public WelshWalkDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
