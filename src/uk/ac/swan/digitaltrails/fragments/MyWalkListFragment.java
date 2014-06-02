package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.DescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWalkDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.EnglishWaypointDescriptionDataSource;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WaypointDataSource;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.sync.SearchWalkLoader;
import uk.ac.swan.digitaltrails.sync.WalkLoader;
import uk.ac.swan.digitaltrails.sync.WhiteRockServerAccessor;
import uk.ac.swan.digitaltrails.utils.GlobalFlags;
import uk.ac.swan.digitaltrails.utils.WhiteRockApp;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Custom WalkListFragment for the MyWalkActivity
 * Customised to have a button.
 * @author Lewis H
 *
 */
/**
 * @author Lewis Hancock
 *
 */
public class MyWalkListFragment extends WalkListFragment {

	/**
	 * 
	 */
	private static final String TAG = "MyWalkListFragment";
	
	private MyWalkListFragment mThis = this;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
	public void onAttach(Activity activity) {

		// get remote walks if possible.
		AccountManager am = AccountManager.get(this.getActivity());
		Account[] accounts = am.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
		if (accounts.length > 0) {
			//Log.d(TAG, "Account Found, getting remote walks");
			//getRemoteWalks();
		} else {
			Log.d(TAG, "No Account Found.");
		}

		super.onAttach(activity);
	}
		
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.WalkListFragment#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		SharedPreferences settings = getActivity().getApplication().getSharedPreferences(GlobalFlags.PREF_NAME, 0);
		int userId = settings.getInt("userId", -1);
		baseUri = WhiteRockContract.WalkWithEnglishDescriptions.CONTENT_URI;
		String select = "((user_id == " + userId +"))";
		Log.d(TAG, "Select String: " + select);
		return new CursorLoader(getActivity(), baseUri, WhiteRockContract.WalkWithEnglishDescriptions.PROJECTION_ALL, select, null, "title" + " COLLATE LOCALIZED ASC");
	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.WalkListFragment#onLoadFinished(android.support.v4.content.Loader, android.database.Cursor)
	 */
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
	
	/**
	 * Get remote walks which were made by the current user.
	 */
	private void getRemoteWalks() {

		// Will keep downloading walks without giving a flying fuck if they are added or not.

		new AsyncTask<Void, Void, List<Walk>>() {

			@Override
			protected List<Walk> doInBackground(Void... params) {
				Log.d(TAG, "Starting AsyncTask");
				List<Walk> walks = new ArrayList<Walk>();
				WhiteRockServerAccessor accessor = new WhiteRockServerAccessor();

				try {
					walks = accessor.getWalks();
				} catch (Exception e) {
					Log.e(TAG, "Failed to run accessor.getWalks");
				}
				
				Log.d(TAG, "Walks Loaded From Server:" + walks.size());
				SharedPreferences settings = getActivity().getApplication().getSharedPreferences(GlobalFlags.PREF_NAME, 0);
				int userId = settings.getInt("userId", -1);
				Log.d(TAG, "Looking for walks by user:" + userId);
				// search for walks not in db.
				for (Walk remoteWalk : walks) {
					if (remoteWalk.getOwner() != userId) {
						walks.remove(remoteWalk);
					}
					for (Walk localWalk : mWalkList) {
						if (remoteWalk.getWalkId() == localWalk.getWalkId()) {
							walks.remove(remoteWalk);
							break;
						}
					}
				}

				// add to database.
				WalkDataSource walkSource = new WalkDataSource(WhiteRockApp.getContext()); 
				WaypointDataSource wpDataSource = new WaypointDataSource(WhiteRockApp.getContext());
				for (Walk remoteWalk : walks) {
					DescriptionDataSource descrDataSource = new EnglishWalkDescriptionDataSource(WhiteRockApp.getContext());
					walkSource.addWalk(remoteWalk);
					descrDataSource.addDescription(remoteWalk.getEnglishDescriptions());

					descrDataSource = new EnglishWaypointDescriptionDataSource(WhiteRockApp.getContext());
					for (Waypoint wp : remoteWalk.getWaypoints()) {
						wpDataSource.addWaypoint(wp);
						descrDataSource.addDescription(wp.getEnglishDescription());
					}
				}
				return walks;
			}

			protected void onPostExecute(List<Walk> walks) {
				getLoaderManager().restartLoader(0, null, mThis);
			}
		}.execute();
	}

}
