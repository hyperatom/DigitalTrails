package uk.ac.swan.digitaltrails;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public final class WaypointTemp {

	public static final LatLng theDock = new LatLng(51.635113, -3.93349);
	public static final LatLng smithCanalTunnel = new LatLng(51.634518, -3.932912);
	public static final LatLng smithCanal = new LatLng(51.636261, -3.932907);
	public static final LatLng riverTawe = new LatLng(51.635872, -3.933305);
	public static final MarkerOptions dockMarker = (new MarkerOptions()
													.position(theDock)
													.title("The Dock")
													.snippet("Short description of the dock"));
	public static final MarkerOptions cananlTunnelMarker = (new MarkerOptions().position(smithCanalTunnel).title("Smith Canal Tunnel"));
	public static final MarkerOptions canalMarker = (new MarkerOptions().position(smithCanal).title("Smith Canal"));
	public static final MarkerOptions taweMarker = (new MarkerOptions().position(riverTawe).title("River Tawe"));

	
}
