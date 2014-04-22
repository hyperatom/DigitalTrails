package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.fragments.EditWaypointDialogFragment.EditWaypointDialogListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
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
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Base class which all instances of Google Maps should be using, apart from when the User "goes on a walk".
 * @author Lewis H
 *
 */
public class MapFragment extends Fragment implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
EditWaypointDialogListener {

	/** Debugging tag */
	private static final String TAG = "MapFragment";
	/** The current map */
	protected GoogleMap mMap;
	/** SupportMapFragemnt to contain GoogleMap */
	protected SupportMapFragment mFragment;
	/** current LocationClient */
	protected LocationClient mLocationClient;
	/** Waypoints for walk */
	protected List<Waypoint> mWaypointList;
	/** Listen for when we close the map */
	protected OnMapClosedListener mCallback;

	public interface OnMapClosedListener {
		public void onMapClosed(List<Waypoint> waypointList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
		}
		return inflater.inflate(R.layout.fragment_map, container, false);
	}

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();

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
		mWaypointList = new ArrayList<Waypoint>();
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		initMap();
		mLocationClient.connect();
	}

	@Override
	public void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDetach() {
		mCallback.onMapClosed(mWaypointList);
		super.onDetach();
	}

	/** 
	 * Configure default settings for the GoogleMap map
	 * @param map the GoogleMap to configure
	 */
	protected void defaultMapConfig(GoogleMap map) {
		map.getUiSettings().setCompassEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setZoomGesturesEnabled(true);
		map.getUiSettings().setRotateGesturesEnabled(true);
		map.setBuildingsEnabled(true);
		map.setOnMapLongClickListener(
				new OnMapLongClickListener() {

					@Override
					public void onMapLongClick(LatLng latlng) {
						Log.d(TAG, "MapLongClick");
						showEditDialog(latlng);
					}		
				});
		map.setOnInfoWindowClickListener(
				new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						showEditDialog(marker);
					}
				});
	}

	protected void showEditDialog(LatLng latlng) {
		Bundle args = new Bundle();
		args.putParcelable(EditWaypointDialogFragment.ARG_POSITION, latlng);
		final Fragment frag = this;
		DialogFragment editWpDialog = new EditWaypointDialogFragment();
		editWpDialog.setArguments(args);
		editWpDialog.setTargetFragment(frag, 0);
		editWpDialog.show(getFragmentManager().beginTransaction(),	"EditWaypointDialogFragment");
	}
	
	protected void showEditDialog(Marker marker) {
		Bundle args = new Bundle();
		args.putParcelable(EditWaypointDialogFragment.ARG_POSITION, marker.getPosition());
		args.putString(EditWaypointDialogFragment.ARG_TITLE, marker.getTitle());
		args.putString(EditWaypointDialogFragment.ARG_SNIPPET, marker.getSnippet());
		final Fragment frag = this;
		DialogFragment editWpDialog = new EditWaypointDialogFragment();
		editWpDialog.setArguments(args);
		editWpDialog.setTargetFragment(frag, 0);
		editWpDialog.show(getFragmentManager().beginTransaction(),	"EditWaypointDialogFragment");
	}
	
	/** 
	 * Initialise the map
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

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d(TAG, "LocClient Connected");
		LatLng userLatLng = new LatLng(mLocationClient.getLastLocation().getLatitude(), mLocationClient.getLastLocation().getLongitude());
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15)); 
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, View view) {
		Log.d(TAG, "Pressed Positive");
		// Get views from dialog
		EditText wpTitle = (EditText) view.findViewById(R.id.name_edit);
		EditText wpLat = (EditText) view.findViewById(R.id.latitude_edit);
		EditText wpLong = (EditText) view.findViewById(R.id.longitude_edit);
		EditText wpDescr = (EditText) view.findViewById(R.id.description_edit);

		Waypoint wp = new Waypoint();
		wp.setId(mWaypointList.size());

		wp.setTitle(wpTitle.getText().toString());

		try {
			wp.setLatitude(Double.parseDouble(wpLat.getText().toString()));
			wp.setLongitude(Double.parseDouble(wpLong.getText().toString()));
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		wp.setAudioFiles(new ArrayList<Audio>());
		wp.setVideos(new ArrayList<Video>());
		wp.setPhotos(new ArrayList<Photo>());

		ArrayList<Description> descriptions = new ArrayList<Description>();
		descriptions.add(new Description(wpDescr.getText().toString()));
		wp.setDescriptions(descriptions);
		mWaypointList.add(wp);
		mMap.addMarker(new MarkerOptions()
		.position(((EditWaypointDialogFragment) dialog).getPosition())
		.title(wp.getTitle()));
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		Log.d(TAG, "Pressed Negative");
		// TODO Auto-generated method stub

	}	
}
