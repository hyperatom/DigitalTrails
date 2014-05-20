package uk.ac.swan.digitaltrails.database;

/**
 * Helper interface which defines constants for database.
 * @author Lewis Hancock
 *
 */
interface DbSchema {

	/**
	 *  Database name.
	 */
	String DB_NAME = "database.db3";
	/**
	 * constant for bug_report table
	 */
	String TABLE_BUG_REPORT = "bug_report";
	/**
	 * constant for content_report table.
	 */
	String TABLE_CONTENT_REPORT = "content_report";
	/**
	 * constant for english_walk_description table
	 */
	String TABLE_ENGLISH_WALK_DESCR = "english_walk_description";
	/**
	 *  constant for english_waypoint_description table.
	 */
	String TABLE_ENGLISH_WAYPOINT_DESCR = "english_waypoint_description";
	/**
	 * constant for welsh_walk_description table
	 */
	String TABLE_WELSH_WALK_DESCR = "welsh_walk_description";
	/**
	 * constant for welsh_waypoint_description table
	 */
	String TABLE_WELSH_WAYPOINT_DESCR = "welsh_waypoint_description";
	/**
	 * constant for setting_type table
	 */
	String TABLE_SETTING_TYPE = "setting_type";
	/**
	 * constant for user_setting table
	 */
	String TABLE_USER_SETTING = "user_setting";
	/**
	 * constant for user table
	 */
	String TABLE_USER = "user";
	/**
	 * constant for walk table
	 */
	String TABLE_WALK = "walk";
	/**
	 * constant for walk_brand table
	 */
	String TABLE_WALK_BRAND = "walk_brand";
	/**
	 * constant for walk_review table
	 */
	String TABLE_WALK_REVIEW = "walk_review";
	/**
	 *  constant for waypoint table.
	 */
	String TABLE_WAYPOINT = "waypoint";
	/**
	 * constant for waypoint_audio table
	 */
	String TABLE_WAYPOINT_AUDIO = "waypoint_audio";
	/**
	 * constant for waypoint_image table
	 */
	String TABLE_WAYPOINT_IMAGE = "waypoint_image";
	/**
	 * constant for waypoint_video table.
	 */
	String TABLE_WAYPOINT_VIDEO = "waypoint_video";
	/**
	 * constant for waypoint_and_english view.
	 */
	String VIEW_WAYPOINT_WITH_ENGLISH_DESCR = "waypoint_and_english";
	
	/**
	 * constant for creating the walk table.
	 */
	String CREATE_TABLE_WALK = 
			"CREATE TABLE `walk` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+"duration_minutes INT,"
			+"distance_miles REAL,"
			+"download_count INT,"
			+"difficulty_rating INT,"
			+"user_id INT,"
			+"walk_id INT"
			+")";
	
	/**
	 * constant for creating the walk_brand table.
	 */
	String CREATE_TABLE_WALK_BRAND = 
			"CREATE TABLE `walk_brand` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"name TEXT,"
			+"logo_file_name TEXT,"
			+"walk_id INT"
			+")";
	
	/**
	 * constant for creating the walk_review table
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
	 * constant for creating waypoint table
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
	 * constant for creating waypoint_audio table
	 */
	String CREATE_TABLE_WAYPOINT_AUDIO = 
			"CREATE TABLE `waypoint_audio` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"file_name TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * constant for creating waypoint_video table
	 */
	String CREATE_TABLE_WAYPOINT_VIDEO = 
			"CREATE TABLE `waypoint_video` (" 
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"file_name TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 *  constant for creating waypoint_image table
	 */
	String CREATE_TABLE_WAYPOINT_IMAGE = 
			"CREATE TABLE `waypoint_image` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"file_name TEXT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * constant to create welsh_waypoint_description table.
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
	 * constant to create welsh_walk_description
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
	 * constant to create the english_waypoint_description
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
	 * constant to create the english_walk_description
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
	 * constant to create the user table
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
	 * constant to create user_setting table
	 */
	String CREATE_TABLE_USER_SETTINGS = 
			"CREATE TABLE `user_setting` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"value TEXT,"
			+"user_id INT,"
			+"setting_type_id INT"
			+")";
	
	/**
	 * constant to create setting_type table
	 */
	String CREATE_TABLE_SETTING_TYPE = 
			"CREATE TABLE `setting_type` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"name TEXT"
			+")";
	
	/**
	 * constant to create content_report table
	 */
	String CREATE_TABLE_CONTENT_REPORT = 
			"CREATE TABLE `content_report` ("
			+"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"description TEXT,"
			+"user_id INT,"
			+"waypoint_id INT"
			+")";
	
	/**
	 * constant to create bug_report table
	 */
	String CREATE_TABLE_BUG_REPORT = 
			"CREATE TABLE `bug_report` (" 
	       +"_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
	       +"description TEXT,"
	       +"user_id INT"
	       +")";
}
