package uk.ac.swan.digitaltrails.sync;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WalkLoaderAdapter extends ArrayAdapter<Walk> {

	private static final String TAG = "WalkLoaderAdapater";
	private final Context mContext;
	private final LayoutInflater mInflater;
	//private ArrayList<Walk> mValues;
	
	public WalkLoaderAdapter(Context context) {
		super(context,  R.layout.fragment_search_list);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
	}
	
//	public void setValues(List<Walk> values) {
//		Log.d(TAG, "settingValues");
//		mValues = (ArrayList<Walk>) values;
//	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "Putting items into list");
		View view;
		view = mInflater.inflate(R.layout.fragment_search_list, parent, false);
		Walk item = (Walk) getItem(position);
		TextView textView = (TextView) view.findViewById(R.id.label);
		Log.d(TAG, "gotTextView");
		if (textView != null) {
			textView.setText(item.getEnglishDescriptions().getTitle());
			Log.d(TAG, "SettingText");
		}
		return view;
	}
	
	public void setData(List<Walk> walks) {
		clear();
		if (walks != null) {
			addAll(walks);
		}
	}
	
}
