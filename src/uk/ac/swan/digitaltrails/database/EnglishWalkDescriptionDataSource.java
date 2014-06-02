package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishWalkDescription;
import uk.ac.swan.digitaltrails.components.Walk;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * @author Lewis Hancock
 * See DescriptionDataSource
 */
public class EnglishWalkDescriptionDataSource extends DescriptionDataSource {

	/**
	 * 
	 */
	private static final String TAG = "EnglishWalkDescriptionDataSource";
	
	/**
	 * @param context
	 */
	public EnglishWalkDescriptionDataSource(Context context) {
		super(context);
		uri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI;
		allColumns = WhiteRockContract.EnglishWalkDescriptions.PROJECTION_ALL;
	}
	
	public EnglishWalkDescription getDescriptionForWalk(Walk walk) {
		EnglishWalkDescription d = new EnglishWalkDescription();
		String 	select  = "((" + allColumns[4] + " == " + walk.getId() + "))";
		d = cursorToDescription(mContext.getContentResolver().query(uri, allColumns, select, null, allColumns[0] + " COLLATE LOCALIZED ASC"));
		
		return d;
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
	protected EnglishWalkDescription cursorToDescription(Cursor cursor) {
		if (cursor != null && cursor.moveToFirst()) {
			EnglishWalkDescription d = new EnglishWalkDescription();
			d.setId(cursor.getLong(0));
			d.setTitle(cursor.getString(1));
			d.setShortDescription(cursor.getString(2));
			d.setLongDescription(cursor.getString(3));
			return d;
		}
		return null;
	}

}
