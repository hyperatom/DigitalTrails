package uk.ac.swan.digitaltrails.utils;

import android.app.Application;
import android.content.Context;

/**
 * @author Lewis Hancock
 *
 */
public class WhiteRockApp extends Application {
	/**
	 * 
	 */
	private static WhiteRockApp instance;
	
	/**
	 * @return
	 */
	public static WhiteRockApp getInstance() {
		return instance;
	}
	
	/**
	 * @return
	 */
	public static Context getContext() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	public void onCreate() {
		instance = this;
		super.onCreate();
	}
	
	
}
