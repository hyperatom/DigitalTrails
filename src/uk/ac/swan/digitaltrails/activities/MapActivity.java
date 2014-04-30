package uk.ac.swan.digitaltrails.activities;

import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Description;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.database.WhiteRockContract;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
/**
 * Activity allowing the user to "go on a walk". Fully functional google map with geospaces etc.
 * @author Lewis H
 *
 */
public class MapActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {
	
	@SuppressWarnings("unused")
	private static final String TAG = "MapActivity";
	
	public static String ARG_EXPLORE = "explore";
	
	/** The current GoogleMap */
	private GoogleMap mMap;
	/** ArrayList of markers currently on the map */
	private ArrayList<Marker> mMarkers;
	/** ArrayList of waypoints in the walk */
	private ArrayList<Waypoint> mWaypoints;
	/** The filter to use when loading data */
	private selectFilter mCurFilter;
	/** Cursor returned from the loader */
	private Cursor mLoaderCursor;
	
	private enum selectFilter {FILTER_WAYPOINT_WITH_DESCR, FILTER_WAYPOINT_WITH_MEDIA };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCurFilter = selectFilter.FILTER_WAYPOINT_WITH_DESCR;
		setContentView(R.layout.activity_map);
		Intent intent = getIntent();
		
		// let us load the map.
		if (intent.getExtras().getInt(ARG_EXPLORE) == 1) {
			int walkId = intent.getExtras().getInt("walkId");	
			mMarkers = new ArrayList<Marker>();
			mWaypoints = new ArrayList<Waypoint>();
			initMap();
			getSupportLoaderManager().initLoader(0, intent.getExtras(), this);
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
				getSupportLoaderManager().initLoader(0, bundle, MapActivity.this);
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
						.snippet(wp.getDescriptions().get(0).getShortDescription()))); // TODO: Fix this. BAD - assuming only 1 desc.
			}
		}

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
				mWaypoints.add(wp);
				createMarkers(mWaypoints);
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

		public void init(Cursor data) {
			mWaypoint = new Waypoint();
			if (data != null && data.getCount() > 0) {
				ArrayList<Audio> audioFiles = new ArrayList<Audio>();
				ArrayList<Video> videoFiles = new ArrayList<Video>();
				ArrayList<Image> imageFiles = new ArrayList<Image>();
				while (data.moveToNext()) {
					
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

} 