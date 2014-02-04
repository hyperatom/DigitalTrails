package uk.ac.swan.digitaltrails.components;

public class Description {

	protected int mId;
	protected String mTitle;
	protected String mShortDescription;
	protected String mLongDescription;
	protected Languages mLanguage;

	public enum Languages {
		ENGLISH, WELSH
	};

	/**
	 * Setter for id
	 * 
	 * @param id
	 *            to be set
	 */
	public void setId(int id) {
		this.mId = id;
	}

	/**
	 * Getter for id
	 * 
	 * @return the id
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Setter for short description
	 * 
	 * @param shortDescription
	 *            to be set
	 */
	public void setShortDescription(String shortDescription) {
		this.mShortDescription = shortDescription;
	}

	/**
	 * Getter for short description
	 * 
	 * @return short description
	 */
	public String getShortDescription() {
		return mShortDescription;
	}

	/**
	 * Setter for long description
	 * 
	 * @param longDescription
	 *            to be set
	 */
	public void setLongDescription(String longDescription) {
		this.mLongDescription = longDescription;
	}

	/**
	 * Getter for long description
	 * 
	 * @return long description
	 */
	public String getLongDescription() {
		return mLongDescription;
	}

	public void setLanguage(Languages language) {
		mLanguage = language;
	}

	public Languages getLanguage() {
		return mLanguage;
	}

	public Description() {
		setShortDescription("");
		setLongDescription("");
	}

	public Description(int id, String shortDesc, String longDesc,
			Languages language) {
		setId(id);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(language);
	}

	public String toString() {
		return "Short: " + getShortDescription() + " long: "
				+ getLongDescription();
	}

}
