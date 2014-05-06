/**
 * 
 */
package uk.ac.swan.digitaltrails.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.ResponseHandler;
import ch.boye.httpclientandroidlib.client.methods.HttpDelete;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.methods.HttpPut;
import ch.boye.httpclientandroidlib.client.methods.HttpRequestBase;
import ch.boye.httpclientandroidlib.entity.StringEntity;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.FileBody;
import ch.boye.httpclientandroidlib.entity.mime.content.StringBody;
import ch.boye.httpclientandroidlib.impl.client.BasicResponseHandler;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.util.EntityUtils;

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
/**
 * @author Lewis Hancock
 *
 */
@SuppressWarnings("deprecation")
public class HTTP {

	/**
	 * 
	 */
	public static final String TAG = "SecureHTTP";
	
	/**
	 * 
	 */
	public static final String BASEURL = "http://whiterockapi.tmilner.co.uk";
	
	/**
	 * 
	 */
	private static final HttpClient mClient = new DefaultHttpClient();
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static enum Type {
		GET,POST,PUT,DELETE
	};

	/**
	 * @param url
	 * @return
	 */
	public static String get(String url){
		return request(null, url, Type.GET);
	}
	
	/**
	 * @param message
	 * @param url
	 * @return
	 */
	public static String post(String message,String url){
		return request(message, url, Type.POST);
	}
	
	/**
	 * @param message
	 * @param url
	 * @return
	 */
	public static String put(String message, String url){
		return request(message, url, Type.PUT);
	}
	
	/**
	 * @param url
	 * @return
	 */
	public static String delete(String url){
		return request(null, url, Type.DELETE);
	}
	
	/**
	 * @param url
	 * @param account
	 * @return
	 */
	public static String secureGet(String url, Account account){
		return secureRequest(null, url, Type.GET, account);
	}
	
	/**
	 * @param message
	 * @param url
	 * @param account
	 * @return
	 */
	public static String securePost(String message, String url, Account account){
		return secureRequest(message, url, Type.POST, account);
	}
	
	/**
	 * @param message
	 * @param url
	 * @param account
	 * @return
	 */
	public static String securePut(String message, String url, Account account){
		return secureRequest(message, url, Type.PUT, account);
	}
	
	/**
	 * @param url
	 * @param account
	 * @return
	 */
	public static String secureDelete(String url, Account account){
		return secureRequest(null, url, Type.DELETE, account);
	}
	
	/**
	 * @param message
	 * @param url
	 * @param type
	 * @param account
	 * @return
	 */
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
	
	/**
	 * Send a request to a URL. 
	 * @param message The data to send to the 
	 * @param url
	 * @param type
	 * @return
	 */
	/**
	 * @param message
	 * @param url
	 * @param type
	 * @return
	 */
	public static String request(String message, String url, Type type){
		
		HttpRequestBase request = null;
		
		//Send auth check request.
		switch(type){
			case GET:
				request = new HttpGet(url);
				break;
			
			case POST:			
				Log.d(TAG, "POSTING");
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
	        mClient.getParams().setBooleanParameter("http.protocol.expect-continue", false);    
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
	
	/**
	 * A method to post a file 
	 * @param uri The URL to post too.
	 * @param account The account to authenticate with
	 * @param file The file
	 * @return The response.
	 */
	/**
	 * @param url
	 * @param account
	 * @param file
	 * @param message
	 * @return
	 */
	public static String postFile(String url, Account account, File file, String message){
		
		//Get AuthToken and Hash
		String hash = "";
		String timestamp = System.currentTimeMillis()+"";
		// Get Username and Password from Account Manager. Get authToken from ser
	
		try{
			hash = computeSignature(account.email + timestamp, account.authToken);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		HttpPost request = new HttpPost(url);
		MultipartEntityBuilder entity = MultipartEntityBuilder.create();
		
		entity.addPart("file", new FileBody(file));
	    try {
	        entity.addPart("post", new StringBody(message));
	    } catch (UnsupportedEncodingException e) {
	        throw new RuntimeException(e);
	    }
	    
		request.setEntity(entity.build());
			
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
	
	/**
	 * @param message
	 * @param key
	 * @return
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
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
