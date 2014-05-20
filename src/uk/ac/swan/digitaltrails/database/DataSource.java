package uk.ac.swan.digitaltrails.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Abstract class to define methods for DataSources.
 * DataSources are to be used to add, update and delete from the database. 
 * @author Lewis Hancock
 *
 */
public abstract class DataSource {

	/**
	 * The context to use
	 */
	protected Context mContext;
	/**
	 * The database to use
	 */
	protected SQLiteDatabase mWhiteRockDB;
	/**
	 * The Table to access
	 */
	protected String mTable;

	/**
	 * Constructor
	 * @param context
	 */
	public DataSource(Context context) {
		mContext = context;
	}

}
