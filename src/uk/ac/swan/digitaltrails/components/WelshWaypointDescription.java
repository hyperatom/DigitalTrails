package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

public class WelshWaypointDescription extends WelshDescription {
	@SerializedName("waypoint_id") protected long mForeignId;

	public WelshWaypointDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	public WelshWaypointDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
