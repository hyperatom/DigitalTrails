package uk.ac.swan.digitaltrails;

import java.util.ArrayList;



public class Walk {
	String title;
	int id;
	String description;
	ArrayList<Integer> points;
	
	public void setID(int in) {
		id=in;
	}
	
	public void setTitle(String in) {
		title=in;
	}
	
	public void setDesc(String in) {
		description=in;
	}
	
	public void setPoints(ArrayList<Integer> in) {
		points=in;
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


