package uk.ac.swan.digitaltrails.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
	private static String DB_PATH = "/data/data/uk.ac.swan.digitaltrails/databases/";
	private static String DB_NAME = "whiterock.sqlite3";

	private SQLiteDatabase mWhiteRockDB;

	private final Context mContext;

	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, 1);
		this.mContext = context;
	}

	/**
	 * Create a database if none already exists.
	 * 
	 * @throws IOException
	 */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
			Log.i("dbhandle", "db exists");
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check database can be opened
	 * 
	 * @return true if successfull, false otherwise.
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	/**
	 * Creates a copy of the database. Used to create empy db.
	 * 
	 * @throws IOException
	 */
	private void copyDataBase() throws IOException {
		Log.i("dbhandle", "copy db");

		// Open your local db as the input stream
		InputStream myInput = mContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	/**
	 * Open the database.
	 * 
	 * @throws SQLException
	 */
	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;

		mWhiteRockDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
	}

	public synchronized void close() {

		if (mWhiteRockDB != null) {
			mWhiteRockDB.close();
		}
		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO: pull from remote server.
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
