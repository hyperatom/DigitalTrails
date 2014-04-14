package uk.ac.swan.digitaltrails.database;

import android.content.Context;

public class MediaDataSource extends DataSource {

	protected final String[] ALL_COLUMNS = { "id", "file_location" };

	protected MediaDataSource(Context context) {
		super(context);
	}

	
}
