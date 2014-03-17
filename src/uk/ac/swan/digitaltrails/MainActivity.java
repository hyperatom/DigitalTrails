package uk.ac.swan.digitaltrails;

import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;

/**
 * Testing activity for content provider.
 * @author Lewis Hancock
 *
 */
public class MainActivity extends ActionBarActivity
			implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>, WalkListFragment.OnWalkSelectedListener {


	private boolean debug = false;
	/**
	 * 	Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cp_test);

		if (debug) {
			ContentValues values = new ContentValues();
			
			values.put("title", "Test Walk 1");
			values.put("short_description", "The First Test Walk");
			values.put("long_description", "The long description of this walk...");
			values.put("walk_id", "1");
			getContentResolver().insert(WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI, values);
		
			values.put("title", "Test Walk 2");
			values.put("short_description", "The Second Test Walk");
			values.put("long_description", "The long description of this walk...");
			values.put("walk_id", "2");
			getContentResolver().insert(WhiteRockContract.EnglishWalkDescriptions.CONTENT_URI, values);
		}
		
		// check if using small layout
		if (findViewById(R.id.fragment_container) != null) {
			
			// if restoring from prev state do nothing
			if (savedInstanceState != null) {
				return;
			}
			
			WalkListFragment walkListFragment = new WalkListFragment();
			
			walkListFragment.setArguments(getIntent().getExtras());
			
			getSupportFragmentManager().beginTransaction()
											.add(R.id.fragment_container, walkListFragment).commit();
		}
	}

	/**
	 * Display selected walk data.
	 */
	@Override
	public void onWalkSelected(int position) {
		WalkDetailsFragment detailsFrag = (WalkDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.walk_details_fragment);
		
		if (detailsFrag != null) {
			// if available and we are in 2-pane view.
			detailsFrag.updateDetailsView(position);
		} else {
			// if in 1 pane view
			WalkDetailsFragment newDetailsFragment = new WalkDetailsFragment();
			Bundle args = new Bundle();
			args.putInt(WalkDetailsFragment.ARG_POSITION, position);
			newDetailsFragment.setArguments(args);
			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			
			transaction.replace(R.id.fragment_container, newDetailsFragment);
			transaction.addToBackStack(null);
			
			transaction.commit();
		
		}
	}
	
	// LoaderCallbacks interface
	
	@Override
	public Loader onCreateLoader(int arg0, Bundle bundle) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
}