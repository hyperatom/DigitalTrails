package uk.ac.swan.digitaltrails;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {
  private GoogleMap map;
  private boolean test = true;
  
@Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (test){
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
  
  private boolean changeMap(MenuItem item) {
	  switch (item.getItemId()) {
	  case R.id.normalMap:
		  if (map != null) {
			  map.setMapType(map.MAP_TYPE_NORMAL);
		  } else {
			  Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
			  return false;
		  }
		  return true;
	  case R.id.hybridMap:
		  if (map != null) {
			  map.setMapType(map.MAP_TYPE_HYBRID);
		  } else {
			  Toast.makeText(getBaseContext(), "Can't change map type", Toast.LENGTH_SHORT).show();
			  return false;
		  }
		  return true;
	  case R.id.satelliteMap:
		  if (map != null) {
			  map.setMapType(map.MAP_TYPE_SATELLITE);
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
	  if (test) {
		 initMapWhiteRock(); // testing only
	  } else {
		  initMap();
	  }
  }

  private void initMap() {
	  if (map == null) {
		  map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		  if (map == null) {
			  Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG).show();
		  }
		  
	  }
  }
  
  private void initMapWhiteRock() {

	  if (map == null) {
		  map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
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
			  PolylineOptions walkOrderOptions = new PolylineOptions()
			  			.add(dockMarker.getPosition())
			  			.add(canalTunnelMarker.getPosition())
			  			.add(canalMarker.getPosition())
			  			.add(taweMarker.getPosition())
			  			.width(10)
			  			.color(Color.BLUE)
			  			.geodesic(true);
			  //Polyline walkOrder = map.addPolyline(walkOrderOptions);
			  map.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
					return false;
				}
			  
			  });
			 
			  // In practice we want to set the bounds to be that of the area we are walking in.
			  // for now I'm just going to zoom in this close cause I can.
			  map.moveCamera(CameraUpdateFactory.newLatLngZoom(WaypointTemp.riverTawe, 15)); 
		  }
	  }
	  
  }
  
} 