package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;
import uk.ac.swan.digitaltrails.utils.Duration;

/**
 * @author Lewis Hancock
 * Component to contain a walk item.
 */
public class Walk implements Parcelable {
	/**
	 * id of the walk
	 */
	@SerializedName("id") private long mId;
	/**
	 * english description for walk
	 */
	@SerializedName("english_description") private EnglishWalkDescription mEnglishDescription;
	/**
	 * welsh description for walk
	 */
	@SerializedName("welsh_description") private WelshWalkDescription mWelshDescription;
	/**
	 * duration in minutes
	 */
	@SerializedName("duration_minutes") private int mDuration; //
	/** Total distance to walk in miles */
	@SerializedName("distance_miles") private double mDistance;
	/**
	 * list of waypoints in the walk
	 */
	@SerializedName("waypoints") private ArrayList<Waypoint> mWaypoints;
	/**
	 * id of the user who created the walk
	 */
	@SerializedName("user_id") private long mOwnerId;
	/**
	 * number of times walk has been downloaded
	 */
	@SerializedName("download_count") private long mDownloadCount;
	/**
	 * how difficult the walk is
	 */
	@SerializedName("difficulty_rating") private int mDifficultyRating;

	/**
	 * Setter for id
	 * 
	 * @param id to be set
	 * @return true if set, false otherwise
	 */
	/**
	 * @param id
	 * @return
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
	/**
	 * @return
	 */
	public long getId() {
		return mId;
	}

	/**
	 * @return
	 */
	public WalkDescription getEnglishDescriptions() {
		return mEnglishDescription;
	}

	/**
	 * @param description
	 * @return
	 */
	public boolean setEnglishDescriptions(EnglishWalkDescription description) {
		mEnglishDescription = description;
		return true;
	}
	
	/**
	 * @return
	 */
	public WalkDescription getWelshDescriptions() {
		return mWelshDescription;
	}

	/**
	 * @param description
	 * @return
	 */
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
	public boolean setDuration(int duration) {
		this.mDuration = duration;
		return true;
	}

	/**
	 * Getter for duration
	 * 
	 * @return duration
	 */
	public double getDuration() {
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

	/**
	 * @return
	 */
	public long getOwner() {
		return mOwnerId;
	}

	/**
	 * @param ownerId
	 * @return
	 */
	public boolean setOwnerId(long ownerId) {
		if (ownerId >= 0) {
			mOwnerId = ownerId;
			return true;
		}
		return false;
		
	}

	/**
	 * @return
	 */
	public long getDownloadCount() {
		return mDownloadCount;
	}

	/**
	 * @param downloadCount
	 * @return
	 */
	public boolean setDownloadCount(long downloadCount) {
		if (downloadCount >= 0) {
			mDownloadCount = downloadCount;
		} else {
			mDownloadCount = 0;
		}
		return true;
	}

	/**
	 * @return
	 */
	public int getDifficultyRating() {
		return mDifficultyRating;
	}

	/**
	 * @param difficultyRating
	 * @return
	 */
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
		setDuration(0);
		setDistance(0);
		setDifficultyRating(0);
		setDownloadCount(0);
	}

	/**
	 * Constructor method.
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param longDescription
	 * @param duration
	 * @param distance
	 * @param ownerId
	 * @param downloadCount
	 * @param difficulty
	 */
	public Walk(int id, String title, String shortDescription,
			String longDescription, int duration, double distance,
			long ownerId, long downloadCount, int difficulty) {
		setId(id);
		setDuration(duration);
		setDistance(distance);
		setOwnerId(ownerId);
		setDownloadCount(downloadCount);
		setDifficultyRating(difficulty);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.mEnglishDescription.mTitle;
	}

	/**
	 * Create from parcelable
	 */
	public static final Parcelable.Creator<Walk> CREATOR = new Creator<Walk>() {
		public Walk createFromParcel(Parcel in) {
			Walk newWalk = new Walk();
			newWalk.setId(in.readLong());
			newWalk.setEnglishDescription(in.readParcelable(EnglishWalkDescription.class.getClassLoader()));
			newWalk.setWelshDescription(in.readParcelable(WelshWalkDescription.class.getClassLoader()));
			newWalk.setDuration(in.readInt());
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
	
	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param readParcelable
	 */
	protected void setWelshDescription(Parcelable readParcelable) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param readParcelable
	 */
	protected void setEnglishDescription(Parcelable readParcelable) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeParcelable(mEnglishDescription, flags);
		dest.writeParcelable(mWelshDescription, flags);
		dest.writeInt(mDuration);
		dest.writeDouble(mDistance);
		Waypoint[] tempArray = new Waypoint[mWaypoints.size()];
		mWaypoints.toArray(tempArray);
		dest.writeParcelableArray(tempArray, flags);
		dest.writeLong(mOwnerId);
		dest.writeLong(mDownloadCount);
		dest.writeInt(mDifficultyRating);
	}
}
