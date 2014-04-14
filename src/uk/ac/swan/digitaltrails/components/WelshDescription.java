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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO WriteToParcel Method
		
	}
}
