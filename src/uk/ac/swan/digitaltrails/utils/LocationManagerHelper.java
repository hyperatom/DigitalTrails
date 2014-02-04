package uk.ac.swan.digitaltrails.utils;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

// Pretty sure this can be removed when we get google maps installed.,
public class LocationManagerHelper implements LocationListener {

	private static double latitude;
	private static double longitude;

	@Override
	public void onLocationChanged(Location loc) {
		latitude = loc.getLatitude();
		longitude = loc.getLongitude();
		this.sendPosition();
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO onStatusChanged

	}

	//TODO: Fix intents.
	private void sendPosition() {
		Intent intent = new Intent("locationUpdated");
		// You can also include some extra data.
		intent.putExtra("lat", latitude);
		intent.putExtra("lon", longitude);

		// LocalBroadcastManager.sendBroadcast(intent);

	}

	public static double getLatitude() {
		return latitude;
	}

	public static double getLongitude() {
		return longitude;
	}

}
