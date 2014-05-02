package uk.ac.swan.digitaltrails.fragments;

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

public class SearchListFragment extends ListFragment 
	implements LoaderCallbacks<List<Walk>>, OnQueryTextListener {

	private static final String TAG = "SearchListFragment";
	
	private OnWalkSelectedListener mCallback;
	private WalkLoaderAdapter mAdapter;
	private SearchView mSearchView;
	private int mLayout;
	private String mCurFilter;
	private Account mConnectedAccount;
	private Context mContext;
	
	public void setConnectedAccount(Account account) {
		mConnectedAccount = account;
	}
	
	public interface OnWalkSelectedListener {
		public void onWalkSelected(int position);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		mCallback.onWalkSelected(position);
		getListView().setItemChecked(position, true);
	}


	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// change layout depending on version of android;
		mLayout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
				android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

		
		//		
//		//TODO: Call from API.
//		//
//		setListAdapter(mAdapter);
//		setListShown(false);
//		getLoaderManager().initLoader(0, null, this);

	}
	
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
	
	@Override
	public void onStart() {
		super.onStart();
		if (getFragmentManager().findFragmentById(R.id.title) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (mConnectedAccount == null) {
			Log.d(TAG, "Account empty");
			//setEmptyText("Not Logged In");
		} else {
			Log.d(TAG, "Account connected - lets do this");
			Bundle bundle = new Bundle();
			// sync no matter what
//			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
//			bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//			ContentResolver.requestSync(mConnectedAccount, WhiteRockContract.AUTHORITY, bundle);
			mAdapter = new WalkLoaderAdapter(this.getActivity().getBaseContext(), null);
			getLoaderManager().initLoader(0,  null,  this);
		}

	}


	@Override
	public boolean onQueryTextChange(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Loader<List<Walk>> onCreateLoader(int arg0, Bundle arg1) {
		return new WalkLoader(getActivity().getApplicationContext());
	}


	@Override
	public void onLoadFinished(Loader<List<Walk>> arg0, List<Walk> walks) {
		mAdapter.setValues(walks);
	}


	@Override
	public void onLoaderReset(Loader<List<Walk>> arg0) {
		mAdapter.clear();
	}

} 