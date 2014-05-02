package uk.ac.swan.digitaltrails.components;

import android.os.Parcel;

public class WelshDescription extends Description {
	
	public WelshDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	public WelshDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
