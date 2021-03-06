package uk.ac.swan.digitaltrails.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

// TODO: Replace strings with constants from DbSchema etc.

/**
 * API for content provider.
 * @author Lewis Hancock
 *
 */
/**
 * @author Lewis Hancock
 *
 */
public class WhiteRockContract {
	
	/**
	 * 
	 */
	public static final String AUTHORITY = "uk.ac.swan.digitaltrails";
	
	// content://<authority>/<path to type> is the syntax here.
	
	/**
	 * 
	 */
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


	/**
	 * Constants for Bug Report table.
	 * @author Lewis Hancock
	 *
	 */
	public static final class BugReport implements ReportColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "bug_report");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.bug_report";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.bug_report";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, DESCRIPTION, USER_ID};
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class ContentReport implements ReportColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "content_report");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.content_report";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.content_report";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, DESCRIPTION, USER_ID};

	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class WalkWithEnglishDescriptions implements WalkDescriptionColumns, WalkColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "walk_and_english");
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_and_english";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_and_english";

		public static final String SORT_ORDER_DEFAULT = ID + " ASC";	
	
		public static final String[] PROJECTION_ALL = {DbSchema.TABLE_WALK+"."+WalkColumns.ID, DURATION_MINUTES, DISTANCE_MILES, DOWNLOAD_COUNT, DIFFICULTY_RATING, USER_ID,
														TITLE, SHORT_DESCR, LONG_DESCR, DbSchema.TABLE_ENGLISH_WALK_DESCR+"."+WalkDescriptionColumns.WALK_ID};
	}
	
	/**
	 * Constants for English Walk Descriptions
	 *
	 */
	public static final class EnglishWalkDescriptions implements WalkDescriptionColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "english_walk_description");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.english_walk_description";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.english_walk_description";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		/** Columns to project: id, title, short, long, walk_id */
		public static final String[] PROJECTION_ALL = {ID, TITLE, SHORT_DESCR, LONG_DESCR, WALK_ID};

	}
	
	/**
	 * Constants for Welsh Walk Descriptions
	 * @author Lewis Hancock
	 *
	 */
	public static final class WelshWalkDescriptions implements WalkDescriptionColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "welsh_walk_description");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.welsh_walk_description";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.welsh_walk_description";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, TITLE, SHORT_DESCR, LONG_DESCR, WALK_ID};

	}
	
	/**
	 * Constants for English Waypoint Descriptions
	 * @author Lewis Hancock
	 *
	 */
	public static final class EnglishWaypointDescriptions implements WaypointDescriptionColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "english_waypoint_description");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.english_waypoint_description";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.english_waypoint_description";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		/** id, title, short, long, wpId, descrId */
		public static final String[] PROJECTION_ALL = {ID, TITLE, SHORT_DESCR, LONG_DESCR, WAYPOINT_ID, REMOTE_ID};

	}
	
	/**
	 * Constants for Welsh Waypoint Descriptions
	 * @author Lewis Hancock
	 *
	 */

	public static final class WelshWaypointDescriptions implements WaypointDescriptionColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "welsh_waypoint_description");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.welsh_waypoint_description";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.welsh_waypoint_description";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, TITLE, SHORT_DESCR, LONG_DESCR, WAYPOINT_ID, REMOTE_ID};

	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class SettingType implements CommonColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "setting_type");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.setting_type";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.setting_type";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, "name"};

	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class UserSetting implements CommonColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "user_setting");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.user_setting";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.user_setting";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, "value", "user_id", "setting_type_id"};

	}
	
	/**
	 * Constants for user table.
	 * @author Lewis Hancock
	 *
	 */

	public static final class User implements CommonColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "user");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.user";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.user";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, "email", "password"};

	}
	
	/**
	 * Constants for walk table
	 * @author Lewis Hancock
	 *
	 */

	public static final class Walk implements WalkColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "walk");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		/** Columns to project: id, duration, distance, dl count, difficulty, user id, walk_id */
		public static final String[] PROJECTION_ALL = {ID, DURATION_MINUTES, DISTANCE_MILES, DOWNLOAD_COUNT, DIFFICULTY_RATING, USER_ID, WALK_ID};
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class WalkBrand implements CommonColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "walk_brand");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_brand";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_brand";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, "name", "logo_file_location", "download_count", "walk_id"};
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class WalkReview implements CommonColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "walk_review");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_review";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_review";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, "title", "description", "rating", "walk_id", "user_id"};
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class Waypoint implements WaypointColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, DbSchema.TABLE_WAYPOINT);
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		/** Project for Waypoint: id, lat, long, is_req, visit_order, walk_id, user_id, waypoint_id */
		public static final String[] PROJECTION_ALL = {ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER, WALK_ID, USER_ID, REMOTE_WAYPOINT_ID};
	}
	
	/**
	 * Constants for WaypointAudio table.
	 * @author Lewis Hancock
	 *
	 */

	public static final class WaypointAudio implements MediaColumns  {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "waypoint_audio");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_audio";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_audio";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		public static final String[] PROJECTION_ALL = {ID, FILE_NAME, WAYPOINT_ID, REMOTE_ID};
	}
	
	/**
	 * Constants for WaypointVideo table.
	 * @author Lewis Hancock
	 *
	 */
	public static final class WaypointVideo implements MediaColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "waypoint_video");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_video";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_video";

		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";	
	
		public static final String[] PROJECTION_ALL = {ID, FILE_NAME, WAYPOINT_ID, REMOTE_ID};

	}
	
	/**
	 * Constants for WaypointImage table.
	 * @author Lewis Hancock
	 *
	 */

	public static final class WaypointImage implements MediaColumns {
		
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "waypoint_image");
	
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_image";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_image";

		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";	
	
		public static final String[] PROJECTION_ALL = {ID, FILE_NAME, WAYPOINT_ID, REMOTE_ID};

	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class WaypointWithEnglishDescription implements WaypointDescriptionColumns, WaypointColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "waypoint_and_english");
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_and_english";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_and_english";

		public static final String SORT_ORDER_DEFAULT = ID + " ASC";	
	
		public static final String[] PROJECTION_ALL = {DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER,
														WaypointColumns.WALK_ID, USER_ID,
														TITLE, SHORT_DESCR, LONG_DESCR, DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WAYPOINT_ID};

	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class WaypointWithMedia implements WaypointColumns, MediaColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "waypoint_and_media");
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_and_media";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_and_media";

		public static final String SORT_ORDER_DEFAULT = ID + " ASC";	
	
		public static final String[] PROJECTION_ALL = {DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER,
														WaypointColumns.WALK_ID, USER_ID,
														FILE_NAME};
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	public static final class WaypointComplete implements WaypointColumns, MediaColumns, WaypointDescriptionColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "waypoint_complete");
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_complete";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.waypoint_complete";

		public static final String SORT_ORDER_DEFAULT = ID + " ASC";	
	
		public static final String[] PROJECTION_ALL = {DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER,
														WaypointColumns.WALK_ID, USER_ID,
														DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+DescriptionColumns.ID, TITLE, SHORT_DESCR, LONG_DESCR,
														DbSchema.TABLE_WAYPOINT_AUDIO+"."+FILE_NAME,
														DbSchema.TABLE_WAYPOINT_IMAGE+"."+FILE_NAME,
														DbSchema.TABLE_WAYPOINT_VIDEO+"."+FILE_NAME};
	}

	/**
	 * Use this to get an entire walk, complete with descriptions, waypoints, media etc.
	 * @author Lewis H
	 *
	 */
	public static final class WalkComplete implements WalkColumns, WalkDescriptionColumns, WaypointColumns, WaypointDescriptionColumns, MediaColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(
				WhiteRockContract.CONTENT_URI, "walk_complete");
		
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_complete";
		
		public static final String CONTENT_TYPE_DIR = ContentResolver.CURSOR_ITEM_BASE_TYPE +
				"vnd.uk.ac.swan.digitaltrails.walk_complete";
		
		public static final String SORT_ORDER_DEFAULT = ID + " ASC";
		
		/**
		 * Walk: Id, Duration, Distance, DownloadCount, difficulty, UserId, WalkId ( = Remote ID)
		 * EnglishDescr: Id, Title, Short, Long, WalkId
		 * WelshDescr: Id, Title, Short, Long, WalkId
		 * Waypoint: Id, Lat, Long, IsReq, VisitOrder, UserId, WalkId, RemoteID
		 * EnglishDescr: Id, Title, Short, Long, WaypointId, RemoteId
		 * WelshDescr: Id, Title, Short, Long, WaypointId, RemoteId
		 * Audio: Id, File, WaypointId, RemoteId
		 * Image: Id, File, WaypointId, RemoteId
		 * Video: Id, File, WaypointId, RemoteId
		 */
		public static final String[] PROJECTION_ALL = {
			DbSchema.TABLE_WALK+"."+WalkColumns.ID, DURATION_MINUTES, DISTANCE_MILES, DOWNLOAD_COUNT, DIFFICULTY_RATING, DbSchema.TABLE_WALK+"."+WalkColumns.USER_ID, DbSchema.TABLE_WALK+"."+WalkColumns.WALK_ID,
			
			DbSchema.TABLE_ENGLISH_WALK_DESCR+"."+WalkDescriptionColumns.ID, DbSchema.TABLE_ENGLISH_WALK_DESCR+"."+WalkDescriptionColumns.TITLE, DbSchema.TABLE_ENGLISH_WALK_DESCR+"."+WalkDescriptionColumns.SHORT_DESCR, DbSchema.TABLE_ENGLISH_WALK_DESCR+"."+WalkDescriptionColumns.LONG_DESCR, DbSchema.TABLE_ENGLISH_WALK_DESCR+"."+WalkDescriptionColumns.WALK_ID, 
			
			DbSchema.TABLE_WELSH_WALK_DESCR+"."+WalkDescriptionColumns.ID, DbSchema.TABLE_WELSH_WALK_DESCR+"."+WalkDescriptionColumns.TITLE, DbSchema.TABLE_WELSH_WALK_DESCR+"."+WalkDescriptionColumns.SHORT_DESCR, DbSchema.TABLE_WELSH_WALK_DESCR+"."+WalkDescriptionColumns.LONG_DESCR, DbSchema.TABLE_WELSH_WALK_DESCR+"."+WalkDescriptionColumns.WALK_ID,
			
			DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.ID, LATITUDE, LONGITUDE, IS_REQUEST, VISIT_ORDER, DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.WALK_ID, DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.USER_ID, DbSchema.TABLE_WAYPOINT+"."+WaypointColumns.REMOTE_WAYPOINT_ID,
			
			DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.ID, DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.TITLE, DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.SHORT_DESCR, DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.LONG_DESCR, DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.WAYPOINT_ID, DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.REMOTE_ID, 

			DbSchema.TABLE_WELSH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.ID, DbSchema.TABLE_WELSH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.TITLE, DbSchema.TABLE_WELSH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.SHORT_DESCR, DbSchema.TABLE_WELSH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.LONG_DESCR, DbSchema.TABLE_WELSH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.WAYPOINT_ID, DbSchema.TABLE_WELSH_WAYPOINT_DESCR+"."+WaypointDescriptionColumns.REMOTE_ID, 

			DbSchema.TABLE_WAYPOINT_AUDIO+"."+MediaColumns.ID, DbSchema.TABLE_WAYPOINT_AUDIO+"."+MediaColumns.FILE_NAME, DbSchema.TABLE_WAYPOINT_AUDIO+"."+MediaColumns.WAYPOINT_ID, DbSchema.TABLE_WAYPOINT_AUDIO+"."+MediaColumns.REMOTE_ID,
		
			DbSchema.TABLE_WAYPOINT_IMAGE+"."+MediaColumns.ID, DbSchema.TABLE_WAYPOINT_IMAGE+"."+MediaColumns.FILE_NAME, DbSchema.TABLE_WAYPOINT_IMAGE+"."+MediaColumns.WAYPOINT_ID, DbSchema.TABLE_WAYPOINT_IMAGE+"."+MediaColumns.REMOTE_ID,

			DbSchema.TABLE_WAYPOINT_VIDEO+"."+MediaColumns.ID, DbSchema.TABLE_WAYPOINT_VIDEO+"."+MediaColumns.FILE_NAME, DbSchema.TABLE_WAYPOINT_VIDEO+"."+MediaColumns.WAYPOINT_ID, DbSchema.TABLE_WAYPOINT_VIDEO+"."+MediaColumns.REMOTE_ID,

		};
	}
		
	/**
	 * Interface to define common columns to all tables.
	 * @author Lewis Hancock
	 *
	 */

	interface CommonColumns extends BaseColumns {
		public static final String ID = "_id";
	}
	
	/**
	 * Interface to define common columns in media tables.
	 * @author Lewis Hancock
	 *
	 */

	interface MediaColumns extends CommonColumns {
		
		public static final String FILE_NAME = "file_name";
		
		public static final String WAYPOINT_ID = "waypoint_id";
		
		public static final String REMOTE_ID = "remote_id";
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	interface WalkColumns extends CommonColumns {
		public static final String DURATION_MINUTES = "duration_minutes";
		public static final String DISTANCE_MILES = "distance_miles";
		public static final String DOWNLOAD_COUNT = "download_count";
		public static final String DIFFICULTY_RATING = "difficulty_rating";
		public static final String USER_ID = "user_id";
		public static final String WALK_ID = "walk_id";
	}
	
	/**
	 * Interface to define common columns in description tables.
	 * @author Lewis Hancock
	 *
	 */

	interface DescriptionColumns extends CommonColumns {
		public static final String TITLE = "title";
		
		public static final String SHORT_DESCR = "short_description";
		
		public static final String LONG_DESCR = "long_description";
		
		public static final String REMOTE_ID = "description_id";
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	interface WaypointDescriptionColumns extends DescriptionColumns {
		public static final String WAYPOINT_ID = "waypoint_id";
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	interface WalkDescriptionColumns extends DescriptionColumns {
		public static final String WALK_ID = "walk_id";
	}

	
	/**
	 * Interface to define common columns in report tables.
	 */

	interface ReportColumns extends CommonColumns {
		public static final String DESCRIPTION = "description";
		public static final String USER_ID = "user_id";
	}
	
	/**
	 * @author Lewis Hancock
	 *
	 */
	interface WaypointColumns extends CommonColumns {
		public static final String LATITUDE = "latitude";
		public static final String LONGITUDE = "longitude";
		public static final String WALK_ID = "walk_id";
		public static final String USER_ID = "user_id";
		public static final String IS_REQUEST = "is_request";
		public static final String VISIT_ORDER = "visit_order";
		public static final String REMOTE_WAYPOINT_ID = "waypoint_id";
	}

}
