package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishWalkDescription;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.components.WelshWalkDescription;
import uk.ac.swan.digitaltrails.components.WelshWaypointDescription;
import uk.ac.swan.digitaltrails.database.DbSchema;
import uk.ac.swan.digitaltrails.database.WalkDataSource;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.fragments.AddWaypointDialogFragment.AddWaypointDialogListener;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Base class which all instances of Google Maps should be using, apart from when the User "goes on a walk".
 * @author Lewis H
 *
 */
/**
 * @author Lewis Hancock
 *
 */
public class MapFragment extends Fragment implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LoaderCallbacks<Cursor>, AddWaypointDialogListener {

	/** Debugging tag */
	/**
	 * 
	 */
	private static final String TAG = "MapFragment";
	/** The current map */
	/**
	 * 
	 */
	protected GoogleMap mMap;
	/** SupportMapFragemnt to contain GoogleMap */
	/**
	 * 
	 */
	protected SupportMapFragment mFragment;
	/** current LocationClient */
	/**
	 * 
	 */
	protected LocationClient mLocationClient;
	/**
	 * The walk we are using
	 */
	protected Walk mWalk;
	/** Listen for when we close the map */
	/**
	 * 
	 */
	protected OnMapClosedListener mCallback;
	/** ArrayList of Markers currently on the map */
	/**
	 * 
	 */
	protected ArrayList<Marker> mMarkers;

	/**
	 * 
	 */
	public static String ARG_POSITION = "position";

	/**
	 * @author Lewis Hancock
	 *
	 */
	public interface OnMapClosedListener {
		public void onMapClosed(List<Waypoint> waypointList);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
		}
		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// ensure interface is implemented
		try {
			mCallback = (OnMapClosedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " Must implement OnMapClosedListener");
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();

		mWalk = new Walk();
		mWalk.setWaypoints(new ArrayList<Waypoint>());
		// add fragment for the map as a child of the current fragment... little awkward but the best way I found
		FragmentManager fm = getChildFragmentManager();
		mFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
		if (mFragment == null) {
			mFragment = SupportMapFragment.newInstance();
			FragmentTransaction transaction = fm.beginTransaction();
			transaction.replace(R.id.map_container, mFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}
		mLocationClient = new LocationClient(getActivity().getApplicationContext(), this, this);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		initMap();
		mMarkers = new ArrayList<Marker>();
		mLocationClient.connect();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onDetach()
	 */
	@Override
	public void onDetach() {
		mCallback.onMapClosed(mWalk.getWaypoints());
		super.onDetach();
	}

	/** 
	 * Configure default settings for the GoogleMap map
	 * @param map the GoogleMap to configure
	 */
	/**
	 * @param map
	 */
	protected void defaultMapConfig(GoogleMap map) {
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setRotateGesturesEnabled(true);
		map.setBuildingsEnabled(true);
		map.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng pos) {
				showAddDialog(pos);
			}
			
		}); 
			
		setInfoClickListener(map);
	}
	
	/**
	 * @param pos
	 */
	protected void showAddDialog(LatLng pos) {
		Bundle args = new Bundle();
		args.putParcelable(AddWaypointDialogFragment.ARG_POSITION, pos);
		DialogFragment dialog = new AddWaypointDialogFragment();
		dialog.setArguments(args);
		dialog.setTargetFragment(this, 0);
		dialog.show(getFragmentManager().beginTransaction(), "AddWaypointDialogFragment");
	}
	

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.AddWaypointDialogFragment.AddWaypointDialogListener#onDialogPositiveClick(android.support.v4.app.DialogFragment, android.view.View)
	 */
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, View view) {
		Log.d(TAG, "OnPosClick");
		Waypoint wp = new Waypoint();
		String title = ((EditText) view.findViewById(R.id.name_edit)).getText().toString().trim();
		String description = ((EditText) view.findViewById(R.id.description_edit)).getText().toString().trim();
		String snippet = description.substring(0, description.length()/2);
		double latitude = Double.parseDouble(((EditText) view.findViewById(R.id.latitude_edit)).getText().toString().trim());
		double longitude = Double.parseDouble(((EditText) view.findViewById(R.id.longitude_edit)).getText().toString().trim());
		EnglishWaypointDescription descr = new EnglishWaypointDescription();
		descr.setTitle(title);
		descr.setLongDescription(description);
		descr.setShortDescription(snippet);
		wp.setEnglishDescription(descr);
		wp.setLatLng(new LatLng(latitude, longitude));
		wp.setLatitude(latitude);
		wp.setLongitude(longitude);
		
		MarkerOptions options = new MarkerOptions();
		options.position(new LatLng(wp.getLatitude(), wp.getLongitude()));
		options.title(wp.getEnglishDescription().getTitle());
		options.snippet(wp.getEnglishDescription().getShortDescription());
		mWalk.getWaypoints().add(wp);
		Log.d(TAG, "Added waypoint to walk");
		wp.setVisitOrder(mWalk.getWaypoints().indexOf(wp));;
		Marker marker = mMap.addMarker(options);
		mMarkers.add(marker);
		Toast toast = Toast.makeText(this.getActivity().getBaseContext(), "Waypoint Successfully Added", Toast.LENGTH_SHORT);
		toast.show();
	}

	
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.fragments.AddWaypointDialogFragment.AddWaypointDialogListener#onDialogNegativeClick(android.support.v4.app.DialogFragment, android.view.View)
	 */
	@Override
	public void onDialogNegativeClick(DialogFragment dialog, View view) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @param map
	 */
	protected void setInfoClickListener(GoogleMap map) {
		// Implemented by children.
	}
	
	/**
	 * @param waypoint
	 */
	protected void showEditDialog(Waypoint waypoint) {
		Bundle args = new Bundle();
		args.putLong(EditWaypointDialogFragment.ARG_INDEX, mWalk.getWaypoints().indexOf(waypoint));
		args.putParcelable(EditWaypointDialogFragment.ARG_POSITION, waypoint.getLatLng());
		args.putString(EditWaypointDialogFragment.ARG_TITLE, waypoint.getEnglishDescription().getTitle());
		args.putString(EditWaypointDialogFragment.ARG_DESCRIPTION, waypoint.getEnglishDescription().getLongDescription());
		DialogFragment editWpDialog = new EditWaypointDialogFragment();
		editWpDialog.setArguments(args);
		editWpDialog.setTargetFragment(this, 0);
		editWpDialog.show(getFragmentManager().beginTransaction(),	"EditWaypointDialogFragment");
	}
	
	/** 
	 * Initialise the map
	 */
	/**
	 * 
	 */
	private void initMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container)).getMap();
			if (mMap == null) {
				Toast.makeText(getActivity().getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG).show();
			} else {
				defaultMapConfig(mMap);
			}
		}
	}

	/**
	 * @param item
	 * @return
	 */
	protected boolean changeMap(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.normal_map:
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			} else {
				Toast.makeText(getActivity().getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		case R.id.hybrid_map:
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			} else {
				Toast.makeText(getActivity().getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		case R.id.satellite_map:
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			} else {
				Toast.makeText(getActivity().getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		return false;
	}

	// Location Listener Implementation 

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle arg0) {
		Log.d(TAG, "LocClient Connected");
		try{
			LatLng userLatLng = new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude());
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15)); 
		}catch(Exception e){
			Toast t = Toast.makeText(getActivity(), "Please turn on your GPS", Toast.LENGTH_LONG);
			t.show();
		}
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param waypoints
	 */
	protected void createMarkers(ArrayList<Waypoint> waypoints) {
		for (Waypoint wp : waypoints) {
			if (wp.isRequest() == false) {
				MarkerOptions options = new MarkerOptions();
				options.position(new LatLng(wp.getLatitude(), wp.getLongitude()));
				options.title(wp.getEnglishDescription().getTitle());
				options.snippet(wp.getEnglishDescription().getShortDescription());
				mMarkers.add(mMap.addMarker(options));
			}
		}
		for (Marker m : mMarkers) {
			m.setDraggable(true);
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = WhiteRockContract.WalkComplete.CONTENT_URI;
		String 	select  = "((" + DbSchema.TABLE_WALK+"."+WhiteRockContract.Walk.ID + " == " + args.getInt("walkId") + "))";
		return new CursorLoader(getActivity().getBaseContext(), baseUri, WhiteRockContract.WalkComplete.PROJECTION_ALL, select, null, WhiteRockContract.WalkComplete.VISIT_ORDER + " COLLATE LOCALIZED ASC");

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		WalkDataSource walkDataSource = new WalkDataSource(getActivity().getBaseContext());
		mWalk = walkDataSource.LoadWalkAndComponentsFromCursor(data);
		if (mWalk != null) {
			createMarkers(mWalk.getWaypoints());
		} else {
			Toast.makeText(getActivity(), "Error Loading Walk", Toast.LENGTH_SHORT).show();
		}
		// In practice we may want to set the bounds to be that of the area we are walking in.
		// for now I'm just going to zoom in this close cause I can.
		if (mMarkers.size() > 0) {
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), 15)); 
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
	}


}
