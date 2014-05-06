package uk.ac.swan.digitaltrails.activities;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.EnglishWaypointDescription;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import uk.ac.swan.digitaltrails.fragments.ErrorDialogFragment;
import uk.ac.swan.digitaltrails.fragments.InfoViewDialogFragment;
import uk.ac.swan.digitaltrails.utils.ReceiveTransitionsIntentService;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
/**
 * Activity allowing the user to "go on a walk". Fully functional google map with geospaces etc.
 * @author Lewis H
 *
 */
public class MapActivity extends ActionBarActivity implements
		LoaderCallbacks<Cursor>, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
		OnAddGeofencesResultListener {
	
	@SuppressWarnings("unused")
	private static final String TAG = "MapActivity";
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int UPDATE_INTERVAL_IN_SECONDS = 10;
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	private static final int FASTEST_INTERVAL_IN_SECONDS = 5;
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

	public static final String ARG_EXPLORE = "explore";
	public static final String ARG_GEOFENCES = "geofences";
	public static final String RECEIVE_GEOFENCES_ENTERED = "uk.ac.swan.digitaltrails.RECEIVE_GEOFENCES_ENTERED";
	public static final String RECEIVE_GEOFENCES_EXITED = "uk.ac.swan.digitaltrails.RECEIVE_GEOFENCES_EXITED";
	public enum REQUEST_TYPE {ADD};
	/** The current GoogleMap */
	private GoogleMap mMap;
	/** ArrayList of markers currently on the map */
	private ArrayList<Marker> mMarkers;
	/** ArrayList of waypoints in the walk */
	private ArrayList<Waypoint> mWaypoints;
	/** ArrayList of Geofences in the walk */
	private ArrayList<Geofence> mGeofences;
	/** The filter to use when loading data */
	private selectFilter mCurFilter;
	/** Cursor returned from the loader */
	private Cursor mLoaderCursor;
	/** LocationClient for keeping track of user location */
	private LocationClient mLocationClient;
	/** Holds current location */
	private Location mCurrentLocation;
	/** */
	private LocationRequest mLocationRequest;
	/** Type of request for LocationRequest */
	private REQUEST_TYPE mRequestType;
	private boolean mInProgress;
	private PendingIntent mTransitionPendingIntent;
	private int numVisited = 0;
	private enum selectFilter {FILTER_WAYPOINT_WITH_DESCR, FILTER_WAYPOINT_WITH_MEDIA };
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(RECEIVE_GEOFENCES_ENTERED)) {
				Log.d(TAG, "Receiving Geofences event");
				geofenceEntered(intent.getExtras().getStringArray(ARG_GEOFENCES));
			}
			if (intent.getAction().equals(RECEIVE_GEOFENCES_EXITED)) {
				geofencesExited(intent.getExtras().getStringArray(ARG_GEOFENCES));

			}
		}
	};
	
	// Activity Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInProgress = false;
		mCurFilter = selectFilter.FILTER_WAYPOINT_WITH_DESCR;
		setContentView(R.layout.activity_map);
		Intent intent = getIntent();
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(RECEIVE_GEOFENCES_ENTERED);
		intentFilter.addAction(RECEIVE_GEOFENCES_EXITED);
		broadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
		// let us load the map.
		if (intent.getExtras().getInt(ARG_EXPLORE) == 1) {

			int walkId = intent.getExtras().getInt("walkId");	
			mMarkers = new ArrayList<Marker>();
			mWaypoints = new ArrayList<Waypoint>();
			initMap();
			getSupportLoaderManager().initLoader(0, intent.getExtras(), this);

			mLocationRequest = LocationRequest.create();
			mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			mLocationRequest.setInterval(UPDATE_INTERVAL);
			mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		}
	}

	
	private void geofenceEntered(String[] triggeredIds) {
		Log.d(TAG, "In geofenceEntered, num geofences triggered: " + triggeredIds.length);
		for (int i = 0; i < triggeredIds.length; i++) {
			Log.d(TAG, "Looking for id: " + triggeredIds[i]);
			String id = triggeredIds[i].trim();
			for (Geofence g : mGeofences) {
				Log.d(TAG, "Geofence id: " + g.getRequestId());
				if (g.getRequestId().equals(id)) {
					Log.d(TAG, "geofence found, finding marker");
					for (Marker m : mMarkers) {
						if (m.getId().equals(id)) {
							Log.d(TAG, "In Marker found, showing window");
							m.showInfoWindow();
							m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
							numVisited++;
						}
					}
				}
			}
		}
		for (Waypoint wp : mWaypoints) {
			if (wp.getVisitOrder() == numVisited + 1) {
				mMarkers.get(mWaypoints.indexOf(wp)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
			}
		}
	}
	
	private void geofencesExited(String[] triggeredIds) {
		Log.d(TAG, "In geofenceEntered, num geofences triggered: " + triggeredIds.length);
		for (int i = 0; i < triggeredIds.length; i++) {
			Log.d(TAG, "Looking for id: " + triggeredIds[i]);
			String id = triggeredIds[i].trim();
			for (Geofence g : mGeofences) {
				Log.d(TAG, "Geofence id: " + g.getRequestId());
				if (g.getRequestId().equals(id)) {
					Log.d(TAG, "geofence found, finding marker");
					for (Marker m : mMarkers) {
						if (m.getId().equals(id)) {
							Log.d(TAG, "In Marker found, showing window");
							if (m.isInfoWindowShown()) {
								m.hideInfoWindow();
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Start a request to monitor geofences by connecting locationclient.
	 */
	private void addGeofences() {
		mRequestType = REQUEST_TYPE.ADD;
		
		if (!servicesConnected()) {
			return;
		}
		
		mLocationClient = new LocationClient(this, this, this);
		
		if (!mInProgress) {
			mInProgress = true;
			mLocationClient.connect();
		} else {
			mLocationClient.disconnect();
			mInProgress = false;
			addGeofences();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.maps_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return changeMap(item);
	}

	private void showInfoViewDialog(Waypoint wp) {
		Bundle args = new Bundle();
		args.putString(InfoViewDialogFragment.ARG_TITLE, wp.getEnglishDescription().getTitle());
		args.putString(InfoViewDialogFragment.ARG_DESCRIPTION, wp.getEnglishDescription().getLongDescription());
		DialogFragment dialog = new InfoViewDialogFragment();
		dialog.setArguments(args);
		dialog.show(getSupportFragmentManager(), "infoDialog");
	}
	

	private PendingIntent getTransitionPendingIntent() {
		Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
		
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	private boolean changeMap(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.normal_map:
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			} else {
				Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		case R.id.hybrid_map:
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			} else {
				Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		case R.id.satellite_map:
			if (mMap != null) {
				mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			} else {
				Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		return false;
	}

	protected void onResume() {
		super.onResume();
		initMap();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		mLocationClient.disconnect();
		super.onStop();
	}

	private void initMap() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap == null) {
				Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG).show();
			} else {
				setDefaultMapConfig();
			}
		}
	}
	
	private void setDefaultMapConfig() {
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setZoomGesturesEnabled(true);
		mMap.getUiSettings().setRotateGesturesEnabled(true);
		mMap.setBuildingsEnabled(true);
		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
				return false;
			}
		});

		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				// find waypoint and show dialog for it.
				showInfoViewDialog(mWaypoints.get(mMarkers.indexOf(marker)));
			}
		});
	}
	
	private void resumeMap() {
		setDefaultMapConfig();
	}

	/**
	 * Create a list of markers, stored in mMarkers
	 * @param waypoints
	 */
	private void createMarkers(ArrayList<Waypoint> waypoints) {
		for (Waypoint wp : waypoints) {
			if (wp.isRequest() == false) {
				mMarkers.add(mMap.addMarker(new MarkerOptions()
						.position(new LatLng(wp.getLatitude(), wp.getLongitude()))
						.title(wp.getEnglishDescription().getTitle())
						.snippet(wp.getEnglishDescription().getShortDescription())));
			}
		}
	}
	
	/**
	 * Create a list of geofences from a list of markers.
	 * @param markers markers to create geofences from
	 * @return the list of geofences
	 */
	private ArrayList<Geofence> createGeofences(ArrayList<Marker> markers) {
		final int radius = 10;
		final int responsiveness = 1500;
		ArrayList<Geofence> geofences = new ArrayList<Geofence>();
		for (Marker m : markers) {	
			geofences.add(
					new Geofence.Builder()
					.setRequestId(m.getId())
					.setCircularRegion(m.getPosition().latitude, m.getPosition().longitude, radius)
					.setExpirationDuration(Geofence.NEVER_EXPIRE)
					.setNotificationResponsiveness(responsiveness)
					.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
					.build());
		}
		for (Geofence g : geofences) {
			g.toString();
		}
		return geofences;
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = WhiteRockContract.WaypointWithEnglishDescriptionWithMedia.CONTENT_URI;
		String 	select  = "((" + WhiteRockContract.Waypoint.WALK_ID + " == " + args.getInt("walkId") + "))";

		/*
		switch (mCurFilter) {
		case FILTER_WAYPOINT_WITH_MEDIA:
			baseUri = WhiteRockContract.WaypointWithMedia.CONTENT_URI;
			select = "((" + WhiteRockContract.Waypoint.ID + " == " + args.getInt("markerId") + "))";
			break;
		case FILTER_WAYPOINT_WITH_DESCR:
			baseUri = WhiteRockContract.WaypointWithEnglishDescription.CONTENT_URI;
			select = "((" + WhiteRockContract.Waypoint.WALK_ID + " == " + args.getInt("walkId") + "))";
			break;
		default:
			baseUri = WhiteRockContract.WaypointWithEnglishDescription.CONTENT_URI;
			select  = "((" + WhiteRockContract.Waypoint.WALK_ID + " == " + args.getInt("walkId") + "))";
			break;
		}*/
		// PROJECT_ALL for reference {WaypointColumns.ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER,
		//WaypointColumns.WALK_ID, USER_ID,
		//TITLE, SHORT_DESCR, LONG_DESCR, FILE_NAME};
		return new CursorLoader(this, baseUri, WhiteRockContract.WaypointWithEnglishDescriptionWithMedia.PROJECTION_ALL, select, null, WhiteRockContract.WaypointWithEnglishDescription.VISIT_ORDER + " COLLATE LOCALIZED ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// create Waypoints from all the db info.
		if (data != null && data.moveToFirst()) {
			if (mCurFilter == selectFilter.FILTER_WAYPOINT_WITH_DESCR) {
				data.moveToPrevious();
				Waypoint wp = new Waypoint();
				ArrayList<Media> mediaList = new ArrayList<Media>();
				while (data.moveToNext()) {
					if (data.getLong(0) == wp.getId()) {
						Media media = new Media();
						media.setFileLocation(data.getString(11));
						wp.getMediaFiles().add(media);
					} else { 
						if (!data.isFirst()) {
							mWaypoints.add(wp);
						}
						mediaList = new ArrayList<Media>();
						wp = new Waypoint();
						wp.setId(data.getLong(0));
						wp.setLatitude(data.getDouble(1));
						wp.setLongitude(data.getDouble(2));
						wp.setLatLng(new LatLng(wp.getLatitude(), wp.getLongitude()));
						wp.setIsRequest(data.getInt(3));
						wp.setVisitOrder(data.getInt(4));
						EnglishWaypointDescription desc = new EnglishWaypointDescription();
						desc.setTitle(data.getString(8));
						desc.setId(data.getLong(7));
						desc.setShortDescription(data.getString(9));
						desc.setLongDescription(data.getString(10));
						wp.setEnglishDescription(desc);
						Media media = new Media();
						media.setFileLocation(data.getString(11));
						media.setWaypoint(wp);
						wp.setMedia(mediaList);
						wp.getMediaFiles().add(media);
					}
				}
				// add the last waypoint
				mWaypoints.add(wp);
				createMarkers(mWaypoints);
				mGeofences = createGeofences(mMarkers);
				mLocationClient = new LocationClient(this, this, this);
				addGeofences();
			} else if (mCurFilter == selectFilter.FILTER_WAYPOINT_WITH_MEDIA) {
				mLoaderCursor = data;
			}
		}
		
		// In practice we may want to set the bounds to be that of the area we are walking in.
		// for now I'm just going to zoom in this close cause I can.
		if (mMarkers.size() > 0) 
		{
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), 15));
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}
	
	public void onBackPressed(){
		Intent intent = new Intent(this, ChooseWalkActivity.class);
		startActivity(intent);
	}

	// Google Play Services stuff	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		switch (reqCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			switch (resCode) {
			case Activity.RESULT_OK:
				// try again
				break;
			}
		}
	}
	
	private boolean servicesConnected() {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		ConnectionResult connectionResult = new ConnectionResult(resultCode, null);
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			int errorCode = connectionResult.getErrorCode();
			
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
		
			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}
		}		
		return false;
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		mInProgress = false;
		// try to resolve any error
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this,  CONNECTION_FAILURE_RESOLUTION_REQUEST);
				
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			int errorCode = connectionResult.getErrorCode();
			// can't resolve error, show error dialog.
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(), "Geofence Detection");
			}
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		Log.d(TAG, "Connected locationclient");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		switch (mRequestType) {
		case ADD:
			Log.d(TAG, "Adding");
			mTransitionPendingIntent = getTransitionPendingIntent();
			mLocationClient.addGeofences(mGeofences, mTransitionPendingIntent, this);
			break;
		}
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected, please reconnect", Toast.LENGTH_SHORT).show();
		mInProgress = false;
		mLocationClient = null;
	}
	
	// LocationListener methods begin
	@Override 
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		if (LocationStatusCodes.SUCCESS == statusCode) {
			// Whatever, it's added so I don't care.
			Log.d(TAG, "Geofences added successfully");
		} else {
			Log.e(TAG, "Geofences not added");
		}
		mInProgress = false;
		mLocationClient.disconnect();
	}
	
} 