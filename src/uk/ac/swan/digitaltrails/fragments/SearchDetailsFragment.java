package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Lewis Hancock
 *
 */
public class SearchDetailsFragment extends WalkDetailsFragment implements LoaderCallbacks<Cursor> {

	/**
	 * 
	 */
	public static String ARG_POSITION;
	/**
	 * 
	 */
	private static final String TAG = "SearchDetailsFragment";
	/**
	 * 
	 */
	private int mCurrentPos = -1;

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.WalkDetailsFragment#getCurrentPosition()
	 */
	public int getCurrentPosition() {
		return mCurrentPos;
	}
	
	/**
	 * Called during startup
	 */
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.WalkDetailsFragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();		
		// check if arguments passed to the fragment
		Bundle args = getArguments();
		if (args != null) {
			updateDetailsView((Walk)args.get(ARG_POSITION));
		} 
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.WalkDetailsFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (savedInstanceState != null) {
			mCurrentPos = savedInstanceState.getInt(ARG_POSITION);
		}
		
		return inflater.inflate(R.layout.fragment_search_detail, container, false);
	}
	
	/**
	 * @param walk
	 */
	public void updateDetailsView(Walk walk) {
		if (walk != null) {
			((TextView) getView().findViewById(R.id.title)).setText(walk.getEnglishDescriptions().getTitle());
			((TextView) getView().findViewById(R.id.long_descr)).setText(walk.getEnglishDescriptions().getLongDescription());
		}
	}

}