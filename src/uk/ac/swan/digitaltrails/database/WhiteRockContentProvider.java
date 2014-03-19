package uk.ac.swan.digitaltrails.database;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import uk.ac.swan.digitaltrails.BuildConfig;
import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

public class WhiteRockContentProvider extends ContentProvider {

	private static final UriMatcher URI_MATCHER = buildUriMatcher();
	private static final String TAG = "WhiteRockContentProvider";
	
	// Constants for URI matcher.
	private static final int WALK_LIST = 1;
	private static final int WALK_ID = 2;
	private static final int WAYPOINT_LIST = 5;
	private static final int WAYPOINT_ID = 6;
	private static final int WELSH_WALK_DESCR_LIST = 10;
	private static final int WELSH_WALK_DESCR_ID = 11;
	private static final int ENGLISH_WALK_DESCR_LIST = 15;
	private static final int ENGLISH_WALK_DESCR_ID = 16;
	private static final int WELSH_WAYPOINT_DESCR_LIST = 20;
	private static final int WELSH_WAYPOINT_DESCR_ID = 21;
	private static final int ENGLISH_WAYPOINT_DESCR_LIST = 25;
	private static final int ENGLISH_WAYPOINT_DESCR_ID = 26;
	private static final int WAYPOINT_AUDIO_LIST = 30;
	private static final int WAYPOINT_AUDIO_ID = 31;
	private static final int WAYPOINT_VIDEO_LIST = 35;
	private static final int WAYPOINT_VIDEO_ID = 36;
	private static final int WAYPOINT_IMAGE_LIST = 40;
	private static final int WAYPOINT_IMAGE_ID = 41;
	private static final int BUG_REPORT_LIST = 45;
	private static final int BUG_REPORT_ID = 46;
	private static final int CONTENT_REPORT_LIST = 50;
	private static final int CONTENT_REPORT_ID = 51;
	private static final int USER_LIST = 55;
	private static final int USER_ID = 56;
	private static final int USER_SETTINGS_LIST = 60;
	private static final int USER_SETTINGS_ID = 61;
	private static final int SETTINGS_TYPE_LIST = 65;
	private static final int SETTINGS_TYPE_ID = 66;
	
	/** Handler for database. */
	private DatabaseHandler mDbHandler;
	/** ThreadLocal storage for batch processing. */
	private final ThreadLocal<Boolean> mIsInBatchMode = new ThreadLocal<Boolean>();
	/**
	 * Create UriMatcher
	 * @return 
	 */
	private static UriMatcher buildUriMatcher() {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = WhiteRockContract.AUTHORITY;
		matcher.addURI(authority, "walk", WALK_LIST);
		matcher.addURI(authority, "walk/#", WALK_ID);
		matcher.addURI(authority, "english_walk_description", ENGLISH_WALK_DESCR_LIST);
		matcher.addURI(authority, "english_walk_description/#", ENGLISH_WALK_DESCR_ID);
		matcher.addURI(authority, "welsh_walk_description", WELSH_WALK_DESCR_LIST);
		matcher.addURI(authority, "welsh_walk_description/#", WELSH_WALK_DESCR_ID);
		matcher.addURI(authority, "waypoint", WAYPOINT_LIST);
		matcher.addURI(authority, "waypoint/#", WAYPOINT_ID);
		matcher.addURI(authority, "waypoint_audio", WAYPOINT_AUDIO_LIST);
		matcher.addURI(authority, "waypoint_audio/#", WAYPOINT_AUDIO_ID);
		matcher.addURI(authority, "waypoint_video", WAYPOINT_VIDEO_LIST);
		matcher.addURI(authority, "waypoint_video/#", WAYPOINT_VIDEO_ID);
		matcher.addURI(authority, "waypoint_image", WAYPOINT_IMAGE_LIST);
		matcher.addURI(authority, "waypoint_image/#", WAYPOINT_IMAGE_ID);
		matcher.addURI(authority, "welsh_waypoint_description", WELSH_WAYPOINT_DESCR_LIST);
		matcher.addURI(authority, "welsh_waypoint_description/#", WELSH_WAYPOINT_DESCR_ID);
		matcher.addURI(authority, "english_waypoint_description", ENGLISH_WAYPOINT_DESCR_LIST);
		matcher.addURI(authority, "english_waypoint_description/#", ENGLISH_WAYPOINT_DESCR_ID);
		matcher.addURI(authority, "bug_report", BUG_REPORT_LIST);
		matcher.addURI(authority, "bug_report/#", BUG_REPORT_ID);
		matcher.addURI(authority, "content_report", CONTENT_REPORT_LIST);
		matcher.addURI(authority, "content_report/#", CONTENT_REPORT_ID);
		matcher.addURI(authority, "user", USER_LIST);
		matcher.addURI(authority, "user/#", USER_ID);
		matcher.addURI(authority, "user_settings", USER_SETTINGS_LIST);
		matcher.addURI(authority, "user_settings/#", USER_SETTINGS_ID);
		matcher.addURI(authority, "settings_type", SETTINGS_TYPE_LIST);
		matcher.addURI(authority, "settings_type/#", SETTINGS_TYPE_ID);
		return matcher;
	}
	

	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		mDbHandler = new DatabaseHandler(context);
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHandler.getWritableDatabase();
		int deleteCount = 0;
		String idStr;
		String where;
		
		switch (URI_MATCHER.match(uri)) {
		case WALK_LIST:
			deleteCount = db.delete(DbSchema.TABLE_WALK, selection, selectionArgs);
			break;
		case WALK_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_WALK, where, selectionArgs);
			break;
		case WAYPOINT_LIST:
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT, selection, selectionArgs);
			break;
		case WAYPOINT_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT, where, selectionArgs);
			break;
		case ENGLISH_WALK_DESCR_LIST:
			deleteCount = db.delete(DbSchema.TABLE_ENGLISH_WALK_DESCR, selection, selectionArgs);
			break;
		case ENGLISH_WALK_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_ENGLISH_WALK_DESCR, where, selectionArgs);
			break;
		case WELSH_WALK_DESCR_LIST:
			deleteCount = db.delete(DbSchema.TABLE_ENGLISH_WALK_DESCR, selection, selectionArgs);
			break;
		case WELSH_WALK_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_ENGLISH_WALK_DESCR, where, selectionArgs);
			break;
		case ENGLISH_WAYPOINT_DESCR_LIST:
			deleteCount = db.delete(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR, selection, selectionArgs);
			break;
		case ENGLISH_WAYPOINT_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR, where, selectionArgs);
			break;
		case WELSH_WAYPOINT_DESCR_LIST:
			deleteCount = db.delete(DbSchema.TABLE_WELSH_WAYPOINT_DESCR, selection, selectionArgs);
			break;
		case WELSH_WAYPOINT_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_WELSH_WAYPOINT_DESCR, where, selectionArgs);
			break;
		case WAYPOINT_AUDIO_LIST:
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT_AUDIO, selection, selectionArgs);
			break;
		case WAYPOINT_AUDIO_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT_AUDIO, where, selectionArgs);
			break;
		case WAYPOINT_VIDEO_LIST:
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT_VIDEO, selection, selectionArgs);
			break;
		case WAYPOINT_VIDEO_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT_VIDEO, where, selectionArgs);
			break;
		case WAYPOINT_IMAGE_LIST:
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT_IMAGE, selection, selectionArgs);
			break;
		case WAYPOINT_IMAGE_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_WAYPOINT_IMAGE, where, selectionArgs);
			break;
		case BUG_REPORT_LIST:
			deleteCount = db.delete(DbSchema.TABLE_BUG_REPORT, selection, selectionArgs);
			break;
		case BUG_REPORT_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_BUG_REPORT, where, selectionArgs);
			break;
		case CONTENT_REPORT_LIST:
			deleteCount = db.delete(DbSchema.TABLE_CONTENT_REPORT, selection, selectionArgs);
			break;
		case CONTENT_REPORT_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_CONTENT_REPORT, where, selectionArgs);
			break;
		case USER_LIST:
			deleteCount = db.delete(DbSchema.TABLE_USER, selection, selectionArgs);
			break;
		case USER_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_USER, where, selectionArgs);
			break;
		case USER_SETTINGS_LIST:
			deleteCount = db.delete(DbSchema.TABLE_USER_SETTING, selection, selectionArgs);
			break;
		case USER_SETTINGS_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_USER_SETTING, where, selectionArgs);
			break;
		case SETTINGS_TYPE_LIST:
			deleteCount = db.delete(DbSchema.TABLE_SETTING_TYPE, selection, selectionArgs);
			break;
		case SETTINGS_TYPE_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			deleteCount = db.delete(DbSchema.TABLE_SETTING_TYPE, where, selectionArgs);
			break;
		default:
			// if not in the case list we don't support updating it.
			throw new IllegalArgumentException(
					"Unsupported URI For Deletion: " + uri);
		}
		if (deleteCount > 0 && !isInBatchMode()) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return deleteCount;
	}

	@Override
	public String getType(Uri uri) {
		final int match = URI_MATCHER.match(uri);
		switch (match) {
		case WALK_LIST:
			return WhiteRockContract.Walk.CONTENT_TYPE;
		case WALK_ID:
			return WhiteRockContract.Walk.CONTENT_TYPE_DIR;
		case WAYPOINT_LIST:
			return WhiteRockContract.Waypoint.CONTENT_TYPE;
		case WAYPOINT_ID:
			return WhiteRockContract.Waypoint.CONTENT_TYPE_DIR;
		case WELSH_WALK_DESCR_LIST:
			return WhiteRockContract.WelshWalkDescriptions.CONTENT_TYPE;
		case WELSH_WALK_DESCR_ID:
			return WhiteRockContract.WelshWalkDescriptions.CONTENT_TYPE_DIR;
		case ENGLISH_WALK_DESCR_LIST:
			return WhiteRockContract.EnglishWalkDescriptions.CONTENT_TYPE;
		case ENGLISH_WALK_DESCR_ID:
			return WhiteRockContract.EnglishWalkDescriptions.CONTENT_TYPE_DIR;
		case WELSH_WAYPOINT_DESCR_LIST:
			return WhiteRockContract.WelshWalkDescriptions.CONTENT_TYPE;
		case WELSH_WAYPOINT_DESCR_ID:
			return WhiteRockContract.WelshWalkDescriptions.CONTENT_TYPE_DIR;
		case ENGLISH_WAYPOINT_DESCR_LIST:
			return WhiteRockContract.EnglishWalkDescriptions.CONTENT_TYPE;
		case ENGLISH_WAYPOINT_DESCR_ID:
			return WhiteRockContract.EnglishWalkDescriptions.CONTENT_TYPE_DIR;
		case WAYPOINT_AUDIO_LIST:
			return WhiteRockContract.WaypointAudio.CONTENT_TYPE;
		case WAYPOINT_AUDIO_ID:
			return WhiteRockContract.WaypointAudio.CONTENT_TYPE_DIR;
		case WAYPOINT_VIDEO_LIST:
			return WhiteRockContract.WaypointVideo.CONTENT_TYPE;
		case WAYPOINT_VIDEO_ID:
			return WhiteRockContract.WaypointVideo.CONTENT_TYPE_DIR;
		case WAYPOINT_IMAGE_LIST:
			return WhiteRockContract.WaypointImage.CONTENT_TYPE;
		case WAYPOINT_IMAGE_ID:
			return WhiteRockContract.WaypointImage.CONTENT_TYPE_DIR;
		case BUG_REPORT_LIST:
			return WhiteRockContract.BugReport.CONTENT_TYPE;
		case BUG_REPORT_ID:
			return WhiteRockContract.BugReport.CONTENT_TYPE_DIR;
		case CONTENT_REPORT_LIST:
			return WhiteRockContract.ContentReport.CONTENT_TYPE;
		case CONTENT_REPORT_ID:
			return WhiteRockContract.ContentReport.CONTENT_TYPE_DIR;
		case USER_LIST:
			return WhiteRockContract.User.CONTENT_TYPE;
		case USER_ID:
			return WhiteRockContract.User.CONTENT_TYPE_DIR;
		case USER_SETTINGS_LIST:
			return WhiteRockContract.UserSetting.CONTENT_TYPE;
		case USER_SETTINGS_ID:
			return WhiteRockContract.UserSetting.CONTENT_TYPE_DIR;
		case SETTINGS_TYPE_LIST:
			return WhiteRockContract.SettingType.CONTENT_TYPE;
		case SETTINGS_TYPE_ID:
			return WhiteRockContract.SettingType.CONTENT_TYPE_DIR;
		default:
			throw new UnsupportedOperationException("URI " + uri + " is not supported.");	
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDbHandler.getWritableDatabase();
		long id = 0;
		switch (URI_MATCHER.match(uri)) {
		case WALK_LIST:
			id = 
				db.insert(DbSchema.TABLE_WALK,
						  null,
					      values);
			db.close();
			return getUriForId(id, uri);
		case WAYPOINT_LIST:
			id = 
				db.insert(DbSchema.TABLE_WAYPOINT,
						  null,
						  values);
			db.close();
			Log.d(TAG, "Attempting insert into waypoint table");
			return getUriForId(id, uri);
		case ENGLISH_WALK_DESCR_LIST:
			id = 
				db.insert(DbSchema.TABLE_ENGLISH_WALK_DESCR,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case WELSH_WALK_DESCR_LIST:
			id = 
				db.insert(DbSchema.TABLE_WELSH_WALK_DESCR,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case ENGLISH_WAYPOINT_DESCR_LIST:
			id = 
				db.insert(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case WELSH_WAYPOINT_DESCR_LIST:
			id = 
				db.insert(DbSchema.TABLE_WELSH_WAYPOINT_DESCR,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case WAYPOINT_AUDIO_LIST:
			id = 
				db.insert(DbSchema.TABLE_WAYPOINT_AUDIO,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case WAYPOINT_VIDEO_LIST:
			id = 
				db.insert(DbSchema.TABLE_WAYPOINT_VIDEO,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case WAYPOINT_IMAGE_LIST:
			id = 
				db.insert(DbSchema.TABLE_WAYPOINT_IMAGE,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case BUG_REPORT_LIST:
			id = 
				db.insert(DbSchema.TABLE_BUG_REPORT,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case CONTENT_REPORT_LIST:
			id = 
				db.insert(DbSchema.TABLE_CONTENT_REPORT,
						  null,
						  values);
			db.close();
			return getUriForId(id, uri);
		case USER_LIST:
			id = 
			db.insert(DbSchema.TABLE_USER,
					  null,
					  values);
			db.close();
			return getUriForId(id, uri);
		case USER_SETTINGS_LIST:
			id = 
			db.insert(DbSchema.TABLE_USER,
					  null,
					  values);
			db.close();
			return getUriForId(id, uri);
		case SETTINGS_TYPE_LIST:
			id = 
			db.insert(DbSchema.TABLE_SETTING_TYPE,
					  null,
					  values);
			db.close();
			return getUriForId(id, uri);
		default:
			throw new IllegalArgumentException(
					"Unsupported URI For Insertion: " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		SQLiteDatabase db = mDbHandler.getReadableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		boolean useAuthorityUri = false;
		switch (URI_MATCHER.match(uri)) {
		case WALK_LIST:
			builder.setTables(DbSchema.TABLE_WALK);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.Walk.SORT_ORDER_DEFAULT;
			}
			break;
		case WALK_ID:
			builder.setTables(DbSchema.TABLE_WALK);
			builder.appendWhere(WhiteRockContract.Walk._ID + " = " + uri.getLastPathSegment());
			break;
		case ENGLISH_WALK_DESCR_LIST:
			builder.setTables(DbSchema.TABLE_ENGLISH_WALK_DESCR);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.EnglishWalkDescriptions.SORT_ORDER_DEFAULT;
			}
			break;		
		case ENGLISH_WALK_DESCR_ID:
			builder.setTables(DbSchema.TABLE_ENGLISH_WALK_DESCR);
			builder.appendWhere(WhiteRockContract.EnglishWalkDescriptions._ID + " = " + uri.getLastPathSegment());
			break;
		case WELSH_WALK_DESCR_LIST:
			builder.setTables(DbSchema.TABLE_WELSH_WALK_DESCR);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.WelshWalkDescriptions.SORT_ORDER_DEFAULT;
			}
			break;		
		case WELSH_WALK_DESCR_ID:
			builder.setTables(DbSchema.TABLE_WELSH_WALK_DESCR);
			builder.appendWhere(WhiteRockContract.WelshWalkDescriptions._ID + " = " + uri.getLastPathSegment());
			break;
		case WAYPOINT_LIST:
			builder.setTables(DbSchema.TABLE_WAYPOINT);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.Waypoint.SORT_ORDER_DEFAULT;
			}
			break;
		case WAYPOINT_ID:
			builder.setTables(DbSchema.TABLE_WAYPOINT);
			builder.appendWhere(WhiteRockContract.Waypoint._ID + " = " + uri.getLastPathSegment());
			break;
		case WAYPOINT_AUDIO_LIST:
			builder.setTables(DbSchema.TABLE_WAYPOINT_AUDIO);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.WaypointAudio.SORT_ORDER_DEFAULT;
			}
			break;
		case WAYPOINT_AUDIO_ID:
			builder.setTables(DbSchema.TABLE_WAYPOINT_AUDIO);
			builder.appendWhere(WhiteRockContract.WaypointAudio._ID + " = " + uri.getLastPathSegment());
			break;
		case WAYPOINT_VIDEO_LIST:
			builder.setTables(DbSchema.TABLE_WAYPOINT_VIDEO);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.WaypointVideo.SORT_ORDER_DEFAULT;
			}
			break;
		case WAYPOINT_VIDEO_ID:
			builder.setTables(DbSchema.TABLE_WAYPOINT_VIDEO);
			builder.appendWhere(WhiteRockContract.WaypointVideo._ID + " = " + uri.getLastPathSegment());
			break;
		case WAYPOINT_IMAGE_LIST:
			builder.setTables(DbSchema.TABLE_WAYPOINT_IMAGE);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.WaypointImage.SORT_ORDER_DEFAULT;
			}
			break;
		case WAYPOINT_IMAGE_ID:
			builder.setTables(DbSchema.TABLE_WAYPOINT_IMAGE);
			builder.appendWhere(WhiteRockContract.WaypointImage._ID + " = " + uri.getLastPathSegment());
			break;
		case ENGLISH_WAYPOINT_DESCR_LIST:
			builder.setTables(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.EnglishWaypointDescriptions.SORT_ORDER_DEFAULT;
			}
			break;
		case ENGLISH_WAYPOINT_DESCR_ID:
			builder.setTables(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR);
			builder.appendWhere(WhiteRockContract.WelshWaypointDescriptions._ID + " = " + uri.getLastPathSegment());
			break;
		case WELSH_WAYPOINT_DESCR_LIST:
			builder.setTables(DbSchema.TABLE_WELSH_WAYPOINT_DESCR);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.WelshWaypointDescriptions.SORT_ORDER_DEFAULT;
			}
			break;
		case WELSH_WAYPOINT_DESCR_ID:
			builder.setTables(DbSchema.TABLE_WELSH_WAYPOINT_DESCR);
			builder.appendWhere(WhiteRockContract.WelshWaypointDescriptions._ID + " = " + uri.getLastPathSegment());
			break;
		case BUG_REPORT_LIST:
			builder.setTables(DbSchema.TABLE_BUG_REPORT);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.BugReport.SORT_ORDER_DEFAULT;
			}
			break;
		case BUG_REPORT_ID:
			builder.setTables(DbSchema.TABLE_BUG_REPORT);
			builder.appendWhere(WhiteRockContract.BugReport._ID + " = " + uri.getLastPathSegment());
			break;
		case CONTENT_REPORT_LIST:
			builder.setTables(DbSchema.TABLE_CONTENT_REPORT);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.ContentReport.SORT_ORDER_DEFAULT;
			}
			break;
		case CONTENT_REPORT_ID:
			builder.setTables(DbSchema.TABLE_CONTENT_REPORT);
			builder.appendWhere(WhiteRockContract.ContentReport._ID + " = " + uri.getLastPathSegment());
			break;
		case USER_LIST:
			builder.setTables(DbSchema.TABLE_USER);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.User.SORT_ORDER_DEFAULT;
			}
			break;
		case USER_ID:
			builder.setTables(DbSchema.TABLE_USER);
			builder.appendWhere(WhiteRockContract.User._ID + " = " + uri.getLastPathSegment());
			break;
		case USER_SETTINGS_LIST:
			builder.setTables(DbSchema.TABLE_USER_SETTING);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.UserSetting.SORT_ORDER_DEFAULT;
			}
			break;
		case USER_SETTINGS_ID:
			builder.setTables(DbSchema.TABLE_USER_SETTING);
			builder.appendWhere(WhiteRockContract.UserSetting._ID + " = " + uri.getLastPathSegment());
			break;
		case SETTINGS_TYPE_LIST:
			builder.setTables(DbSchema.TABLE_SETTING_TYPE);
			if (TextUtils.isEmpty(sortOrder)) {
				sortOrder = WhiteRockContract.SettingType.SORT_ORDER_DEFAULT;
			}
			break;
		case SETTINGS_TYPE_ID:
			builder.setTables(DbSchema.TABLE_SETTING_TYPE);
			builder.appendWhere(WhiteRockContract.SettingType._ID + " = " + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri + " case argument is " + URI_MATCHER.match(uri));
		}
		
		Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		if (useAuthorityUri) {
			cursor.setNotificationUri(getContext().getContentResolver(), WhiteRockContract.CONTENT_URI);
		} else {
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

		SQLiteDatabase db = mDbHandler.getWritableDatabase();
		int updateCount = 0;
		String idStr;
		String where;

		switch(URI_MATCHER.match(uri)) {
		case WALK_LIST:
			updateCount = db.update(DbSchema.TABLE_WALK, values, selection, selectionArgs);
			break;
		case WALK_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_WALK, values, where, selectionArgs);
			break;
		case WAYPOINT_LIST:
			updateCount = db.update(DbSchema.TABLE_WAYPOINT, values, selection, selectionArgs);
			break;
		case WAYPOINT_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_WAYPOINT, values, where, selectionArgs);
			break;
		case ENGLISH_WALK_DESCR_LIST:
			updateCount = db.update(DbSchema.TABLE_ENGLISH_WALK_DESCR, values, selection, selectionArgs);
			break;
		case ENGLISH_WALK_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_ENGLISH_WALK_DESCR, values, where, selectionArgs);
			break;
		case WELSH_WALK_DESCR_LIST:
			updateCount = db.update(DbSchema.TABLE_ENGLISH_WALK_DESCR, values, selection, selectionArgs);
			break;
		case WELSH_WALK_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_ENGLISH_WALK_DESCR, values, where, selectionArgs);
			break;
		case ENGLISH_WAYPOINT_DESCR_LIST:
			updateCount = db.update(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR, values, selection, selectionArgs);
			break;
		case ENGLISH_WAYPOINT_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_ENGLISH_WAYPOINT_DESCR, values, where, selectionArgs);
			break;
		case WELSH_WAYPOINT_DESCR_LIST:
			updateCount = db.update(DbSchema.TABLE_WELSH_WAYPOINT_DESCR, values, selection, selectionArgs);
			break;
		case WELSH_WAYPOINT_DESCR_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_WELSH_WAYPOINT_DESCR, values, where, selectionArgs);
			break;
		case WAYPOINT_AUDIO_LIST:
			updateCount = db.update(DbSchema.TABLE_WAYPOINT_AUDIO, values, selection, selectionArgs);
			break;
		case WAYPOINT_AUDIO_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_WAYPOINT_AUDIO, values, where, selectionArgs);
			break;
		case WAYPOINT_VIDEO_LIST:
			updateCount = db.update(DbSchema.TABLE_WAYPOINT_VIDEO, values, selection, selectionArgs);
			break;
		case WAYPOINT_VIDEO_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_WAYPOINT_VIDEO, values, where, selectionArgs);
			break;
		case WAYPOINT_IMAGE_LIST:
			updateCount = db.update(DbSchema.TABLE_WAYPOINT_IMAGE, values, selection, selectionArgs);
			break;
		case WAYPOINT_IMAGE_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_WAYPOINT_IMAGE, values, where, selectionArgs);
			break;
		case BUG_REPORT_LIST:
			updateCount = db.update(DbSchema.TABLE_BUG_REPORT, values, selection, selectionArgs);
			break;
		case BUG_REPORT_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_BUG_REPORT, values, where, selectionArgs);
			break;
		case CONTENT_REPORT_LIST:
			updateCount = db.update(DbSchema.TABLE_CONTENT_REPORT, values, selection, selectionArgs);
			break;
		case CONTENT_REPORT_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_CONTENT_REPORT, values, where, selectionArgs);
			break;
		case USER_LIST:
			updateCount = db.update(DbSchema.TABLE_USER, values, selection, selectionArgs);
			break;
		case USER_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_USER, values, where, selectionArgs);
			break;
		case USER_SETTINGS_LIST:
			updateCount = db.update(DbSchema.TABLE_USER_SETTING, values, selection, selectionArgs);
			break;
		case USER_SETTINGS_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_USER_SETTING, values, where, selectionArgs);
			break;
		case SETTINGS_TYPE_LIST:
			updateCount = db.update(DbSchema.TABLE_SETTING_TYPE, values, selection, selectionArgs);
			break;
		case SETTINGS_TYPE_ID:
			idStr = uri.getLastPathSegment();
			where = WhiteRockContract.Walk._ID + " = " + idStr;
			if (!TextUtils.isEmpty(selection)) {
				where += " AND " + selection;
			}
			updateCount = db.update(DbSchema.TABLE_SETTING_TYPE, values, where, selectionArgs);
			break;

		default:
			// if not in the case list we don't support updating it.
			throw new IllegalArgumentException(
					"Unsupported URI For Update: " + uri);
		}
		// tell listeners the DB has changed.
		if (updateCount > 0 && !isInBatchMode()) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return updateCount;

	}


	@Override
	public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
	         throws OperationApplicationException {
				
		SQLiteDatabase db = mDbHandler.getWritableDatabase();
		mIsInBatchMode.set(true);
		db.beginTransaction();
		try {
			final ContentProviderResult[] retResult = super.applyBatch(operations);
			db.setTransactionSuccessful();
			getContext().getContentResolver().notifyChange(WhiteRockContract.CONTENT_URI, null);
			return retResult;
		} finally {
			mIsInBatchMode.remove();
			db.endTransaction();
		}
	}
	
	// Depending on how we are actually storing our images etc. this could deal with opening them!
	@Override
	public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
		if ((URI_MATCHER.match(uri) != WAYPOINT_AUDIO_ID) ||
				(URI_MATCHER.match(uri) != WAYPOINT_VIDEO_ID) ||
				(URI_MATCHER.match(uri) != WAYPOINT_IMAGE_ID)) {
			
			throw new IllegalArgumentException("URI Invalid. Use ID Based URI");
		}
		return openFileHelper(uri, mode);
	}
	
	/**
	 * Log queries in Honeycomb and higher
	 * @param builder
	 * @param projection
	 * @param selection
	 * @param sortOrder
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void logQuery(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder) {
		if (BuildConfig.DEBUG) {
			Log.v("WhiteRock", "query: " + builder.buildQuery(projection,
															  selection,
															  null,
															  null,
															  sortOrder,
															  null));
		}
	}
	

	/**
	 * Log queries in anything below honeycomb.
	 */
	/*
	@SuppressWarnings("deprecation")
	private void logQueryDeprecated(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder) {
		if (BuildConfig.DEBUG) {
			Log.v("WhiteRock", "query: " + builder.buildQuery(projection,
															  selection,
															  null,
															  null,
															  sortOrder,
															  null));
		}
	}
	*/
	
	/**
	 * Get the ID where we are inserting into
	 * @param id
	 * @param uri
	 * @return
	 */
	private Uri getUriForId(long id, Uri uri) {
		Log.d(TAG, "getUriForId:: "+id);
		if (id > 0) {
			Uri itemUri = ContentUris.withAppendedId(uri, id);
			if (!isInBatchMode()) {
				// notify all listeners of changes.
				getContext().getContentResolver().notifyChange(itemUri, null);
			}
			return itemUri;
		}
		throw new SQLException("Problem inserting into uri: " + uri + " " + id);
	}
	
	private boolean isInBatchMode() {
		return mIsInBatchMode.get() != null && mIsInBatchMode.get();
	}
	
}
