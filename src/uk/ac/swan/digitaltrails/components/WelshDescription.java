package uk.ac.swan.digitaltrails.components;

public class WelshDescription extends Description {
	
	public WelshDescription(int id, String shortDesc, String longDesc) {
		setId(id);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.WELSH.ordinal());
	}

}
