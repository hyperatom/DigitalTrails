package uk.ac.swan.digitaltrails.sync;

import java.util.ArrayList;
import java.util.List;
import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Lewis Hancock
 * ArrayAdapter which takes Walk objects and displays them on a custom ListFragment Layout
 */
public class WalkLoaderAdapter extends ArrayAdapter<Walk> {

	/**
	 * 
	 */
	private static final String TAG = "WalkLoaderAdapater";
	/**
	 * 
	 */
	private final Context mContext;
	/**
	 * 
	 */
	private final LayoutInflater mInflater;
	//private ArrayList<Walk> mValues;
	
	/**
	 * @param context
	 */
	public WalkLoaderAdapter(Context context) {
		super(context,  R.layout.fragment_search_list);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
	}
	
//	public void setValues(List<Walk> values) {
//		Log.d(TAG, "settingValues");
//		mValues = (ArrayList<Walk>) values;
//	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = mInflater.inflate(R.layout.fragment_search_list, parent, false);
		Walk item = (Walk) getItem(position);
		TextView textView = (TextView) view.findViewById(R.id.label);
		if (textView != null) {
			textView.setText(item.getEnglishDescriptions().getTitle());
		}
		return view;
	}
	
	/**
	 * Set this ArrayAdapter's data to a List<Walk>
	 * @param walks the List to set the data to.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setData(List<Walk> walks) {
		clear();
		if (walks != null) {
			addAll(walks);
		}
	}
	
}
