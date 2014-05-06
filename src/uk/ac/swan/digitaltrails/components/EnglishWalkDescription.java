package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

/**
 * @author Lewis Hancock
 *
 */
public class EnglishWalkDescription extends WalkDescription {

	/**
	 * Constructor 
	 * @param id id of description
	 * @param title title of description
	 * @param shortDesc short description
	 * @param longDesc long description
	 */
	public EnglishWalkDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	/**
	 * default constructor
	 */
	public EnglishWalkDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
