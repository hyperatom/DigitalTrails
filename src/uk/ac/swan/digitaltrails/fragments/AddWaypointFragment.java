package uk.ac.swan.digitaltrails.fragments;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.activities.MapActivity;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Fragment to allow user to add waypoints to a walk.
 * @author Lewis Hancock
 *
 */
public class AddWaypointFragment extends Fragment implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {

	/** Debugging tag */
	private static final String TAG = "AddWaypointFragment";

	
	/** The current map */
	private GoogleMap mMap;
	/** SupportMapFragemnt to contain GoogleMap */
	private SupportMapFragment mFragment;
	/** current LocationClient */
	private LocationClient mLocationClient;
	/** Waypoints for walk */
	private List<Waypoint> mWaypointList;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null) {
		}

		return inflater.inflate(R.layout.fragment_add_waypoint, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	
	/** 
	 * Configure default settings for the GoogleMap map
	 * @param map the GoogleMap to configure
	 */
	private void defaultMapConfig(GoogleMap map) {
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
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						final LatLng position = latlng;
						// Add the positive button
						builder.setPositiveButton(R.string.confirm_waypoint, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Waypoint wp = new Waypoint();
								wp.setId(mWaypointList.size());
								wp.setTitle("Waypoint " + wp.getId());
								wp.setLatitude(position.latitude);
								wp.setLongitude(position.longitude);
								wp.setAudioFiles(new ArrayList<Audio>());
								wp.setVideos(new ArrayList<Video>());
								wp.setPhotos(new ArrayList<Photo>());
								wp.setDescriptions(new ArrayList<Description>());
								mWaypointList.add(wp);
								mMap.addMarker(new MarkerOptions()
										.position(position)
										.title(wp.getTitle()));
							}
						});
						// Add the negative button
						builder.setNegativeButton(R.string.cancel_waypoint, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// do nothing
							}
						});  
						//Set the title
						builder.setTitle(R.string.add_waypoint_dialog_title)
						.setMessage(R.string.add_waypoint_dialog_message);
						//Build and create dialog
						builder.create();
						builder.show();					
					}

				}
		);	
		
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

}
