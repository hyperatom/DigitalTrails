package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Lewis Hancock
 * Class for Description components
 */
public class Description implements Parcelable {

	/**
	 * id of description
	 */
	@SerializedName("id") protected long mId;
	/**
	 * title of description
	 */
	@SerializedName("title") protected String mTitle;
	/**
	 * short description text
	 */
	@SerializedName("short_description") protected String mShortDescription;
	/**
	 * long description text
	 */
	@SerializedName("long_description") protected String mLongDescription;
	/**
	 * Language of this description.
	 */
	protected int mLanguage;

	/**
	 * @author Lewis Hancock
	 *
	 */
	public enum Languages {
		ENGLISH, WELSH
	};

	/**
	 * Setter for id
	 * 
	 * @param id
	 *            to be set
	 */
	/**
	 * @param id
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Getter for id
	 * 
	 * @return the id
	 */
	/**
	 * @return
	 */
	public long getId() {
		return mId;
	}


	/**
	 * set the title
	 * @param title title to set
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * @return The title of the description
	 */
	public String getTitle() {
		return mTitle;
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

	/**
	 * setter for language
	 * @param language the language to set
	 */
	public void setLanguage(int language) {
		mLanguage = language;
	}

	/**
	 * @return get the language
	 */
	public int getLanguage() {
		return mLanguage;
	}

	/**
	 * Default constructor
	 */
	public Description() {
		setShortDescription("");
		setLongDescription("");
	}
	
	/**
	 * Constructor
	 * @param longDescription The long description of the description
	 */
	public Description(String longDescription) {
		setLongDescription(longDescription);
		setShortDescription(longDescription.substring(0, longDescription.length()/2));
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Title:" + getTitle() + " Short: " + getShortDescription() + " Long: "
				+ getLongDescription() + " Language: " + getLanguage();
	}

	// todo: impl parcelable.
	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
