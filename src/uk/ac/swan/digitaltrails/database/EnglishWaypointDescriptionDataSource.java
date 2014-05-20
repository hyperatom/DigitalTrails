package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import android.content.Context;
import android.database.Cursor;

/**
 * @author Lewis Hancock
 * See DescriptionDataSource
 */
public class EnglishWaypointDescriptionDataSource extends DescriptionDataSource {

	/**
	 * @param context
	 */
	public EnglishWaypointDescriptionDataSource(Context context) {
		super(context);
		uri = WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI;
		allColumns = WhiteRockContract.EnglishWaypointDescriptions.PROJECTION_ALL;
	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.database.DescriptionDataSource#getAllDescriptions()
	 */
	@Override
	public List<Description> getAllDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.database.DescriptionDataSource#cursorToDescription(android.database.Cursor)
	 */
	@Override
	protected Description cursorToDescription(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
