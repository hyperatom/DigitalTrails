package uk.ac.swan.digitaltrails.components;

import android.os.Parcelable;

public class Photo extends Media implements Parcelable {
	
	public Photo() {
		super();
	}

	public Photo(int id, String location, Waypoint place) {
		super(id, location, place);
	}
	
	
	
}
