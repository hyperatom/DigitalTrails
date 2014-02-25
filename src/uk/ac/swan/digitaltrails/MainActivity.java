package uk.ac.swan.digitaltrails;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
public class MainActivity extends FragmentActivity {
  private GoogleMap map;
  private final boolean TEST = true;
  
@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (TEST){
    	try {
	    	initMapWhiteRock();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    } else {
	    try {
	    	initMap();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
    }
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
		  if (map != null) {
			  map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		  } else {
			  Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
			  return false;
		  }
		  return true;
	  case R.id.hybrid_map:
		  if (map != null) {
			  map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		  } else {
			  Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
			  return false;
		  }
		  return true;
	  case R.id.satellite_map:
		  if (map != null) {
			  map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
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
	  if (TEST) {
		 initMapWhiteRock(); // testing only
	  } else {
		  initMap();
	  }
  }
  
  @Override
  protected void onPause() {
	  super.onPause();
  }

  private void initMap() {
	  if (map == null) {
		  map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		  if (map == null) {
			  Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG).show();
		  }
	  }
  }
  
  private void initMapWhiteRock() {

	  if (map == null) {
		  map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		  if (map == null) {
			  Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG).show();
		  } else {
			  map.getUiSettings().setCompassEnabled(true);
			  map.getUiSettings().setMyLocationButtonEnabled(true);
			  map.setMyLocationEnabled(true);
			  map.getUiSettings().setZoomControlsEnabled(true);
			  map.getUiSettings().setZoomGesturesEnabled(true);
			  map.getUiSettings().setRotateGesturesEnabled(true);
			  // add markers
			  Marker dockMarker = map.addMarker(WaypointTemp.dockMarker);
			  Marker canalTunnelMarker = map.addMarker(WaypointTemp.cananlTunnelMarker);
			  Marker canalMarker = map.addMarker(WaypointTemp.canalMarker);
			  Marker taweMarker = map.addMarker(WaypointTemp.taweMarker);

			  map.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					return false;
				}
			  
			  });
			  
			  map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				@Override
				public void onInfoWindowClick(Marker marker) {
					showInfoViewDialog();
					// Display correct information in the thing.
				}
			  });
			 
			  // In practice we may want to set the bounds to be that of the area we are walking in.
			  // for now I'm just going to zoom in this close cause I can.
			  map.moveCamera(CameraUpdateFactory.newLatLngZoom(WaypointTemp.riverTawe, 15)); 
		  }
	  }
  }
  
  // Has to be a static inner class or Android won't let it happen...
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