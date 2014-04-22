package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Child of WalkDetailsFragment for displaying details of a walk when we want to edit said walk.
 * @author Lewis H
 *
 */
public class EditWalkFragment extends WalkDetailsFragment {
	
	private static final String TAG = "EditWalkFragment";
	private long mDescrId;
	
	public long getDescriptionId() {
		return mDescrId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mCurrentPos = savedInstanceState.getInt(ARG_POSITION);
		}
		mCurrentPos = getArguments().getInt(ARG_POSITION);
		return inflater.inflate(R.layout.fragment_edit_walk, container, false);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mTitleText = (TextView) getView().findViewById(R.id.title);
		mLongDescrText = (TextView) getView().findViewById(R.id.long_descr);
		Log.d(TAG, "current pos: " + mCurrentPos);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		//mAdapter.swapCursor(data);
		if (data != null && data.moveToFirst()) {
			Log.d("DATA LOG", "Cursor has info, pos: " + mCurrentPos);
			for (int i = 0; i < data.getColumnCount(); i++) {
				Log.d("DATA LOG", "Col " + i + " value: " + data.getString(i));
			}
			mDescrId = data.getInt(0);
			mTitleText.setText(data.getString(1));
			mLongDescrText.setText(data.getString(3));
		} else {
			Log.d("DATA LOG", "Cursor is empty, wtf, the mCurrentPos is: " + mCurrentPos);
		}
	}

	
	@Override
	public void updateDetailsView(int position) {

	}
	
}
