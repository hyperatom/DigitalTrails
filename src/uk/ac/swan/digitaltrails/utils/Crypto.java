package uk.ac.swan.digitaltrails.utils;

import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;



import android.util.Base64;
import android.util.Log;

/**
 * 
 * @author Lewis Hancock
 * Contains Cryptography functions including HMAC_SHA1 encryption.
 *
 */
/**
 * @author Lewis Hancock
 *
 */
public class Crypto {

	/**
	 * 
	 */
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	/**
	 * 
	 */
	private static final String TAG = "Crypto";

	/**
	 * 
	 * @param secret
	 * @param data
	 * @return
	 * @throws java.security.SignatureException
	 */
	/**
	 * @param secret
	 * @param data
	 * @return
	 * @throws java.security.SignatureException
	 */
	private String calculateHMAC(String secret, String data) throws java.security.SignatureException {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
			Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
			mac.init(signingKey);
			
			// compute raw hmac on input data
			byte[] rawHmac = mac.doFinal(data.getBytes());
			String result = Base64.encodeToString(rawHmac, Base64.DEFAULT);
			return result;
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage());
			throw new SignatureException("Failed to generate HMAC: " + e.getMessage());
		}
	}
	
}
