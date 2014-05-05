package uk.ac.swan.digitaltrails.components;

import android.os.Parcel;

public class EnglishDescription extends Description {
	
	public EnglishDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
	
	public EnglishDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.ENGLISH.ordinal());
	}
}
