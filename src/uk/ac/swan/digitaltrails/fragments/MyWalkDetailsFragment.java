package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MyWalkDetailsFragment extends WalkDetailsFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mCurrentPos = savedInstanceState.getInt(ARG_POSITION);
		}
		return inflater.inflate(R.layout.fragment_my_walk_details, container, false);
	}
	
	/**
	 * Display details for the walk
	 * @param position
	 */
	public void updateDetailsView(int position) {
		Button editButton = (Button)getView().findViewById(R.id.editWalkButton);
		Button deleteButton = (Button)getView().findViewById(R.id.deleteWalkButton);
		mCurrentPos = position + 1;	// primary keys start at 1 so we gotta do this
		if (mCurrentPos > 0){
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
			getLoaderManager().restartLoader(1, null, this);
		} else {
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}
}
