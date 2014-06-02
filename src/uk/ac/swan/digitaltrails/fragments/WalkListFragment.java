package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.*;
import android.support.v4.widget.SearchViewCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author Lewis Hancock
 *
 */
public class WalkListFragment extends ListFragment 
	implements LoaderCallbacks<Cursor>, OnQueryTextListener {

	/**
	 * 
	 */
	private OnWalkSelectedListener mCallback;

	/**
	 * 
	 */
	private static final String TAG = "WalkListFragment";
	
	/**
	 * 
	 */
	private EditText search;
	
	/**
	 * 
	 */
	protected ArrayList<Walk> mWalkList;
	/**
	 * 
	 */
	protected ArrayList<Integer> mWalkIds;
	/**
	 * 
	 */
	protected SimpleCursorAdapter mAdapter;
	/**
	 * 
	 */
	protected SearchViewCompat mSearchView;
	/**
	 * 
	 */
	protected String mCurFilter; 
	/**
	 * 
	 */
	protected int mLayout;

	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public interface OnWalkSelectedListener {
		public void onWalkSelected(int position);
	}
	
	
	/**
	 * @param walkList
	 */
	public void setWalkList(ArrayList<Walk> walkList) {
		mWalkList = walkList;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Walk> getWalkList() {
		return mWalkList;
	}
	
	/**
	 * @return
	 */
	public ArrayList<Integer> getWalkIds() {
		return mWalkIds;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// change layout depending on the version of android running.
		mLayout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
				android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		search = (EditText) activity.findViewById(R.id.searchQuery);
		search.setVisibility(EditText.VISIBLE);
		
		try {
			mCallback = (OnWalkSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement onWalkSelectedListener");
		}
	}
	
	public void onPause(){
		search.setVisibility(EditText.GONE);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onWalkSelected(position);
		getListView().setItemChecked(position, true);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		mWalkIds = new ArrayList<Integer>();
		if (getFragmentManager().findFragmentById(R.id.title) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//setHasOptionsMenu(true);
		setEmptyText("No Walks");	//TODO load this from resource.
		mAdapter = new SimpleCursorAdapter(getActivity(), mLayout, null,
					new String[] {WhiteRockContract.EnglishWalkDescriptions.TITLE },
					new int[] {android.R.id.text1}, 0);	
		setListAdapter(mAdapter);
		setListShown(false);
		getLoaderManager().initLoader(0, null, this);
	}
	

	/**
	 * 
	 */
	protected static String [] WALK_SUMMARY_PROJECTION = {WhiteRockContract.EnglishWalkDescriptions._ID, WhiteRockContract.EnglishWalkDescriptions.TITLE, WhiteRockContract.EnglishWalkDescriptions.WALK_ID };
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		if (mCurFilter != null) {
			baseUri = Uri.withAppendedPath(WhiteRockContract.CONTENT_URI, Uri.encode(mCurFilter));
		} else {
			baseUri = WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI;
		}
		
		String select = "((_id))";
		return new CursorLoader(getActivity(), baseUri, WALK_SUMMARY_PROJECTION, select, null, WhiteRockContract.EnglishWalkDescriptions.WALK_ID + " COLLATE LOCALIZED ASC");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.d(TAG, "LoadFinished, data size: " + data.getCount());
		mAdapter.swapCursor(data);
		if (data != null && data.moveToFirst()) {
			mWalkIds.add(data.getInt(2));
			while (data.moveToNext())
			{
				Log.d(TAG, ""+data.getInt(2));
				mWalkIds.add(data.getInt(2));
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	// onQueryTextListener interface
	/* (non-Javadoc)
	 * @see android.support.v7.widget.SearchView.OnQueryTextListener#onQueryTextChange(java.lang.String)
	 */
	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see android.support.v7.widget.SearchView.OnQueryTextListener#onQueryTextSubmit(java.lang.String)
	 */
	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
