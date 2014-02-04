package uk.ac.swan.digitaltrails.components;

import java.util.ArrayList;

import uk.ac.swan.digitailtrails.utils.Duration;

public class Walk {
	private String mTitle;
	private long mId;
	private String mShortDescription;
	private String mLongDescription;
	private String mShortDescriptionWelsh;
	private String mLongDescriptionWelsh;
	private Duration mDuration;
	private double mDistance; /** Total distance to walk in CHOSEN FORMAT */
	private ArrayList<Place> mPlaces;
	private String mOwner;
	
	//TODO: Clean up the mess this variable has left across the whole project.
	private ArrayList<Integer> points; // Y????
	
	/**
	 * Setter for id
	 * @param id to be set
	 */
	public void setId(long id) {
		this.mId = id;
	}
	
	/**
	 * Getter for id
	 * @return the id
	 */
	public long getId() {
		return mId;
	}

	/**
	 * Setter for title
	 * @param title to be set
	 */
	public void setTitle(String title) {
		this.mTitle = title;
	}
	
	/**
	 * Getter for title
	 * @return The title
	 */
	public String getTitle() {
		return mTitle;
	}

	/**
	 * Setter for short description
	 * @param shortDescription to be set
	 */
	public void setShortDescription(String shortDescription) {
		this.mShortDescription = shortDescription;
	}
	
	/**
	 * Getter for short description
	 * @return short description
	 */
	public String getShortDescription() {
		return mShortDescription;
	}
	
	/**
	 * Setter for long description
	 * @param longDescription to be set
	 */
	public void setLongDescription(String longDescription) {
		this.mLongDescription = longDescription;
	}
	
	/**
	 * Getter for long description
	 * @return long description
	 */
	public String getLongDescription() {
		return mLongDescription;
	}
	
	/**
	 * Setter for duration
	 * @param duration to be set
	 */
	public void setDuration(Duration duration) {
		this.mDuration = duration;
	}
	
	/**
	 * Getter for duration
	 * @return duration
	 */
	public Duration getDuration() {
		return mDuration;
	}
	
	/**
	 * Setter for distance
	 * @param distance to be set
	 */
	public void setDistance(double distance) {
		this.mDistance = distance;
	}
	
	/**
	 * Getter for distance
	 * @return distance
	 */
	public double getDistance() {
		return mDistance;
	}
	
	/**
	 * Setter for places
	 * @param places to be set
	 */
	public void setPlaces(ArrayList<Place> places) {
		this.mPlaces = places;
	}
	
	/**
	 * Getter for places
	 * @return places
	 */
	public ArrayList<Place> getPlaces() {
		return mPlaces;
	}

	public void setPoints(ArrayList<Integer> in) {
		points = in;
	}
	
	public String getOwner() {
		return mOwner;
	}
	
	public void setOwner(String owner) {
		mOwner = owner;
	}
	
	/**
	 * Default Constructor
	*/
	public Walk() {
		setId(-1);
		setTitle("New Walk");
		setDuration(new Duration(0, 0));
		setDistance(-1);
		setOwner("none");
	}
	
	/**
	 * Constructor method.
	 * @param id
	 * @param title
	 * @param shortDescription
	 * @param longDescription
	 * @param duration
	 * @param distance
	 */
	public Walk(int id, String title, String shortDescription, String longDescription, Duration duration, double distance, String owner) {
		setId(id);
		setTitle(title);
		setDuration(duration);
		setDistance(distance);
		setOwner(owner);
	}
	

}
