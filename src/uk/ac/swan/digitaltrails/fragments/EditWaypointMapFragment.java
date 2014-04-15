package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class EditWaypointMapFragment extends MapFragment implements LoaderCallbacks<Cursor> {

	public static String ARG_POSITION = "position";

	private static String TAG = "EditWaypointFragment";

	/** ArrayList of Markers currently on the map */
	private ArrayList<Marker> mMarkers;
	/** Cursor returned from loader */
	private Cursor mLoaderCursor;

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


	private void createMarkers(ArrayList<Waypoint> waypoints) {
		for (Waypoint wp : waypoints) {
			if (wp.isRequest() == false) {
				mMarkers.add(mMap.addMarker(new MarkerOptions()
				.position(new LatLng(wp.getLatitude(), wp.getLongitude()))
				.title(wp.getTitle())
				.snippet(wp.getDescriptions().get(0).getShortDescription()))); // TODO: Fix this. BAD - assuming only 1 desc.
			}
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
		// create Waypoints from all the db info.
		if (data != null && data.moveToFirst()) {
			ArrayList<Description> dList = new ArrayList<Description>();
			ArrayList<Media> mediaList = new ArrayList<Media>();
			Waypoint wp = new Waypoint();
			wp.setId(data.getLong(0));
			wp.setLatitude(data.getDouble(1));
			wp.setLongitude(data.getDouble(2));
			wp.setIsRequest(data.getInt(3));
			wp.setVisitOrder(data.getInt(4));
			wp.setTitle(data.getString(7));
			Description desc = new Description();
			desc.setLanguage(Description.Languages.ENGLISH.ordinal());
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
					wp.setIsRequest(data.getInt(3));
					wp.setVisitOrder(data.getInt(4));
					wp.setTitle(data.getString(7));
					desc = new Description();
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
