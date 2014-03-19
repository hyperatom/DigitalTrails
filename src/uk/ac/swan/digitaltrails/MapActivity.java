package uk.ac.swan.digitaltrails;

import java.util.ArrayList;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
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

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends FragmentActivity implements LoaderCallbacks<Cursor> {
	
	private static final String TAG = "MapActivity";
	
	private GoogleMap mMap;
	private ArrayList<Marker> mMarkers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Intent intent = getIntent();
		int walkId = intent.getExtras().getInt("walkId");	
		mMarkers = new ArrayList<Marker>();
		initMap();
		getSupportLoaderManager().initLoader(0, intent.getExtras(), this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.maps_menu, menu); // fix this to change the map view etc.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return changeMap(item);
	}

	private void showInfoViewDialog() {
		DialogFragment dialog = InfoViewDialogFragment.newInstance();
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
				showInfoViewDialog();
				// Display correct information in the thing.
			}
		});

	}
	
	
	private void resumeMap() {
		setDefaultMapConfig();
	}

	/**
	 * For testing interactions only.
	 */
	private void initMapWhiteRock() {

		if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			if (mMap == null) {
				Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG).show();
			} else {
				mMap.getUiSettings().setCompassEnabled(true);
				mMap.getUiSettings().setMyLocationButtonEnabled(true);
				mMap.setMyLocationEnabled(true);
				mMap.getUiSettings().setZoomControlsEnabled(true);
				mMap.getUiSettings().setZoomGesturesEnabled(true);
				mMap.getUiSettings().setRotateGesturesEnabled(true);
				// add markers
				Marker dockMarker = mMap.addMarker(WaypointTemp.dockMarker);
				Marker canalTunnelMarker = mMap.addMarker(WaypointTemp.cananlTunnelMarker);
				Marker canalMarker = mMap.addMarker(WaypointTemp.canalMarker);
				Marker taweMarker = mMap.addMarker(WaypointTemp.taweMarker);

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
						showInfoViewDialog();
						// Display correct information in the thing.
					}
				});

				// In practice we may want to set the bounds to be that of the area we are walking in.
				// for now I'm just going to zoom in this close cause I can.
				mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WaypointTemp.riverTawe, 15)); 
			}
		}
	}

	// Has to be a class or Android won't let it happen...
	/**
	 * 
	 * @author Lewis Hancock
	 *
	 */
	public static class InfoViewDialogFragment extends DialogFragment {

		static InfoViewDialogFragment newInstance() {
			InfoViewDialogFragment dialog = new InfoViewDialogFragment();
			return dialog;
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

	//TODO: Use a Join to get the data we need, currently we can only get lat and long and visit_order.
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Uri baseUri;
		baseUri = WhiteRockContract.Waypoint.CONTENT_URI;
		String select = "((" + WhiteRockContract.Waypoint.WALK_ID + " == " + args.getInt("walkId") + "))";
		// PROJECT_ALL for reference {ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER, WALK_ID, USER_ID};
		return new CursorLoader(this, baseUri, WhiteRockContract.Waypoint.PROJECTION_ALL, select, null, WhiteRockContract.Waypoint.VISIT_ORDER + " COLLATE LOCALIZED ASC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// nothing on resume if possible.
		// create markers from all the waypoint info.
	
		if (data != null && data.moveToFirst()) {
			data.moveToPosition(-1); // put us back to the -1 position so we can do the while() loop properly
			while (data.moveToNext()) {
				Log.d(TAG, "Visit Order: " + data.getInt(4));
				Double lat = data.getDouble(1);
				Double longitude = data.getDouble(2);
				int isReq = data.getInt(3);
				int visitOrder = data.getInt(4);
				if (isReq != 1) {
					mMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(lat, longitude)).title("Waypoint " + visitOrder).snippet("Placeholder text for Waypoint " + visitOrder)));
				}
			}
		}
		// In practice we may want to set the bounds to be that of the area we are walking in.
		// for now I'm just going to zoom in this close cause I can.
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMarkers.get(0).getPosition(), 15)); 
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}


} 