package uk.ac.swan.digitaltrails.datasources;

import uk.ac.swan.digitailtrails.utils.DatabaseHandler;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SingletonDataSource {
	
	protected static SingletonDataSource mInstance = null;
	protected static Context mContext;
	protected DatabaseHandler mDbHandler;
	protected SQLiteDatabase mWhiteRockDB;
	
	/**
	 * Used to initialise class. MUst be called before getInstance() 
	 * @param context The context used
	 */
	public static void initialize(Context context) {
		mContext = context;
	}
	
	/**
	 * Check if class initialised
	 * @return true if initialised, false otherwise.
	 */
	public static boolean isInitialized() {
		return mContext != null;
	}
	
	/**
	 * Private constructor - use mContext to initialise vars.
	 */
	protected SingletonDataSource() {
		// initialize vars with mContext
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized SingletonDataSource getInstance() {
		if (mContext == null) {
			throw new IllegalArgumentException("Can't get instance. Must initialize class before");
		}
		
		if (mInstance == null) {
			mInstance = new SingletonDataSource();
		}
		
		return mInstance;
	}
	
	/**
	 * 
	 * @throws SQLException
	 */
	public synchronized void initDatabaseHandler() throws SQLException {
		mDbHandler = new DatabaseHandler(mContext);
		mWhiteRockDB = mDbHandler.getWritableDatabase();
	}
	
	/**
	 * 
	 */
	public void close() {
		mDbHandler.close();
	}
	
	/**
	 * 
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Clone is not allowed");
	}
}
