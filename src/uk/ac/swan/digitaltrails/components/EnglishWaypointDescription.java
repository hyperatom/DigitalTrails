package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

public class EnglishWaypointDescription extends WaypointDescription {

	public EnglishWaypointDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	public EnglishWaypointDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
