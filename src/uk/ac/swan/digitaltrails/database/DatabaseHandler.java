package uk.ac.swan.digitaltrails.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHandler::";
	private static final String DB_NAME = DbSchema.DB_NAME;
	private static final int DB_VERSION = 1;
	
	private static String DB_PATH = "";
	private SQLiteDatabase mDatabase;
		
	private final Context mContext;

	@SuppressLint("SdCardPath")
	public DatabaseHandler(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		
		// Find the correct path based on Android version.
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		Log.d(TAG,"DB Path is: " + DB_PATH+DB_NAME);
		Log.d(TAG, "Version Number: " + android.os.Build.VERSION.SDK_INT);
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "CREATING");
		// create User tables
		db.execSQL(DbSchema.CREATE_TABLE_USER);
		db.execSQL(DbSchema.CREATE_TABLE_SETTING_TYPE);
		db.execSQL(DbSchema.CREATE_TABLE_USER_SETTINGS);
		
		// Create Walk Tables
		db.execSQL(DbSchema.CREATE_TABLE_WALK);
		db.execSQL(DbSchema.CREATE_TABLE_WALK_BRAND);
		db.execSQL(DbSchema.CREATE_TABLE_WALK_REVIEW);
		db.execSQL(DbSchema.CREATE_TABLE_ENGLISH_WALK_DESCR);
		db.execSQL(DbSchema.CREATE_TABLE_WELSH_WALK_DESCR);
		
		// create Waypoint tables
		db.execSQL(DbSchema.CREATE_TABLE_WAYPOINT);
		db.execSQL(DbSchema.CREATE_TABLE_WAYPOINT_AUDIO);
		db.execSQL(DbSchema.CREATE_TABLE_WAYPOINT_IMAGE);
		db.execSQL(DbSchema.CREATE_TABLE_WAYPOINT_VIDEO);
		db.execSQL(DbSchema.CREATE_TABLE_ENGLISH_WAYPOINT_DESCR);
		db.execSQL(DbSchema.CREATE_TABLE_WESLH_WAYPOINT_DESCR);
		
		// create Report tables.
		db.execSQL(DbSchema.CREATE_TABLE_BUG_REPORT);
		db.execSQL(DbSchema.CREATE_TABLE_CONTENT_REPORT);
		
		/*
		if (!existsDatabase()) {
			Log.d(TAG, "Db doesn't exist - begin copy");
			this.getReadableDatabase();
			this.close();
			try {
				copyDatabase();
				Log.d(TAG, "copied");
			} catch (IOException e) {
				throw new Error("Can't copy database");
			}
		}
		*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	/**
	 * Check the database file exists
	 * @return
	 */
	private boolean existsDatabase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}
	
	/**
	 * Copy DB from assets so we can use it in the app.
	 * @throws IOException
	 */
	private void copyDatabase() throws IOException {
		InputStream input = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream output = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		}
		output.flush();
		output.close();
		input.close();
	}
}
