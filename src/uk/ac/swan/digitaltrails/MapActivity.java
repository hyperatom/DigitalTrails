package uk.ac.swan.digitaltrails;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapActivity extends FragmentActivity {
	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Intent intent = getIntent();
		
		initMap();
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
	}
	
	private void createMarkers(ArrayList<Marker> markers) {
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
	}
	
	private void resumeMap() {
		setDefaultMapConfig();
	}

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
			builder.setMessage(R.string.dialog_info_view)
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