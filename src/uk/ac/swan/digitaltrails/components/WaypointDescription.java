package uk.ac.swan.digitaltrails.components;

import com.google.gson.annotations.SerializedName;

/**
 * @author Lewis Hancock
 * Component to contain waypoint description item.
 */
public class WaypointDescription extends Description {
	/**
	 * 
	 */
	@SerializedName("waypoint_id") protected long mForeignId;

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
