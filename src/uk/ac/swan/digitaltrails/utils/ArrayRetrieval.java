package uk.ac.swan.digitaltrails.utils;

import java.util.ArrayList;

// I can't remember why I wrote this... erghh - Lewis
public class ArrayRetrieval {

	public static ArrayList<String> getElementNames(ArrayList<?> list) {
		ArrayList<String> retList = new ArrayList<String>();
		
		for (int i = 0; i < list.size(); i++) {
			retList.add(list.get(i).toString());
		}
		return retList;
	}
	
}
