package uk.ac.swan.digitaltrails.utils;

import java.io.IOException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//TODO: Databases are placed onto the SD card, no other strategy if SD card missing.

public class DatabaseHandler extends SQLiteOpenHelper {
	public static final String BUG_TABLE = "BugReports";
	public static final String REPORT_TABLE = "ContentReports";
	public static final String MAP_VIEW_TABLE = "DefaultMapView";
	public static final String ENGLISH_WALK_DESC_TABLE = "EnglishWalkDescriptions";
	public static final String ENGLISH_WAYPOINT_DESC_TABLE = "EnglishWaypointDescriptions";
	public static final String SETTING_TYPE_TABLE = "SettingTypes";
	public static final String USER_TABLE = "Users";
	public static final String USER_SETTINGS_TABLE = "UserSettings";
	public static final String WALK_BRANDS_TABLE = "WalkBrands";
	public static final String WALK_REVIEWS_TABLE = "WalkReviews";
	public static final String WALKS_TABLE = "Walks";
	public static final String AUDIO_TABLE = "WaypointAudios";
	public static final String IMAGES_TABLE = "WaypointImages";
	public static final String VIDEOS_TABLE = "WaypointVideos";
	public static final String WAYPOINT_TABLE = "Waypoints";
	public static final String WELSH_WALK_DESC_TABLE = "WelshWalkDescriptions";
	public static final String WELSH_WAYPOINT_DESC_TABLE = "WelshWaypointDescriptions";
	//private static final String DB_PATH_EXT = Environment.getExternalStorageDirectory().getPath()+"/uk.ac.swan.ac.digitaltrails/databases/";
	private static final String DB_NAME = "whiterock.sqlite3";
	private static final int DB_VERSION = 1;
	
	private final Context mContext;

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO: create from text file.
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		//TODO Create a better update strategy.
		/*
		 * Log.w(DatabaseHandler.class.getName(),
		 * "Upgrading database from verson " + oldVersion + " to " + newVersion
		 * + ",this will destroy old data"); mContext.deleteDatabase(DB_NAME);
		 * // drop all tables onCreate(mWhiteRockDB); // recreate from remote
		 * server
		 */
	}
}
