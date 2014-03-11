package uk.ac.swan.digitaltrails;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WalkDetailsFragment extends Fragment {

	public final static String ARG_POSITION = "position";
	
	private int mCurrentPos = -1;
	
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
		TextView details = (TextView) getActivity().findViewById(R.id.details);
		details.setText("Testing " + position);
		mCurrentPos = position;
	}
	
}
