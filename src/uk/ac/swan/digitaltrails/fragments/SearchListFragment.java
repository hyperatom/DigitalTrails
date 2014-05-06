package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
import uk.ac.swan.digitaltrails.activities.SearchActivity;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.sync.WalkLoader;
import uk.ac.swan.digitaltrails.sync.WalkLoaderAdapter;
import uk.ac.swan.digitaltrails.sync.WhiteRockServerAccessor;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * @author Lewis Hancock
 *
 */
public class SearchListFragment extends ListFragment 
	implements LoaderCallbacks<List<Walk>>, OnQueryTextListener {

	/**
	 * 
	 */
	private static final String TAG = "SearchListFragment";
	
	/**
	 * 
	 */
	private OnWalkSelectedListener mCallback;
	/**
	 * 
	 */
	private WalkLoaderAdapter mAdapter;
	/**
	 * 
	 */
	private SearchView mSearchView;
	/**
	 * 
	 */
	private int mLayout;
	/**
	 * 
	 */
	private String mCurFilter;
	/**
	 * 
	 */
	private Account mConnectedAccount;
	/**
	 * 
	 */
	private Context mContext;
	/**
	 * 
	 */
	private ArrayList<Long> mWalkIds;
	
	/**
	 * @param account
	 */
	public void setConnectedAccount(Account account) {
		mConnectedAccount = account;
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public interface OnWalkSelectedListener {
		public void onWalkSelected(Walk walk);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onWalkSelected(mAdapter.getItem(position));
		Log.d(TAG, "Walk Id of item: " + mAdapter.getItem(position).getId());
		getListView().setItemChecked(position, true);
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// change layout depending on version of android;
		mLayout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
				android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
		mWalkIds = new ArrayList<Long>();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnWalkSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implemented onWalkSelectedListener");
		}
		mContext = activity.getBaseContext();
		AccountManager am = AccountManager.get(mContext);
		Account[] accounts = am.getAccountsByType(AccountGeneral.ACCOUNT_TYPE);
		mConnectedAccount = accounts[0];

	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
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
		
		if (mConnectedAccount == null) {
			Log.d(TAG, "Account empty");
			setEmptyText("Not Logged In");
		} else {
			setEmptyText("No Walks");
			Log.d(TAG, "Account connected - lets do this");
			Bundle bundle = new Bundle();
			// sync no matter what
//			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//			ContentResolver.requestSync(mConnectedAccount, WhiteRockContract.AUTHORITY, bundle);
			mAdapter = new WalkLoaderAdapter(this.getActivity().getBaseContext());
			setListAdapter(mAdapter);
			setListShown(false);
			getLoaderManager().initLoader(0,  null,  this);
		}

	}


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

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<List<Walk>> onCreateLoader(int arg0, Bundle arg1) {
		return new WalkLoader(getActivity());
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<List<Walk>> arg0, List<Walk> walks) {
		Log.d(TAG, "onLoadFinished");
		mAdapter.setData(walks);
		for (Walk w : walks) {
			mWalkIds.add(w.getId());
		}
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<List<Walk>> arg0) {
		mAdapter.setData(null);
	}

} 