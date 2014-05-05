package uk.ac.swan.digitaltrails.accounts;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.swan.digitaltrails.R;
import uk.ac.swan.digitaltrails.components.Account;
import uk.ac.swan.digitaltrails.utils.GlobalFlags;
import uk.ac.swan.digitaltrails.utils.HTTP;
import uk.ac.swan.digitaltrails.utils.WhiteRockApp;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author Lewis Hancock
 * Communicates with DigitalTrails website
 */
public class DigitalTrailsComServerAuthenticate implements ServerAuthenticate {

	/**
	 * Static tag for the class
	 */
	private static final String TAG = "Comunicator";
	
	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.accounts.ServerAuthenticate#userSignUp(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Account userSignUp(String firstName, String lastName, String email,
			String pass, String authType) throws Exception {

		Gson gson = new GsonBuilder().create();
		
		String check = HTTP.get(HTTP.BASEURL+"/users/emailcheck/"+email);
		
		JSONObject checkObj = new JSONObject(check);
		
		//If the email is already registered.
		if(checkObj.getBoolean("exists") == true){
			Account acc =  new Account();
			acc.email = "Email Already Exists";
			return acc;
		}
		
		Account account = new Account(email,pass,firstName,lastName);
		
		String message = gson.toJson(account);

		String response = HTTP.post(message, HTTP.BASEURL+"/users");
		
		Log.d(TAG, response);
	    
		Account loggedIn = gson.fromJson(response, Account.class);
		SharedPreferences settings = WhiteRockApp.getInstance().getSharedPreferences(GlobalFlags.PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("userId", loggedIn.id);
		editor.commit();
		Log.d(TAG, "userId logged in: " + settings.getInt("userId", -99));

		return loggedIn;
	}

	/* (non-Javadoc)
	 * @see uk.ac.swan.digitaltrails.accounts.ServerAuthenticate#userSignIn(java.lang.String, java.lang.String, java.lang.String)
	 */
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
		SharedPreferences settings = WhiteRockApp.getInstance().getSharedPreferences(GlobalFlags.PREF_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("userId", loggedIn.id);
		editor.commit();
		Log.d(TAG, "userId logged in: " + settings.getInt("userId", -99));
		// TODO: remove id on logout
	    return loggedIn.password;
	}
}
