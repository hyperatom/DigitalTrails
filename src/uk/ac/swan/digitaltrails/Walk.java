package uk.ac.swan.digitaltrails;

import java.util.ArrayList;

//TODO: Create a Points type or use ArrayList<WalkPoint> points; 
public class Walk {
	String title;
	int id;
	String description;
	ArrayList<Integer> points; // Y????
	//TODO: WalkPoints should be added as part of the walk, not stored in the middle of nowhere.

	//TODO: Change all params from "in" to something meaningful.
	public void setID(int in) {
		id = in;
	}

	public void setTitle(String in) {
		title = in;
	}

	public void setDesc(String in) {
		description = in;
	}

	public void setPoints(ArrayList<Integer> in) {
		points = in;
	}

	public String getDesc() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<Integer> getPoints() {
		return points;
	}

	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}

}
