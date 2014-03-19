package uk.ac.swan.digitaltrails.components;

import android.os.Parcel;
import android.os.Parcelable;

public class Description implements Parcelable {

	protected long mId;
	protected String mTitle;
	protected String mShortDescription;
	protected String mLongDescription;
	protected int mLanguage;
	protected long mForeignId;

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

	public Description(int id, String shortDesc, String longDesc,
			Languages language) {
		setId(id);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(language.ordinal());
	}

	public String toString() {
		return "Short: " + getShortDescription() + " long: "
				+ getLongDescription();
	}

	public static final Parcelable.Creator<Description> CREATOR = new Creator<Description>() {
		public Description createFromParcel(Parcel in) {
			Description newDescription = new Description();
			newDescription.setId(in.readLong());
			newDescription.setTitle(in.readString());
			newDescription.setShortDescription(in.readString());
			newDescription.setLongDescription(in.readString());
			newDescription.setLanguage(in.readInt());
			newDescription.setForeignId(in.readLong());
			return newDescription;
		}

		@Override
		public Description[] newArray(int size) {
			return new Description[size];
		}
	};

	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeString(mTitle);
		dest.writeString(mShortDescription);
		dest.writeString(mLongDescription);
		dest.writeInt(mLanguage);
		dest.writeLong(mForeignId);
		
	}

}
