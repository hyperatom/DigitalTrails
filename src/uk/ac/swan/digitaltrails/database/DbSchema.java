package uk.ac.swan.digitaltrails.database;

/**
 * Helper interface which defines constants for database.
 * @author Lewis Hancock
 */
interface DbSchema {

	String DB_NAME = "database.db";
	String TABLE_BUG_REPORT = "bug_report";
	String TABLE_CONTENT_REPORT = "content_report";
	String TABLE_ENGLISH_WALK_DESCR = "english_walk_description";
	String TABLE_ENGLISH_WAYPOINT_DESCR = "english_waypoint_description";
	String TABLE_WELSH_WALK_DESCR = "welsh_walk_description";
	String TABLE_WELSH_WAYPOINT_DESCR = "welsh_waypoint_description";
	String TABLE_SETTING_TYPE = "setting_type";
	String TABLE_USER_SETTING = "user_setting";
	String TABLE_USER = "user";
	String TABLE_WALK = "walk";
	String TABLE_WALK_BRAND = "walk_brand";
	String TABLE_WALK_REVIEW = "walk_review";
	String TABLE_WAYPOINT = "waypoint";
	String TABLE_WAYPOINT_AUDIO = "waypoint_audio";
	String TABLE_WAYPOINT_IMAGE = "waypoint_image";
	String TABLE_WAYPOINT_VIDEO = "waypoint_video";
	
	// TODO: Create Strings to create tables. 
	// Probably need to update the database SQL to SQLite first. 
	//Currently all SQL is normal. therefore invalid.
	
	String CREATE_TABLE_WALK = 
			"";
	
	String CREATE_TABLE_WALK_BRAND = "";
	
	String CREATE_TABLE_WALK_REVIEW = "";
	
	String CREATE_TABLE_WAYPOINT = "";
	
	String CREATE_TABLE_WAYPOINT_AUDIO = "";
	
	String CREATE_TABLE_WAYPOINT_VIDEO = "";
	
	String CREATE_TABLE_WAYPOINT_IMAGE = "";
	
	String CREATE_TABLE_WESLH_WAYPOINT_DESCR = "";
	
	String CREATE_TABLE_WELSH_WALK_DESCR = "";
	
	String CREATE_TABLE_ENGLISH_WAYPOINT_DESCR = "";
	
	String CREATE_TABLE_ENGLISH_WALK_DESCR = "";
	
	String CREATE_TABLE_USER = "";
	
	String CREATE_TABLE_USER_SETTINGS = "";
	
	String CREATE_TABLE_SETTING_TYPE = "";
	
	String CREATE_TABLE_CONTENT_REPORT = "";
	
	String CREATE_TABLE_BUG_REPORT = "";
}
