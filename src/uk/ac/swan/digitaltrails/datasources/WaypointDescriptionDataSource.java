package uk.ac.swan.digitaltrails.datasources;

import android.content.Context;
import uk.ac.swan.digitaltrails.components.Description.Languages;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;

public class WaypointDescriptionDataSource extends DescriptionDataSource {

	protected WaypointDescriptionDataSource(Context context, Languages language) {
		super(context);
		switch (language) {
		case ENGLISH:	mTable = DatabaseHandler.ENGLISH_WAYPOINT_DESC_TABLE;
					 	break;
		case WELSH: 	mTable = DatabaseHandler.WELSH_WAYPOINT_DESC_TABLE;
						break;
		}
	}

}
