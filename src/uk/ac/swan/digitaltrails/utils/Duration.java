package uk.ac.swan.digitaltrails.utils;


/**
 * 
 * @author Lewis Hancock
 * Class to hold hours and minutes it takes to complete a walk.
 */
public class Duration {
	private int hours;
	private int minutes;
	
	/**
	 * Set the number of hours the walk should take.
	 * @param hours The number of hours the walk is expected to take
	 * @return 0 for successful, -1 otherwise
	 */
	public int setHours(int hours) {
		if (hours >= 0 && hours <= 23) {
			this.hours = hours;
			return 0;
		} else {
			return -1;
		}
	}
	
	/**
	 * Get the hours the walk should take
	 * @return the number of hours.
	 */
	public int getHours() {
		return hours;
	}
	
	/**
	 * Set the number of minutes the walk should take
	 * @param minutes the number of minutes to set
	 * @return 0 if successful, -1 otherwise.
	 */
	public int setMinutes(int minutes) {
		if (minutes >= 0 && minutes <= 59) {
			this.minutes = minutes;
			return 0;
		} else {
			return -1;
		}
	}
	
	/**
	 * Get number of minutes
	 * @return the number of minutes
	 */
	public int getMinutes() {
		return minutes;
	}
	
	/**
	 * Constructor class
	 * @param hours the hours of duration
	 * @param minutes the minutes of duration
	 */
	public Duration(int hours, int minutes) {
		setHours(hours);
		setMinutes(minutes);
	}
	
	/**
	 * Default Constructor, init to 0:0
	 */
	public Duration() {
		setHours(0);
		setMinutes(0);
	}
	
	public Duration(int minutes) {
		setHours((int) Math.floor(minutes/60));
		setMinutes((int)minutes%60);
	}

	/**
	 * ToString method, returns duration in formation hours:minutes
	 */
	public String toString() {
		return getHours()+":"+getMinutes();
	}
}
