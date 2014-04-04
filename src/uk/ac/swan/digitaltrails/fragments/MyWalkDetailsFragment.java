package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.activities.EditWalksActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
		return inflater.inflate(R.layout.my_walk_details_fragment, container, false);
	}
	
	/**
	 * Display details for the walk
	 * @param position
	 */
	public void updateDetailsView(int position) {
		
		//TODO: Change commented out bits to work for edit and delete buttons.
		
		//Button buttonStart = (Button)getView().findViewById(R.id.startButton);
		
		mCurrentPos = position + 1;	// primary keys start at 1 so we gotta do this
		if (mCurrentPos > 0){
			
			//buttonStart.setEnabled(true);
			getLoaderManager().restartLoader(1, null, this);
		} else {
			//buttonStart.setEnabled(false);
		}
	}
}
