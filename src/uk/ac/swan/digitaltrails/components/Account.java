package uk.ac.swan.digitaltrails.components;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
	public String email;
	public String first_name;
	public String last_name;
	public String authToken;
	public String password;
	public int id;
	
	public Account(){
		
	}
	
	public Account(String email, String password){
		this.email = email;
		this.password = this.authToken = password;
	}
	
	public Account(String email, String pass, String firstName, String lastName) {
		this.email = email;
		this.password = pass;
		this.first_name = firstName;
		this.last_name = lastName;
	}

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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(id);
		out.writeString(email);
		out.writeString(first_name);
		out.writeString(last_name);
		out.writeString(authToken);
	}
	
	public String toString(){
		return "Email: " + email + ", authToken: " + authToken + ", password: " + password;
 	}

}
