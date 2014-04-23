package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EditWalkFragment extends WalkDetailsFragment {
	
	private static final String TAG = "EditWalkFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
		}
		return inflater.inflate(R.layout.fragment_edit_walk, container, false);
	}
	
	@Override
	public void updateDetailsView(int position) {
		mCurrentPos = position + 1;
		if (mCurrentPos > 0) {
			getLoaderManager().restartLoader(1, null, this);
		}
	}
	
}
