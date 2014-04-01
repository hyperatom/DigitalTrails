package uk.ac.swan.digitaltrails;

import java.util.ArrayList;
import java.util.List;

import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.SimpleGeofence;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.google.android.gms.plus.model.people.Person.Image;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends FragmentActivity implements
		LoaderCallbacks<Cursor>, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
		OnAddGeofencesResultListener {
	
	private static final String TAG = "MapActivity";
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int MILLISECONDS_PER_SECOND = 1000;
	private static final int UPDATE_INTERVAL_IN_SECONDS = 10;
	private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
	private static final int FASTEST_INTERVAL_IN_SECONDS = 5;
	private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
	
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
	private int mRequestType;
	private enum selectFilter {FILTER_WAYPOINT_WITH_DESCR, FILTER_WAYPOINT_WITH_MEDIA };

	// Activity Methods
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCurFilter = selectFilter.FILTER_WAYPOINT_WITH_DESCR;
		setContentView(R.layout.activity_map);
		Intent intent = getIntent();
		int walkId = intent.getExtras().getInt("walkId");	
		mMarkers = new ArrayList<Marker>();
		mWaypoints = new ArrayList<Waypoint>();
		initMap();
		// intent.getExtras() contains walkId
		getSupportLoaderManager().initLoader(0, intent.getExtras(), this);
		mLocationClient = new LocationClient(this, this, this);
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
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

	private void showInfoViewDialog() {
		InfoViewDialogFragment dialog = InfoViewDialogFragment.newInstance();
		dialog.init(mLoaderCursor);
		dialog.show(getSupportFragmentManager(), "dialog");
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
				Bundle bundle = new Bundle();
				bundle.putInt("markerId", mMarkers.indexOf(marker));
				mCurFilter = selectFilter.FILTER_WAYPOINT_WITH_MEDIA;
				// load data for the dialog
				MapActivity.this.getSupportLoaderManager().initLoader(0, bundle, MapActivity.this);
				showInfoViewDialog();
			}
		});
	}
	
	private void resumeMap() {
		setDefaultMapConfig();
	}

	private void createMarkers(ArrayList<Waypoint> waypoints) {
		for (Waypoint wp : waypoints) {
			if (wp.isRequest() == false) {
				mMarkers.add(mMap.addMarker(new MarkerOptions()
						.position(new LatLng(wp.getLatitude(), wp.getLongitude()))
						.title(wp.getTitle())
						.snippet(wp.getDescriptions().get(0).getShortDescription()))); // BAD - assuming only 1 desc.
			}
		}
	}
	
	private ArrayList<Geofence> createGeofences(ArrayList<Waypoint> waypoints) {
		final int radius = 8;
		final int responsiveness = 1500;
		ArrayList<Geofence> geofences = new ArrayList<Geofence>();
		for (Waypoint wp : waypoints) {	
			geofences.add(
					new Geofence.Builder()
					.setRequestId(wp.getTitle())
					.setCircularRegion(wp.getLatitude(), wp.getLongitude(), radius)
					.setExpirationDuration(Geofence.NEVER_EXPIRE)
					.setNotificationResponsiveness(responsiveness)
					.build());
		}
		return geofences;
	}
	
	//TODO: Use a Join to get the data we need, currently we can only get lat and long and visit_order.
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
						mWaypoints.add(wp);
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
				
				// add the last waypoint
				mWaypoints.add(wp);
				createMarkers(mWaypoints);
				mGeofences = createGeofences(mWaypoints);
				//mLocationClient.addGeofences(mGeofences, pendingIntent, this);
			} else if (mCurFilter == selectFilter.FILTER_WAYPOINT_WITH_MEDIA) {
				mLoaderCursor = data;
			}
		}
		
		// In practice we may want to set the bounds to be that of the area we are walking in.
		// for now I'm just going to zoom in this close cause I can.
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), 15)); 
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}
	
	// Has to be a class or Android won't let it happen...
	/**
	 * 
	 * @author Lewis Hancock
	 *
	 */
	public static class InfoViewDialogFragment extends DialogFragment {

		private Waypoint mWaypoint;
		
		static InfoViewDialogFragment newInstance() {
			InfoViewDialogFragment dialog = new InfoViewDialogFragment();
			return dialog;
		}

		// TODO: Im pretty sure this is dumb...
		public void init(Cursor data) {
			mWaypoint = new Waypoint();
			if (data != null && data.getCount() > 0) {
				List<Audio> audioFiles = new ArrayList<Audio>();
				List<Video> videoFiles = new ArrayList<Video>();
				List<Image> imageFiles = new ArrayList<Image>();
				while (data.moveToNext()) {
					//TODO: add to lists.
				}
			}
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			//builder.setMessage(R.string.dialog_info_view)
			builder.setMessage("This is where we can see text, videos, audio etc. about this waypoint.")
			.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// user cancelled
				}
			});
			return builder.create();
		}
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
		
		// try to resolve any error
		if (connectionResult.hasResolution()) {
			try {
				connectionResult.startResolutionForResult(this,  CONNECTION_FAILURE_RESOLUTION_REQUEST);
				
			} catch (IntentSender.SendIntentException e) {
				e.printStackTrace();
			}
		} else {
			// can't resolve error, show error dialog.
			//showErrorDialog(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnected(Bundle dataBundle) {
		Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(this, "Disconnected, please reconnect", Toast.LENGTH_SHORT).show();
	}
	
	// LocationListener methods begin
	@Override 
	public void onLocationChanged(Location location) {
		// compare location to waypoint locations.
	}

	@Override
	public void onAddGeofencesResult(int statusCode, String[] geofenceRequestIds) {
		if (LocationStatusCodes.SUCCESS == statusCode) {
			
		} else {
			
		}
	}
	
} 
