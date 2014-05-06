package uk.ac.swan.digitaltrails.database;

/**
 * Helper interface which defines constants for database.
 * @author Lewis Hancock
 *
 */
interface DbSchema {

	/**
	 * 
	 */
	String DB_NAME = "database.db3";
	/**
	 * 
	 */
	String TABLE_BUG_REPORT = "bug_report";
	/**
	 * 
	 */
	String TABLE_CONTENT_REPORT = "content_report";
	/**
	 * 
	 */
	String TABLE_ENGLISH_WALK_DESCR = "english_walk_description";
	/**
	 * 
	 */
	String TABLE_ENGLISH_WAYPOINT_DESCR = "english_waypoint_description";
	/**
	 * 
	 */
	String TABLE_WELSH_WALK_DESCR = "welsh_walk_description";
	/**
	 * 
	 */
	String TABLE_WELSH_WAYPOINT_DESCR = "welsh_waypoint_description";
	/**
	 * 
	 */
	String TABLE_SETTING_TYPE = "setting_type";
	/**
	 * 
	 */
	String TABLE_USER_SETTING = "user_setting";
	/**
	 * 
	 */
	String TABLE_USER = "user";
	/**
	 * 
	 */
	String TABLE_WALK = "walk";
	/**
	 * 
	 */
	String TABLE_WALK_BRAND = "walk_brand";
	/**
	 * 
	 */
	String TABLE_WALK_REVIEW = "walk_review";
	/**
	 * 
	 */
	String TABLE_WAYPOINT = "waypoint";
	/**
	 * 
	 */
	String TABLE_WAYPOINT_AUDIO = "waypoint_audio";
	/**
	 * 
	 */
	String TABLE_WAYPOINT_IMAGE = "waypoint_image";
	/**
	 * 
	 */
	String TABLE_WAYPOINT_VIDEO = "waypoint_video";
	/**
	 * 
	 */
	String VIEW_WAYPOINT_WITH_ENGLISH_DESCR = "waypoint_and_english";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WALK = 
			"CREATE TABLE `walk` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"duration_minutes INT,"
			+"distance_miles REAL,"
			+"download_count INT,"
			+"difficulty_rating INT,"
			+"user_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WALK_BRAND = 
			"CREATE TABLE `walk_brand` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"name TEXT,"
			+"logo_file_name TEXT,"
			+"walk_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WALK_REVIEW = 
			"CREATE TABLE `walk_review` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"title TEXT,"
			+"description TEXT,"
			+"rating INT,"
			+"walk_id INT,"
			+"user_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WAYPOINT = 
			"CREATE TABLE `waypoint` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"latitude REAL,"
			+"longitude REAL,"
			+"is_request INT,"
			+"visit_order INT,"
			+"walk_id INT,"
			+"user_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WAYPOINT_AUDIO = 
			"CREATE TABLE `waypoint_audio` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"file_name TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WAYPOINT_VIDEO = 
			"CREATE TABLE `waypoint_video` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"file_name TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WAYPOINT_IMAGE = 
			"CREATE TABLE `waypoint_image` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"file_name TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WESLH_WAYPOINT_DESCR = 
			"CREATE TABLE `welsh_waypoint_description` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"title TEXT,"
			+"short_description TEXT,"
			+"long_description TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_WELSH_WALK_DESCR = 
			"CREATE TABLE `welsh_walk_description` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"title TEXT,"
			+"short_description TEXT,"
			+"long_description TEXT,"
			+"walk_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_ENGLISH_WAYPOINT_DESCR = 
			"CREATE TABLE `english_waypoint_description` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"title TEXT,"
			+"short_description TEXT,"
			+"long_description TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_ENGLISH_WALK_DESCR = 
			"CREATE TABLE `english_walk_description` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"title TEXT,"
			+"short_description TEXT,"
			+"long_description TEXT,"
			+"walk_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_USER = 
			"CREATE TABLE `user` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"first_name TEXT,"
			+"last_name TEXT,"
			+"email TEXT,"
			+"password TEXT,"
			+"salt TEXT,"
			+"UNIQUE (`email`)"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_USER_SETTINGS = 
			"CREATE TABLE `user_setting` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"value TEXT,"
			+"user_id INT,"
			+"setting_type_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_SETTING_TYPE = 
			"CREATE TABLE `setting_type` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"name TEXT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_CONTENT_REPORT = 
			"CREATE TABLE `content_report` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"description TEXT,"
			+"user_id INT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * 
	 */
	String CREATE_TABLE_BUG_REPORT = 
			"CREATE TABLE `bug_report` (" 
	       +"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
	       +"description TEXT,"
	       +"user_id INT"
	       +")";
}
