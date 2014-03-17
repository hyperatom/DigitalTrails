package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WalkDetailsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	public final static String ARG_POSITION = "position";
	
	private int mCurrentPos = -1;
	private SimpleCursorAdapter mAdapter;
	
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
		
		//setHasOptionsMenu(true);
		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.fragment_walk_details, null,
					new String[] {WhiteRockContract.EnglishWalkDescriptions.TITLE,
								  WhiteRockContract.EnglishWalkDescriptions.SHORT_DESCR,
								  WhiteRockContract.EnglishWalkDescriptions.LONG_DESCR },
					new int[] {R.id.details}, 0);	
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
		mCurrentPos = position;
		
	}

	private static String[] WALK_DESCRIPTION_PROJECT = {WhiteRockContract.EnglishWalkDescriptions.ID, WhiteRockContract.EnglishWalkDescriptions.TITLE, WhiteRockContract.EnglishWalkDescriptions.SHORT_DESCR, WhiteRockContract.EnglishWalkDescriptions.LONG_DESCR};
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		baseUri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI;
		String select = "((_id ==" + mCurrentPos + "))";
		return new CursorLoader(getActivity(), baseUri, WALK_DESCRIPTION_PROJECT, select, null, WhiteRockContract.EnglishWalkDescriptions.ID + " COLLATE LOCALIZED ASC");
	} 

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}
	
}
