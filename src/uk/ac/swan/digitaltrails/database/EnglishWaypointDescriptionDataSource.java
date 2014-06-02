package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishWalkDescription;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Waypoint;
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
	
	public Description getDescriptonFromWaypoint(Waypoint wp) {
		EnglishWaypointDescription d = new EnglishWaypointDescription();
		String 	select  = "((" + allColumns[4] + " == " + wp.getId() + "))";
		d = (EnglishWaypointDescription) cursorToDescription(mContext.getContentResolver().query(uri, allColumns, select, null, allColumns[0] + " COLLATE LOCALIZED ASC"));
		return d;

	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.database.DescriptionDataSource#cursorToDescription(android.database.Cursor)
	 */
	@Override
	protected Description cursorToDescription(Cursor cursor) {
		EnglishWaypointDescription desc = new EnglishWaypointDescription();
		if (cursor != null & cursor.moveToFirst()) {
			desc.setId(cursor.getLong(0));
			desc.setTitle(cursor.getString(1));
			desc.setShortDescription(cursor.getString(2));
			desc.setLongDescription(cursor.getString(3));
		}
		return desc;
	}

}
