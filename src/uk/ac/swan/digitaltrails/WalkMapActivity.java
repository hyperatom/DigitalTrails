/**
 * 
 * Android Activity for maps.
 * 
 * 
 */

package uk.ac.swan.digitaltrails;

import java.io.IOException;
import java.util.ArrayList;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.utils.DatabaseHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;



//TODO: remove OSM references, add Google Maps.
import org.osmdroid.bonuspack.overlays.ExtendedOverlayItem;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTileProviderArray;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;

public class WalkMapActivity extends Activity {

	int currentPointID = 0;
	Walk thisWalk = new Walk();
	ArrayList<WalkPoint> walkPoints = new ArrayList<WalkPoint>();
	ArrayList<Integer> walkPointIDsVisited = new ArrayList<Integer>();
	double currentLat = 52.2435;
	double currentLong = -4.26323;
	private MapController mapController;
	private MapView mapView;
	LocationManager locationManager;
	DatabaseHandler dbHandle;
	private MapTileProviderArray mapProvider;
	private ResourceProxy resourceProxy;
	LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			String infoText = new String();
			switch (status) {
			case 1:
				infoText = ("GPS available");
				break;
			case 2:
				infoText = "GPS temporarily unavailable";
				break;
			}
			// Toast.makeText(getApplicationContext(), infoText,
			// Toast.LENGTH_LONG).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			// Toast.makeText(getApplicationContext(),
			// "Enabled new provider " + provider,
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		// TODO: minimise GPS updating to save battery life.
		public void onLocationChanged(Location location) {
			// if this is a gps location, we can use it
			if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {

			}
			currentLat = location.getLatitude();
			currentLong = location.getLongitude();

			Log.i("WalkMapActivity", "current Lat: " + currentLat
					+ " current Long:" + currentLong);
			// GeoPoint centre = convertToGeoPoint(currentLat,
			// currentLong);

			// mapController.setCenter(centre);
			locationChanged();
		}
	};

	// TODO: GeoPoints are an OSM type, find Google Maps equivalent.
	private GeoPoint convertToGeoPoint(double lat, double lon) {
		return new GeoPoint((int) (lat * 1E6), (int) (lon * 1E6));
	}

	// Waypoint icons?
	private Drawable createMarkerIcon(int markerNumber, int width, int height,
			boolean visited) {
		width = 24;
		height = 24;
		Drawable markerImg;
		if (visited) {
			markerImg = this.getResources().getDrawable(R.drawable.donepoint);
		} else {
			markerImg = this.getResources().getDrawable(R.drawable.todopoint);

		}

		Bitmap canvasBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// Create a canvas, that will draw on to canvasBitmap.
		Canvas imageCanvas = new Canvas(canvasBitmap);

		// Set up the paint for use with our Canvas
		Paint imagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		imagePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		imagePaint.setTextAlign(Align.CENTER);
		imagePaint.setTextSize(15f);
		// Typeface tf = Typeface.create("Helvetica", 0);
		// imagePaint.setTypeface(tf);
		// Draw the image to our canvas
		markerImg.draw(imageCanvas);

		// Draw the text on top of our image
		imageCanvas.drawText(String.valueOf(markerNumber), (width / 2),
				(height / 2) + 5, imagePaint);
		// Combine background and text to a LayerDrawable

		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {
				markerImg, new BitmapDrawable(canvasBitmap) });
		return layerDrawable;
	}

	// Draws waypoints, updated when visited.
	public ArrayList<ExtendedOverlayItem> drawMarkers() {
		ArrayList<ExtendedOverlayItem> markerPoints = new ArrayList<ExtendedOverlayItem>();
		int markerNum = 0;
		for (WalkPoint thisPoint : walkPoints) {
			markerNum++;

			Drawable thisItem;

			if (walkPointIDsVisited.contains(Integer.valueOf(markerNum - 1))) {

				thisItem = createMarkerIcon(markerNum, 24, 24, true);
			} else {
				thisItem = createMarkerIcon(markerNum, 24, 24, false);

			}
			ExtendedOverlayItem myLocationOverlayItem = new ExtendedOverlayItem(
					thisPoint.getTitle(), "Tap and hold for info...",
					convertToGeoPoint(thisPoint.getLat(), thisPoint.getLon()),
					getApplicationContext());
			myLocationOverlayItem.setMarker(thisItem);
			markerPoints.add(myLocationOverlayItem);
		}

		return markerPoints;

	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.walkmap);
		Intent i = getIntent();
		int walkID = i.getIntExtra("walkId", 0);
		DatabaseHandler dbHandle = new DatabaseHandler(this);
		try {
			dbHandle.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			dbHandle.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}

		thisWalk = dbHandle.getWalkID(walkID);

		walkPoints = dbHandle.getWalkPointsForWalkID(walkID);

		Log.i("WalkMapActivity", "There were " + walkPoints.size()
				+ " points loaded.");
		dbHandle.close();

		mapView = (MapView) findViewById(R.id.mapview);

		// can use this for testing when no internet is available - offline maps
		// etc.
		// mapView.setUseDataConnection(false);

		// mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
		String fileName = "AberaeronTiles.zip";
		// File destinationFile = new
		// File(Environment.getExternalStorageDirectory()+"/osmdroid/" +
		// fileName);

		// XYTileSource TILERENDERER = new XYTileSource("MapQuest",
		// ResourceProxy.string.offline_mode, 14, 18, 256, ".jpg",
		// "http://127.0.0.1");
		// this.mapView.setTileSource(TILERENDERER);

		// SimpleRegisterReceiver simpleReceiver = new
		// SimpleRegisterReceiver(this.getBaseContext());
		// IArchiveFile[] archives = {
		// ArchiveFileFactory.getArchiveFile(destinationFile) };
		// MapTileModuleProviderBase moduleProvider = new
		// MapTileFileArchiveProvider(
		// simpleReceiver,
		// TILERENDERER,
		// archives);

		// this.mapProvider = new MapTileProviderArray(TILERENDERER, null, new
		// MapTileModuleProviderBase[] { moduleProvider });
		// this.mapView.setUseDataConnection(false);
		// this.mapView = new MapView(this, 256, this.resourceProxy,
		// this.mapProvider);
		// this.mapView.setUseDataConnection(false);
		
		mapView.setBuiltInZoomControls(true);
		mapView.setMultiTouchControls(true);

		mapController = mapView.getController();
		mapController.setZoom(18);

		updateMapPoints();

		WalkPoint firstWp = walkPoints.get(0);

		mapController.setCenter(convertToGeoPoint(firstWp.getLat(),
				firstWp.getLon()));

		// topLabel.setText(thisWalk.getTitle());
		// descText.setText(thisWalk.getDesc());
		Log.i("walkmapactivity", "onCreate complete");
	}

	@Override
	protected void onResume() {
		super.onResume();

		// enable the location service

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!enabled) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);
		}

		Location lastKnownLocation = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastKnownLocation != null) {
			currentLat = lastKnownLocation.getLatitude();
			currentLong = lastKnownLocation.getLongitude();
		}
		Log.i("walkmapactivity", "after last known location");

		locationChanged();

		// updates every 5 secs
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 0, locationListener);

	}

	@Override
	protected void onPause() {
		super.onPause();

		// disable the location service
		locationManager.removeUpdates(locationListener);

	}

	private void updateMapPoints() {
		Log.i("walkmapactivity", "updateMapPoints");

		GeoPoint currentPos = convertToGeoPoint(currentLat, currentLong);

		// setup the you are here marker

		ExtendedOverlayItem myLocationOverlayItem = new ExtendedOverlayItem(
				"You are here", "", currentPos, getApplicationContext());
		Drawable myCurrentLocationMarker = this.getResources().getDrawable(
				R.drawable.currentloc);
		myLocationOverlayItem.setMarker(myCurrentLocationMarker);
		ResourceProxy resourceProxy = new DefaultResourceProxyImpl(
				getApplicationContext());
		// setup the other markers
		final ArrayList<ExtendedOverlayItem> items = drawMarkers();
		items.add(myLocationOverlayItem);
		ItemizedOverlayWithFocus<ExtendedOverlayItem> currentLocationOverlay = new ItemizedOverlayWithFocus<ExtendedOverlayItem>(
				items,
				new ItemizedIconOverlay.OnItemGestureListener<ExtendedOverlayItem>() {
					public boolean onItemSingleTapUp(final int index,
							final ExtendedOverlayItem item) {
						// Toast.makeText(getApplicationContext(),
						// walkPoints.get(index).getTitle(),
						// Toast.LENGTH_SHORT).show();
						if (index < walkPoints.size()) { // if it's not the
															// current location
															// marker
							mapController.animateTo(walkPoints.get(index)
									.getLat(), walkPoints.get(index).getLon());
						} else {

						}
						// Log.i("mapView","tapped index:"+walkPoints.get(index).getTitle());
						return true;
					}

					public boolean onItemLongPress(final int index,
							final ExtendedOverlayItem item) {
						if (index < walkPoints.size()) {
							displayInfo(walkPoints.get(index).getId(),
									thisWalk.getId()); // display
							// info
							// screen,
							// passing
							// global
							// point
							// id
						}
						return true;
					}

				}, resourceProxy);
		// ItemizedOverlayWithBubble<ExtendedOverlayItem> bubblePoints = new
		// ItemizedOverlayWithBubble<ExtendedOverlayItem>(this, items, mapView);
		currentLocationOverlay.setFocusItemsOnTap(true);

		// bubblePoints.setFocusItemsOnTap(true);
		this.mapView.getOverlays().clear();
		this.mapView.getOverlays().add(currentLocationOverlay);
		mapView.invalidate(); // redraw the map
		// this.mapView.getOverlays().add(bubblePoints);
		Log.i("walkmapactivity", "updateMapPoints complete");

	}

	private double calculateDistance(WalkPoint point1, WalkPoint point2) {
		double lat1 = point1.getLat();
		double lon1 = point1.getLon();
		double lat2 = point2.getLat();
		double lon2 = point2.getLon();

		// radius of the earth in km
		int earthrad = 6371;

		// get lat/lon of both input points

		double dLat = deg2rad(lat2 - lat1);
		double dLon = deg2rad(lon2 - lon1);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = earthrad * c;

		/*
		 * 
		 * Location location1 = new Location("");
		 * 
		 * location1.setLatitude(lat1); location1.setLongitude(lon1); Location
		 * location2 = new Location(""); location2.setLatitude(lat2);
		 * location2.setLongitude(lon2);
		 * 
		 * float e=location1.distanceTo(location2); e=e/1000; //convert to km
		 */
		return d;
	}

	private double deg2rad(double deg) {
		return deg * (Math.PI / 180);
	}

	// to be called upon each location change to update view.
	private void locationChanged() {
		Log.i("walkmapactivity", "location changed");

		TextView descText = (TextView) findViewById(R.id.textView2);
		WalkPoint currentPosition = new WalkPoint();
		currentPosition.setLat(currentLat);
		currentPosition.setLon(currentLong);

		if (thisWalk.points != null) {
			WalkPoint firstPoint = walkPoints.get(0);
			double theDist = calculateDistance(firstPoint, currentPosition);
			descText.setText("\nDistance to first point is " + theDist + "m");
		}
		WalkPoint currentPoint = walkPoints.get(currentPointID);

		// this method will calculate how close the user is to the next point on
		// the list
		double distanceToNext = calculateDistance(currentPoint, currentPosition);

		if (distanceToNext < 0.005) { // if we are 5m from the next point, then
										// that means we are at it
			// display info here if it hasn't already been displayed....
			// show info screen

			// check to see if point has been visited yet.
			if (!walkPointIDsVisited.contains(Integer.valueOf(currentPointID))) {

				// get global point ID of the current point
				displayInfo(walkPoints.get(currentPointID).getId(),
						thisWalk.getId());

				// add local array index for the visited point to the visited
				// array
				walkPointIDsVisited.add(Integer.valueOf(currentPointID));

				// increment current point to next point when point is visited
				if (currentPointID + 1 < walkPoints.size()) {// dont increment
																// beyond index
																// range
					currentPointID++;
					mapController.animateTo(walkPoints.get(currentPointID)
							.getLat(), walkPoints.get(currentPointID).getLon());

				} else {

				}
				if (currentPointID == walkPoints.size() - 1) // last point
					Toast.makeText(getApplicationContext(), "Walk complete!",
							Toast.LENGTH_LONG).show();

			}

		}

		updateMapPoints();

	}

	private void displayInfo(int walkPointID, int walkID) {
		Intent i = new Intent(WalkMapActivity.this, InfoActivity.class);
		i.putExtra("locationID", walkPointID);
		i.putExtra("walkID", walkID);

		Log.i("", "displaying info of point id " + walkPointID);
		startActivity(i);
	}

	@Override
	protected void onDestroy() {
		// Unregister since the activity is about to be closed.
		// This is somewhat like [[NSNotificationCenter defaultCenter]
		// removeObserver:name:object:]
		// LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
}
