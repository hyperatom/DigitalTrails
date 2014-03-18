package uk.ac.swan.digitaltrails.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SingletonDataSource {
	
	protected static SingletonDataSource mInstance = null;
	protected static Context mContext;
	protected DatabaseHandler mDbHandler;
	protected SQLiteDatabase mWhiteRockDB;
	protected String mTable;
	
	public SingletonDataSource(Context context) {
		mDbHandler = new DatabaseHandler(context);
	}
	
	public void open() throws SQLException {
		mWhiteRockDB = mDbHandler.getWritableDatabase();
	}
	
	public void close() {
		mDbHandler.close();
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
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Clone is not allowed");
	}
}
