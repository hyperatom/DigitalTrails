package uk.ac.swan.digitaltrails.components;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Media implements Parcelable{

	protected long mId;
	protected String mFileLocation;
	protected Waypoint mWaypoint;
	
	public long getId() {
		return mId;
	}
	
	public void setId(long id) {
		this.mId = id;
	}
	
	public String getFileLocation() {
		return mFileLocation;
	}
	
	public void setFileLocation(String location) {
		this.mFileLocation = location;
	}
	
	public Waypoint getWaypoint() {
		return mWaypoint;
	}
	
	public void setWaypoint(Waypoint waypoint) {
		mWaypoint = waypoint;
	}
	
	public Media() {
		setId(-1);
		setFileLocation("NULL");
	}
	
	public Media(int id, String fileLocation, Waypoint place) {
		setId(id);
		setFileLocation(fileLocation);
		setWaypoint(place);
	}

	public static final Parcelable.Creator<Media> CREATOR = new Creator<Media>() {
		public Media createFromParcel(Parcel in) {
			Media newMedia = new Media();
			newMedia.setId(in.readLong());
			newMedia.setFileLocation(in.readString());
			//newMedia.setWaypoint(Waypoint.class.getClassLoader());
			//mWaypoint = (Waypoint) in.readParcelable(Waypoint.class.getClassLoader());
			return newMedia;
		}

		@Override
		public Media[] newArray(int size) {
			return new Media[size];
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
		dest.writeString(mFileLocation);
		dest.writeParcelable(mWaypoint, flags);
	}
	
}
