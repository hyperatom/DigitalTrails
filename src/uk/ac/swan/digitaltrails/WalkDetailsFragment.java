package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WalkDetailsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	public static final String ARG_POSITION = "position";
	private static final String TAG = "WalkDetailsFragment";
	private int mCurrentPos = -1;
	private TextView mTitleText;
	private TextView mShortDescrText;
	private TextView mLongDescrText;
	
	public int getCurrentPosition() {
		return mCurrentPos;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (savedInstanceState != null) {
			mCurrentPos = savedInstanceState.getInt(ARG_POSITION);
		}
		
		return inflater.inflate(R.layout.fragment_walk_details, container, false);
	}

	/**
	 * Called during startup
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		mTitleText = (TextView) getView().findViewById(R.id.title);
		mShortDescrText = (TextView) getView().findViewById(R.id.short_descr);
		mLongDescrText = (TextView) getView().findViewById(R.id.long_descr);
		//setHasOptionsMenu(true);
		/*mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.fragment_walk_details, null,
					new String[] {WhiteRockContract.EnglishWalkDescriptions.TITLE,
								  WhiteRockContract.EnglishWalkDescriptions.SHORT_DESCR,
								  WhiteRockContract.EnglishWalkDescriptions.LONG_DESCR },
					new int[] {R.id.title, R.id.short_descr, R.id.long_descr}, 0);	
		*/

		getLoaderManager().initLoader(1, null, this);
		
		// check if arguments passed to the fragment
		
		Bundle args = getArguments();
		if (args != null) {
			updateDetailsView(args.getInt(ARG_POSITION));
		} else if (mCurrentPos != -1) {
			updateDetailsView(mCurrentPos);			
		}	
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState); 
		outState.putInt(ARG_POSITION, mCurrentPos);
	}

	/**
	 * Display details for the walk
	 * @param position
	 */
	public void updateDetailsView(int position) {
		mCurrentPos = position + 1;	// primary keys start at 1 so we gotta do this
		getLoaderManager().restartLoader(1, null, this);
	}
	
	public void updateDetailsView(int position, Cursor data) {
		
	}

	private static String[] WALK_DESCRIPTION_PROJECTION = {WhiteRockContract.EnglishWalkDescriptions.ID, WhiteRockContract.EnglishWalkDescriptions.TITLE, WhiteRockContract.EnglishWalkDescriptions.SHORT_DESCR, WhiteRockContract.EnglishWalkDescriptions.LONG_DESCR};
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		baseUri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI;
		String select = "((walk_id == " + mCurrentPos + "))";
		Log.d(TAG, "Select String: " + select);
		return new CursorLoader(getActivity(), baseUri, WALK_DESCRIPTION_PROJECTION, select, null, WhiteRockContract.EnglishWalkDescriptions.WALK_ID+ " COLLATE LOCALIZED ASC");
	} 

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		//mAdapter.swapCursor(data);
		if (data != null && data.moveToFirst()) {
			Log.d("DATA LOG", "Cursor has info...");
			for (int i = 0; i < data.getColumnCount(); i++) {
				Log.d("DATA LOG", "Col " + i + " value: " + data.getString(i));
			}
			
			mTitleText.setText(data.getString(1));
			mShortDescrText.setText(data.getString(2));
			mLongDescrText.setText(data.getString(3));
		} else {
			Log.d("DATA LOG", "Cursor is empty, wtf, the mCurrentPos is: " + mCurrentPos);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
	

	
}
