/**
 * @author Lewis Hancock
 * @version %I% %G%
 *
 */
package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.SerializedName;

public class Waypoint implements Parcelable {

	@SerializedName("id") private long mId;
	@SerializedName("longitude") private double mLongitude;
	@SerializedName("latitude") private double mLatitude;
	private LatLng mLatLng;
	private Marker mMarker;
	@SerializedName("visit_order") private int mVisitOrder;
	@SerializedName("is_request") private boolean mIsRequest; 
	@SerializedName("english_description") private EnglishWaypointDescription mEnglishDescr;
	@SerializedName("welsh_description") private WelshWaypointDescription mWelshDescr;
	@SerializedName("images") private ArrayList<Photo> mPhotos;
	@SerializedName("audio") private ArrayList<Audio> mAudioFiles;
	@SerializedName("videos") private ArrayList<Video> mVideos;
	private ArrayList<Media> mMedia;
	@SerializedName("user_id") private long mUserId;
	@SerializedName("walk_id") private long mWalkId;

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

	
	public WelshDescription getWelshDescription() {
		return mWelshDescr;
	}
	
	public void setWelshDescription(WelshWaypointDescription welshDescr) {
		mWelshDescr = welshDescr;
	}
	
	public EnglishDescription getEnglishDescription() {
		return mEnglishDescr;
	}
	
	public void setEnglishDescription(EnglishWaypointDescription englishDescr) {
		mEnglishDescr = englishDescr;
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
		setId(-1);
		setEnglishDescription(new EnglishWaypointDescription());
		getEnglishDescription().setTitle("New Place");
		setLatitude(0);
		setLongitude(0);
		setLatLng(new LatLng(mLatitude, mLongitude));
	}

	public Waypoint(int id, String title, EnglishWaypointDescription engDescr, WelshWaypointDescription welshDescr, double longitude, double latitude,
			ArrayList<Photo> photos, ArrayList<Audio> audioFiles,
			ArrayList<Video> videos) {
		setId(id);
		setLatitude(latitude);
		setLongitude(longitude);
		setPhotos(photos);
		setAudioFiles(audioFiles);
		setVideos(videos);
		setEnglishDescription(engDescr);
		setWelshDescription(welshDescr);
		setLatLng(new LatLng(mLatitude, mLongitude));
	}
	
	public String toString() {
		return getEnglishDescription().getTitle() + " " + mLatitude + " " + mLongitude;		
	}

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
