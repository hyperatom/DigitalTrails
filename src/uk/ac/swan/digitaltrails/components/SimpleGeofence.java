package uk.ac.swan.digitaltrails.components;

import com.google.android.gms.location.Geofence;

public class SimpleGeofence {

	private final String mId;
	private final double mLatitude;
	private final double mLongitude;
	private final float mRadius;
	private long mExpirationDuration;
	private int mTransitionType;
	
	public SimpleGeofence(String id, double latitude, double longitude, float radius, long expiration, int transition) {
		
		this.mId = id;
		this.mLatitude = latitude;
		this.mLongitude = longitude;
		this.mRadius = radius;
		this.mExpirationDuration = expiration;
		this.mTransitionType = transition;
	}
	
	// getters and setters
	public String getId() {
		return mId;
	}
	
	public double getLatitude() {
		return mLatitude;
	}
	
	public double getLongitude() {
		return mLongitude;
	}
	
	public float getRadius() {
		return mRadius;
	}
	
	public long getExpirationDuration() {
		return mExpirationDuration;
	}
	
	public int getTransitionType() {
		return mTransitionType;
	}
	
	/**
	 * Create Location Services Geofence object from a 
	 * SimpleGeofence
	 * 
	 * @return A Geofence
	 */
	public Geofence toGeofence() {
		return new Geofence.Builder()
					.setRequestId(mId)
					.setTransitionTypes(mTransitionType)
					.setCircularRegion(mLatitude, mLongitude, mRadius)
					.setExpirationDuration(mExpirationDuration)
					.build();
	}
}
