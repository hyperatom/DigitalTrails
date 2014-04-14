package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import android.content.Context;
import android.database.Cursor;

public class EnglishWaypointDescriptionDataSource extends DescriptionDataSource {

	public EnglishWaypointDescriptionDataSource(Context context) {
		super(context);
		uri = WhiteRockContract.EnglishWaypointDescriptions.CONTENT_URI;
		allColumns = WhiteRockContract.EnglishWaypointDescriptions.PROJECTION_ALL;
	}

	@Override
	public List<Description> getAllDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Description cursorToDescription(Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}

}
