package uk.ac.swan.digitaltrails.accounts;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

/**
 * Communicates with DigitalTrails website
 * 
 */
public class DigitalTrailsComServerAuthenticate implements ServerAuthenticate {
	
	private static final String TAG = "Comunicator";
	
	@Override
	public String userSignUp(String firstName, String lastName, String email,
			String pass, String authType) throws Exception {
		
		String salt = "";
		
		int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams);

		//Get a salt
		HttpGet saltRequest = new HttpGet("http://whiterockapi.tmilner.co.uk/session/salt");
		HttpResponse saltResponse = client.execute(saltRequest);
		String responseText = EntityUtils.toString(saltResponse.getEntity());

		try {
	        JSONObject jObj = new JSONObject(responseText);
	        salt = jObj.getString("salt");
	    } catch (JSONException e) {
	        Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }
		
		//Build the user object.
		JSONObject user = new JSONObject();
		
		String authToken = computeAuthToken(pass, salt);
		
		user.put("first_name", firstName);
		user.put("last_name", lastName);
		user.put("email", email);
		user.put("password", authToken);
		user.put("salt", salt);
		
		String json = user.toString();

		HttpPost request = new HttpPost("http://whiterockapi.tmilner.co.uk/users");
		request.setEntity(new StringEntity(json));
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json; charset=UTF-8");

		HttpResponse response = client.execute(request);
		StatusLine status = response.getStatusLine();
		
		
		if(status.getStatusCode() == HttpStatus.SC_OK){
			return authToken;
		}else{
			return null;
		}
	}

	@Override
	public String userSignIn(String user, String pass, String authType)
			throws Exception {
		
		String authToken = null;
		String timestamp = ""+System.currentTimeMillis();
		String message = user+timestamp;
		String salt = "";
		String hash = "";
		
		
		//Setup a HTTP client.
		int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
		HttpClient client = new DefaultHttpClient(httpParams);

		
		
		//Get a salt.
		HttpGet saltRequest = new HttpGet("http://whiterockapi.tmilner.co.uk/session/salt/"+user);
		HttpResponse saltResponse = client.execute(saltRequest);
		String json = EntityUtils.toString(saltResponse.getEntity());

		try {
	        JSONObject jObj = new JSONObject(json);
	        salt = jObj.getString("salt");
	    } catch (JSONException e) {
	        Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }
	    
		
		
		
		//Get AuthToken and Hash
		try{
			authToken = computeAuthToken(pass, salt);
			hash = computeSignature(message, authToken);
		}catch(Exception e){
			e.printStackTrace();
		}

		
		//Send auth check request.
		HttpGet request = new HttpGet("http://whiterockapi.tmilner.co.uk/session");
		request.setHeader("X-EMAIL", user);
		request.setHeader("X-TIMESTAMP", timestamp);
		request.setHeader("X-HASH", hash);
		
		Log.d(TAG, "HMAC: " + hash + ", Data: " + message);

		
		HttpResponse response = client.execute(request);
		StatusLine status = response.getStatusLine();
		
		
		if(status.getStatusCode() == HttpStatus.SC_OK){
			return authToken;
		}else{
			return null;
		}
	}

	private static String computeAuthToken(String password, String salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec keyspec = new PBEKeySpec(password.toCharArray(),salt.getBytes(),1000,(256));
		SecretKey secret = factory.generateSecret(keyspec);
		
		StringBuilder string = new StringBuilder();
	    for (byte b : secret.getEncoded()) {
	        String hexString = Integer.toHexString(0x00FF & b);
	        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
	    }
	    
	    String authToken = string.toString();
		
		Log.d(TAG, "Auth: " + authToken + ", SaltedPassword: " + password+salt);
		
		return authToken;
	}
	
	private static String computeSignature(String message, String key) throws GeneralSecurityException, UnsupportedEncodingException {
        // Get an hmac_sha1 key from the raw key bytes
        byte[] keyBytes = key.getBytes();           
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        // Get an hmac_sha1 Mac instance and initialize with the signing key
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);

        // Compute the hmac on input data bytes
        byte[] rawHmac = mac.doFinal(message.getBytes());

        StringBuilder string = new StringBuilder();
	    for (byte b : rawHmac) {
	        String hexString = Integer.toHexString(0x00FF & b);
	        string.append(hexString.length() == 1 ? "0" + hexString : hexString);
	    }
	    
	    return string.toString();
	    
	}
}
