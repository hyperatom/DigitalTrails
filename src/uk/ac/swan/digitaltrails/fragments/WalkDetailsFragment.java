package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.activities.MapActivity;
import uk.ac.swan.digitaltrails.database.DbSchema;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.database.WhiteRockContract.WalkColumns;
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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * @author Lewis Hancock
 *
 */
public class WalkDetailsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	/**
	 * 
	 */
	public static final String ARG_POSITION = "position";
	/**
	 * 
	 */
	private static final String TAG = "WalkDetailsFragment";
	/**
	 * 
	 */
	protected int mCurrentPos = -1;
	/**
	 * 
	 */
	protected TextView mTitleText;
	/**
	 * 
	 */
	protected TextView mLongDescrText;
	
	/**
	 * @return
	 */
	public int getCurrentPosition() {
		return mCurrentPos;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (savedInstanceState != null) {
			mCurrentPos = savedInstanceState.getInt(ARG_POSITION);
		}
		
		return inflater.inflate(R.layout.fragment_choose_walk_detail, container, false);
	}

	/**
	 * Called during startup
	 */
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart, mCurrentPos = " + mCurrentPos);
		mTitleText = (TextView) getView().findViewById(R.id.title);
		mLongDescrText = (TextView) getView().findViewById(R.id.long_descr);
		//setHasOptionsMenu(true);
		getLoaderManager().initLoader(1, null, this);
		// check if arguments passed to the fragment
		Bundle args = getArguments();
		if (args != null) {
			updateDetailsView(args.getInt(ARG_POSITION));
		} else if (mCurrentPos > 0) {
			updateDetailsView(mCurrentPos);			
		}	
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		getLoaderManager().restartLoader(1, null, this);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState); 
		outState.putInt(ARG_POSITION, mCurrentPos);
	}

	/**
	 * Display details for the walk
	 * @param position
	 */
	/**
	 * @param position
	 */
	public void updateDetailsView(int position) {
		
		Button buttonStart = (Button)getView().findViewById(R.id.startButton);
		
		mCurrentPos = position;
		if (mCurrentPos >= 0){
			buttonStart.setEnabled(true);
			getLoaderManager().restartLoader(1, null, this);
		} else {
			buttonStart.setEnabled(false);
		}
		
	}

	/**
	 * 
	 */
	private static String[] WALK_DESCRIPTION_PROJECTION = {WhiteRockContract.EnglishWalkDescriptions.ID, WhiteRockContract.EnglishWalkDescriptions.TITLE, WhiteRockContract.EnglishWalkDescriptions.SHORT_DESCR, WhiteRockContract.EnglishWalkDescriptions.LONG_DESCR};
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		baseUri = WhiteRockContract.WalkWithEnglishDescriptions.CONTENT_URI;
		String select = "((walk_id == " + mCurrentPos + "))";
		Log.d(TAG, "Select String: " + select);
		return new CursorLoader(getActivity(), baseUri, WhiteRockContract.WalkWithEnglishDescriptions.PROJECTION_ALL, select, null, WhiteRockContract.EnglishWalkDescriptions.WALK_ID+ " COLLATE LOCALIZED ASC");
	} 

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data != null && data.moveToFirst()) {
			Log.d("DATA LOG", "Cursor has info, pos: " + mCurrentPos);
			for (int i = 0; i < data.getColumnCount(); i++) {
				Log.d("DATA LOG", "Col " + i + " value: " + data.getString(i));
			}
			//DURATION_MINUTES, DISTANCE_MILES, DOWNLOAD_COUNT, DIFFICULTY_RATING, USER_ID, TITLE, SHORT_DESCR, LONG_DESCR, WALK_ID;
			mTitleText.setText(data.getString(6));
			mLongDescrText.setText(data.getString(8));
			
			String difficulty = "";
			switch(data.getInt(4)){
			case 0:
			case 1:
				difficulty = "Easy";
				break;
			case 2:
			case 3:
				difficulty = "Medium";
				break;
			case 4:
			case 5:
				difficulty = "Hard";
				break;
			}
			
			RatingBar rating = ((RatingBar) getView().findViewById(R.id.ratingBar1));
			rating.setRating(data.getInt(4));
			rating.setEnabled(false);
			((TextView) getView().findViewById(R.id.textView7)).setText(difficulty);
			((TextView) getView().findViewById(R.id.textView8)).setText(""+data.getInt(2));
			((TextView) getView().findViewById(R.id.textView6)).setText(""+data.getInt(1));
			
		} else {
			Log.d("DATA LOG", "Cursor is empty, wtf, the mCurrentPos is: " + mCurrentPos);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
	}
}
