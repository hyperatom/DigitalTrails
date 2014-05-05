package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

public class WalkDescription extends Description {
	@SerializedName("walk_id") protected long mForeignId;

	public void setForeignId(long id) {
		mForeignId = id;
	}

	public long getForeignId() {
		return mForeignId;
	}
	
}
