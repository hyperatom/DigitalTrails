package uk.ac.swan.digitaltrails.components;

public class EnglishDescription extends Description {
	
	public EnglishDescription(int id, String shortDesc, String longDesc) {
		setId(id);
		setShortDescription(shortDesc);
		setLongDescription(longDesc);
		setLanguage(Description.Languages.ENGLISH.ordinal());
	}

}
