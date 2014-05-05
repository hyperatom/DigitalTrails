package uk.ac.swan.digitaltrails.fragments;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.utils.GlobalFlags;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Custom WalkListFragment for the MyWalkActivity
 * Customised to have a button.
 * @author Lewis H
 *
 */
public class MyWalkListFragment extends WalkListFragment {

	private static final String TAG = "MyWalkListFragment";
	
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
		
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		SharedPreferences settings = getActivity().getApplication().getSharedPreferences(GlobalFlags.PREF_NAME, 0);
		int userId = settings.getInt("userId", -1);
		baseUri = WhiteRockContract.WalkWithEnglishDescriptions.CONTENT_URI;
		String select = "((user_id == " + userId +"))";
		Log.d(TAG, "Select String: " + select);
		return new CursorLoader(getActivity(), baseUri, WhiteRockContract.WalkWithEnglishDescriptions.PROJECTION_ALL, select, null, WhiteRockContract.EnglishWalkDescriptions.WALK_ID + " COLLATE LOCALIZED ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG, "LoadFinished, data size: " + data.getCount());
		mAdapter.swapCursor(data);
		if (data != null && data.moveToFirst()) {
			mWalkIds.add(data.getInt(0));
			while (data.moveToNext())
			{
				Log.d(TAG, ""+data.getInt(0));
				mWalkIds.add(data.getInt(0));
			}
		}		
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
		for (int x : mWalkIds) {
			Log.d(TAG, "Ids in list: " + x);
		}
	}

}
