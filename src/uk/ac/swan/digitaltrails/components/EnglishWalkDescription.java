package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

public class EnglishWalkDescription extends EnglishDescription {
	@SerializedName("walk_id") protected long mForeignId;

	public EnglishWalkDescription(int id, String title, String shortDesc, String longDesc) {
		setId(id);
		setTitle(title);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}


	public EnglishWalkDescription() {
		setId(-1);
		setTitle("");
		setShortDescription("");
		setLongDescription("");
	}
}
