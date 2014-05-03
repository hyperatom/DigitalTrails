package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.swan.digitaltrails.utils.Duration;

public class Walk implements Parcelable {
	@SerializedName("id") private long mId;
	@SerializedName("english_description") private EnglishWalkDescription mEnglishDescription;
	@SerializedName("welsh_description") private WelshWalkDescription mWelshDescription;
	@SerializedName("duration_minutes") private Duration mDuration;
	/** Total distance to walk in miles */
	@SerializedName("distance_miles") private double mDistance;
	@SerializedName("waypoints") private ArrayList<Waypoint> mWaypoints;
	@SerializedName("user_id") private long mOwnerId;
	@SerializedName("download_count") private long mDownloadCount;
	@SerializedName("difficulty_rating") private int mDifficultyRating;

	/**
	 * Setter for id
	 * 
	 * @param id to be set
	 * @return true if set, false otherwise
	 */
	public boolean setId(long id) {
		if (id >= 0) {
			this.mId = id;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Getter for id
	 * 
	 * @return the id
	 */
	public long getId() {
		return mId;
	}

	public EnglishDescription getEnglishDescriptions() {
		return mEnglishDescription;
	}

	public boolean setEnglishDescriptions(EnglishWalkDescription description) {
		mEnglishDescription = description;
		return true;
	}
	
	public WelshDescription getWelshDescriptions() {
		return mWelshDescription;
	}

	public boolean setWelshDescriptions(WelshWalkDescription description) {
		mWelshDescription = description;
		return true;
	}

	/**
	 * Setter for duration
	 * 
	 * @param duration
	 *            to be set
	 */
	public boolean setDuration(Duration duration) {
		this.mDuration = duration;
		return true;
	}

	/**
	 * Getter for duration
	 * 
	 * @return duration
	 */
	public Duration getDuration() {
		return mDuration;
	}

	/**
	 * Setter for distance
	 * 
	 * @param distance
	 *            to be set
	 */
	public boolean setDistance(double distance) {
		this.mDistance = distance;
		return true;
	}

	/**
	 * Getter for distance
	 * 
	 * @return distance
	 */
	public double getDistance() {
		return mDistance;
	}

	/**
	 * Setter for places
	 * 
	 * @param waypoints
	 *            to be set
	 */
	public boolean setWaypoints(ArrayList<Waypoint> waypoints) {
		this.mWaypoints = waypoints;
		return true;
	}

	/**
	 * Getter for places
	 * 
	 * @return places
	 */
	public ArrayList<Waypoint> getWaypoints() {
		return mWaypoints;
	}

	public long getOwner() {
		return mOwnerId;
	}

	public boolean setOwnerId(long ownerId) {
		if (ownerId >= 0) {
			mOwnerId = ownerId;
			return true;
		}
		return false;
		
	}

	public long getDownloadCount() {
		return mDownloadCount;
	}

	public boolean setDownloadCount(long downloadCount) {
		if (downloadCount >= 0) {
			mDownloadCount = downloadCount;
		} else {
			mDownloadCount = 0;
		}
		return true;
	}

	public int getDifficultyRating() {
		return mDifficultyRating;
	}

	public boolean setDifficultyRating(int difficultyRating) {
		if (difficultyRating > 0 && difficultyRating < 6) {
			mDifficultyRating = difficultyRating;
		} else {
			mDifficultyRating = 0;
		}
		return true;

	}

	/**
	 * Default Constructor
	 */
	public Walk() {
		// Does not set id or ownerId cause that would break things.
		setDuration(new Duration(0, 0));
		setDistance(0);
		setDifficultyRating(0);
		setDownloadCount(0);
	}

	/**
	 * Constructor method.
	 * 
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param longDescription
	 * @param duration
	 * @param distance
	 */
	public Walk(int id, String title, String shortDescription,
			String longDescription, Duration duration, double distance,
			long ownerId, long downloadCount, int difficulty) {
		setId(id);
		setDuration(duration);
		setDistance(distance);
		setOwnerId(ownerId);
		setDownloadCount(downloadCount);
		setDifficultyRating(difficulty);
	}

	public String toString() {
		return this.mEnglishDescription.mTitle;
	}

	public static final Parcelable.Creator<Walk> CREATOR = new Creator<Walk>() {
		public Walk createFromParcel(Parcel in) {
			Walk newWalk = new Walk();
			newWalk.setId(in.readLong());
			newWalk.setEnglishDescription(in.readParcelable(EnglishDescription.class.getClassLoader()));
			newWalk.setWelshDescription(in.readParcelable(WelshDescription.class.getClassLoader()));
			newWalk.setDuration(new Duration(in.readInt(), in.readInt()));
			newWalk.setDistance(in.readDouble());
			//newWalk.setWaypoints(in.readTypedList(new ArrayList<Waypoint>(), Waypoint.CREATOR));
			return newWalk;
		}

		@Override
		public Walk[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Walk[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	protected void setWelshDescription(Parcelable readParcelable) {
		// TODO Auto-generated method stub
		
	}

	protected void setEnglishDescription(Parcelable readParcelable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeParcelable(mEnglishDescription, flags);
		dest.writeParcelable(mWelshDescription, flags);
		dest.writeInt(mDuration.getHours());
		dest.writeInt(mDuration.getMinutes());
		dest.writeDouble(mDistance);
		Waypoint[] tempArray = new Waypoint[mWaypoints.size()];
		mWaypoints.toArray(tempArray);
		dest.writeParcelableArray(tempArray, flags);
		dest.writeLong(mOwnerId);
		dest.writeLong(mDownloadCount);
		dest.writeInt(mDifficultyRating);
	}
}
