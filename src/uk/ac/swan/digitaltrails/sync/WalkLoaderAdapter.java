package uk.ac.swan.digitaltrails.sync;

import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WalkLoaderAdapter extends ArrayAdapter<Walk> {

	private final Context mContext;
	private Walk[] mValues;
	
//	public WalkLoaderAdapter(Context context, Walk[] values) {
//		super(context,  R.layout.fragment_search_list, values);
//		mContext = context;
//		mValues = values;
//	}
	
	public WalkLoaderAdapter(Context context, List<Walk> values) {
		super(context,  R.layout.fragment_search_list, values);
		mContext = context;
		if (values != null) {
			mValues = new Walk[values.size()];
			values.toArray(mValues);
		} else {
			mValues = new Walk[0];
		}
	}

	public void setValues(List<Walk> walks) {
		mValues = new Walk[walks.size()];
		walks.toArray(mValues);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.fragment_search_list,  parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		textView.setText(mValues[position].getEnglishDescriptions().getTitle());
		return rowView;
	}
	
}
