package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

/**
 * @author Lewis Hancock
 * component to contain a walkdescription item.
 */
public class WalkDescription extends Description {
	/**
	 * foreign id of walk description
	 */
	@SerializedName("walk_id") protected long mForeignId;

	/**
	 * @param id
	 */
	public void setForeignId(long id) {
		mForeignId = id;
	}

	/**
	 * @return
	 */
	public long getForeignId() {
		return mForeignId;
	}
	
}
