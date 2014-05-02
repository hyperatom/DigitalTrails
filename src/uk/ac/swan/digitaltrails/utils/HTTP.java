/**
 * 
 */
package uk.ac.swan.digitaltrails.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import uk.ac.swan.digitaltrails.components.Account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.util.Log;

/**
 * @author Thomas Milner
 *
 */
public class HTTP {

	public static final String TAG = "SecureHTTP";
	
	public static final String BASEURL = "http://whiterockapi.tmilner.co.uk";
	
	private static final HttpClient mClient = new DefaultHttpClient();
	
	public static enum Type {
		GET,POST,PUT,DELETE
	};

	public static String get(String url){
		return request(null, url, Type.GET);
	}
	
	public static String post(String message,String url){
		return request(message, url, Type.POST);
	}
	
	public static String put(String message, String url){
		return request(message, url, Type.PUT);
	}
	
	public static String delete(String url){
		return request(null, url, Type.DELETE);
	}
	
	public static String secureGet(String url, Account account){
		return secureRequest(null, url, Type.GET, account);
	}
	
	public static String securePost(String message, String url, Account account){
		return secureRequest(message, url, Type.POST, account);
	}
	
	public static String securePut(String message, String url, Account account){
		return secureRequest(message, url, Type.PUT, account);
	}
	
	public static String secureDelete(String url, Account account){
		return secureRequest(null, url, Type.DELETE, account);
	}
	
	public static String secureRequest(String message, String url, Type type, Account account){
		//Get AuthToken and Hash
		String hash = "";
		String timestamp = System.currentTimeMillis()+"";
		// Get Username and Password from Account Manager. Get authToken from ser
		
		if(message == null){
			message = account.email + timestamp;
		}
		
		try{
			hash = computeSignature(message, account.authToken);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		HttpRequestBase request = null;
		
		//Send auth check request.
		switch(type){
			case GET:
				request = new HttpGet(url);
				break;
			
			case POST:			
				request = new HttpPost(url);
				StringEntity postJSON = null;
				try {
					postJSON = new StringEntity(message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((HttpPost)request).setEntity(postJSON);
				break;
				
			case DELETE:
				request = new HttpDelete(url);
				break;
				
			case PUT:
				request = new HttpPut(url);
		
				StringEntity putJSON = null;
				try {
					putJSON = new StringEntity(message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((HttpPost)request).setEntity(putJSON);
				break;
		}
		
		request.setHeader("X-EMAIL", account.email);
		request.setHeader("X-TIMESTAMP", timestamp);
		request.setHeader("X-HASH", hash);
		
		Log.d(TAG, "HMAC: " + hash + ", Data: " + message);

		
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        return mClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
	}
	
	public static String request(String message, String url, Type type){
		
		HttpRequestBase request = null;
		
		//Send auth check request.
		switch(type){
			case GET:
				request = new HttpGet(url);
				break;
			
			case POST:			
				request = new HttpPost(url);
				StringEntity postJSON = null;
				try {
					postJSON = new StringEntity(message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((HttpPost)request).setEntity(postJSON);
				break;
				
			case DELETE:
				request = new HttpDelete(url);
				break;
				
			case PUT:
				request = new HttpPut(url);
		
				StringEntity putJSON = null;
				try {
					putJSON = new StringEntity(message);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((HttpPost)request).setEntity(putJSON);
				break;
		}
		
		Log.d(TAG, "Data: " + message);

		
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        Log.d(TAG, "Successfully got responseHandler");
			return mClient.execute(request, responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
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
