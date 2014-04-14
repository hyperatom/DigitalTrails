package uk.ac.swan.digitaltrails.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Class to define methods for DataSources.
 * DataSources are to be used to add, update and delete from the database. 
 * @author Lewis H
 *
 */
public class DataSource {

	protected Context mContext;
	protected SQLiteDatabase mWhiteRockDB;
	protected String mTable;

	public DataSource(Context context) {
		mContext = context;
	}

}
