package uk.ac.swan.digitaltrails.components;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * @author Thomas Milner
 * Account component, contains information needed for an Account
 */
public class Account implements Parcelable {
	/**
	 * Email string
	 */
	public String email;
	/**
	 * first name string
	 */
	public String first_name;
	/**
	 * last name string
	 */
	public String last_name;
	/**
	 * account auth token
	 */
	public String authToken;
	/**
	 * account password
	 */
	public String password;
	/**
	 * account id
	 */
	public int id;
	
	/**
	 * default constructor
	 */
	public Account(){
		
	}
	
	/**
	 * constructor
	 * @param email user's email
	 * @param password user's password
	 */
	public Account(String email, String password){
		this.email = email;
		this.password = this.authToken = password;
	}
	
	/**
	 * constructor
	 * @param email user's email
	 * @param pass user's password
	 * @param firstName user's firstname
	 * @param lastName user's lastname
	 */
	public Account(String email, String pass, String firstName, String lastName) {
		this.email = email;
		this.password = this.authToken = pass;
		this.first_name = firstName;
		this.last_name = lastName;
		
		Log.d("Account", "New account created. email: " + email + " pass " + pass + " first " + firstName + " last " + lastName + " authToken " + this.authToken);
	}

	/**
	 * Creates this object from parcelable.
	 */
	public static final Parcelable.Creator<Account> CREATOR = new Creator<Account>() {  
		public Account createFromParcel(Parcel source) { 
			Account user = new Account();
			user.id = source.readInt();
			user.email = source.readString();
			user.first_name = source.readString();
			user.last_name = source.readString();
			user.authToken = source.readString();
			return user; 
		}

		@Override
		public Account[] newArray(int size) {
			return new Account[size];
		}	
	};

	/* (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(email);
		out.writeString(first_name);
		out.writeString(last_name);
		out.writeString(authToken);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Email: " + email + ", authToken: " + authToken + ", password: " + password;
 	}

}
