package uk.ac.swan.digitaltrails;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    //getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
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
			  Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG);
		  }
		  
	  }
  }
  
  private void initMapWhiteRock() {
	  // hard coding for demonstration purposes.
	  LatLng theDock = new LatLng(51.635113, -3.93349);
	  LatLng smithCanalTunnel = new LatLng(51.634518, -3.932912);
	  LatLng smithCanal = new LatLng(51.636261, -3.932907);
	  LatLng riverTawe = new LatLng(51.635872, -3.933305);
	  
	  if (map == null) {
		  map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		  if (map == null) {
			  Toast.makeText(getApplicationContext(), "Can't make the map", Toast.LENGTH_LONG);
		  } else {
			  // add markers
			  Marker dockMarker = map.addMarker(new MarkerOptions().position(theDock).title("The Dock"));
			  Marker cananlTunnelMarker = map.addMarker(new MarkerOptions().position(smithCanalTunnel).title("Smith Canal Tunnel"));
			  Marker canalMarker = map.addMarker(new MarkerOptions().position(smithCanal).title("Smith Canal"));
			  Marker taweMarker = map.addMarker(new MarkerOptions().position(riverTawe).title("River Tawe"));
			  map.getUiSettings().setCompassEnabled(false);
			  map.getUiSettings().setMyLocationButtonEnabled(true);
			  map.getUiSettings().setZoomControlsEnabled(false);
			  map.getUiSettings().setZoomGesturesEnabled(true);
			  map.getUiSettings().setRotateGesturesEnabled(true);
		  }
	  }
	  
  }
  
} 