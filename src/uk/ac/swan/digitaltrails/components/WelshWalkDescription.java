package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

public class WelshWalkDescription extends WalkDescription {

	public WelshWalkDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	public WelshWalkDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
