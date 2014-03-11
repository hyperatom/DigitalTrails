package uk.ac.swan.digitaltrails;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

/**
 * Testing activity for content provider.
 * @author Lewis Hancock
 *
 */
public class MainActivity extends ActionBarActivity
			implements WalkListFragment.OnWalkSelectedListener {



	/**
	 * 	Called when the activity is first created
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cp_test);
		
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
}