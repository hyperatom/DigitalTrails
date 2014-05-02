package uk.ac.swan.digitaltrails.accounts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.swan.digitaltrails.components.Account;
import uk.ac.swan.digitaltrails.utils.HTTP;
import android.util.Log;

/**
 * Communicates with DigitalTrails website
 * 
 */
public class DigitalTrailsComServerAuthenticate implements ServerAuthenticate {

	private static final String TAG = "Comunicator";

	@Override
	public Account userSignUp(String firstName, String lastName, String email,
			String pass, String authType) throws Exception {

		Gson gson = new GsonBuilder().create();
		
		Account account = new Account(email,pass,firstName,lastName);
		
		String message = gson.toJson(account);

		String response = HTTP.post(message, HTTP.BASEURL+"/users");
		
		Log.d(TAG, response);
	    
		Account loggedIn = gson.fromJson(response, Account.class);
	    
		return loggedIn;
	}

	@Override
	public String userSignIn(String user, String pass, String authType)
			throws Exception {
		
		Account account = new Account(user,pass);
	
		Gson gson = new GsonBuilder().create();
		
		String message = gson.toJson(account);
					
		String response = HTTP.post(message, HTTP.BASEURL+"/session");
		
		Log.d(TAG, response);
					        
	    Account loggedIn = gson.fromJson(response, Account.class);
		Log.d(TAG, "pass = " + loggedIn.password);
	    return loggedIn.password;

	}
}
