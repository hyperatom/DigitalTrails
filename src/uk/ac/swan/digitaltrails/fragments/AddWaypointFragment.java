package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddWaypointFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
		}
		
		return inflater.inflate(R.layout.fragment_choose_walk_detail, container, false);
		
	}
	
}
