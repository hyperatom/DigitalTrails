package uk.ac.swan.digitaltrails.activities;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.accounts.AccountGeneral;
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
import uk.ac.swan.digitaltrails.fragments.ErrorDialogFragment;
import uk.ac.swan.digitaltrails.fragments.ImageGridFragment;
import uk.ac.swan.digitaltrails.fragments.InfoViewDialogFragment;
import uk.ac.swan.digitaltrails.utils.ReceiveTransitionsIntentService;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
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
 * @author Lewis Hancock
 *
 */
public class MapActivity extends ActionBarActivity implements
		LoaderCallbacks<Cursor>, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
		OnAddGeofencesResultListener {
	
	/**
	 * Static tag string for the class.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "MapActivity";
	/**
	 * 
	 */
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	/**
	 * number of milliseconds in a second
	 */
	private static final int MILLISECONDS_PER_SECOND = 1000;
	/**
	 * How often to update, in seconds.
	 */
	private static final int UPDATE_INTERVAL_IN_SECONDS = 10;
	/**
	 * How often to update.
	 */
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	/**
	 * Fastest interval, in seconds.
	 */
	private static final int FASTEST_INTERVAL_IN_SECONDS = 5;
	/**
	 * Fastest interval.
	 */ 
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

	/**
	 * Static constant for argument explore.
	 */
	public static final String ARG_EXPLORE = "explore";
	/**
	 * static constant for argument geofences
	 */
	public static final String ARG_GEOFENCES = "geofences";
	/**
	 * static constant for argument receive geofences entered
	 */
	public static final String RECEIVE_GEOFENCES_ENTERED = "uk.ac.swan.digitaltrails.RECEIVE_GEOFENCES_ENTERED";
	/**
	 * static constant for argument receive geofences exited
	 */
	public static final String RECEIVE_GEOFENCES_EXITED = "uk.ac.swan.digitaltrails.RECEIVE_GEOFENCES_EXITED";
	/**
	 * enum for the type of request for location client
	 */
	public enum REQUEST_TYPE {ADD};
	/** The current GoogleMap */
	private GoogleMap mMap;
	/** ArrayList of markers currently on the map */
	private ArrayList<Marker> mMarkers;
	/** the walk we are on */
	private Walk mWalk;
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
	/**
	 * The current location request
	 */
	private LocationRequest mLocationRequest;
	/** Type of request for LocationRequest */
	/**
	 * the current request type
	 */
	private REQUEST_TYPE mRequestType;
	/**
	 * Is request in progress
	 */
	private boolean mInProgress;
	/**
	 * The current transition pending intent
	 */
	private PendingIntent mTransitionPendingIntent;
	/**
	 * The number of waypoints visited
	 */
	private int numVisited = 0;
	/**
	 * Filter for the query to run.
	 */
	private enum selectFilter {FILTER_WAYPOINT_WITH_DESCR, FILTER_WAYPOINT_WITH_MEDIA };
	
	/**
	 * BroadcastReceiver to listen for when a geofence is entered / left
	 */
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
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInProgress = false;
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
			initMap();
			getSupportLoaderManager().initLoader(0, intent.getExtras(), this);

			mLocationRequest = LocationRequest.create();
			mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
			mLocationRequest.setInterval(UPDATE_INTERVAL);
			mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		}
		
		if(getResources().getBoolean(R.bool.portrait_only)){
	        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    }
	}

	
	/**
	 * Controls what happens when a geofence is entered
	 * @param triggeredIds All the geofences entered
	 */
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
		for (Waypoint wp : mWalk.getWaypoints()) {
			if (wp.getVisitOrder() == numVisited + 1) {
				mMarkers.get(mWalk.getWaypoints().indexOf(wp)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
			}
		}
	}
	
	/**
	 * Controls what happens when a geofence is exited
	 * @param triggeredIds The list of geofences exited
	 */
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
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.maps_menu, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return changeMap(item);
	}

	/**
	 * Displays the information DialogFragment for the chosen waypoint
	 * @param wp The waypoint to display information for
	 */
	private void showInfoViewDialog(Waypoint wp) {
		Bundle args = new Bundle();
		args.putString(InfoViewDialogFragment.ARG_TITLE, wp.getEnglishDescription().getTitle());
		args.putString(InfoViewDialogFragment.ARG_DESCRIPTION, wp.getEnglishDescription().getLongDescription());
		DialogFragment dialog = new InfoViewDialogFragment();
		dialog.setArguments(args);
		dialog.show(getSupportFragmentManager(), "infoDialog");
	}
	
	private void showImageDialog(Waypoint wp) {
		String audioPlaceHolder = "android.resource://uk.ac.swan.digitaltrails/" + R.drawable.ic_action_volume_on + ".png";
		Bundle args = new Bundle();
		int count = wp.getPhotos().size();
		for (Photo p : wp.getPhotos()) {
			Log.d(TAG, "FileLoc: " + p.getFileLocation());
		}
		int audioCount = wp.getAudioFiles().size();
		int total = count+audioCount;
		Log.d(TAG, "Count: " + count + " audio: " + audioCount + " total: " + total);
		// fill with our placeholder images for audio.
		// TODO: Update for use with video.
		String[] paths = new String[total];
		int x = 0;
		if (count > 0 || audioCount > 0) {
			for (int i = 0; i < total; i++) {
				if (i < audioCount) {
					paths[i] = audioPlaceHolder;
					Log.d(TAG, "i inside audioCount: " + i);
				} else {
					Log.d(TAG, "i when we get to photos: " + i);
					paths[i] = wp.getPhotos().get(x).getFileLocation();
					x++;
				}
			}
		}
		
		String[] audioPaths = new String[audioCount];

		if (audioCount > 0) {
			for (int i = 0; i < audioCount; i++) {
				audioPaths[i] = wp.getAudioFiles().get(i).getFileLocation();
			}
		}
		
		// TODO: Uncomment when we want to work with videos.
//		int videoCount = wp.getVideos().size();
//		String[] videoPaths = new String[videoCount];
//		
//		if (videoCount > 0) {
//			for (int i = 0; i < videoCount; i++) {
//				videoPaths[i] = wp.getVideos().get(i).getFileLocation();
//			}
//		}
		
		args.putStringArray(ImageGridFragment.IMAGE_PATH_EXTRA, paths);
		args.putStringArray(ImageGridFragment.ARG_AUDIO_PATH, audioPaths);
		args.putString(ImageGridFragment.ARG_TITLE, wp.getEnglishDescription().getTitle());
		args.putString(ImageGridFragment.ARG_DESCRIPTION, wp.getEnglishDescription().getLongDescription());
		DialogFragment dialog = new ImageGridFragment();
		dialog.setArguments(args);
		dialog.show(getSupportFragmentManager(), "imageDialog");
	}
	

	/**
	 * Get the ReceiveTransitionIntentService
	 * @return the ReceiveTransitionIntentService
	 */
	private PendingIntent getTransitionPendingIntent() {
		Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
		
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
	
	/**
	 * Change the type of the map.
	 * @param item The menu item
	 * @return True if successful
	 */
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

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	protected void onResume() {
		super.onResume();
		initMap();
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onStop()
	 */
	@Override
	protected void onStop() {
		if (mLocationClient != null) {
			mLocationClient.disconnect();
		}
		super.onStop();
	}

	/**
	 * Initialise the map
	 */
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
	
	/**
	 * Set the default map config.
	 */
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
				//showInfoViewDialog(mWaypoints.get(mMarkers.indexOf(marker)));
				showImageDialog(mWalk.getWaypoints().get(mMarkers.indexOf(marker)));
			}
		});
	}
	
	/**
	 * Resume the map
	 */
	private void resumeMap() {
		setDefaultMapConfig();
	}

	/**
	 * Create a list of markers, stored in mMarkers
	 * @param waypoints The waypoints to create markers from
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
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri = WhiteRockContract.WalkComplete.CONTENT_URI;
		String 	select  = "((" + DbSchema.TABLE_WALK+"."+WhiteRockContract.Walk.ID + " == " + args.getInt("walkId") + "))";

		return new CursorLoader(this, baseUri, WhiteRockContract.WalkComplete.PROJECTION_ALL, select, null, WhiteRockContract.WalkComplete.VISIT_ORDER + " COLLATE LOCALIZED ASC");
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android.support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// create Waypoints from all the db info.
		// It might be better to create a map instead of just using the numbers and referring to projection..
		WalkDataSource walkDataSource = new WalkDataSource(getBaseContext());
		
		mWalk = walkDataSource.LoadWalkAndComponentsFromCursor(data);
		if (mWalk != null) {
			createMarkers(mWalk.getWaypoints());
			mGeofences = createGeofences(mMarkers);
			mLocationClient = new LocationClient(this, this, this);
			addGeofences();	
		} else {
			Toast.makeText(getBaseContext(), "Error loading walk", Toast.LENGTH_SHORT).show();
		}
	
		
		// In practice we may want to set the bounds to be that of the area we are walking in.
		// for now I'm just going to zoom in this close cause I can.
		if (mMarkers.size() > 0) 
		{
			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), 15));
		}
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android.support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}

	// Google Play Services stuff	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int, android.content.Intent)
	 */
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
	
	/**
	 * Run when connected to services
	 * @return true if successful, false otherwise
	 */
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
	
	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
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

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
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

	/* (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected, please reconnect", Toast.LENGTH_SHORT).show();
		mInProgress = false;
		mLocationClient = null;
	}
	
	// LocationListener methods begin
	/* (non-Javadoc)
	 * @see com.google.android.gms.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override 
	public void onLocationChanged(Location location) {
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener#onAddGeofencesResult(int, java.lang.String[])
	 */
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

	//TODO: all these logOutButton and onSync methods should be put into another class; not rewritten in each activity.
	// Dunno who did this.
	
	public void logOutButton(MenuItem v){
		AccountManager am = AccountManager.get(this);
		Account a = am.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];
		String authToken;
		try {
			authToken = am.peekAuthToken(a, AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS);
			am.invalidateAuthToken(AccountGeneral.ACCOUNT_TYPE, authToken);
			am.removeAccount(a, null, null);
			Intent i = new Intent(this, SplashActivity.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void onSync(MenuItem v){
		// Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        AccountManager am = AccountManager.get(this);
		Account a = am.getAccountsByType(AccountGeneral.ACCOUNT_TYPE)[0];
        ContentResolver.requestSync(a, WhiteRockContract.AUTHORITY, settingsBundle);
	}
	
} 