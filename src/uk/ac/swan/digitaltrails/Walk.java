package uk.ac.swan.digitaltrails;

import java.util.ArrayList;

public class Walk {
	String title;
	int id;
	String shortDescription;
	String longDescription;
	Duration duration;
	double distance; /** Total distance to walk in CHOSEN FORMAT */
	ArrayList<Place> places;
	
	//TODO: Clean up the mess this variable has left across the whole project.
	ArrayList<Integer> points; // Y????
	
	/**
	 * Setter for id
	 * @param id to be set
	 */
	public void setID(int id) {
		this.id = id;
	}
	
	/**
	 * Getter for id
	 * @return the id
	 */
	public int getID() {
		return id;
	}

	/**
	 * Setter for title
	 * @param title to be set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Getter for title
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for short description
	 * @param shortDescription to be set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	/**
	 * Getter for short description
	 * @return short description
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	
	/**
	 * Setter for long description
	 * @param longDescription to be set
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	/**
	 * Getter for long description
	 * @return long description
	 */
	public String getLongDescription() {
		return longDescription;
	}
	
	/**
	 * Setter for duration
	 * @param duration to be set
	 */
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	/**
	 * Getter for duration
	 * @return duration
	 */
	public Duration getDuration() {
		return duration;
	}
	
	/**
	 * Setter for distance
	 * @param distance to be set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	/**
	 * Getter for distance
	 * @return distance
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * Setter for places
	 * @param places to be set
	 */
	public void setPlaces(ArrayList<Place> places) {
		this.places = places;
	}
	
	/**
	 * Getter for places
	 * @return places
	 */
	public ArrayList<Place> getPlaces() {
		return places;
	}

	public void setPoints(ArrayList<Integer> in) {
		points = in;
	}
	
	/**
	 * Default Constructor
	*/
	public Walk() {
		setID(-1);
		setTitle("New Walk");
		setShortDescription("Short Description");
		setLongDescription("Long Description");
		setDuration(new Duration(0, 0));
		setDistance(-1);
	}
	
	public Walk(int id) {
		
	}
	

}
