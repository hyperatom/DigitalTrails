package uk.ac.swan.digitaltrails.utils;

import android.app.Application;
import android.content.Context;

public class WhiteRockApp extends Application {
	private static WhiteRockApp instance;
	
	public static WhiteRockApp getInstance() {
		return instance;
	}
	
	public static Context getContext() {
		return instance;
	}
	
	public void onCreate() {
		instance = this;
		super.onCreate();
	}
	
	
}
