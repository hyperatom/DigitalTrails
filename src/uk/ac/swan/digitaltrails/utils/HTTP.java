/**
 * 
 */
package uk.ac.swan.digitaltrails.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import uk.ac.swan.digitaltrails.components.Account;
import android.util.Log;
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

/**
 * @author Thomas Milner
 * Class to do HTTP requests.
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
	public static final String BASEURL = "http://s523031181.websitehome.co.uk/api";
	
	/**
	 * 
	 */
	private static final HttpClient mClient = new DefaultHttpClient();
	
	public static enum Type {
		GET,POST,PUT,DELETE
	};

	/**
	 * perform get request
	 * @param url url to get
	 * @return the result of the request
	 */
	public static String get(String url){
		return request(null, url, Type.GET);
	}
	
	/**
	 * perform post request
	 * @param message message to send
	 * @param url url to send to 
	 * @return result of request
	 */
	public static String post(String message,String url){
		return request(message, url, Type.POST);
	}
	
	/**
	 * perform put request
	 * @param message message to send
	 * @param url url to put to
	 * @return result of the request
	 */
	public static String put(String message, String url){
		return request(message, url, Type.PUT);
	}
	
	/**
	 * perform delete request
	 * @param url url to perform request on
	 * @return result of request
	 */
	public static String delete(String url){
		return request(null, url, Type.DELETE);
	}
	
	/**
	 * do a secure get request
	 * @param url url to request from
	 * @param account the account asking to get
	 * @return result of request
	 */
	public static String secureGet(String url, Account account){
		return secureRequest(null, url, Type.GET, account);
	}
	
	/**
	 * do a secure post
	 * @param message the message to pass
	 * @param url url to post to
	 * @param account account which is posting
	 * @return the result of request
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
