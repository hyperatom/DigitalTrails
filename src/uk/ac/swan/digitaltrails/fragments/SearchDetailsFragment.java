package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchDetailsFragment extends Fragment implements LoaderCallbacks<Cursor> {

	public static String ARG_POSITION;
	private static final String TAG = "SearchDetailsFragment";
	private int mCurrentPos = -1;
	private TextView mTitleText;
	private TextView mLongDescrText;

	public int getCurrentPosition() {
		return mCurrentPos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mCurrentPos = savedInstanceState.getInt(ARG_POSITION);
		}
		return inflater.inflate(R.layout.fragment_choose_walk_detail, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		mTitleText = (TextView) getView().findViewById(R.id.title);
		mLongDescrText = (TextView) getView().findViewById(R.id.long_descr);
		getLoaderManager().initLoader(1, null, this);
		
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
	public void updateDetailsView(int position) {
		mCurrentPos = position + 1; // dealing with primary keys @ 1
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
} 