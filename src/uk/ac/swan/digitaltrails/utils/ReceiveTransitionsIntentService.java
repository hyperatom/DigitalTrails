package uk.ac.swan.digitaltrails.utils;

import java.util.List;

import uk.ac.swan.digitaltrails.activities.MapActivity;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Lewis Hancock
 * Deals with entering and exiting geofences.
 */
public class ReceiveTransitionsIntentService extends IntentService {
	
	/**
	 * Constructor
	 * Sets identifier for the service
	 */
	public ReceiveTransitionsIntentService() {
		super("ReceiveTransitionsIntentService");
		Log.d("ReceiveTransitionsIntentService", "Alive");
		// TODO Auto-generated constructor stub
	}

	/**
	 * Handles incoming intents.
	 * @param intent The intent sent by Location Services. 
	 */
	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		// Check for errors
		if (LocationClient.hasError(intent)) {
			int errorCode = LocationClient.getErrorCode(intent);
			Log.e("ReceiveTransitionsIntentService", "Location Services Error: " + Integer.toString(errorCode));
		} else {
			// No error. Deal with the geofence that triggered this.
			int transitionType = LocationClient.getGeofenceTransition(intent);
			if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
				List<Geofence>  triggerList = LocationClient.getTriggeringGeofences(intent);
				String[] triggerIds = new String[triggerList.size()];
				
				for (int i = 0; i < triggerIds.length; i++) {
					triggerIds[i] = triggerList.get(i).getRequestId();
				}
				
				for (int i = 0; i < triggerIds.length; i++) {
					Log.d("ReceiveTransitionsIntentService", "This geofence has been entered: " + triggerIds[i]);
				}
				
				Intent triggeredIntent = new Intent(MapActivity.RECEIVE_GEOFENCES_ENTERED);
				triggeredIntent.putExtra(MapActivity.ARG_GEOFENCES, triggerIds);
				LocalBroadcastManager.getInstance(this).sendBroadcast(triggeredIntent);
			} else if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
				List<Geofence>  triggerList = LocationClient.getTriggeringGeofences(intent);
				String[] triggerIds = new String[triggerList.size()];
				Intent triggeredIntent = new Intent(MapActivity.RECEIVE_GEOFENCES_EXITED);
				triggeredIntent.putExtra(MapActivity.ARG_GEOFENCES, triggerIds);
				LocalBroadcastManager.getInstance(this).sendBroadcast(triggeredIntent);

			} else {
				Log.e("ReceiveTransitionsIntentService", "Geofence Transition Error: " + Integer.toString(transitionType));
			}
		}
		
	}


}
