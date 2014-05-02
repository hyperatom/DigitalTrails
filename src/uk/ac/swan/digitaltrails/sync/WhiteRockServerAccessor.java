package uk.ac.swan.digitaltrails.sync;

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
import uk.ac.swan.digitaltrails.utils.HTTP;

/**
 * Access server to get remote data from database.
 * Helps to encapsulate actions against server.
 * @author Lewis Hancock
 * @author Tom Milner
 *
 */
public class WhiteRockServerAccessor {
	// TODO: Add error codes.
	private Account mAccount;
	
	public WhiteRockServerAccessor(Account account){
		mAccount = account;
	}
	
	public WhiteRockServerAccessor() {
		
	}
	
	/**
	 * Method which gets all walks.
	 * @throws Exception
	 */
	public ArrayList<Walk> getWalks() throws Exception {
		
		String response = HTTP.get(HTTP.BASEURL+"/walks");
       
		Gson gson = new GsonBuilder().create();

        return gson.fromJson(response, new TypeToken<List<Walk>>(){}.getType());
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	public Walk getWalk(int id) throws Exception {
		
		String response = HTTP.get(HTTP.BASEURL+"/walks/"+id);
       

		Gson gson = new GsonBuilder().create();

        return gson.fromJson(response, Walk.class);
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	public ArrayList<Walk> searchForWalk(String query) throws Exception {
		
		String response = HTTP.get(HTTP.BASEURL+"/walks/search/"+query);

		Gson gson = new GsonBuilder().create();

		return gson.fromJson(response, new TypeToken<List<Walk>>(){}.getType());
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	public Walk addWalk(Walk walk) throws Exception {
		
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(walk);
		
		String response = HTTP.securePost(message, HTTP.BASEURL+"/walks", mAccount);

		return gson.fromJson(response, Walk.class);
	}
	
	/**
	 * Method which gets a walk.
	 * @param id the walk ID to get. 
	 * @throws Exception
	 */
	public Walk updateWalk(Walk walk) throws Exception {
		
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(walk);
		
		String response = HTTP.securePut(message, HTTP.BASEURL+"/walks/"+walk.getId(), mAccount);

		return gson.fromJson(response, Walk.class);
	}
	
	//NOT WORKING....
	public Media uploadMedia(Media media){
		
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(media);
		
		String response = HTTP.securePut(message, HTTP.BASEURL+"/walks/", mAccount);

		return gson.fromJson(response, Photo.class);
	}
	
	//TODO: add more get methods as necessary

}
