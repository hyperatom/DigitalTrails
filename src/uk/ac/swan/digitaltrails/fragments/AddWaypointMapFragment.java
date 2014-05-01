package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishDescription;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.fragments.AddWaypointDialogFragment.AddWaypointDialogListener;
import uk.ac.swan.digitaltrails.fragments.EditWaypointDialogFragment.EditWaypointDialogListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddWaypointMapFragment extends MapFragment implements 
LoaderCallbacks<Cursor>, EditWaypointDialogListener {

	public static String ARG_POSITION = "position";

	private static String TAG = "AddWaypointMapFragment";

	/** ArrayList of Markers currently on the map */
	private ArrayList<Marker> mMarkers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMarkers = new ArrayList<Marker>();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		Bundle args = getArguments();
		getLoaderManager().initLoader(0, args, this);
	}
	
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
			wp.setTitle(title);
			if (wp.getDescriptions().size() > 0) {
				int count = 0;
				for (Description d : wp.getDescriptions()) {
					if (d.getLanguage() == Description.Languages.ENGLISH.ordinal())
					{
						d.setLongDescription(description);
						d.setShortDescription(snippet);	
						count++;
					}
				}
				if (count == 0) {
					wp.getDescriptions().add(new Description(description));
				}
			} else {
				EnglishDescription d = new EnglishDescription(0, title+" description", snippet, description);
				wp.getDescriptions().add(d);
			}
			wp.setLatLng(new LatLng(latitude, longitude));

			MarkerOptions options = new MarkerOptions();
			options.position(new LatLng(wp.getLatitude(), wp.getLongitude()));
			options.title(wp.getTitle());
			for (Description d : wp.getDescriptions()) {
				if (d.getLanguage() == Description.Languages.ENGLISH.ordinal()) {
					options.snippet(d.getShortDescription());
				}
			}
			Marker marker = mMap.addMarker(options);
			marker.setDraggable(true);
			mMarkers.get((int) index).remove();
			mMarkers.set((int) index, marker);
		}
	}
	
	@Override
	public void onEditDialogNegativeClick(DialogFragment dialog, View view) {
	}
	
	private void createMarkers(ArrayList<Waypoint> waypoints) {
		for (Waypoint wp : waypoints) {
			if (wp.isRequest() == false) {
				MarkerOptions options = new MarkerOptions();
				options.position(new LatLng(wp.getLatitude(), wp.getLongitude()));
				options.title(wp.getTitle());
				for (Description d : wp.getDescriptions()) {
					if (d.getLanguage() == Description.Languages.ENGLISH.ordinal()) {
						options.snippet(d.getShortDescription());
					}
				}
				mMarkers.add(mMap.addMarker(options));
			}
		}
		for (Marker m : mMarkers) {
			m.setDraggable(true);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = WhiteRockContract.WaypointWithEnglishDescriptionWithMedia.CONTENT_URI;
		String 	select  = "((" + WhiteRockContract.Waypoint.WALK_ID + " == " + args.getInt(ARG_POSITION) + "))";
		// PROJECT_ALL for reference {WaypointColumns.ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER,
		//WaypointColumns.WALK_ID, USER_ID,
		//TITLE, SHORT_DESCR, LONG_DESCR, FILE_NAME};
		return new CursorLoader(getActivity().getBaseContext(), baseUri, WhiteRockContract.WaypointWithEnglishDescriptionWithMedia.PROJECTION_ALL, select, null, WhiteRockContract.WaypointWithEnglishDescription.VISIT_ORDER + " COLLATE LOCALIZED ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO: Yeah, this could be put into a Waypoint(Cursor data) constructor really. Time is of the essence
		// so we're programming a little badly.
		if (data != null && data.moveToFirst()) {
			ArrayList<Description> dList = new ArrayList<Description>();
			ArrayList<Media> mediaList = new ArrayList<Media>();
			Waypoint wp = new Waypoint();
			wp.setId(data.getLong(0));
			wp.setLatitude(data.getDouble(1));
			wp.setLongitude(data.getDouble(2));
			wp.setLatLng(new LatLng(wp.getLatitude(), wp.getLongitude()));
			wp.setIsRequest(data.getInt(3));
			wp.setVisitOrder(data.getInt(4));
			wp.setTitle(data.getString(7));
			Description desc = new EnglishDescription();
			desc.setShortDescription(data.getString(8));
			desc.setLongDescription(data.getString(9));
			dList.add(desc);
			Media media = new Media();
			media.setFileLocation(data.getString(10));
			media.setWaypoint(wp);
			wp.setMedia(mediaList);
			wp.setDescriptions(dList);
			wp.getMediaFiles().add(media);

			while (data.moveToNext()) {
				if (data.getLong(0) == wp.getId()) {
					media = new Media();
					media.setFileLocation(data.getString(10));
				} else { 
					mWaypointList.add(wp);
					wp = new Waypoint();
					wp.setId(data.getLong(0));
					wp.setLatitude(data.getDouble(1));
					wp.setLongitude(data.getDouble(2));
					wp.setLatLng(new LatLng(wp.getLatitude(), wp.getLongitude()));
					wp.setIsRequest(data.getInt(3));
					wp.setVisitOrder(data.getInt(4));
					wp.setTitle(data.getString(7));
					desc = new EnglishDescription();
					desc.setLanguage(Description.Languages.ENGLISH.ordinal());
					desc.setShortDescription(data.getString(8));
					desc.setLongDescription(data.getString(9));
					dList.add(desc);
					media = new Media();
					media.setFileLocation(data.getString(10));
					media.setWaypoint(wp);
					wp.setMedia(mediaList);
					wp.setDescriptions(dList);
					wp.getMediaFiles().add(media);
				}
			}
			mWaypointList.add(wp);
			createMarkers((ArrayList<Waypoint>) mWaypointList);
		} 

		// In practice we may want to set the bounds to be that of the area we are walking in.
		// for now I'm just going to zoom in this close cause I can.
		if (mMarkers.size() > 0) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), 15)); 
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle arg0) {

	}


}
