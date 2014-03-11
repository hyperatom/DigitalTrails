package uk.ac.swan.digitaltrails;

import java.util.ArrayList;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.utils.ArrayRetrieval;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WalkListFragment extends ListFragment {

	private OnWalkSelectedListener mCallback;
	
	private ArrayList<Walk> mWalkList;
	
	public interface OnWalkSelectedListener {
		public void onWalkSelected(int position);
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// change layout depending on the version of android running.
		int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
				android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
	
		String[] temp = {"W1", "W2", "W3", "W4"};
		setListAdapter(new ArrayAdapter<String>(getActivity(), layout, temp));

		// hacky "getElementNames" class which just creates a new array of all the toString calls over a type.
		//setListAdapter(new ArrayAdapter<String>(getActivity(), layout, ArrayRetrieval.getElementNames(mWalkList)));
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mCallback = (OnWalkSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement onWalkSelectedListener");
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onWalkSelected(position);
		
		getListView().setItemChecked(position, true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (getFragmentManager().findFragmentById(R.id.details) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}
	
}
