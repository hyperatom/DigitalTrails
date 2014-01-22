package uk.ac.swan.digitaltrails;


/**
 * 
 * @author Lewis Hancock
 * Class to hold hours and minutes it takes to complete a walk.
 */
public class Duration {

	private int hours;
	private int minutes;
	
	public void setHours(int hours) {
		if (hours >= 0 && hours <= 23) {
			this.hours = hours;
		} else {
			
		}
	}
	
	public int getHours() {
		return hours;
	}
	
	public void setMinutes(int minutes) {
		if (minutes >= 0 && minutes <= 59) {
			this.minutes = minutes;
		} else {
			
		}
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public Duration(int hours, int minutes) {
		setHours(hours);
		setMinutes(minutes);
	}
	
	public Duration() {
		
	}
}
