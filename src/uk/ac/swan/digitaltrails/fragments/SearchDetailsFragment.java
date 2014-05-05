package uk.ac.swan.digitaltrails.fragments;

import android.database.Cursor;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

public class SearchDetailsFragment extends WalkDetailsFragment implements LoaderCallbacks<Cursor> {

	public static String ARG_POSITION;
	private static final String TAG = "SearchDetailsFragment";
	private int mCurrentPos = -1;

	public int getCurrentPosition() {
		return mCurrentPos;
	}
	
	public void updateDetailsView(int position) {
		mCurrentPos = position;
		if (mCurrentPos > 0) {
			getLoaderManager().restartLoader(1, null, this);
		}
	}

}