package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MyWalkListFragment extends WalkListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		ListView listView = (ListView) v.findViewById(android.R.id.list);
		ViewGroup parent = (ViewGroup) listView.getParent();
		
		// remove default ListView, add custom view
		int listViewIndex = parent.indexOfChild(listView);
		parent.removeViewAt(listViewIndex);
		LinearLayout mLinearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_my_walk_list, container, false);
		parent.addView(mLinearLayout, listViewIndex, listView.getLayoutParams());;
		return v;
	}
}
