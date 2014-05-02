package uk.ac.swan.digitaltrails.accounts;

import java.io.Serializable;

/**
 * 
 * @author Lewis H
 *
 */
public class User implements Serializable {

	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mObjectId;
	public String mSessionToken;
	
	public String getFirstName() {
		return mFirstName;
	}
	
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}
	
	public String getLastName() {
		return mLastName;
	}
	
	public void setLastName(String lastName) {
		mLastName = lastName;
	}
	
	public String getEmail() {
		return mEmail;
	}
	
	public void setEmail(String email) {
		mEmail = email;
	}
	
	public String getObjectId() {
		return mObjectId;
	}
	
	public void setObjectId(String objectId) {
		mObjectId = objectId;
	}
}
