package uk.ac.swan.digitaltrails;

import java.util.ArrayList;

public class WalkPoint {
	private double lat;
	private double lon;
	private String title;
	private String infoText;
	private int id;
	private ArrayList<String> imgList;
	
	public WalkPoint() {
		// TODO: Create dummy constructor values
	}
	
	public WalkPoint(int walkid, double latitude, double longitude, String theTitle, String theInfoText, ArrayList<String> imgArray) {
		lat=0;
		lon=0;
		id=walkid;
		lat=latitude;
		lon=longitude;
		title=theTitle;
		infoText=theInfoText;
		imgList=imgArray;
	}
	
	public WalkPoint(int walkid, double latitude, double longitude, String theTitle, String theInfoText) {
		lat=0;
		lon=0;
		id=walkid;
		lat=latitude;
		lon=longitude;
		title=theTitle;
		infoText=theInfoText;	
	}
	
	public void setImgList(ArrayList<String> in) {
		imgList=in;
	}
	
	public void setID(int in) {
		id=in;
	}
	
	public void setLat(double in) {
		lat=in;
	}
	
	public void setLon(double in) {
		lon=in;
	}
	
	public void setTitle(String in) {
		title=in;
	}
	
	public void setInfoText(String in) {
		infoText=in;
	}
	
	public int getID() {
		return id;
	}
	
	public ArrayList<String> getImgList() {
		return imgList;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getInfoText() {
		return infoText;
	}	
}