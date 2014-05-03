package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class Description implements Parcelable {

	@SerializedName("id") protected long mId;
	@SerializedName("title") protected String mTitle;
	@SerializedName("short_description") protected String mShortDescription;
	@SerializedName("long_description") protected String mLongDescription;
	protected int mLanguage;
	@SerializedName("waypoint_id") protected long mForeignId;

	public enum Languages {
		ENGLISH, WELSH
	};

	/**
	 * Setter for id
	 * 
	 * @param id
	 *            to be set
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * Getter for id
	 * 
	 * @return the id
	 */
	public long getId() {
		return mId;
	}

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		mTitle = title;
	}

	/**
	 * 
	 * @return
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

	public void setForeignId(long id) {
		mForeignId = id;
	}

	public long getForeignId() {
		return mForeignId;
	}

	public void setLanguage(int language) {
		mLanguage = language;
	}

	public int getLanguage() {
		return mLanguage;
	}

	public Description() {
		setShortDescription("");
		setLongDescription("");
	}
	
	public Description(String longDescription) {
		setLongDescription(longDescription);
		setShortDescription(longDescription.substring(0, longDescription.length()/2));
	}


	public String toString() {
		return "Title:" + getTitle() + " Short: " + getShortDescription() + " Long: "
				+ getLongDescription() + " Language: " + getLanguage();
	}

	// todo: impl parcelable.
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
