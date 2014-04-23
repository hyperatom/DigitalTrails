package uk.ac.swan.digitaltrails.database;

import java.util.List;

import uk.ac.swan.digitaltrails.components.Description;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class EnglishWalkDescriptionDataSource extends DescriptionDataSource {

	private static final String TAG = "EnglishWalkDescriptionDataSource";
	
	public EnglishWalkDescriptionDataSource(Context context) {
		super(context);
		uri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI;
		allColumns = WhiteRockContract.EnglishWalkDescriptions.PROJECTION_ALL;
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
