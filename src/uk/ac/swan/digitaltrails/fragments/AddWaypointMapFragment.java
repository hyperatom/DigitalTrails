package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishWalkDescription;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.fragments.EditWaypointDialogFragment.EditWaypointDialogListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author Lewis Hancock
 *
 */
public class AddWaypointMapFragment extends MapFragment implements EditWaypointDialogListener {

	/**
	 * 
	 */
	public static String ARG_POSITION = "position";

	/**
	 * 
	 */
	private static String TAG = "AddWaypointMapFragment";

	/** ArrayList of Markers currently on the map */
	/**
	 * 
	 */
	private ArrayList<Marker> mMarkers;

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.MapFragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMarkers = new ArrayList<Marker>();
	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.MapFragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		Bundle args = getArguments();
		//getLoaderManager().initLoader(0, args, this);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.MapFragment#setInfoClickListener(com.google.android.gms.maps.GoogleMap)
	 */
	@Override
	protected void setInfoClickListener(GoogleMap map) {
		map.setOnInfoWindowClickListener(
				new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						showEditDialog(mWaypointList.get(mMarkers.indexOf(marker)));
					}
				});
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.EditWaypointDialogFragment.EditWaypointDialogListener#onEditDialogPositiveClick(android.support.v4.app.DialogFragment, android.view.View)
	 */
	@Override
	public void onEditDialogPositiveClick(DialogFragment dialog, View view) {
		long index = dialog.getArguments().getLong(EditWaypointDialogFragment.ARG_INDEX); 
		if (index < mWaypointList.size()) {
			Waypoint wp = mWaypointList.get((int) index);
			String title = ((EditText) view.findViewById(R.id.name_edit)).getText().toString().trim();
			String description = ((EditText) view.findViewById(R.id.description_edit)).getText().toString().trim();
			String snippet = description.substring(0, description.length()/2);
			double latitude = Double.parseDouble(((EditText) view.findViewById(R.id.latitude_edit)).getText().toString().trim());
			double longitude = Double.parseDouble(((EditText) view.findViewById(R.id.longitude_edit)).getText().toString().trim());
			if (wp.getEnglishDescription() != null) {
				EnglishWaypointDescription d = (EnglishWaypointDescription) wp.getEnglishDescription();
				d.setTitle(title);
				d.setLongDescription(description);
				d.setShortDescription(snippet);	
			} else {
				EnglishWaypointDescription d = new EnglishWaypointDescription(-1, title, snippet, description);
				wp.setEnglishDescription(d);
			}
			wp.setLatLng(new LatLng(latitude, longitude));

			MarkerOptions options = new MarkerOptions();
			options.position(new LatLng(wp.getLatitude(), wp.getLongitude()));
			options.title(wp.getEnglishDescription().getTitle());
			options.snippet(wp.getEnglishDescription().getShortDescription());
			Marker marker = mMap.addMarker(options);
			marker.setDraggable(true);
			mMarkers.get((int) index).remove();
			mMarkers.set((int) index, marker);
		}
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.EditWaypointDialogFragment.EditWaypointDialogListener#onEditDialogNegativeClick(android.support.v4.app.DialogFragment, android.view.View)
	 */
	@Override
	public void onEditDialogNegativeClick(DialogFragment dialog, View view) {
	}


}
