package uk.ac.swan.digitaltrails.sync;

import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Access server to get remote data from database.
 * Helps to encapsulate actions against server.
 * @author Lewis Hancock
 * @author Tom Milner
 *
 */
public class WhiteRockServerAccessor {
	
	/**
	 * Method which gets all walks.
	 * @param auth
	 * @throws Exception
	 */
	public void getWalks(String auth) throws Exception {
		DefaultHttpClient httpClient = new DefaultHttpClient();
	}
	
	//TODO: add more get methods as necessary

}
