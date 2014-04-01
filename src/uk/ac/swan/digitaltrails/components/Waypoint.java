/**
 * @author Lewis Hancock
 * @version %I% %G%
 *
 */
package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Waypoint implements Parcelable {

	private long mId;
	private String mTitle;
	private double mLongitude;
	private double mLatitude;
	private LatLng mLatLng;
	private Marker mMarker;
	private int mVisitOrder;
	private boolean mIsRequest; 
	private ArrayList<Photo> mPhotos;
	private ArrayList<Audio> mAudioFiles;
	private ArrayList<Video> mVideos;
	private ArrayList<Description> mDescriptions;
	private ArrayList<Media> mMedia;
	private long mUserId;
	private long mWalkId;

	/**
	 * 
	 * @return
	 */
	public long getId() {
		return mId;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		this.mLatitude = latitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public void setLongitude(double longitude) {
		this.mLongitude = longitude;
	}
	
	public LatLng getLatLng() {
		return mLatLng;
	}
	
	public void setLatLng(LatLng latLng) {
		mLatLng = latLng;
	}
	
	public Marker getMarker() {
		return mMarker;
	}
	
	public void setMarker(Marker marker) {
		mMarker = marker;
	}

	public void setIsRequest(boolean isRequest) {
		mIsRequest = isRequest;
	}
	
	public void setIsRequest(int isRequest) {
		if (isRequest > 0) {
			mIsRequest = true;
		} else {
			mIsRequest = false;
		}
	}
	
	public boolean isRequest() {
		return mIsRequest;
	}
	
	public void setVisitOrder(int visitOrder) {
		mVisitOrder = visitOrder;
	}
	
	public int getVisitOrder() {
		return mVisitOrder;
	}
	
	public ArrayList<Photo> getPhotos() {
		return mPhotos;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.mPhotos = photos;
	}

	public ArrayList<Audio> getAudioFiles() {
		return mAudioFiles;
	}

	public void setAudioFiles(ArrayList<Audio> audioFiles) {
		this.mAudioFiles = audioFiles;
	}

	public ArrayList<Video> getVideos() {
		return mVideos;
	}

	public void setVideos(ArrayList<Video> videos) {
		this.mVideos = videos;
	}
	
	public void setMedia(ArrayList<Media> media) {
		this.mMedia = media;
	}

	public ArrayList<Media> getMediaFiles() {
		return mMedia;
	}

	
	public ArrayList<Description> getDescriptions() {
		return mDescriptions;
	}
	
	public void setDescriptions(ArrayList<Description> descriptions) {
		mDescriptions = descriptions;
	}
	
	public long getWalkId() {
		return mWalkId;
	}
	
	public void setWalkId(long walkId) {
		if (walkId >= 0) {
			mWalkId = walkId;
		}
	}
	
	public long getUserId() {
		return mUserId;
	}
	
	public void setUserId(long userId) {
		if (userId >= 0) {
			mUserId = userId;
		}
	}

	public Waypoint() {
		setId(0);
		setTitle("New Place");
		setLatitude(0);
		setLongitude(0);
	}

	public Waypoint(int id, String title, ArrayList<Description> descriptions, double longitude, double latitude,
			ArrayList<Photo> photos, ArrayList<Audio> audioFiles,
			ArrayList<Video> videos) {
		setId(0);
		setTitle("New Place");
		setLatitude(0);
		setLongitude(0);
		setPhotos(photos);
		setAudioFiles(audioFiles);
		setVideos(videos);
		setDescriptions(descriptions);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(mId);
		dest.writeString(mTitle);
		dest.writeParcelable(mLatLng, flags);
		dest.writeInt(mVisitOrder);
		int temp = mIsRequest ? 1 : 0;
		dest.writeInt(temp);
		Photo[] photoTempArray = new Photo[mPhotos.size()];
		mPhotos.toArray(photoTempArray);
		dest.writeParcelableArray(photoTempArray, flags);
		Audio[] audioTempArray = new Audio[mAudioFiles.size()];
		mAudioFiles.toArray(audioTempArray);
		dest.writeParcelableArray(audioTempArray, flags);
		Video[] videoTempArray = new Video[mVideos.size()];
		mVideos.toArray(videoTempArray);
		dest.writeParcelableArray(videoTempArray, flags);
		Description[] tempDescrArray = (Description[]) mDescriptions.toArray();
		dest.writeParcelableArray(tempDescrArray, flags);
		
		
		/*
		private long mId;
		private String mTitle;
		private double mLongitude;
		private double mLatitude;
		private LatLng mLatLng;
		private Marker mMarker;
		private int mVisitOrder;
		private boolean mIsRequest; 
		private ArrayList<Photo> mPhotos;
		private ArrayList<Audio> mAudioFiles;
		private ArrayList<Video> mVideos;
		private ArrayList<Description> mDescriptions;
		private long mUserId;
		private long mWalkId;
		*/
	}
	
	public static final Parcelable.Creator<Waypoint> CREATOR = new Creator<Waypoint>() {
		public Waypoint createFromParcel(Parcel in) {
			return null;
		}

		@Override
		public Waypoint[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Waypoint[size];
		}
	};
}
