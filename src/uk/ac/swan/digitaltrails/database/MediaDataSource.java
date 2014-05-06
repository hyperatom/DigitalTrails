package uk.ac.swan.digitaltrails.database;

import android.content.Context;

/**
 * @author Lewis Hancock
 *
 */
public class MediaDataSource extends DataSource {

	/**
	 * 
	 */
	protected final String[] ALL_COLUMNS = { "id", "file_location" };

	/**
	 * @param context
	 */
	protected MediaDataSource(Context context) {
		super(context);
	}

	
}
