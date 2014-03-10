package uk.ac.swan.digitaltrails;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity {

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	public static class DetailFragment extends Fragment {
		
		public static DetailFragment newInstance(int index) {
			DetailFragment f = new DetailFragment();
			
			Bundle args = new Bundle();
			args.putInt("index", index);
			f.setArguments(args);
			return f;
		}
		
		public int getShownIndex() {
			return getArguments().getInt("index", 0);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			if (container == null) {
				return null;
			}
			ScrollView scroller = new ScrollView(getActivity());
			TextView text = new TextView(getActivity());
			int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getActivity().getResources().getDisplayMetrics());
			text.setPadding(padding, padding, padding, padding);
			scroller.addView(text);
			text.setText("RARRR");
			return scroller;
		}
		
		public void setText(String item) {
			TextView view = (TextView) getView().findViewById(R.id.detailsText);
			view.setText(item);
		}
	}
	
	public static class WalkListFragment extends ListFragment {
	    boolean mDualPane;
	    int mCurCheckPosition = 0;
	    
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);

	        // Populate list with our static array of titles.
	        setListAdapter(new ArrayAdapter<String>(getActivity(),
	                android.R.layout.simple_list_item_activated_1, 5));

	        // Check to see if we have a frame in which to embed the details
	        // fragment directly in the containing UI.
	        View detailsFrame = getActivity().findViewById(R.id.detailFragment);
	        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

	        if (savedInstanceState != null) {
	            // Restore last state for checked position.
	            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
	        }

	        if (mDualPane) {
	            // In dual-pane mode, the list view highlights the selected item.
	            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	            // Make sure our UI is in the correct state.
	            showDetails(mCurCheckPosition);
	        }
	    }

	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putInt("curChoice", mCurCheckPosition);
	    }

	    @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        showDetails(position);
	    }

	    /**
	     * Helper function to show the details of a selected item, either by
	     * displaying a fragment in-place in the current UI, or starting a
	     * whole new activity in which it is displayed.
	     */
	    void showDetails(int index) {
	        mCurCheckPosition = index;

	        if (mDualPane) {
	            // We can display everything in-place with fragments, so update
	            // the list to highlight the selected item and show the data.
	            getListView().setItemChecked(index, true);

	            // Check what fragment is currently shown, replace if needed.
	            DetailFragment details = (DetailFragment) getFragmentManager().findFragmentById(R.id.detailFragment);
	            if (details == null || details.getShownIndex() != index) {
	                // Make new fragment to show this selection.
	                details = DetailFragment.newInstance(index);

	                // Execute a transaction, replacing any existing fragment
	                // with this one inside the frame.
	                FragmentTransaction ft = getFragmentManager().beginTransaction();
	                ft.replace(R.id.detailsText, details);
	                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	                ft.commit();
	            }

	        } else {
	            // Otherwise we need to launch a new activity to display
	            // the dialog fragment with selected text.
	            Intent intent = new Intent();
	            intent.setClass(getActivity(), DetailActivity.class);
	            intent.putExtra("index", index);
	            startActivity(intent);
	        }
	    }

	}
}