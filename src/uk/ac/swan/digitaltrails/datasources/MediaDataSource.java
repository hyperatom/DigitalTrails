package uk.ac.swan.digitaltrails.datasources;

import android.content.Context;

public class MediaDataSource extends SingletonDataSource {

	protected final String[] ALL_COLUMNS = { "id", "file_location" };
	
	protected MediaDataSource(Context context) {
		super(context);
	}

	
}
