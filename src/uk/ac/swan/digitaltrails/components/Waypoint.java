package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.annotations.SerializedName;

/**
 * @author Lewis Hancock
 * component to contain a waypoint item
 *
 */
public class Waypoint implements Parcelable {

	/**
	 * 
	 */
	@SerializedName("id") private long mId;
	/**
	 * 
	 */
	@SerializedName("longitude") private double mLongitude;
	/**
	 * 
	 */
	@SerializedName("latitude") private double mLatitude;
	/**
	 * 
	 */
	private LatLng mLatLng;
	/**
	 * 
	 */
	private Marker mMarker;
	/**
	 * 
	 */
	@SerializedName("visit_order") private int mVisitOrder;
	/**
	 * 
	 */
	@SerializedName("is_request") private boolean mIsRequest; 
	
	/**
	 * 
	 */
	@SerializedName("english_description") private EnglishWaypointDescription mEnglishDescr;
	/**
	 * 
	 */
	@SerializedName("welsh_description") private WelshWaypointDescription mWelshDescr;
	/**
	 * 
	 */
	@SerializedName("images") private ArrayList<Photo> mPhotos;
	/**
	 * 
	 */
	@SerializedName("audio") private ArrayList<Audio> mAudioFiles;
	/**
	 * 
	 */
	@SerializedName("videos") private ArrayList<Video> mVideos;
	/**
	 * 
	 */
	private ArrayList<Media> mMedia;
	/**
	 * 
	 */
	@SerializedName("user_id") private long mUserId;
	/**
	 * 
	 */
	@SerializedName("walk_id") private long mWalkId;

	/**
	 * 
	 * @return
	 */
	/**
	 * @return
	 */
	public long getId() {
		return mId;
	}

	/**
	 * 
	 * @param id
	 */
	/**
	 * @param id
	 */
	public void setId(long id) {
		this.mId = id;
	}

	/**
	 * @return
	 */
	public double getLatitude() {
		return mLatitude;
	}

	/**
	 * @param latitude
	 */
	public void setLatitude(double latitude) {
		this.mLatitude = latitude;
	}

	/**
	 * @return
	 */
	public double getLongitude() {
		return mLongitude;
	}

	/**
	 * @param longitude
	 */
	public void setLongitude(double longitude) {
		this.mLongitude = longitude;
	}
	
	/**
	 * @return
	 */
	public LatLng getLatLng() {
		return mLatLng;
	}
	
	/**
	 * @param latLng
	 */
	public void setLatLng(LatLng latLng) {
		mLatLng = latLng;
	}
	
	/**
	 * @return
	 */
	public Marker getMarker() {
		return mMarker;
	}
	
	/**
	 * @param marker
	 */
	public void setMarker(Marker marker) {
		mMarker = marker;
	}

	/**
	 * @param isRequest
	 */
	public void setIsRequest(boolean isRequest) {
		mIsRequest = isRequest;
	}
	
	/**
	 * @param isRequest
	 */
	public void setIsRequest(int isRequest) {
		if (isRequest > 0) {
			mIsRequest = true;
		} else {
			mIsRequest = false;
		}
	}
	
	/**
	 * @return
	 */
	public boolean isRequest() {
		return mIsRequest;
	}
	
	/**
	 * @param visitOrder
	 */
	public void setVisitOrder(int visitOrder) {
		mVisitOrder = visitOrder;
	}
	
	/**
	 * @return
	 */
	public int getVisitOrder() {
		return mVisitOrder;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Photo> getPhotos() {
		return mPhotos;
	}

	/**
	 * @param photos
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.mPhotos = photos;
	}

	/**
	 * @return
	 */
	public ArrayList<Audio> getAudioFiles() {
		return mAudioFiles;
	}

	/**
	 * @param audioFiles
	 */
	public void setAudioFiles(ArrayList<Audio> audioFiles) {
		this.mAudioFiles = audioFiles;
	}

	/**
	 * @return
	 */
	public ArrayList<Video> getVideos() {
		return mVideos;
	}

	/**
	 * @param videos
	 */
	public void setVideos(ArrayList<Video> videos) {
		this.mVideos = videos;
	}
	
	/**
	 * @param media
	 */
	public void setMedia(ArrayList<Media> media) {
		this.mMedia = media;
	}

	/**
	 * @return
	 */
	public ArrayList<Media> getMediaFiles() {
		return mMedia;
	}

	
	/**
	 * @return
	 */
	public WaypointDescription getWelshDescription() {
		return mWelshDescr;
	}
	
	/**
	 * @param welshDescr
	 */
	public void setWelshDescription(WelshWaypointDescription welshDescr) {
		mWelshDescr = welshDescr;
	}
	
	/**
	 * @return
	 */
	public EnglishWaypointDescription getEnglishDescription() {
		return mEnglishDescr;
	}
	
	/**
	 * @param englishDescr
	 */
	public void setEnglishDescription(EnglishWaypointDescription englishDescr) {
		mEnglishDescr = englishDescr;
	}
	
	/**
	 * @return
	 */
	public long getWalkId() {
		return mWalkId;
	}
	
	/**
	 * @param walkId
	 */
	public void setWalkId(long walkId) {
		if (walkId >= 0) {
			mWalkId = walkId;
		}
	}
	
	/**
	 * @return
	 */
	public long getUserId() {
		return mUserId;
	}
	
	/**
	 * @param userId
	 */
	public void setUserId(long userId) {
		if (userId >= 0) {
			mUserId = userId;
		}
	}

	/**
	 * Default Constructor
	 */
	public Waypoint() {
		setId(-1);
		setEnglishDescription(new EnglishWaypointDescription());
		getEnglishDescription().setTitle("New Place");
		setLatitude(0);
		setLongitude(0);
		setLatLng(new LatLng(mLatitude, mLongitude));
	}

	/**
	 * Constructor
	 * @param id
	 * @param title
	 * @param engDescr
	 * @param welshDescr
	 * @param longitude
	 * @param latitude
	 * @param photos
	 * @param audioFiles
	 * @param videos
	 */
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getEnglishDescription().getTitle() + " " + mLatitude + " " + mLongitude;		
	}

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
