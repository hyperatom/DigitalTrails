package uk.ac.swan.digitaltrails.sync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import uk.ac.swan.digitaltrails.components.Account;
import uk.ac.swan.digitaltrails.components.Audio;
import uk.ac.swan.digitaltrails.components.Media;
import uk.ac.swan.digitaltrails.components.Photo;
import uk.ac.swan.digitaltrails.components.Video;
import uk.ac.swan.digitaltrails.components.Walk;
import uk.ac.swan.digitaltrails.components.Waypoint;
import uk.ac.swan.digitaltrails.utils.HTTP;

/**
 * Access server to get remote data from database.
 * Helps to encapsulate actions against server.
 * @author Lewis Hancock
 * @author Tom Milner
 *
 */
/**
 * @author Lewis Hancock
 *
 */
public class WhiteRockServerAccessor {
	// TODO: Add error codes.
	/**
	 * 
	 */
	private static final String TAG = "Server Accessor";
	/**
	 * 
	 */
	private Account mAccount;
	
	/**
	 * @param account
	 */
	public WhiteRockServerAccessor(Account account){
		mAccount = account;
	}
	
	/**
	 * 
	 */
	public WhiteRockServerAccessor() {
		
	}
	
	/**
	 * Method which gets all walks.
	 * @throws Exception
	 */
	/**
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Walk> getWalks() throws Exception {
		
		String response = HTTP.get(HTTP.BASEURL+"/walks");
		
		Log.d(TAG, response);
       
		Gson gson = new GsonBuilder().create();

        return gson.fromJson(response, new TypeToken<List<Walk>>(){}.getType());
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	/**
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Walk getWalk(int id) throws Exception {
		
		String response = HTTP.get(HTTP.BASEURL+"/walks/"+id);
       
		Log.d(TAG, response);

		Gson gson = new GsonBuilder().create();

        return gson.fromJson(response, Walk.class);
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	/**
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public ArrayList<Walk> searchForWalk(String query) throws Exception {
		
		String response = HTTP.get(HTTP.BASEURL+"/walks/search/"+query);

		Log.d(TAG, response);
		
		Gson gson = new GsonBuilder().create();

		return gson.fromJson(response, new TypeToken<List<Walk>>(){}.getType());
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	/**
	 * @param walk
	 * @return
	 * @throws Exception
	 */
	public Walk addWalk(Walk walk) throws Exception {
		
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(walk);
		
		String response = HTTP.securePost(message, HTTP.BASEURL+"/walks", mAccount);

		Log.d(TAG, response);
		
		return gson.fromJson(response, Walk.class);
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	/**
	 * @param walk
	 * @return
	 * @throws Exception
	 */
	public Walk updateWalk(Walk walk) throws Exception {
		
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(walk);
		
		String response = HTTP.securePut(message, HTTP.BASEURL+"/walks/"+walk.getId(), mAccount);

		Log.d(TAG, response);
		
		return gson.fromJson(response, Walk.class);
	}
	
	//NOT WORKING....
	/**
	 * @param media
	 * @return
	 */
	public Media uploadMedia(Media media){
		
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(media);
		
		Waypoint waypoint = media.getWaypoint();
		
		File file = new File(media.getFileLocation());
		
		String response = HTTP.postFile(HTTP.BASEURL+"/walks/"+waypoint.getWalkId()+"/waypoints/"+waypoint.getId()+"/image", mAccount, file, message);

		Log.d(TAG, response);
		
		return gson.fromJson(response, Photo.class);
	}
	
	//TODO: add more get methods as necessary

}
