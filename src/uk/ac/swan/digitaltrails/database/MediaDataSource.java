package uk.ac.swan.digitaltrails.database;

import android.content.Context;

/**
 * @author Lewis Hancock
 * See DataSource
 */
public class MediaDataSource extends DataSource {

	/**
	 * 
	 */
	protected final String[] ALL_COLUMNS = { "id", "file_location", "remote_id" };

	/**
	 * @param context
	 */
	protected MediaDataSource(Context context) {
		super(context);
	}

	
}
